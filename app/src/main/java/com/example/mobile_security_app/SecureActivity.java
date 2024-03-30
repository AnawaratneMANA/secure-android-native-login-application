package com.example.mobile_security_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SecureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_secure);

        // TODO: Validate if the user is having a proper authenticated session.
    }

    /**
     *  This method is used to log out user when click on the log out button
     */
    private void logout(){
        // TODO: Implementation Pending.
    }
}