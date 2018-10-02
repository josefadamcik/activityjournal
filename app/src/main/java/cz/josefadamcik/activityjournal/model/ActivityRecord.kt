package cz.josefadamcik.activityjournal.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatterBuilder




sealed class ActivityRecordDuration {
    object Undergoing : ActivityRecordDuration()
    data class Done(val minutes: Int) : ActivityRecordDuration()
}

data class ActivityRecord(
    val title: String,
    val date: String,
    val time: String,
    val duration: ActivityRecordDuration
) {
    companion object {
        private val timeFormatter = DateTimeFormatterBuilder()
                .appendPattern("H:mm")
                .toFormatter()

        private val dateFormatter = DateTimeFormatterBuilder()
                .appendPattern("d.M.yyyy")
                .toFormatter()
    }

    val parsedDateTime : LocalDateTime
        get() = LocalDateTime.of(
                LocalDate.parse(date, dateFormatter),
                LocalTime.parse(time, timeFormatter)
        )

}