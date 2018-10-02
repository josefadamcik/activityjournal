package cz.josefadamcik.activityjournal.model

import org.threeten.bp.LocalDateTime

sealed class ActivityRecordDuration {
    object Undergoing : ActivityRecordDuration()
    data class Done(val minutes: Int) : ActivityRecordDuration()
}

data class ActivityRecord(
    val title: String,
    val start: LocalDateTime,
    val duration: ActivityRecordDuration
)