package cz.josefadamcik.activityjournal.screens.addactivity

import cz.josefadamcik.activityjournal.common.DateTimeProvider
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.model.ActivityRecordTimeParser
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
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

    private val activityRecordRepository = mockk<ActivityRecordsRepository>()
    private lateinit var flowModel: AddActivityFlowModel

    @Before
    fun setUp() {
        flowModel = AddActivityFlowModel(defaultTitle, currentDateTimeProvider, ActivityRecordTimeParser(), activityRecordsRepository = activityRecordRepository)
    }

    @Test
    fun nullTitle_defaultTitleIsProvided() {
        flowModel.title = null

        val activityRecord = flowModel.produceActivityRecord()

        activityRecord.title.shouldBe(defaultTitle)
    }

    @Test
    fun emptyTitle_defaultTitleIsProvided() {
        flowModel.title = ""

        val activityRecord = flowModel.produceActivityRecord()

        activityRecord.title.shouldBe(defaultTitle)
    }

    @Test
    fun nullTime_currentTimeIsUsed() {
        flowModel.time = null

        val activityRecord = flowModel.produceActivityRecord()

        activityRecord.start.toLocalTime().shouldBe(
                LocalTime.of(12, 0)
        )
    }

    @Test
    fun emptyDate_currentTimeIsUsed() {
        flowModel.date = ""

        val activityRecord = flowModel.produceActivityRecord()

        activityRecord.start.toLocalDate().shouldBe(
                LocalDate.of(2018, Month.OCTOBER, 12)
        )
    }

    @Test
    fun nullDate_currentTimeIsUsed() {
        flowModel.date = null
        val activityRecord = flowModel.produceActivityRecord()

        activityRecord.start.toLocalDate().shouldBe(
                LocalDate.of(2018, Month.OCTOBER, 12)
        )
    }

    @Test
    fun nullDuration_activityRecordContainsNullDuration() {
        flowModel.duration = null
        val activityRecord = flowModel.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Undergoing)
    }

    @Test
    fun emptyDuration_activityRecordContainsNullDuration() {
        flowModel.duration = ""
        val activityRecord = flowModel.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Undergoing)
    }

    @Test
    fun filledDuration_activityRecordContainsNullDuration() {
        val durationMinutes = 10
        flowModel.duration = "$durationMinutes"

        val activityRecord = flowModel.produceActivityRecord()

        activityRecord.duration.shouldBe(ActivityRecordDuration.Done(durationMinutes))
    }
}
