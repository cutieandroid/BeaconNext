package com.example.beaconnext.Wrapper;

public class SharedPrefWrapper {
    String type;
    Object object;

    public SharedPrefWrapper() {
    }
    public SharedPrefWrapper(String type, Object object) {
        this.type = type;
        this.object = object;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
