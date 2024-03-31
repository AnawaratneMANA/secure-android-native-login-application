package com.example.mobile_security_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationActivity extends AppCompatActivity {

    private EditText username;
    private EditText passwordOne;
    private EditText passwordTwo;
    private Button registerButton;
    private static final Logger LOGGER = Logger.getLogger(RegistrationActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        // Register Interface Elements.
        registerInterfaceElements();
    }

    /**
     * This method is used to register the interface elements.
     */
    private void registerInterfaceElements(){
        username = findViewById(R.id.editTextTextPersonName);
        passwordOne = findViewById(R.id.editTextTextPassword1);
        passwordTwo = findViewById(R.id.editTextTextPassword2);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
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
        // Run validation
        inputValidation();

        // Proceed with storing data in the database.
        UUID uuid = UUID.randomUUID();
        String randomSalt = uuid.toString();

        // Validate the username.
        String usernameParam = username.getText().toString();
        if(usernameValidation(usernameParam)) {
            Toast.makeText(this, "Username is already there in the database!", Toast.LENGTH_SHORT).show();
            LOGGER.warning("Username is already there in the database!");
            throw new RuntimeException("Username is already there in the database!");
        }

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", usernameParam);
        contentValues.put("salt", randomSalt);
        String hashedPassword = getHashedPassword(passwordOne.getText().toString(), randomSalt);
        contentValues.put("password", hashedPassword);
        long id = db.insert("user", null, contentValues);
        LOGGER.info("Insert database record: " + id);
        db.close();
        // Redirect the user to the login screen.
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private String getHashedPassword(String password, String randomSalt) {
       try {
           password = password + randomSalt;
           MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
           messageDigest.update(password.getBytes());
           byte [] bytes = messageDigest.digest();

           // Convert to Hex Decimal
           StringBuilder stringBuilder = new StringBuilder();
           for (byte b : bytes){
               stringBuilder.append(String.format("%02x", b));
           }
           LOGGER.info("Password hash: " + stringBuilder.toString());
           return stringBuilder.toString();
       } catch (NoSuchAlgorithmException e){
           e.printStackTrace();
           LOGGER.warning("Error during password hashing");
           return null;
       }
    }

    @SuppressLint("SetTextI18n")
    public boolean inputValidation(){
        LOGGER.info("Input field validation");
        boolean isValid = true;

        // Validations
        if(TextUtils.isEmpty(username.getText().toString())){
            username.setText("Username can't be empty");
            isValid = false;
        }

        if(TextUtils.isEmpty(passwordOne.getText().toString())){
            passwordOne.setText("Password one can't be empty");
            isValid = false;
        }

        if(TextUtils.isEmpty(passwordTwo.getText().toString())){
            passwordTwo.setText("Password two can't be empty");
            isValid = false;
        }

        // Password comparison between one and two
        if(!passwordOne.getText().toString().contentEquals(passwordTwo.getText().toString())){
            Toast.makeText(this, "Password are not identical", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method is used to validate the username,
     * check whether if already exists in the database.
     * @param username
     * @return boolean
     */
    public boolean usernameValidation(String username) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "username = ?";
        String[] columns = { "username" };
        String[] selectionArgs = { username };
        @SuppressLint("Recycle") Cursor cursor = db.query("user", columns,
                selection, selectionArgs, null, null, null);
        LOGGER.info("Records found : " + cursor.getCount());
        db.close();
        return cursor.getCount() > 0;
    }


}