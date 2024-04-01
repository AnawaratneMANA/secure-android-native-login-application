package com.example.mobile_security_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    // Variables
    private TextView registerText;
    private EditText username;
    private EditText password;
    private Button loginButton;
    private static final Logger LOGGER = Logger.getLogger(LoginActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Register UI Elements
        setInterfaceElements();

        // Listeners and Logic
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the registration activity.
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This method going to register all the XML elements in the Java class.
     */
    private void setInterfaceElements() {
        registerText = findViewById(R.id.registerLink);
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    login();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method going to login user to the system and set the global session,
     * If valid user, then user will be navigate to the {@link SecureActivity}
     */
    private void login() {
        // Input field validation.
        if (!inputFieldValidation()) {
            LOGGER.warning("Input field validation failed!");
            throw new RuntimeException("Input field validation failed!");
        }
        // Validate the User Login, Query the table.
        if(isValidUser(username.getText().toString(), password.getText().toString())){
            // Proceed with the login
            Intent intent = new Intent(LoginActivity.this, SecureActivity.class);
            startActivity(intent);
        } else {
            // When the password is not matching.
            LOGGER.warning("Entered information are incorrect!");
            Toast.makeText(this, "Entered information are incorrect!", Toast.LENGTH_SHORT).show();
            throw new RuntimeException("Entered information are incorrect!");
        }
    }

    /**
     * This method is used to validate the input fields given by the user.
     * @return
     */
    @SuppressLint("SetTextI18n")
    public Boolean inputFieldValidation() {
        LOGGER.info("Input field validation");
        boolean isValid = true;

        // Validations
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setText("Username can't be empty");
            isValid = false;
        }

        // Validations
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setText("Password can't be empty");
            isValid = false;
        }
        return isValid;
    }

    public Boolean isValidUser(String username, String password){
        // Query the database to find out, if the valid user is attempt to login.
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "username = ?";
        String[] columns = { "username", "password", "salt" };
        String[] selectionArgs = { username };
        @SuppressLint("Recycle") Cursor cursor = db.query("user", columns,
                selection, selectionArgs, null, null, null);
        LOGGER.info("Records found : " + cursor.getCount());
        db.close();

        // Perform the Hashing again and compare the stored hash with the given hash.
        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            int passwordIndex = cursor.getColumnIndex("password");
            int saltIndex = cursor.getColumnIndex("salt");

            if (passwordIndex != -1 && saltIndex != -1) {
                @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
                @SuppressLint("Range") String salt = cursor.getString(cursor.getColumnIndex("salt"));
                // Use password and salt as needed
                cursor.close();
                // Perform the comparison
                return storedPassword.contentEquals(
                        Objects.requireNonNull(
                                PasswordHashUtility.getHashedPassword(password, salt)));
            }
        }
        return false;
    }
}