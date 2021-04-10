package com.example.bloodbank.Home.NavigationDrawer.History;

public class DonatedBloodHistory {

   String patientAge, patientRelation, patientGender, patientBloodType;

    public DonatedBloodHistory() {
    }

    public DonatedBloodHistory(String patientAge, String patientRelation, String patientGender, String patientBloodType) {
        this.patientAge = patientAge;
        this.patientRelation = patientRelation;
        this.patientGender = patientGender;
        this.patientBloodType = patientBloodType;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientRelation() {
        return patientRelation;
    }

    public void setPatientRelation(String patientRelation) {
        this.patientRelation = patientRelation;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientBloodType() {
        return patientBloodType;
    }

    public void setPatientBloodType(String patientBloodType) {
        this.patientBloodType = patientBloodType;
    }
}
