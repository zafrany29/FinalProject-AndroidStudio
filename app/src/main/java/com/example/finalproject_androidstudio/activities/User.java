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

    public User(String fname, String lname, String profession, String gender, String location, String userType, String email, String password, String phone, String text, String years) {
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

    public User(String fname, String lname, String gender, String location, String userType, String email, String password, String phone, String text) {
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
}