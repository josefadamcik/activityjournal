package cz.josefadamcik.activityjournal

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


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
        actClickOnFab()

        assertToolbarTitle("Add activity")
    }

    @Test
    fun addActivityFlow_titleCanBeEnteredAndTheNewRecordDisplayed() {
        actClickOnFab()

        val testTitle = "a new title for our activity"
        actEnterTitliIntoInput(testTitle)

        //returned back to the timeline
        assertToolbarTitle("Timeline")

        //check if the activity was added to the list
        onView(withText(testTitle))
                .check(matches(isDisplayed()))
    }

    @Test
    fun addActivityFlow_titleCanBeEnteredAndTheNewRecordDisplayed2() {
        actClickOnFab()

        val testTitle = "another title for the activity"
        actEnterTitliIntoInput(testTitle)

        //returned back to the timeline
        assertToolbarTitle("Timeline")

        //check if the activity was added to the list
        onView(withText(testTitle))
                .check(matches(isDisplayed()))

    }

    private fun actClickOnFab() {
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()))
                .perform(click())
    }

    private fun actEnterTitliIntoInput(testTitle: String) {
        onView(withId(R.id.title_input))
                .check(matches(isCompletelyDisplayed()))
                .perform(typeText(testTitle), pressImeActionButton())
    }

    private fun assertToolbarTitle(title: String) {
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
                .check(matches(withText(title)))
    }
}