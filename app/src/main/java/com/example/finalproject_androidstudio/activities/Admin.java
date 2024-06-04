package com.example.finalproject_androidstudio.activities;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.List;

public class Admin {
    @PropertyName("verifiedBabysitters")
    private List<String> verifiedBabysitters;

    @PropertyName("blacklistedBabysitters")
    private List<String> blacklistedBabysitters;

    @PropertyName("emailAddress")
    private String email;

    @PropertyName("whoAmI")
    private String whoAmI;
    
    public Admin() {

    }

    public Admin(String email) {
        this.email = email;
        this.whoAmI = "ADMIN";
        this.verifiedBabysitters = new ArrayList<>();
        this.blacklistedBabysitters = new ArrayList<>();
    }

    // Getters
    public List<String> getVerifiedBabysitters() {
        return verifiedBabysitters;
    }

    public List<String> getBlacklistedBabysitters() {
        return blacklistedBabysitters;
    }

    public String getEmail() {
        return email;
    }

    public String getWhoAmI() {
        return whoAmI;
    }

    // Setters
    public void setVerifiedBabysitters(List<String> verifiedBabysitters) {
        this.verifiedBabysitters = verifiedBabysitters;
    }

    public void setBlacklistedBabysitters(List<String> blacklistedBabysitters) {
        this.blacklistedBabysitters = blacklistedBabysitters;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWhoAmI(String whoAmI) {
        this.whoAmI = whoAmI;
    }
}
