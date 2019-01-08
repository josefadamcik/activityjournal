package cz.josefadamcik.activityjournal.screens.addactivity

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.test.StepperNavigationActions
import cz.josefadamcik.activityjournal.MainActivity
import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import cz.josefadamcik.activityjournal.test.*
import io.kotlintest.*
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.collections.shouldNotContainExactlyInAnyOrder
import io.kotlintest.matchers.match
import io.kotlintest.matchers.shouldHave
import io.kotlintest.matchers.string.shouldNotBeEmpty
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

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

    private val activityRecordsRepository by inject<ActivityRecordsRepository>()

    @Test
    fun fabPressed_addActivityFlowDisplayed() {
        actClickOnFab()

        assertStepIs(1)
    }

    @Test
    fun fabPressed_inputIsFocusedAndKeyboardDisplayed() {
        actClickOnFab()

        onView(withId(R.id.input_title))
                .check(matches(allOf(
                        ViewMatchers.isFocusable(),
                        ViewMatchers.hasFocus()
                )))

        isKeyboardShown()
    }

    @Test
    fun addActivityFlow_titleCanBeEnteredAndTheNewRecordDisplayed() {

        actExecuteAddActivityWithoutTimeFlow(testTitle)

        assertOnTimeline()

        assertAnItemWithTitleWasAdded()
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

        assertAnItemWithTitleWasAdded()
    }

    @Test
    fun addActivityFlow_nextGoesToSecondStep() {
        actClickOnFab()
        actEnterTitle(testTitle)
        actMoveToNextStep()
        assertStepIs(2)
    }

    @Test
    fun addActivityFlow_nextDoesNotGoAnywhereWhenTitleNotInserted() {
        actClickOnFab()
        actMoveToNextStep()
        assertStepIs(1)
    }

    @Test
    fun addActivityFlow_imeActionDoesNotGoAnywhereWhenEmptyTitleInserted() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit("")
        assertStepIs(1)
    }

    @Test
    fun addActivityFlow_errorIsDisplayedWhenNextButtonPressedWithoutTitle() {
        actClickOnFab()
        actMoveToNextStep()
        assertEmptyTitleErrorDisplayed()
    }

    @Test
    fun addActivityFlow_errorIsDisplayedWhenImeActionUsedAndTitleEmpty() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit("")
        assertEmptyTitleErrorDisplayed()
    }

    @Test
    fun addActivityFlow_weStayOnTheSecondStepWhenRotated() {
        actClickOnFab()
        actEnterTitle(testTitle)
        actMoveToNextStep()
        assertStepIs(2)

        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape())

        assertStepIs(2)
    }

    @Test
    fun addActivityFlow_emptyTitleMessageIsClearedOnceUserEntersAtLeastOneCharacter() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit("")
        actEnterTitle("a")
        assertNoEmptyTitleErrorDisplayed()
    }


    @Test
    fun addActivityFlow_nextGoesToSecondStepAfterRotation() {
        actClickOnFab()
        actEnterTitle(testTitle)

        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape())

        assertStepIs(1)

        actMoveToNextStep()
    }

    @Test
    fun addActivityFlow_titleMultipleActivityRecordsCanBeAdded() {
        actExecuteAddActivityWithoutTimeFlow(testTitle)
        assertOnTimeline()

        val testTitle2 = "another title"
        actExecuteAddActivityWithoutTimeFlow(testTitle2)
        assertOnTimeline()

        // check if the activity was added to the list
        activityRecordsRepository.getActivityRecords().apply {
            shouldHaveSize(2)
            find { testTitle == it.title } .shouldNotBe(null)
            find { testTitle2 == it.title} .shouldNotBe(null)
        }
    }

    @Test
    fun addActivity_chooseStartTime() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        actEnterStartingTime()
        actClickOnFinishButton(buttonTextStartTracking)

        assertOnTimeline()

        activityRecordsRepository.getActivityRecords().shouldContainOneItemWhich {
            title.shouldBe(testTitle)
            duration.shouldBe(ActivityRecordDuration.Undergoing)
            start.toLocalTime().shouldBe(LocalTime.of(10,0))
        }
    }

    @Test
    fun addActivity_chooseStartTimeAndDate() {
        actClickOnFab()
        actEnterTitleToInputAndSubmit(testTitle)
        actEnterStartingTime()
        actEnterDate()
        actClickOnFinishButton(buttonTextStartTracking)

        assertOnTimeline()

        activityRecordsRepository.getActivityRecords().shouldContainOneItemWhich {
            title.shouldBe(testTitle)
            duration.shouldBe(ActivityRecordDuration.Undergoing)
            start.toLocalTime().shouldBe(LocalTime.of(10,0))
            start.toLocalDate().shouldBe(LocalDate.of(2018, 9, 25))
        }
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

        activityRecordsRepository.getActivityRecords().shouldContainOneItemWhich {
            title.shouldBe(testTitle)
            start.toLocalTime().shouldBe(LocalTime.of(10,0))
            start.toLocalDate().shouldBe(LocalDate.of(2018, 9, 25))
            duration.shouldBe(ActivityRecordDuration.Done(90))
        }
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
        actEnterTitle(testTitle)
        actMoveToNextStep()

        assertStepIs(2)

        Espresso.pressBack()
        Espresso.pressBack()

        assertStepIs(1)
    }

    @Test
    fun addActivity_backButtonFromTheFirstStepQuitsTheFlow() {
        actClickOnFab()

        runWithStepperLayoutSupport(activityRule.activity.findViewById<StepperLayout>(R.id.stepperLayout)) {
            Espresso.pressBack()
            assertOnTimeline()
        }
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
        actEnterTitle(testTitle)
        actMoveToNextStep()
        assertStepIs(2)
        actClickOnNavUpButton()
        assertOnTimeline()
    }

    private fun assertAnItemWithTitleWasAdded() {
        activityRecordsRepository.getActivityRecords().shouldContainOneItemWhich {
            title.shouldBe(testTitle)
        }
    }

    private fun assertEmptyTitleErrorDisplayed() {
        onView(withText(R.string.err_empty_title)).check(matches(isDisplayed()))
    }

    private fun assertNoEmptyTitleErrorDisplayed() {
        onView(withText(R.string.err_empty_title)).check(doesNotExist())
    }

    private fun isKeyboardShown(): Boolean {
        val inputMethodManager = InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
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

    private fun actEnterTitle(testTitle: String) {
        onView(withId(R.id.input_title))
                .check(matches(isCompletelyDisplayed()))
                .perform(typeText(testTitle))
    }

    private fun actMoveToNextStep() {
        onView(withId(R.id.stepperLayout)).perform(StepperNavigationActions.clickNext())
    }

    private fun actMoveToPrevStep() {
        onView(withId(R.id.stepperLayout))
                .perform(StepperNavigationActions.clickBack())
    }
}


inline fun List<ActivityRecord>.shouldContainOneItemWhich(block:  ActivityRecord.() -> Unit ) {
    apply {
        shouldHaveSize(1)
        get(0).apply(block)
    }
}
