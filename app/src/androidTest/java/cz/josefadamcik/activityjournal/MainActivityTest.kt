package cz.josefadamcik.activityjournal

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.test.StepperNavigationActions
import cz.josefadamcik.activityjournal.test.runWithStepperLayoutSupport
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private val testTitle = "a new title for our activity"
    private val duration = "90"
    private val startTime: String = "10:00"
    private val date = "25.9.2018"
    private val buttonTextStartTracking = "Start tracking"
    private val buttonTextAddActivity = "Add activity"

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

        assertStepIs(1)
    }



    @Test
    fun addActivityFlow_titleCanBeEnteredAndTheNewRecordDisplayed() {

        actExecuteAddActivityWithoutTimeFlow(testTitle)

        assertOnTimeline()
        // check if the activity was added to the list

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle))
        ))
    }

    @Test
    fun addActivity_nextButtonOnStepperShouldWork() {
        actClickOnFab()
        onView(withId(R.id.input_title))
                .check(matches(isCompletelyDisplayed()))
                .perform(typeText(testTitle))

        onView(withId(R.id.stepperLayout))
                .perform(StepperNavigationActions.clickNext())

        assertStepIs(2)
        actClickOnFinishButton(buttonTextStartTracking)

        assertOnTimeline()
        // check if the activity was added to the list

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle))
        ))
    }


    @Test
    fun addActivityFlow_nextGoesToSecondStep() {
        actClickOnFab()
        onView(withId(R.id.stepperLayout)).perform(StepperNavigationActions.clickNext())
        assertStepIs(2)
    }

    @Test
    fun addActivityFlow_titleMultipleActivityRecordsCanBeAdded() {
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
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        actEnterStartingTime()
        actClickOnFinishButton(buttonTextStartTracking)

        assertOnTimeline()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(withText("10:00")),
                hasDescendant(withText("Undergoing"))
        ))
    }


    @Test
    fun addActivity_chooseStartTimeAndDate() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        actEnterStartingTime()
        actEnterDate()
        actClickOnFinishButton(buttonTextStartTracking)

        assertOnTimeline()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(withText("10:00")),
                hasDescendant(withText(date)),
                hasDescendant(withText("Undergoing"))
        ))
    }

    @Test
    fun addActivity_chooseStartTimeAndDateAndDuration() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        actEnterStartingTime()
        actEnterDate()
        actEnterDuration()
        actClickOnFinishButton(buttonTextAddActivity)

        assertOnTimeline()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(withText("10:00")),
                hasDescendant(withText(date)),
                hasDescendant(withText(duration))
        ))
    }

    @Test
    fun addActivity_stepperBackWorks() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)

        assertStepIs(2)
        onView(withId(R.id.stepperLayout))
                .perform(StepperNavigationActions.clickBack())

        assertStepIs(1)
    }

    @Test
    fun addActivity_stepperBackAndForwardWorks() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        assertStepIs(2)
        actEnterStartingTime()
        actEnterDate()
        actEnterDuration()

        onView(withId(R.id.stepperLayout))
                .perform(StepperNavigationActions.clickBack())

        assertStepIs(1)

        onView(withId(R.id.stepperLayout))
                .perform(StepperNavigationActions.clickNext())

        assertStepIs(2)

        onView(withId(R.id.input_time))
                .check(matches(allOf(ViewMatchers.withText(startTime))))
        onView(withId(R.id.input_duration))
                .check(matches(allOf(ViewMatchers.withText(duration))))
        onView(withId(R.id.input_date))
                .check(matches(allOf(ViewMatchers.withText(date))))
    }


    private fun actEnterDate() {
        onView(withId(R.id.input_date))
                .check(matches(isDisplayed()))
                .perform(typeText(date))
    }

    private fun onRecyclerViewRowAtPositionCheck(recyclerViewId: Int, position: Int, itemMatcher: Matcher<View>) {
        onView(withId(recyclerViewId)).perform(scrollToPosition<RecyclerView.ViewHolder>(position))
        onView(withRecyclerView(recyclerViewId)
                .atPosition(position))
                .check(matches(itemMatcher))
    }

    private fun actEnterStartingTime() {
        onView(withId(R.id.input_time))
                .check(matches(isDisplayed()))
                .perform(typeText(startTime))
    }

    private fun actEnterDuration() {
        onView(withId(R.id.input_duration))
                .check(matches(isDisplayed()))
                .perform(typeText(duration))
    }

    private fun actExecuteAddActivityWithoutTimeFlow(testTitle: String) {
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)

        assertStepIs(2)
        actClickOnFinishButton(buttonTextStartTracking)
    }

    private fun assertStepIs(step: Int) {
        assertToolbarTitle("Add activity $step/2")
    }

    private fun actClickOnFinishButton(expectedButtonText: String) {
        onView(withId(R.id.button_add))
                .check(matches(allOf(
                        isDisplayed(),
                        withText(expectedButtonText)
                )))
                .perform(
                        closeSoftKeyboard(),
                        click()
                )
    }

    private fun assertOnTimeline() {
        assertToolbarTitle("Timeline")
    }

    private fun actClickOnFab() {
        onView(withId(R.id.fab))
                .check(matches(isCompletelyDisplayed()))
                .perform(click())
    }

    private fun actEnterTitleToInputAndSubmit(testTitle: String) {
        runWithStepperLayoutSupport(activityRule.activity.findViewById<StepperLayout>(R.id.stepperLayout)) {
            onView(withId(R.id.input_title))
                    .check(matches(isCompletelyDisplayed()))
                    .perform(typeText(testTitle), pressImeActionButton())
        }
    }

    private fun assertToolbarTitle(title: String) {
        onView(allOf(
                instanceOf(TextView::class.java),
                withParent(withId(R.id.toolbar)),
                isCompletelyDisplayed(),
                withText(title)))
            .check(matches(withText(title)))
    }
}