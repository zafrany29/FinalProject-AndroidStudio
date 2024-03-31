package com.example.finalproject_androidstudio.activities;

public class User {
    public String fname;
    public String lname;
    public String profession;
    public String gender;
    public String location;
    public String userType;
    public String email;
    public String password;
    public String phone;
    public String text;
    public String years;
    public boolean active = true;
    public double rate = -1;

    public User() {
    }

    public User(String fname, String lname, String profession, String gender, String location,
                String userType, String email, String password, String phone, String text,
                String years) {
        this.fname = fname;
        this.lname = lname;
        this.profession = profession;
        this.gender = gender;
        this.location = location;
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.text = text;
        this.years = years;
    }

    public User(String fname, String lname, String gender, String location, String userType,
                String email, String password, String phone, String text) {
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.location = location;
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.text = text;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}