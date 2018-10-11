package cz.josefadamcik.activityjournal

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import cz.josefadamcik.activityjournal.di.appModule
import org.koin.android.ext.android.startKoin

open class ActivityJournalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        initDi()
    }

    protected open fun initDi() {
        startKoin(this, listOf(appModule))
    }
}
