package com.example.bloodbank.Home.NavigationDrawer.Requests;

public class ReceiveRequests {

    public String userNumber, uid,donorStatus,bloodGroup;

    public ReceiveRequests()
    {

    }

    public ReceiveRequests(String userNumber, String uid, String donorStatus, String bloodGroup) {
        this.userNumber = userNumber;
        this.uid = uid;
        this.donorStatus = donorStatus;
        this.bloodGroup = bloodGroup;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDonorStatus() {
        return donorStatus;
    }

    public void setDonorStatus(String donorStatus) {
        this.donorStatus = donorStatus;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
