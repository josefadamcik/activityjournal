package cz.josefadamcik.activityjournal.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatterBuilder

class ActivityRecordTimeParser {
    private val timeFormatter = DateTimeFormatterBuilder()
            .appendPattern("H:mm")
            .toFormatter()

    private val dateFormatter = DateTimeFormatterBuilder()
            .appendPattern("d.M.yyyy")
            .toFormatter()

    fun parseTimeField(time: String): LocalTime = LocalTime.parse(time, timeFormatter)

    fun parseDateField(date: String): LocalDate = LocalDate.parse(date, dateFormatter)


}