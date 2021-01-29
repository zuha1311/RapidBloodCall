package com.example.bloodbank;

public class DonatedBloodHistory {

    String date;
    String quantity;
    String location;
    String id;

    public DonatedBloodHistory(String date, String quantity, String location, String id) {
        this.date = date;
        this.quantity = quantity;
        this.location = location;
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
