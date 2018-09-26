package cz.josefadamcik.activityjournal.screens.addactivity

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import cz.josefadamcik.activityjournal.DateTimeProvider
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AddActivityFlowTest {
    private val defaultTitle = "default title"
    private val currentTime = "12:00"
    private val currentDate = "12.10.2018"


    private val currentDateTimeProvider = mock<DateTimeProvider> {
        on { provideCurrentTime() } doReturn currentTime
        on { provideCurrentDate() } doReturn currentDate
    }

    private lateinit var flow : AddActivityFlow

    @Before
    fun setUp() {
        flow = AddActivityFlow(defaultTitle, currentDateTimeProvider)
    }

    @Test
    fun nullTitle_defaultTitleIsProvided() {
        flow.title = null

        val activityRecord = flow.produceActivityRecord()

        assertThatDefaultTitleWasUsed(activityRecord)
    }

    @Test
    fun emptyTitle_defaultTitleIsProvided() {
        flow.title = ""

        val activityRecord = flow.produceActivityRecord()

        assertThatDefaultTitleWasUsed(activityRecord)
    }

    @Test
    fun nullTime_currentTimeIsUsed() {
        flow.time = null

        val activityRecord = flow.produceActivityRecord()

        assertThatCurrentTimeWasUsed(activityRecord)
    }

    @Test
    fun emptyDate_currentTimeIsUsed() {
        flow.date = ""

        val activityRecord = flow.produceActivityRecord()

        assertThatCurrentDateWasUsed(activityRecord)
    }

    @Test
    fun nullDate_currentTimeIsUsed() {
        flow.date = null
        val activityRecord = flow.produceActivityRecord()

        assertThatCurrentDateWasUsed(activityRecord)
    }


    @Test
    fun nullDuration_activityRecordContainsNullDuration() {
        flow.duration = null
        val activityRecord = flow.produceActivityRecord()


        assertEquals(ActivityRecordDuration.Undergoing, activityRecord.duration)
    }

    @Test
    fun emptyDuration_activityRecordContainsNullDuration() {
        flow.duration = ""
        val activityRecord = flow.produceActivityRecord()

        assertEquals(ActivityRecordDuration.Undergoing, activityRecord.duration)
    }

    @Test
    fun filledDuration_activityRecordContainsNullDuration() {
        flow.duration = "10"
        val activityRecord = flow.produceActivityRecord()

        assertTrue {
            activityRecord.duration is ActivityRecordDuration.Done
                && (activityRecord.duration as ActivityRecordDuration.Done).minutes == 10
        }
    }

    private fun assertThatDefaultTitleWasUsed(activityRecord: ActivityRecord) {
        assertTrue {
            activityRecord.title.isNotEmpty() && defaultTitle == activityRecord.title
        }
    }

    private fun assertThatCurrentTimeWasUsed(activityRecord: ActivityRecord) {
        assertTrue {
            activityRecord.time.isNotEmpty() && currentTime == activityRecord.time
        }
    }

    private fun assertThatCurrentDateWasUsed(activityRecord: ActivityRecord) {
        assertTrue {
            activityRecord.date.isNotEmpty() && currentDate == activityRecord.date
        }
    }


}