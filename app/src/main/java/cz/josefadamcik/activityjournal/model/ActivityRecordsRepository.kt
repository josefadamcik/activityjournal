package cz.josefadamcik.activityjournal.model

class ActivityRecordsRepository {
    private val activityRecordsList = mutableListOf<ActivityRecord>()

    fun add(item: ActivityRecord) {
        activityRecordsList.add(item)


        activityRecordsList.sortWith(Comparator<ActivityRecord> { record1: ActivityRecord, record2: ActivityRecord ->
            //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the
            val parsedDateTime1 = record1.parsedDateTime
            val parsedDateTime2 = record2.parsedDateTime
            if (record1.duration == ActivityRecordDuration.Undergoing
                    && record2.duration != ActivityRecordDuration.Undergoing) {
                return@Comparator -1
            } else if (record1.duration != ActivityRecordDuration.Undergoing
                    && record2.duration == ActivityRecordDuration.Undergoing) {
                return@Comparator 1
            } else {
                return@Comparator parsedDateTime2.compareTo(parsedDateTime1)
            }
        })
    }

    fun getActivityRecords(): List<ActivityRecord> = activityRecordsList
}