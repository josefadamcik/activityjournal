package cz.josefadamcik.activityjournal.di

import cz.josefadamcik.activityjournal.common.DateTimeProvider
import cz.josefadamcik.activityjournal.common.DateTimeProviderImpl
import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.model.ActivityRecordTimeParser
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import cz.josefadamcik.activityjournal.screens.addactivity.AddActivityFlowModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single<DateTimeProvider> { DateTimeProviderImpl() }
    single<ActivityRecordsRepository> { ActivityRecordsRepository(get()) }
    factory { ActivityRecordTimeParser() }

    module("screens.addactivity") {
        viewModel {
            AddActivityFlowModel(
                    androidContext().resources.getString(R.string.add_activity_default_title),
                    get(),
                    get()
            )
        }
    }

}
