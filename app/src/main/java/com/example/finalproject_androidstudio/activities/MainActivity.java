package com.example.finalproject_androidstudio.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_androidstudio.R;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    public static MyUser user;
    private Babysitter babysitter;

    public static MyUser getUser() {
        return user;
    }

    public static void setUser(MyUser user) {
        MainActivity.user = user;
    }
    public void setBabysitter(Babysitter babysitter) {
        this.babysitter = babysitter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

    }
}