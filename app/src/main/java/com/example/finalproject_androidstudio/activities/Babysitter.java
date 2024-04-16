package com.example.finalproject_androidstudio.activities;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Babysitter extends MyUser {
    @PropertyName("id_photo_url")
    private String idPhotoUrl;
    @PropertyName("profile_photo_url")
    private String profilePhotoUrl;
    @PropertyName("social_link")
    private String socialLink;
    @PropertyName("experience")
    private String experience;
    @PropertyName("speciality")
    private String speciality;
    @PropertyName("kidsAgeRange")
    private String kidsAgeRange;
    @PropertyName("salary")
    private double salary;
    @PropertyName("rating")
    private double rating;
    @PropertyName("isVerified")
    private boolean isVerified;
    @PropertyName("description")
    private String description;

    public Babysitter() {
        // Default constructor required for Firestore
    }

    public Babysitter(String fullName, String email, String phoneNumber, String location, String description, List<String> calendar,
                      String idPhotoUrl, String profilePhotoUrl, String socialLink, String experience,
                      String speciality, String kidsAgeRange, double salary, double rating, boolean isVerified) {
        // Set whoAmI to BABYSITTER since it's specific to this class
        super(fullName, email, phoneNumber, location, calendar, WhoAmI.BABYSITTER);
        // Initialize other fields specific to Babysitter
        this.idPhotoUrl = idPhotoUrl;
        this.profilePhotoUrl = profilePhotoUrl;
        this.socialLink = socialLink;
        this.experience = experience;
        this.speciality = speciality;
        this.kidsAgeRange = kidsAgeRange;
        this.salary = salary;
        this.rating = rating;
        this.isVerified = isVerified;
        this.description = description;
    }

    @PropertyName("id_photo_url")
    public String getIdPhotoUrl() {
        return idPhotoUrl;
    }

    @PropertyName("id_photo_url")
    public void setIdPhotoUrl(String idPhotoUrl) {
        this.idPhotoUrl = idPhotoUrl;
    }

    @PropertyName("profile_photo_url")
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    @PropertyName("profile_photo_url")
    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    @PropertyName("social_link")
    public String getSocialLink() {
        return socialLink;
    }

    @PropertyName("social_link")
    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    @PropertyName("experience")
    public String getExperience() {
        return experience;
    }

    @PropertyName("experience")
    public void setExperience(String experience) {
        this.experience = experience;
    }

    @PropertyName("speciality")
    public String getSpeciality() {
        return speciality;
    }

    @PropertyName("speciality")
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @PropertyName("kids_age_range")
    public String getKidsAgeRange() {
        return kidsAgeRange;
    }

    @PropertyName("kids_age_range")
    public void setKidsAgeRange(String kidsAgeRange) {
        this.kidsAgeRange = kidsAgeRange;
    }

    @PropertyName("salary")
    public double getSalary() {
        return salary;
    }

    @PropertyName("salary")
    public void setSalary(double salary) {
        this.salary = salary;
    }

    @PropertyName("rating")
    public double getRating() {
        return rating;
    }

    @PropertyName("rating")
    public void setRating(double rating) {
        this.rating = rating;
    }

    @PropertyName("is_verified")
    public boolean isVerified() {
        return isVerified;
    }

    @PropertyName("is_verified")
    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }
}
