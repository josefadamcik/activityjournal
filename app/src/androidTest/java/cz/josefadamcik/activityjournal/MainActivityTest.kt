package cz.josefadamcik.activityjournal

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.withParent
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun applicationStart_activityDisplays() {
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()))
        onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()))
    }

    @Test
    fun fabPressed_addActivityFlowDisplayed() {
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
                .check(matches(withText("Add activity")))
    }

    @Test
    fun addActivityFlow_titleCanBeEntered() {
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withId(R.id.title_input))
                .check(matches(isCompletelyDisplayed()))
                .perform(typeText("a new title for our activity"))
    }
}