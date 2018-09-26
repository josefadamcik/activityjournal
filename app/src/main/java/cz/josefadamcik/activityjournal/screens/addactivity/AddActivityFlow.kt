package cz.josefadamcik.activityjournal.screens.addactivity

import cz.josefadamcik.activityjournal.DateTimeProvider
import cz.josefadamcik.activityjournal.model.ActivityRecord

/**
 * Captures information form the whole flow of the activity creation.
 */
class AddActivityFlow(
        private val defaultTitle: String,
        private val currentTimeProvider: DateTimeProvider
) {

    var title: String? = null
    var time: String? = null
    var date: String? = null


    fun produceActivityRecord(): ActivityRecord {
        return ActivityRecord(
                if (title.isNullOrEmpty()) defaultTitle else title as String,
                if (date.isNullOrEmpty()) currentTimeProvider.provideCurrentDate() else date as String,
                if (time.isNullOrEmpty()) currentTimeProvider.provideCurrentTime() else time as String)
    }

}
