package com.example.beaconnext.models.AuthModels.Requests;

public class FirstLoginRequest {
    private Integer moodleId;
    private String email,oldPassword, newPassword,deviceId;

    public FirstLoginRequest() {
    }

    public FirstLoginRequest(String email, String oldPassword, String newPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
    public FirstLoginRequest(Integer moodleId, String oldPassword, String newPassword, String deviceId) {
        this.moodleId = moodleId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMoodleId() {
        return moodleId;
    }

    public void setMoodleId(Integer moodleId) {
        this.moodleId = moodleId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getdeviceId() {
        return deviceId;
    }

    public void setdeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
