package com.saad.example.nearbyservices.ui;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by Team Mashwara on 03/04/2020.
 */
public class LoginActivityTest extends TestCase {

    LoginActivity loginActivity;


    public void setUp() throws Exception {
        super.setUp();

        loginActivity=new LoginActivity();
    }
    @Test // for valid credentials
    public void testValidCredentials() {
        String email, pass;
        email="tayyaba123@gmail.com";
        pass="123456";
        boolean actual_output= loginActivity.verifyCredentials(email,pass);
        boolean expected_output =true;
        assertEquals(expected_output, actual_output);
    }

    @Test // for invalid credentials
    public void testInValidCredentials() {
        String email, pass;
        email="tayyaba12@gmail.com";
        pass="12345";
        boolean actual_output= loginActivity.verifyCredentials(email,pass);
        boolean expected_output =false;
        assertEquals(expected_output, actual_output);
    }

    @Test // for invalid credentials
    public void testEmptyEmail() {
        String email, pass;
        email="";
        pass="123456";
        boolean actual_output= loginActivity.verifyCredentials(email,pass);
        boolean expected_output =false;
        assertEquals(expected_output, actual_output);
    }

    @Test // for invalid credentials
    public void testPasswordEmpty() {
        String email, pass;
        email="tayyaba123@gmail.com";
        pass="";
        boolean actual_output= loginActivity.verifyCredentials(email,pass);
        boolean expected_output =false;
        assertEquals(expected_output, actual_output);
    }

}