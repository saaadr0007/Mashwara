package com.saad.example.nearbyservices.ui;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.saad.example.nearbyservices.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;



public class RegisterActivityTest {

    @Rule
    //public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
    //  LoginActivity.class);
    //public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    public IntentsTestRule<RegisterActivity> intentsTestRule =
            new IntentsTestRule<>(RegisterActivity.class);

    @Before
    public void setUp() {
        //Before Test case execution
    }


    @Test
    public void EmptyFields()  {
        String Email="";
        String username="";
        String Password="";
        String rePassword="";
        // input some text in the edit text
        Espresso.onView(withId(R.id.email)).perform(typeText(Email));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.username)).perform(typeText(username));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.paassword)).perform(typeText(Password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.Re_type_pass)).perform(typeText(rePassword));
        Espresso.closeSoftKeyboard();
        // perform button click
        //Espresso.onView(withId(R.id.checkBox)).perform(click());
        Espresso.onView(withId(R.id.Signup_button)).perform(click());
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        //intended(hasComponent(LandingActivity.class.getName()));
    }

    @Test
    public void InvalidEmail()  {
        String Email="moiz";
        String username="moiz";
        String Password="moiz";
        String rePassword="moiz";
        // input some text in the edit text
        Espresso.onView(withId(R.id.email)).perform(typeText(Email));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.username)).perform(typeText(username));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.paassword)).perform(typeText(Password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.Re_type_pass)).perform(typeText(rePassword));
        Espresso.closeSoftKeyboard();
        // perform button click
        Espresso.onView(withId(R.id.checkBox)).perform(click());
        Espresso.onView(withId(R.id.Signup_button)).perform(click());
        onView(withId(R.id.email)).check(matches(isDisplayed()));
    }

    @Test
    public void PasswordMissMatch()  {
        String Email="moizahmed76@gmail.com";
        String username="moiz673";
        String Password="moiz";
        String rePassword="moiz";
        // input some text in the edit text
        Espresso.onView(withId(R.id.email)).perform(typeText(Email));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.username)).perform(typeText(username));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.paassword)).perform(typeText(Password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.Re_type_pass)).perform(typeText(rePassword));
        Espresso.closeSoftKeyboard();
        // perform button click
        Espresso.onView(withId(R.id.checkBox)).perform(click());
        Espresso.onView(withId(R.id.Signup_button)).perform(click());
        onView(withId(R.id.email)).check(matches(isDisplayed()));
    }

    @Test
    public void RegisterationSuccess() throws InterruptedException {
        String Email="moizahmed777@gmail.com";
        String username="moizahmed1992";
        String Password="moizahmed";
        String rePassword="moizahmed";
        // input some text in the edit text
        Espresso.onView(withId(R.id.email)).perform(typeText(Email));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.username)).perform(typeText(username));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.paassword)).perform(typeText(Password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.Re_type_pass)).perform(typeText(rePassword));
        Espresso.closeSoftKeyboard();
        // perform button click
        Espresso.onView(withId(R.id.checkBox)).perform(click());
        Espresso.onView(withId(R.id.Signup_button)).perform(click());
        Thread.sleep(2000L);
        intended(hasComponent(LandingActivity.class.getName()));
    }


    @After
    public void tearDown() throws Exception {
        //After Test case Execution
    }
}
