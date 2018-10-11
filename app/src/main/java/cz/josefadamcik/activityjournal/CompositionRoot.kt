package cz.josefadamcik.activityjournal

import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository

object CompositionRoot {
    var dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()
    var repository: ActivityRecordsRepository =  ActivityRecordsRepository(dateTimeProvider)
}
