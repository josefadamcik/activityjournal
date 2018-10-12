package cz.josefadamcik.activityjournal.common

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

interface DateTimeProvider {
    fun provideCurrentLocalTime(): LocalTime
    fun provideCurrentLocalDate(): LocalDate
    fun provideCurrentLocalDateTime(): LocalDateTime
}
