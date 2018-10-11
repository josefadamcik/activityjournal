package cz.josefadamcik.activityjournal.test

import android.app.Activity

import androidx.test.rule.ActivityTestRule
import cz.josefadamcik.activityjournal.di.appModule
import org.koin.standalone.StandAloneContext

/**
 * Rule that uses koin and autostarts the activity
 */
class KoinActivityTestRule<T : Activity>(activityClass: Class<T>) : ActivityTestRule<T>(activityClass) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        StandAloneContext.startKoin(listOf(appModule))
    }

    override fun afterActivityFinished() {
        super.afterActivityFinished()
        StandAloneContext.stopKoin()
    }
}
