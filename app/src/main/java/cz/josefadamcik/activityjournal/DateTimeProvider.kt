package cz.josefadamcik.activityjournal

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

interface DateTimeProvider {
    fun provideCurrentLocalTime(): LocalTime
    fun provideCurrentLocalDate(): LocalDate
}
