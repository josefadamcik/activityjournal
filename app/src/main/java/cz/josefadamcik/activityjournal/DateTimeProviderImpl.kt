package cz.josefadamcik.activityjournal

import org.threeten.bp.Clock
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import java.text.SimpleDateFormat
import java.util.Date

class DateTimeProviderImpl(
    private val clock : Clock = Clock.systemDefaultZone()
) : DateTimeProvider {
    override fun provideCurrentLocalTime(): LocalTime = LocalTime.now(clock)
    override fun provideCurrentLocalDate(): LocalDate = LocalDate.now(clock)
}