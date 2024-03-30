package com.example.mobile_security_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    // Variables
    private TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Register UI Elements
        setInterfaceElements();

        // Listeners and Logic
        registerText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the registration activity.
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     *  This method going to register all the XML elements in the Java class.
     */
    private void setInterfaceElements() {
        registerText = findViewById(R.id.registerLink);
    }

    /**
     *  This method going to login user to the system and set the global session,
     *  If valid user, then user will be navigate to the {@link SecureActivity}
     */
    private void login(){
        // TODO: Implementation Pending
    }
}