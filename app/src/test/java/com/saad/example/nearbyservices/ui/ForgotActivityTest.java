package com.saad.example.nearbyservices.ui;

import com.saad.example.nearbyservices.R;

import junit.framework.TestCase;

import org.junit.Test;

import static android.provider.Settings.System.getString;

/**
 * Created by Team Mashwara on 04/04/2020.
 */
public class ForgotActivityTest extends TestCase {

    ForgotActivity forgotActivity;
    public void setUp() throws Exception {
        super.setUp();
        forgotActivity= new ForgotActivity();
    }
    @Test // for verify credentials
    public void testVerifyCredientials() {
        String email= "tayyaba123@gmail.com";
        boolean actual_output=forgotActivity.verifyCredientials(email);
        boolean expected_output= true;
        assertEquals(expected_output,actual_output);
    }

    @Test // for invalid credentials
    public void testInvalidCredientials() {
        String email= "saad123";
        boolean actual_output=forgotActivity.verifyCredientials(email);
        boolean expected_output= false;
        assertEquals(expected_output,actual_output);
    }

    @Test // for empty fields
    public void testEmptyCredientials() {
        String email= "";
        boolean actual_output=forgotActivity.verifyCredientials(email);
        boolean expected_output= false;
        assertEquals(expected_output,actual_output);
    }
}