package com.saad.example.nearbyservices.ui;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import com.saad.example.nearbyservices.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;


public class LoginActivityTest {

    @Rule
    //public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
          //  LoginActivity.class);
    //public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    public IntentsTestRule<LoginActivity> intentsTestRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp()  {
        //Before Test case execution
    }


    @Test
    public void LoginPassed() throws InterruptedException {
        String validEmail="123@gmail.com";
        String validPassword="moizahmed";
        // input some text in the edit text
        Espresso.onView(withId(R.id.emaileditText)).perform(typeText(validEmail));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.passeditText)).perform(typeText(validPassword));
        Espresso.closeSoftKeyboard();
        // perform button click
        Espresso.onView(withId(R.id.button)).perform(click());
        Thread.sleep(2000L);
        intended(hasComponent(LandingActivity.class.getName()));
 }
    @Test
    public void InvalidDetails() {
        String InvalidEmail="Moiz";
        String InvalidPassword="Moiz";
        Espresso.onView(withId(R.id.emaileditText)).perform(typeText(InvalidEmail));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.passeditText)).perform(typeText(InvalidPassword));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.emaileditText)).check(matches(isDisplayed()));
    }
    @Test
    public void Emptyfields()  {
        String InvalidEmail="";
        String InvalidPassword="";
        Espresso.onView(withId(R.id.emaileditText)).perform(typeText(InvalidEmail));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.passeditText)).perform(typeText(InvalidPassword));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.emaileditText)).check(matches(isDisplayed()));
    }

    @Test
    public void EmptyEmailWithfillPassword()  {
        String InvalidEmail="";
        String InvalidPassword="abcd";
        Espresso.onView(withId(R.id.emaileditText)).perform(typeText(InvalidEmail));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.passeditText)).perform(typeText(InvalidPassword));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.emaileditText)).check(matches(isDisplayed()));
    }

    @Test
    public void EmptyPasswordWithfillEmail() {
        String InvalidEmail="abcd";
        String InvalidPassword="";
        Espresso.onView(withId(R.id.emaileditText)).perform(typeText(InvalidEmail));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.passeditText)).perform(typeText(InvalidPassword));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.emaileditText)).check(matches(isDisplayed()));
    }



    @After
    public void tearDown() throws Exception {
        //After Test case Execution
    }
}
