package com.example.lab_5

import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun orientationTest(){

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        onView(withId(R.id.editText)).perform(typeText("Hello"), closeSoftKeyboard())
        onView(withId(R.id.button)).perform(click())
        onView(withId(R.id.button)).check(matches(withText("Hell")))

        onView(withId(R.id.editText)).check(matches(withText("Hello")))


        activityRule.scenario.onActivity {
           it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
       }

        onView(withId(R.id.editText)).check(matches(withText("Hello")))
        onView(withId(R.id.button)).check(matches(withText("World!")))
    }
}