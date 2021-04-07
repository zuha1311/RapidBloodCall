package com.example.bloodbank;

public class Hospitals {

    String HospitalName,Address,Timings;

    public Hospitals(String name, String address, String timings) {
        HospitalName = name;
        Address = address;
        Timings = timings;
    }

    public Hospitals() {
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String name) {
        HospitalName = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTimings() {
        return Timings;
    }

    public void setTimings(String timings) {
        Timings = timings;
    }
}
