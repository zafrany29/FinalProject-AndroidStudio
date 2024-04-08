package com.example.finalproject_androidstudio.activities;

public class User {
    public String fname;
    public String lname;
    public String gender;
    public String location;
    public String birthDate;
    public String email;
    public String password;
    public String phone;
    public String text = "";
    public Permissions permission = Permissions.USER;
    public boolean active = true;

    public User() {
    }

    public User(String fname, String lname, String gender, String location,
                String birthDate, String email, String password, String phone,
                String text) {
        if (fname == null || fname.trim().isEmpty()) throw new IllegalArgumentException("First name is mandatory");
        if (lname == null || lname.trim().isEmpty()) throw new IllegalArgumentException("Last name is mandatory");
        if (gender == null || gender.trim().isEmpty()) throw new IllegalArgumentException("Gender is mandatory");
        if (location == null || location.trim().isEmpty()) throw new IllegalArgumentException("Location is mandatory");
        if (birthDate == null || birthDate.trim().isEmpty()) throw new IllegalArgumentException("BirthDate is mandatory");
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Email is mandatory");
        if (password == null || password.trim().isEmpty()) throw new IllegalArgumentException("Password is mandatory");
        if (phone == null || phone.trim().isEmpty()) throw new IllegalArgumentException("Phone is mandatory");

        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.location = location;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.text = text; // Optional, can be empty
    }

    // Constructor without optional text (uses default)
    public User(String fname, String lname, String gender, String location,
                String birthDate, String email, String password, String phone) {
        this(fname, lname, gender, location, birthDate, email, password, phone, "");
    }

    public Permissions getPermission() {
        return permission;
    }

    public void setPermission(Permissions permission) {
        this.permission = permission;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        if (fname == null || fname.trim().isEmpty()) throw new IllegalArgumentException("First name is mandatory");
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        if (lname == null || lname.trim().isEmpty()) throw new IllegalArgumentException("Last name is mandatory");
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) throw new IllegalArgumentException("Gender is mandatory");
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) throw new IllegalArgumentException("Location is mandatory");
        this.location = location;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        if (birthDate == null || birthDate.trim().isEmpty()) throw new IllegalArgumentException("BirthDate is mandatory");
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Email is mandatory");
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) throw new IllegalArgumentException("Password is mandatory");
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) throw new IllegalArgumentException("Phone number is mandatory");
        this.phone = phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}