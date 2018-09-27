package cz.josefadamcik.activityjournal

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matcher
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

        assertToolbarTitle("Add activity 1/2")
    }

    @Test
    fun addActivityFlow_titleCanBeEnteredAndTheNewRecordDisplayed() {
        val testTitle = "a new title for our activity"

        actExecuteAddActivityWithoutTimeFlow(testTitle)

        assertOnTimeline()
        // check if the activity was added to the list

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle))
        ))

    }

    @Test
    fun addActivityFlow_titleMultipleActivityRecordsCanBeAdded() {
        val testTitle = "a new title for our activity"
        actExecuteAddActivityWithoutTimeFlow(testTitle)
        assertOnTimeline()

        val testTitle2 = "another title"
        actExecuteAddActivityWithoutTimeFlow(testTitle2)
        assertOnTimeline()

        // check if the activity was added to the list


        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle))
        ))
        onRecyclerViewRowAtPositionCheck(R.id.list, 1, allOf(
                hasDescendant(withText(testTitle2))
        ))

    }

    @Test
    fun addActivity_chooseStartTime() {
        val testTitle = "a new title for our activity"

        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        actEnterStartingTime()
        actClickOnFinishButton()

        assertOnTimeline()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(withText("10:00")),
                hasDescendant(withText("Undergoing"))
        ))

    }

    @Test
    fun addActivity_chooseStartTimeAndDate() {
        val testTitle = "a new title for our activity"

        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        actEnterStartingTime()
        actEnterDate()
        actClickOnFinishButton()

        assertOnTimeline()



        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(withText("10:00")),
                hasDescendant(withText("25.9.2018")),
                hasDescendant(withText("Undergoing"))
        ))
    }

    @Test
    fun addActivity_chooseStartTimeAndDateAndDuration() {
        val testTitle = "a new title for our activity"

        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        actEnterStartingTime()
        actEnterDate()
        actEnterDuration()
        actClickOnFinishButton()

        assertOnTimeline()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(withText("10:00")),
                hasDescendant(withText("25.9.2018")),
                hasDescendant(withText("90"))
        ))


    }

    private fun actEnterDate() {
        onView(withId(R.id.input_date))
                .check(matches(isDisplayed()))
                .perform(typeText("25.9.2018"))
    }

    private fun onRecyclerViewRowAtPositionCheck(recyclerViewId: Int, position: Int, itemMatcher: Matcher<View>) {
        onView(withId(recyclerViewId)).perform(scrollToPosition<RecyclerView.ViewHolder>(position));
        onView(withRecyclerView(recyclerViewId)
                .atPosition(position))
                .check(matches(itemMatcher))
    }

    private fun actEnterStartingTime() {
        onView(withId(R.id.input_time))
                .check(matches(isDisplayed()))
                .perform(typeText("10:00"))
    }

    private fun actEnterDuration() {
        onView(withId(R.id.input_duration))
                .check(matches(isDisplayed()))
                .perform(typeText("90"))
    }


    private fun actExecuteAddActivityWithoutTimeFlow(testTitle: String) {
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)

        assertToolbarTitle("Add activity 2/2")
        actClickOnFinishButton()
    }

    private fun actClickOnFinishButton() {
        onView(withId(R.id.button_add))
                .check(matches(allOf(
                        isDisplayed(),
                        withText("Add activity")
                )))
                .perform(click())
    }

    private fun assertOnTimeline() {
        assertToolbarTitle("Timeline")
    }

    private fun actClickOnFab() {
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()))
                .perform(click())
    }

    private fun actEnterTitleToInputAndSubmit(testTitle: String) {
        onView(withId(R.id.input_title))
                .check(matches(isCompletelyDisplayed()))
                .perform(typeText(testTitle), pressImeActionButton())
    }

    private fun assertToolbarTitle(title: String) {
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
                .check(matches(withText(title)))
    }
}