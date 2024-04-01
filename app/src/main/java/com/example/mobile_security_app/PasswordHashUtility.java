package com.example.mobile_security_app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class PasswordHashUtility {
    private static final Logger LOGGER = Logger.getLogger(PasswordHashUtility.class.getName());
    public static String getHashedPassword(String password, String randomSalt) {
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
}
