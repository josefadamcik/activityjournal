package cz.josefadamcik.activityjournal.test

import android.app.Activity
import android.app.Application
import androidx.test.InstrumentationRegistry

import androidx.test.rule.ActivityTestRule
import cz.josefadamcik.activityjournal.di.appModule
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import org.koin.log.Logger
import org.koin.standalone.StandAloneContext

fun <T : Activity> ActivityTestRule<T>.startKoin(
    modules: List<Module>,
    extraProperties: Map<String, Any> = HashMap(),
    loadProperties: Boolean = false,
    logger: Logger = AndroidLogger()
) {
    (androidx.test.InstrumentationRegistry.getTargetContext().applicationContext as Application).apply {
        startKoin(this, modules, extraProperties, loadProperties, logger)
    }
}

/**
 * Rule that uses koin and autostarts the activity
 */
class KoinActivityTestRule<T : Activity>(activityClass: Class<T>) : ActivityTestRule<T>(activityClass) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        startKoin(listOf(appModule))
    }

    override fun afterActivityFinished() {
        super.afterActivityFinished()
        StandAloneContext.stopKoin()
    }
}
