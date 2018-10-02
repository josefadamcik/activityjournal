package cz.josefadamcik.activityjournal.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


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


    private val activityRecord = ActivityRecord(
            title = "first activity",
            time = "10:00",
            date = "12.10.2018",
            duration = ActivityRecordDuration.Done(60)
    )

    private val repository = ActivityRecordsRepository().apply {
        add(activityRecord)
    }


    @Test
    fun `when new item's time is before first it should be returned as second`() {
        //arrange
        val record = activityRecord.copy(time = "8:00")

        //act
        repository.add(record)

        //assert
        assertEquals(record, repository.getActivityRecords()[1])
    }


    @Test
    fun `when new item's time is after first it should be returned as first`() {
        //arrange
        val record = activityRecord.copy(time = "12:00")

        //act
        repository.add(record)

        //assert
        assertEquals(record, repository.getActivityRecords()[0])
    }

    @Test
    fun `when new item is before first but undergoing it should be returned as first`() {
        //arrange
        val record = activityRecord.copy(time = "8:00", duration = ActivityRecordDuration.Undergoing)

        //act
        repository.add(record)

        //assert
        assertEquals(record, repository.getActivityRecords()[0])
    }

    @Test
    fun `when two undergoing items are added they should be ordered by time`() {
        //arrange
        val record = activityRecord.copy(time = "8:00", duration = ActivityRecordDuration.Undergoing)
        val record2 = activityRecord.copy(time = "9:00", duration = ActivityRecordDuration.Undergoing)

        //act
        repository.add(record)
        repository.add(record2)

        //assert
        assertEquals(record2, repository.getActivityRecords()[0])
        assertEquals(record, repository.getActivityRecords()[1])
    }


    @Test
    fun `when new item's date is after first it should be returned as first`() {
        //arrange
        val record = activityRecord.copy(date = "13.10.2018")

        //act
        repository.add(record)

        //assert
        assertEquals(record, repository.getActivityRecords()[0])
    }



}