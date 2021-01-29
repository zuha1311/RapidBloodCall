package com.example.bloodbank;

public class ReceivedBloodHistory {

    String date,id,qty,location;

    public ReceivedBloodHistory(String date, String id, String qty, String location) {
        this.date = date;
        this.id = id;
        this.qty = qty;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
