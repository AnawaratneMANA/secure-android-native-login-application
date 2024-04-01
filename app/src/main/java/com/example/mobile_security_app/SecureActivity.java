package com.example.mobile_security_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;
import java.util.logging.Logger;

public class SecureActivity extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(SecureActivity.class.getName());
    private Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_secure);

        // Validate the user session.
        LOGGER.info("Validating user session");
        String username = SessionManager.getInstance(this).getUsername();
        if(Objects.isNull(username)){
            Intent intent = new Intent(SecureActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // This will prevent user from coming back to the SecureActivity
        }
        // Set the user interface elements
        setUserInterfaceElements();
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    /**
     *  This method is used to log out user when click on the log out button
     */
    private void logout(){
        // Clear the session and forward user to the Login screen again.
        LOGGER.info("Logging out the user");
        SessionManager.getInstance(this).saveSession(null);
        Intent intent = new Intent(SecureActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // This will prevent user from coming back to the SecureActivity
    }

    /**
     *  This method is used to set the user element details.
     */
    private void setUserInterfaceElements(){
        // Register the elements.
        logOutButton = findViewById(R.id.logOutButton);
    }
}