package cz.josefadamcik.activityjournal

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.test.StepperNavigationActions
import cz.josefadamcik.activityjournal.di.appModule
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import cz.josefadamcik.activityjournal.test.*
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
@LargeTest
class AddActivityFlowTest : KoinTest {

    private val testTitle = "a new title for our activity"
    private val duration = "90"
    private val startTime: String = "10:00"
    private val date = "25.9.2018"
    private val buttonTextStartTracking = "Start tracking"
    private val buttonTextAddActivity = "Add activity"

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = KoinActivityTestRule(MainActivity::class.java)

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

        actMoveToNextStep()

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
        actMoveToNextStep()
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

        onRecyclerViewRowAtPositionCheck(R.id.list, 1, allOf(
                hasDescendant(withText(testTitle))
        ))
        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
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
        actMoveToPrevStep()

        assertStepIs(1)
    }

    @Test
    fun addActivity_backButtonReturnsToPreviousStep() {
        actClickOnFab()
        actMoveToNextStep()

        assertStepIs(2)

        Espresso.pressBack()

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

        actMoveToPrevStep()

        assertStepIs(1)

        actMoveToNextStep()

        assertStepIs(2)

        onView(withId(R.id.input_time))
                .check(matches(allOf(ViewMatchers.withText(startTime))))
        onView(withId(R.id.input_duration))
                .check(matches(allOf(ViewMatchers.withText(duration))))
        onView(withId(R.id.input_date))
                .check(matches(allOf(ViewMatchers.withText(date))))
    }

    @Test
    fun addActivity_navUpJumpsOutOfTheFirstStep() {
        actClickOnFab()
        assertStepIs(1)
        actClickOnNavUpButton()
        assertOnTimeline()
    }

    @Test
    fun addActivity_navUpJumpsOutOfTheSecondStep() {
        actClickOnFab()
        actMoveToNextStep()
        assertStepIs(2)
        actClickOnNavUpButton()
        assertOnTimeline()
    }

    @Test
    fun addActivityFlow_undergoingActivityHasAFinishButton() {
        actExecuteAddActivityWithoutTimeFlow(testTitle)

        assertOnTimeline()
        // check if the activity was added to the list
        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(allOf(
                        withId(R.id.button_finish),
                        isDisplayed()
                ))
        ))
    }

    private fun actEnterDate() {
        onView(withId(R.id.input_date))
                .check(matches(isDisplayed()))
                .perform(typeText(date))
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

    private fun actMoveToNextStep() {
        onView(withId(R.id.stepperLayout)).perform(StepperNavigationActions.clickNext())
    }

    private fun actMoveToPrevStep() {
        onView(withId(R.id.stepperLayout))
                .perform(StepperNavigationActions.clickBack())
    }
}
