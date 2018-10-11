package cz.josefadamcik.activityjournal.test

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import cz.josefadamcik.activityjournal.TestApplication

class TestApplicationTestRunner : AndroidJUnitRunner() {

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApplication::class.java!!.getName(), context)
    }
}
