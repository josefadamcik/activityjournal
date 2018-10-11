package cz.josefadamcik.activityjournal

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import cz.josefadamcik.activityjournal.test.clickChildViewWithId
import cz.josefadamcik.activityjournal.test.onRecyclerViewRowAtPositionCheck
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.*
import org.junit.runner.RunWith
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime

@RunWith(AndroidJUnit4::class)
@LargeTest
class TimelineTest {
    private val testTitle = "a new title for our activity"

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)



    companion object {
        @MockK
        lateinit var dateTimeProvider: DateTimeProvider

        @BeforeClass
        @JvmStatic
        fun setupAll() {
            MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
            CompositionRoot.dateTimeProvider = dateTimeProvider
            CompositionRoot.repository = ActivityRecordsRepository(dateTimeProvider)
        }

        @AfterClass
        @JvmStatic
        fun teartDownAll() {
            MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
            CompositionRoot.dateTimeProvider = DateTimeProviderImpl()
            CompositionRoot.repository = ActivityRecordsRepository(dateTimeProvider)
        }


    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        obtainCompositionRoot().clear()
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

        val activityRecords = obtainCompositionRoot().getActivityRecords()
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
        val start = LocalDateTime.of(2018, 10, 11, 8, 0)
        val durationMin: Long = 60
        every { dateTimeProvider.provideCurrentLocalDateTime() } returns start.plusMinutes(durationMin)

        arrangeData(
                arrangeUndergoingActivity(start)
        )

        val position = 0
        val buttonId = R.id.button_finish


        actLaunchActivity()
        actClickOnDescendantButtonInListItem(position, buttonId)

        val activityRecords = obtainCompositionRoot().getActivityRecords()
        activityRecords[position].duration.shouldBe(ActivityRecordDuration.Done(durationMin.toInt()))
    }

    private fun assertNoFinishButtonDisplayed(position: Int, buttonId: Int) {
        onRecyclerViewRowAtPositionCheck(R.id.list, position, allOf(
                hasDescendant(allOf(
                        withId(buttonId),
                        not(isDisplayed())
                ))
        ))
    }

    private fun actClickOnDescendantButtonInListItem(position: Int, buttonId: Int) {
        onView(withId(R.id.list))
                .perform(
                        RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position),
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, clickChildViewWithId(buttonId))
                )
        Espresso.onIdle()
    }


    private fun arrangeFinishedActivity(): ActivityRecord {
        return ActivityRecord(
                title = testTitle,
                start = LocalDateTime.now(),
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
            obtainCompositionRoot().add(it)
        }
    }

    private fun obtainCompositionRoot() = CompositionRoot.repository

    private fun actLaunchActivity() {
        activityRule.launchActivity(null)
    }
}
