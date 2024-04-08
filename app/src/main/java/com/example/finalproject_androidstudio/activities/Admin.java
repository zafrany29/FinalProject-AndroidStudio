package com.example.finalproject_androidstudio.activities;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Admin extends User {
    @PropertyName("verified_babysitters")
    private List<Babysitter> verifiedBabysitters;

    // Default constructor required for Firestore
    public Admin() {
        super(); // Call the default constructor of the parent class
    }

    public Admin(String fullName, String email, String phoneNumber, String location, List<String> calendar, List<Babysitter> verifiedBabysitters) {
        // Set whoAmI to ADMIN since it's specific to this class
        super(fullName, email, phoneNumber, location, calendar, WhoAmI.ADMIN);
        this.verifiedBabysitters = verifiedBabysitters;
    }

    @PropertyName("verified_babysitters")
    public List<Babysitter> getVerifiedBabysitters() {
        return verifiedBabysitters;
    }

    @PropertyName("verified_babysitters")
    public void setVerifiedBabysitters(List<Babysitter> verifiedBabysitters) {
        this.verifiedBabysitters = verifiedBabysitters;
    }
}
