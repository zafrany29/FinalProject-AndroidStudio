package com.example.finalproject_androidstudio.activities;

import android.widget.ImageView;

public class Babysitter extends User{
//    public ImageView profileImage;
//    public ImageView idImage;
    public String facebookURL;
    public String profileImageRef; // Reference to profile image
    public String idImageRef; // Reference to ID image
    public String years; // Optional
    public String speciality; // Optional
    public String kidsAgeRange; // Optional
    public float salary; // Optional, use Float to allow null
    public float rate = -1;
    public boolean isVerified = false;

    public Babysitter() {
    }

    public Babysitter(String fname, String lname, String gender, String location, String birthDate,
                      String email, String password, String phone, String text, String facebookURL,
                      String profileImageRef, String idImageRef, String years, String speciality,
                      String kidsAgeRange, Float salary) {
        super(fname, lname, gender, location, birthDate, email, password, phone, text);
        this.permission = Permissions.BABYSITTER; // Set permission to BABYSITTER
        this.facebookURL = facebookURL;
        this.profileImageRef = profileImageRef;
        this.idImageRef = idImageRef;
        this.years = years; // Optional
        this.speciality = speciality; // Optional
        this.kidsAgeRange = kidsAgeRange; // Optional
        this.salary = salary != null ? salary : 0; // Optional
        this.rate = -1;
        this.isVerified = false; // Default to false
    }

    // Constructor without optional fields (except rate, which is explicitly set)
    public Babysitter(String fname, String lname, String gender, String location, String birthDate,
                      String email, String password, String phone, String facebookURL,
                      String profileImageRef, String idImageRef) {
        this(fname, lname, gender, location, birthDate, email, password, phone, "", facebookURL,
                profileImageRef, idImageRef, null, null, null, null);
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public void setFacebookURL(String facebookURL) {
        if (facebookURL == null || facebookURL.trim().isEmpty()) {
            throw new IllegalArgumentException("Facebook URL is mandatory");
        }
        this.facebookURL = facebookURL;
    }

    public String getProfileImageRef() {
        return profileImageRef;
    }

    public void setProfileImageRef(String profileImageRef) {
        if (profileImageRef == null || profileImageRef.trim().isEmpty()) {
            throw new IllegalArgumentException("Profile image reference is mandatory");
        }
        this.profileImageRef = profileImageRef;
    }

    public String getIdImageRef() {
        return idImageRef;
    }

    public void setIdImageRef(String idImageRef) {
        if (idImageRef == null || idImageRef.trim().isEmpty()) {
            throw new IllegalArgumentException("ID image reference is mandatory");
        }
        this.idImageRef = idImageRef;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getKidsAgeRange() {
        return kidsAgeRange;
    }

    public void setKidsAgeRange(String kidsAgeRange) {
        this.kidsAgeRange = kidsAgeRange;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
