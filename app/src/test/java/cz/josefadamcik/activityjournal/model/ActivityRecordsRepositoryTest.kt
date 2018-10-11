package cz.josefadamcik.activityjournal.model

import cz.josefadamcik.activityjournal.DateTimeProviderImpl
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.Month

/**
 * Timeline should look like this:
 *
 * 10:00 Undergoing
 * 8:00 Undergoing
 * 12:00 Finished
 * 10:00 Finished
 *
 */
class ActivityRecordsRepositoryTest {

    private val todayDate = LocalDate.of(2018, Month.OCTOBER, 12)
    private val time10AM = LocalTime.of(10, 0)
    private val time12AM = LocalTime.of(12, 0)
    private val time8AM = LocalTime.of(8, 0)

    private val activityRecord = ActivityRecord(
            title = "first activity",
            start = LocalDateTime.of(todayDate, time10AM),
            duration = ActivityRecordDuration.Done(60)
    )

    private val repository = ActivityRecordsRepository(DateTimeProviderImpl()).apply {
        add(activityRecord)
    }

    @Test
    fun `when new item's time is before first it should be returned as second`() {
        // arrange
        val record = activityRecord.copy(start = LocalDateTime.of(todayDate, time8AM))

        // act
        repository.add(record)

        // assert
        assertEquals(record, repository.getActivityRecords()[1])
    }

    @Test
    fun `when new item's time is after first it should be returned as first`() {
        // arrange
        val record = activityRecord.copy(start = LocalDateTime.of(todayDate, time12AM))

        // act
        repository.add(record)

        // assert
        assertEquals(record, repository.getActivityRecords()[0])
    }

    @Test
    fun `when new item is before first but undergoing it should be returned as first`() {
        // arrange
        val record = activityRecord.copy(start = LocalDateTime.of(todayDate, time8AM), duration = ActivityRecordDuration.Undergoing)

        // act
        repository.add(record)

        // assert
        assertEquals(record, repository.getActivityRecords()[0])
    }

    @Test
    fun `when two undergoing items are added they should be ordered by time`() {
        // arrange
        val record = activityRecord.copy(
                start = LocalDateTime.of(todayDate, time8AM),
                duration = ActivityRecordDuration.Undergoing)
        val record2 = activityRecord.copy(
                start = LocalDateTime.of(todayDate, LocalTime.of(9, 0)),
                duration = ActivityRecordDuration.Undergoing)

        // act
        repository.add(record)
        repository.add(record2)

        // assert
        assertEquals(record2, repository.getActivityRecords()[0])
        assertEquals(record, repository.getActivityRecords()[1])
    }

    @Test
    fun `when new item's date is after first it should be returned as first`() {
        // arrange
        val record = activityRecord.copy(start = LocalDateTime.of(
                LocalDate.of(2018, Month.OCTOBER, 13),
                time10AM
        ))

        // act
        repository.add(record)

        // assert
        assertEquals(record, repository.getActivityRecords()[0])
    }
}
