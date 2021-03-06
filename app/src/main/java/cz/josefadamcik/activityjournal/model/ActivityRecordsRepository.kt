package cz.josefadamcik.activityjournal.model

import cz.josefadamcik.activityjournal.common.DateTimeProvider
import org.threeten.bp.Duration

class ActivityRecordsRepository(
    private val dateTimeProvider: DateTimeProvider
) {
    private var activityRecordsList = mutableListOf<ActivityRecord>()
    private val parser = ActivityRecordTimeParser()

    fun add(item: ActivityRecord) {
        activityRecordsList.add(item)

        activityRecordsList.sortWith(Comparator<ActivityRecord> {
            record1: ActivityRecord, record2: ActivityRecord ->
            // a negative integer, zero, or a positive integer as the first
            // argument is less than, equal to, or greater than the

            return@Comparator if (record1.duration == ActivityRecordDuration.Undergoing &&
                    record2.duration != ActivityRecordDuration.Undergoing) {
                -1
            } else if (record1.duration != ActivityRecordDuration.Undergoing &&
                    record2.duration == ActivityRecordDuration.Undergoing) {
                1
            } else {
                record2.start.compareTo(record1.start)
            }
        })
    }

    fun getActivityRecords(): List<ActivityRecord> = activityRecordsList
    fun clear() {
        activityRecordsList.clear()
    }

    fun makeItemDone(item: ActivityRecord) {
        val now = dateTimeProvider.provideCurrentLocalDateTime()
        activityRecordsList = activityRecordsList.asSequence().map {
            if (item == it) {
                it.copy(duration = ActivityRecordDuration.Done(Duration.between(it.start, now).toMinutes().toInt()))
            } else {
                it
            }
        }.toMutableList()
    }
}
