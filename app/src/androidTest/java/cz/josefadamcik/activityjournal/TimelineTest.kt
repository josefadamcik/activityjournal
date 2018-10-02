package cz.josefadamcik.activityjournal

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.test.onRecyclerViewRowAtPositionCheck
import org.hamcrest.Matchers.*
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
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false )


    @Before
    fun setUp() {
        CompositionRoot.repository.clear()
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
            CompositionRoot.repository.add(it)
        }
    }

    private fun actLaunchActivity() {
        activityRule.launchActivity(null)
    }


}