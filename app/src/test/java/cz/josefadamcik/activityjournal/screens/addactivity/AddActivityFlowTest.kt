package cz.josefadamcik.activityjournal.screens.addactivity

import cz.josefadamcik.activityjournal.DateTimeProvider
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AddActivityFlowTest {
    private val defaultTitle = "default title"
    private val currentTime = "12:00"
    private val currentDate = "12.10.2018"

    private val currentDateTimeProvider = mockk<DateTimeProvider>() {
        every { provideCurrentTime() } returns currentTime
        every { provideCurrentDate() } returns currentDate
    }

    private lateinit var flow: AddActivityFlow

    @Before
    fun setUp() {
        flow = AddActivityFlow(defaultTitle, currentDateTimeProvider)
    }

    @Test
    fun nullTitle_defaultTitleIsProvided() {
        flow.title = null

        val activityRecord = flow.produceActivityRecord()

        activityRecord.title.shouldBe(defaultTitle)
    }

    @Test
    fun emptyTitle_defaultTitleIsProvided() {
        flow.title = ""

        val activityRecord = flow.produceActivityRecord()

        activityRecord.title.shouldBe(defaultTitle)
    }

    @Test
    fun nullTime_currentTimeIsUsed() {
        flow.time = null

        val activityRecord = flow.produceActivityRecord()

        activityRecord.time.shouldBe(currentTime)
    }

    @Test
    fun emptyDate_currentTimeIsUsed() {
        flow.date = ""

        val activityRecord = flow.produceActivityRecord()

        activityRecord.date.shouldBe(currentDate)
    }

    @Test
    fun nullDate_currentTimeIsUsed() {
        flow.date = null
        val activityRecord = flow.produceActivityRecord()

        activityRecord.date.shouldBe(currentDate)
    }

    @Test
    fun nullDuration_activityRecordContainsNullDuration() {
        flow.duration = null
        val activityRecord = flow.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Undergoing)
    }

    @Test
    fun emptyDuration_activityRecordContainsNullDuration() {
        flow.duration = ""
        val activityRecord = flow.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Undergoing)
    }

    @Test
    fun filledDuration_activityRecordContainsNullDuration() {
        val durationMinutes = 10
        flow.duration = "$durationMinutes"

        val activityRecord = flow.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Done(durationMinutes))
    }
}