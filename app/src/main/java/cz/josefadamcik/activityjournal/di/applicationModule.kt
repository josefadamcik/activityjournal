package cz.josefadamcik.activityjournal.di

import cz.josefadamcik.activityjournal.DateTimeProvider
import cz.josefadamcik.activityjournal.DateTimeProviderImpl
import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.model.ActivityRecordTimeParser
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import cz.josefadamcik.activityjournal.screens.addactivity.AddActivityFlow
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val appModule = module {
    single<DateTimeProvider> { DateTimeProviderImpl() }
    single<ActivityRecordsRepository> { ActivityRecordsRepository(get()) }
    factory { ActivityRecordTimeParser() }
    factory { AddActivityFlow(androidContext().resources.getString(R.string.add_activity_default_title), get(), get()) }
}
