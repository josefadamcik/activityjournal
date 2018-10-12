package cz.josefadamcik.activityjournal.screens.addactivity

import cz.josefadamcik.activityjournal.common.DateTimeProvider
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.model.ActivityRecordTimeParser
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.Month

class AddActivityFlowModelTest {
    private val defaultTitle = "default title"
    private val currentTime = "12:00"
    private val currentDate = "12.10.2018"

    private val currentDateTimeProvider = mockk<DateTimeProvider>() {
        every { provideCurrentLocalTime() } returns LocalTime.of(12, 0)
        every { provideCurrentLocalDate() } returns LocalDate.of(2018, Month.OCTOBER, 12)
    }

    private lateinit var modelFlow: AddActivityModelFlow

    @Before
    fun setUp() {
        modelFlow = AddActivityModelFlow(defaultTitle, currentDateTimeProvider, ActivityRecordTimeParser())
    }

    @Test
    fun nullTitle_defaultTitleIsProvided() {
        modelFlow.title = null

        val activityRecord = modelFlow.produceActivityRecord()

        activityRecord.title.shouldBe(defaultTitle)
    }

    @Test
    fun emptyTitle_defaultTitleIsProvided() {
        modelFlow.title = ""

        val activityRecord = modelFlow.produceActivityRecord()

        activityRecord.title.shouldBe(defaultTitle)
    }

    @Test
    fun nullTime_currentTimeIsUsed() {
        modelFlow.time = null

        val activityRecord = modelFlow.produceActivityRecord()

        activityRecord.start.toLocalTime().shouldBe(
                LocalTime.of(12, 0)
        )
    }

    @Test
    fun emptyDate_currentTimeIsUsed() {
        modelFlow.date = ""

        val activityRecord = modelFlow.produceActivityRecord()

        activityRecord.start.toLocalDate().shouldBe(
                LocalDate.of(2018, Month.OCTOBER, 12)
        )
    }

    @Test
    fun nullDate_currentTimeIsUsed() {
        modelFlow.date = null
        val activityRecord = modelFlow.produceActivityRecord()

        activityRecord.start.toLocalDate().shouldBe(
                LocalDate.of(2018, Month.OCTOBER, 12)
        )
    }

    @Test
    fun nullDuration_activityRecordContainsNullDuration() {
        modelFlow.duration = null
        val activityRecord = modelFlow.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Undergoing)
    }

    @Test
    fun emptyDuration_activityRecordContainsNullDuration() {
        modelFlow.duration = ""
        val activityRecord = modelFlow.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Undergoing)
    }

    @Test
    fun filledDuration_activityRecordContainsNullDuration() {
        val durationMinutes = 10
        modelFlow.duration = "$durationMinutes"

        val activityRecord = modelFlow.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Done(durationMinutes))
    }
}
