package com.example.beaconnext.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.beaconnext.R;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.models.AttendanceRequest;
import com.example.beaconnext.models.AttendanceResponse;
import com.example.beaconnext.singleton.LocalStorage;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForegroundScan extends Service {

    public static final String TAG = ForegroundScan.class.getSimpleName();

    private ProximityManager proximityManager;

    String buuid="";
    LocalStorage ls;

    AuthApiInterface apiInterface;

    private boolean isRunning; // Flag indicating if service is already running.

    @Override
    public void onCreate() {
        super.onCreate();
        setupProximityManager();
        ls= new LocalStorage(this);
        apiInterface= AuthApiClient.getClient().create(AuthApiInterface.class);
        isRunning = false;
    }

    private void setupProximityManager() {
        // Create proximity manager instance
        proximityManager = ProximityManagerFactory.create(this);

        // Configure proximity manager basic options
        proximityManager.configuration()
                //Using ranging for continuous scanning or MONITORING for scanning with intervals
                .scanPeriod(ScanPeriod.RANGING)
                //Using BALANCED for best performance/battery ratio
                .scanMode(ScanMode.BALANCED);

        // Set up iBeacon listener
        proximityManager.setIBeaconListener(createIBeaconListener());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startInForeground();
        isRunning = true;
        Toast.makeText(this, "Service is running...", Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();
        Runnable runnable;
        // Schedule the function to run every, for example, 10 seconds
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                // Your repeated function goes here
                // Toast.makeText(ForegroundScan.this, "service is still running", Toast.LENGTH_SHORT).show();
                startScanning();
                stopScanning();
                markAttendance();
                handler.postDelayed(this, 20000); // 10 seconds interval
            }
        }, 0); // 10 seconds initial delay

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startInForeground() {
        Notification notification = new Notification.Builder(
                this, "running_channel"
        ).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("BeaconNext is Running")
                .setContentText("Marking Attendance")
                .build();

        startForeground(1, notification);

    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
                //Toast.makeText(ForegroundScan.this, "Scanning service started.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                Log.i(TAG, "onIBeaconDiscovered: " + ibeacon.toString());
                buuid=ibeacon.getProximityUUID().toString();
                // Toast.makeText(ForegroundScan.this, ibeacon.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {
                super.onIBeaconLost(ibeacon, region);
                Log.e(TAG, "onIBeaconLost: " + ibeacon.toString());
            }
        };
    }

    private void stopScanning() {
        //Stop scanning if scanning is in progress
        if (proximityManager.isScanning()) {
            proximityManager.stopScanning();
            //  Toast.makeText(this, "Scanning stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void markAttendance(){

        System.out.println(ls.getToken());
        System.out.println(buuid);
            AttendanceRequest attendanceRequest= new AttendanceRequest(buuid);
            Call<AttendanceResponse> attendanceCall= apiInterface.markattendance(ls.getToken(),attendanceRequest);
            attendanceCall.enqueue(new Callback<AttendanceResponse>() {
                @Override
                public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(ForegroundScan.this, "Marked", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        stopSelf();
                        Toast.makeText(ForegroundScan.this, "Cannot mark", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AttendanceResponse> call, Throwable t) {
                    Toast.makeText(ForegroundScan.this, "network issue", Toast.LENGTH_SHORT).show();

                }
            });
        }




}
