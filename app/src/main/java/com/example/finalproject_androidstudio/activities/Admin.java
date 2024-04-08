package com.example.finalproject_androidstudio.activities;

public class Admin extends User{

    public Admin() {
    }

    public Admin(String fname, String lname, String gender, String location, String birthDate, String email, String password, String phone) {
        super(fname, lname, gender, location, birthDate, email, password, phone, "Admin");
        this.permission = Permissions.ADMIN;
    }
}
