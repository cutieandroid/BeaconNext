package com.example.beaconnext.models.AuthModels.Response;

public class LoginResponse {
    String success;
    String token;

    public LoginResponse() {
    }


    public LoginResponse(String success, String token) {
        this.success = success;
        this.token = token;
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
}
