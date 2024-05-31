package dev.digitaldreamweavers.qrguard;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testFloatingScanButton() {
        onView(withId(R.id.scanButton)).perform(click());
        // Verify that the scan activity is started or appropriate UI changes occur
    }

    @Test
    public void testSettingsButton() {
        onView(withId(R.id.btn_settings)).perform(click());
        onView(withId(R.id.setting_head)).check(matches(withText("Settings")));
    }
}