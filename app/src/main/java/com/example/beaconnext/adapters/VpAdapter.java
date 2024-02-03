package com.example.beaconnext.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.beaconnext.fragments.Profesorlogin;
import com.example.beaconnext.fragments.ProfessorReg;
import com.example.beaconnext.fragments.StudentReg;
import com.example.beaconnext.fragments.Studentlogin;

public class VpAdapter extends FragmentPagerAdapter {

    public VpAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Studentlogin();
            case 1:
                return new Profesorlogin();

            case 2:
                return new StudentReg();

            case 3:
                return new ProfessorReg();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
