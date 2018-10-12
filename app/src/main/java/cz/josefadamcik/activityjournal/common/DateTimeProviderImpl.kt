package cz.josefadamcik.activityjournal.common

import org.threeten.bp.Clock
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

class DateTimeProviderImpl(
    private val clock: Clock = Clock.systemDefaultZone()
) : DateTimeProvider {
    override fun provideCurrentLocalTime(): LocalTime = LocalTime.now(clock)
    override fun provideCurrentLocalDate(): LocalDate = LocalDate.now(clock)

    override fun provideCurrentLocalDateTime(): LocalDateTime = LocalDateTime.now(clock)
}
