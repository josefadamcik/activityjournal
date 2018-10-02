package cz.josefadamcik.activityjournal

import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository

object CompositionRoot {
    val repository: ActivityRecordsRepository by lazy { ActivityRecordsRepository() }
}
