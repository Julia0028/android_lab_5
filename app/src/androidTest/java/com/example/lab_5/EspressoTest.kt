package com.example.lab_5

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class EspressoTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(FirstActivity::class.java)

    private fun firstActivityExist() {
        onView(withId(R.id.butToSecond)).check(matches(isDisplayed()))
        onView(withId(R.id.butToFirst)).check(doesNotExist())
        onView(withId(R.id.butToThird)).check(doesNotExist())
        onView(withId(R.id.butToFirstFromThird)).check(doesNotExist())
        onView(withId(R.id.butToSecFromThird)).check(doesNotExist())
    }

    private fun secondActivityExist() {
        onView(withId(R.id.butToFirst)).check(matches(isDisplayed()))
        onView(withId(R.id.butToThird)).check(matches(isDisplayed()))
        onView(withId(R.id.butToSecond)).check(doesNotExist())
        onView(withId(R.id.butToFirstFromThird)).check(doesNotExist())
        onView(withId(R.id.butToSecFromThird)).check(doesNotExist())
    }

    private fun thirdActivityExist() {
        onView(withId(R.id.butToFirstFromThird)).check(matches(isDisplayed()))
        onView(withId(R.id.butToSecFromThird)).check(matches(isDisplayed()))
        onView(withId(R.id.butToFirst)).check(doesNotExist())
        onView(withId(R.id.butToThird)).check(doesNotExist())
        onView(withId(R.id.butToSecond)).check(doesNotExist())
    }

    private fun toAboutFromMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
        onView(withText("About")).perform(click());
        onView(withText("About")).check(matches(isDisplayed()));
        pressBack()
    }

    @Test
    fun firstToSecond() {
        firstActivityExist()
        toAboutFromMenu()
        onView(withId(R.id.butToSecond)).perform(click())
        secondActivityExist()
    }

    @Test
    fun secondToThird() {
        firstToSecond()
        toAboutFromMenu()
        onView(withId(R.id.butToThird)).perform(click())
        thirdActivityExist()
    }

    @Test
    fun secondToFirst() {
        firstToSecond()
        onView(withId(R.id.butToFirst)).perform(click())
        firstActivityExist()
    }


    @Test
    fun thirdToSecond() {
        secondToThird()
        toAboutFromMenu()
        onView(withId(R.id.butToSecFromThird)).perform(click())
        secondActivityExist()
    }

    @Test
    fun thirdToFirst() {
        secondToThird()
        onView(withId(R.id.butToFirstFromThird)).perform(click())
        firstActivityExist()
    }

    @Test
    fun testBackstackDepth() {
        ActivityScenario.launch(FirstActivity::class.java).use { scenario ->
        firstToSecond()
        onView(withId(R.id.butToThird)).perform(click())
        onView(withId(R.id.butToSecFromThird)).perform(click())
        onView(withId(R.id.butToFirst)).perform(click())
        onView(withId(R.id.butToSecond)).perform(click())
        onView(withId(R.id.butToThird)).perform(click())
        pressBack()
        onView(withId(R.id.butToFirst)).perform(click())
        onView(withId(R.id.butToSecond)).perform(click())
        onView(withId(R.id.butToThird)).perform(click())
        pressBack()
        pressBack()
        pressBackUnconditionally()
            Assert.assertTrue(
                    scenario.state == Lifecycle.State.DESTROYED
            )
        }
    }
}