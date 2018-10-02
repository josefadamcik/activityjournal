package cz.josefadamcik.activityjournal

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class ActivityJournalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
