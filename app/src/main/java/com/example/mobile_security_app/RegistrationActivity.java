package com.example.mobile_security_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);
    }

    /**
     *  This method going to register the user to the SQLite database with hashed
     *  passwords and salting mechanism to secure the user login.
     *
     *  Validations
     *  ✅  Check if the user is already register to the system using the same username.
     *  ✅  If user registration is success then user will be navigated back to the login.
     */
    private void register(){
        // TODO: Implementation Pending.
    }


}