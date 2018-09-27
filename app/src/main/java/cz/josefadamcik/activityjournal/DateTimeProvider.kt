package cz.josefadamcik.activityjournal

interface DateTimeProvider {
    fun provideCurrentTime(): String
    fun provideCurrentDate(): String
}