package cz.josefadamcik.activityjournal.di

import cz.josefadamcik.activityjournal.DateTimeProvider
import cz.josefadamcik.activityjournal.DateTimeProviderImpl
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import org.koin.dsl.module.module

val appModule = module {
    single<DateTimeProvider> { DateTimeProviderImpl() }
    single<ActivityRecordsRepository> { ActivityRecordsRepository(get()) }
}

