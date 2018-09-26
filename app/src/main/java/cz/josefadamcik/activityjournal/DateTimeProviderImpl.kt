package cz.josefadamcik.activityjournal

import java.text.SimpleDateFormat
import java.util.Date

class DateTimeProviderImpl : DateTimeProvider {
    private val timeFormat = SimpleDateFormat("H:mm")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy")

    override fun provideCurrentTime(): String {
        return timeFormat.format(Date())
    }

    override fun provideCurrentDate(): String {
        return dateFormat.format(Date())
    }
}