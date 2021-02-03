package com.example.bloodbank;

public class FindDonors {

    public String userNumber, bloodGroup, donorStatus,uid;

    public FindDonors()
    {}


    public FindDonors(String userNumber, String bloodGroup, String donorStatus,String uid) {
        this.userNumber = userNumber;
        this.bloodGroup = bloodGroup;
        this.donorStatus = donorStatus;
        this.uid = uid;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDonorStatus() {
        return donorStatus;
    }

    public void setDonorStatus(String donorStatus) {
        this.donorStatus = donorStatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
