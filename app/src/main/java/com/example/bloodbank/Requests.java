package com.example.bloodbank;

public class Requests {
    String requestUsername;

    public Requests(String requestUsername) {
        this.requestUsername = requestUsername;
    }

    public String getRequestUsername() {
        return requestUsername;
    }

    public void setRequestUsername(String requestUsername) {
        this.requestUsername = requestUsername;
    }
}
