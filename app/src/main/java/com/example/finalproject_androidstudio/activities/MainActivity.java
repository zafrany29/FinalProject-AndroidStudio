package com.example.finalproject_androidstudio.activities;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject_androidstudio.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static User user;
    private Babysitter babysitter;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
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