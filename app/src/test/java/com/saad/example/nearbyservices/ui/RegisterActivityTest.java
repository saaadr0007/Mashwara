package com.saad.example.nearbyservices.ui;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by Team Mashwara on 04/04/2020.
 */
public class RegisterActivityTest extends TestCase {
    RegisterActivity registerActivity;

    public void setUp() throws Exception {
        super.setUp();
        registerActivity= new RegisterActivity();
    }
    @Test  // valid credientials
    public void testVerifyCredientials() {
        String email;
        String username;
        String pass;
        String retype_pass;
        email= "saad123@yahoo.com";
        username= "saad";
        pass= "123456";
        retype_pass= "123456";
        boolean actual_output= registerActivity.verifyCredientials(email,username,pass,retype_pass);
        boolean expected_output= true;
        assertEquals(expected_output,actual_output);
    }

    @Test  // invalid email credientials
    public void testInvalidEmail() {
        String email;
        String username;
        String pass;
        String retype_pass;
        email= "saad123yahoo.com";
        username= "saad";
        pass= "123456";
        retype_pass= "123456";
        boolean actual_output= registerActivity.verifyCredientials(email,username,pass,retype_pass);
        boolean expected_output= false;
        assertEquals(expected_output,actual_output);
    }

    @Test  // invalid email field empty
    public void testEmailfield_Empty() {
        String email;
        String username;
        String pass;
        String retype_pass;
        email= "";
        username= "saad";
        pass= "123456";
        retype_pass= "123456";
        boolean actual_output= registerActivity.verifyCredientials(email,username,pass,retype_pass);
        boolean expected_output= false;
        assertEquals(expected_output,actual_output);
    }

    @Test  // invalid username credientials
    public void testInvalidUsername() {
        String email;
        String username;
        String pass;
        String retype_pass;
        email= "saad123@yahoo.com";
        username= "";
        pass= "123456";
        retype_pass= "123456";
        boolean actual_output= registerActivity.verifyCredientials(email,username,pass,retype_pass);
        boolean expected_output= false;
        assertEquals(expected_output,actual_output);
    }

    @Test  // invalid password credientials
    public void testVerifyPass() {
        String email;
        String username;
        String pass;
        String retype_pass;
        email= "saad123@yahoo.com";
        username= "saad";
        pass= "";
        retype_pass= "123456";
        boolean actual_output= registerActivity.verifyCredientials(email,username,pass,retype_pass);
        boolean expected_output= false;
        assertEquals(expected_output,actual_output);
    }

    @Test  // invalid retype credientials
    public void testVerifyRetype_password() {
        String email;
        String username;
        String pass;
        String retype_pass;
        email= "saad123@yahoo.com";
        username= "saad";
        pass= "123456";
        retype_pass= "";
        boolean actual_output= registerActivity.verifyCredientials(email,username,pass,retype_pass);
        boolean expected_output= false;
        assertEquals(expected_output,actual_output);
    }

    @Test  // invalid credientials password mismatch
    public void testVerifyPassword_Match() {
        String email;
        String username;
        String pass;
        String retype_pass;
        email= "saad123@yahoo.com";
        username= "saad";
        pass= "123456";
        retype_pass= "00000";
        boolean actual_output= registerActivity.verifyCredientials(email,username,pass,retype_pass);
        boolean expected_output= false;
        assertEquals(expected_output,actual_output);
    }
}