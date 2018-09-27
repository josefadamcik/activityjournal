package cz.josefadamcik.activityjournal.model

sealed class ActivityRecordDuration {
    object Undergoing : ActivityRecordDuration()
    data class Done(val minutes: Int) : ActivityRecordDuration()
}

data class ActivityRecord(
    val title: String,
    val date: String,
    val time: String,
    val duration: ActivityRecordDuration
)