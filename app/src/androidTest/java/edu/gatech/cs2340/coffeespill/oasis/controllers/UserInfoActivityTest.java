package edu.gatech.cs2340.coffeespill.oasis.controllers;

import android.os.Build;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.intent.Intents;

import edu.gatech.cs2340.coffeespill.oasis.R;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserInfoActivityTest {
    @Rule
    public final IntentsTestRule<UserInfoActivity> mActivityRule = new IntentsTestRule<>(UserInfoActivity.class);

    @Test
    public void backPressed() {
        Espresso.pressBack();
        Intents.intended(IntentMatchers.hasComponent(ShelterListActivity.class.getName()));
        try {
            Thread.sleep(2000); // this is needed :(
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            assertTrue(mActivityRule.getActivity().isDestroyed());
        }
    }

    @Test
    public void logoutPressed() {
        onView(withId(R.id.logout)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(HomeScreenActivity.class.getName()));
        try {
            Thread.sleep(2000); // this is needed :(
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            assertTrue(mActivityRule.getActivity().isDestroyed());
        }
    }

    @Test
    public void checkElements() {
        onView(withId(R.id.textView8)).check(matches(withText("User Info")));
        onView(withId(R.id.logout)).check(matches(withText("Logout")));
        onView(withId(R.id.userEmail)).check(matches(withText("andrewatdcs@yahoo.com")));
    }
}