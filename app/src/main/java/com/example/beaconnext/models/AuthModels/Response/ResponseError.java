package com.example.beaconnext.models.AuthModels.Response;

public class ResponseError {
    String error;

    public ResponseError(String error) {
        this.error = error;
    }

    public ResponseError() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
