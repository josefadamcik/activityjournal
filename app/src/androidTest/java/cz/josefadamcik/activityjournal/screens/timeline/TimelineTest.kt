package cz.josefadamcik.activityjournal.screens.timeline

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import cz.josefadamcik.activityjournal.common.DateTimeProvider
import cz.josefadamcik.activityjournal.MainActivity
import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.di.appModule
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import cz.josefadamcik.activityjournal.test.*
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.threeten.bp.LocalDateTime

@RunWith(AndroidJUnit4::class)
@LargeTest
class TimelineTest : KoinTest {
    private val testTitle = "a new title for our activity"

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)

    private val dateTimeProvider by inject<DateTimeProvider>()
    private val activityRecordsRepository by inject<ActivityRecordsRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        activityRule.startKoin(listOf(appModule))
    }

    @After
    fun tearDown() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun applicationStart_activityDisplays() {

        actLaunchActivity()

        onView(withId(R.id.fab))
                .check(matches(isDisplayed()))
        onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()))
    }

    @Test
    fun undergoingActivityHasAFinishButton() {
        arrangeData(
            arrangeUndergoingActivity()
        )

        actLaunchActivity()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(allOf(
                        withId(R.id.button_finish),
                        isDisplayed()
                ))
        ))
    }

    @Test
    fun pastActivityHasNotAFinishButton() {
        arrangeData(
            arrangeFinishedActivity()
        )

        actLaunchActivity()

        assertNoFinishButtonDisplayed(0, R.id.button_finish)
    }

    @Test
    fun clickOnFinishButton_changesActivityRecordToPast() {
        arrangeData(
            arrangeUndergoingActivity()
        )
        val position = 0
        val buttonId = R.id.button_finish

        actLaunchActivity()
        actClickOnDescendantButtonInListItem(position, buttonId)

        val activityRecords = activityRecordsRepository.getActivityRecords()
        activityRecords[position].duration.shouldBeTypeOf<ActivityRecordDuration.Done>()
    }

    @Test
    fun clickOnFinishButton_displaysChangedActivityOnTimeline() {
        arrangeData(
                arrangeUndergoingActivity()
        )
        val position = 0
        val buttonId = R.id.button_finish

        actLaunchActivity()
        actClickOnDescendantButtonInListItem(position, buttonId)

        assertNoFinishButtonDisplayed(position, buttonId)
    }

    @Test
    fun clickOnFinishButton_changesActivityDurationToCorrectNumber() {
        val start = arrangeStartDate()
        val durationMin: Long = 60
        declareMockK<DateTimeProvider>()
        every { dateTimeProvider.provideCurrentLocalDateTime() } returns start.plusMinutes(durationMin)

        arrangeData(
                arrangeUndergoingActivity(start)
        )

        val position = 0
        val buttonId = R.id.button_finish

        actLaunchActivity()
        actClickOnDescendantButtonInListItem(position, buttonId)

        val activityRecords = activityRecordsRepository.getActivityRecords()
        activityRecords[position].duration.shouldBe(ActivityRecordDuration.Done(durationMin.toInt()))
    }

    @Test
    fun undergoingRecord_isProperlyDisplayed() {
        val start = arrangeStartDate()
        arrangeData(
                arrangeUndergoingActivity(start)
        )

        actLaunchActivity()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(withText("8:00")),
                hasDescendant(withText("11.10.2018")),
                hasDescendant(withText("Undergoing")),
                hasDescendant(allOf(
                        withId(R.id.button_finish),
                        isDisplayed()
                ))
        ))
    }

    @Test
    fun doneRecord_isProperlyDisplayed() {
        val start = arrangeStartDate()
        arrangeData(
                arrangeFinishedActivity(start)
        )

        actLaunchActivity()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(withText("8:00")),
                hasDescendant(withText("11.10.2018")),
                hasDescendant(withText("60")),
                hasDescendant(allOf(
                        withId(R.id.button_finish),
                        not(isDisplayed())
                ))
        ))
    }

    @Test
    fun content_shouldSurviveRotation() {
        arrangeData(
                arrangeFinishedActivity(arrangeStartDate())
        )

        actLaunchActivity()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle))
        ))

        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape())

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle))
        ))
    }

    private fun assertNoFinishButtonDisplayed(position: Int, buttonId: Int) {
        onRecyclerViewRowAtPositionCheck(R.id.list, position, allOf(
                hasDescendant(allOf(
                        withId(buttonId),
                        not(isDisplayed())
                ))
        ))
    }

    private fun arrangeStartDate() = LocalDateTime.of(2018, 10, 11, 8, 0)

    private fun actClickOnDescendantButtonInListItem(position: Int, buttonId: Int) {
        onView(withId(R.id.list))
                .perform(
                        RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position),
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, clickChildViewWithId(buttonId))
                )
        Espresso.onIdle()
    }

    private fun arrangeFinishedActivity(start: LocalDateTime = LocalDateTime.now()): ActivityRecord {
        return ActivityRecord(
                title = testTitle,
                start = start,
                duration = ActivityRecordDuration.Done(60)
        )
    }

    private fun arrangeUndergoingActivity(start: LocalDateTime = LocalDateTime.now()): ActivityRecord {
        return ActivityRecord(
                title = testTitle,
                start = start,
                duration = ActivityRecordDuration.Undergoing
        )
    }

    private fun arrangeData(vararg items: ActivityRecord) {
        items.forEach {
            activityRecordsRepository.add(it)
        }
    }

    private fun actLaunchActivity() {
        activityRule.launchActivity(null)
    }
}
