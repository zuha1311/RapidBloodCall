package com.example.bloodbank.Home.DonateBlood;

public class Hospitals {

    String name,address,timings,mobile;

    public Hospitals(String name, String address, String timings,String mobile) {
        name = name;
        address = address;
        timings = timings;
        mobile = mobile;
    }

    public Hospitals() {
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        address = address;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        timings = timings;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
