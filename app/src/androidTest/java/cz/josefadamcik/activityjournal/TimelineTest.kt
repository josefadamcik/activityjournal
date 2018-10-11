package cz.josefadamcik.activityjournal

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.test.clickChildViewWithId
import cz.josefadamcik.activityjournal.test.onRecyclerViewRowAtPositionCheck
import io.kotlintest.matchers.types.shouldBeTypeOf
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime

@RunWith(AndroidJUnit4::class)
@LargeTest
class TimelineTest {
    private val testTitle = "a new title for our activity"

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)

    @Before
    fun setUp() {
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

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
                hasDescendant(withText(testTitle)),
                hasDescendant(allOf(
                        withId(R.id.button_finish),
                        not(isDisplayed())
                ))
        ))
    }

    @Test
    fun clickOnFinishButton_changesActivityRecordToPast() {
        arrangeData(
            arrangeUndergoingActivity()
        )

        actLaunchActivity()

        onView(ViewMatchers.withId(R.id.list))
                .perform(
                        RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0),
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.button_finish))
                )

        Espresso.onIdle()

        val activityRecords = obtainCompositionRoot().getActivityRecords()

        activityRecords[0].duration.shouldBeTypeOf<ActivityRecordDuration.Done>()

        onRecyclerViewRowAtPositionCheck(R.id.list, 0, allOf(
            hasDescendant(allOf(
                    withId(R.id.button_finish),
                    not(isDisplayed())
            ))
        ))
    }


    private fun arrangeFinishedActivity(): ActivityRecord {
        return ActivityRecord(
                title = testTitle,
                start = LocalDateTime.now(),
                duration = ActivityRecordDuration.Done(60)
        )
    }

    private fun arrangeUndergoingActivity(): ActivityRecord {
        return ActivityRecord(
                title = testTitle,
                start = LocalDateTime.now(),
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
