package com.example.finalproject_androidstudio.activities;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Admin {
    @PropertyName("verified_babysitters")
    private List<Babysitter> verifiedBabysitters;

    @PropertyName("full_name")
    private String fullName;

    @PropertyName("email_address")
    private String email;

    @PropertyName("who_am_i")
    private String whoAmI;
    
    public Admin() {

    }

    public Admin(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.whoAmI = "ADMIN";
    }

    @PropertyName("verified_babysitters")
    public List<Babysitter> getVerifiedBabysitters() {
        return verifiedBabysitters;
    }

    @PropertyName("verified_babysitters")
    public void setVerifiedBabysitters(List<Babysitter> verifiedBabysitters) {
        this.verifiedBabysitters = verifiedBabysitters;
    }

    @PropertyName("full_name")
    public String getFullName() {
        return fullName;
    }

    @PropertyName("full_name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @PropertyName("email_address")
    public String getEmail() {
        return email;
    }

    @PropertyName("email_address")
    public void setEmail(String email) {
        this.email = email;
    }

    @PropertyName("who_am_i")
    public String getWhoAmI() {
        return whoAmI;
    }
}
