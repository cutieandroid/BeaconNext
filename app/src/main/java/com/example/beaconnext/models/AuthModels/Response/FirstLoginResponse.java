package com.example.beaconnext.models.AuthModels.Response;

public class FirstLoginResponse {
    private String success,token,deviceId;


    public FirstLoginResponse(String success, String token) {
        this.success = success;
        this.token = token;
    }


    public FirstLoginResponse() {
    }

    public FirstLoginResponse(String success, String token, String deviceId) {
        this.success = success;
        this.token = token;
        this.deviceId = deviceId;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
