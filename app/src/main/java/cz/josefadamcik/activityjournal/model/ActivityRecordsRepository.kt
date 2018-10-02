package cz.josefadamcik.activityjournal.model

class ActivityRecordsRepository {
    private val activityRecordsList = mutableListOf<ActivityRecord>()
    private val parser = ActivityRecordTimeParser()

    fun add(item: ActivityRecord) {
        activityRecordsList.add(item)

        activityRecordsList.sortWith(Comparator<ActivityRecord> { record1: ActivityRecord, record2: ActivityRecord ->
            //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the
            if (record1.duration == ActivityRecordDuration.Undergoing
                    && record2.duration != ActivityRecordDuration.Undergoing) {
                return@Comparator -1
            } else if (record1.duration != ActivityRecordDuration.Undergoing
                    && record2.duration == ActivityRecordDuration.Undergoing) {
                return@Comparator 1
            } else {
                return@Comparator record2.start.compareTo(record1.start)
            }
        })
    }

    fun getActivityRecords(): List<ActivityRecord> = activityRecordsList
}