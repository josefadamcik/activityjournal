package cz.josefadamcik.activityjournal.screens.addactivity

import androidx.lifecycle.ViewModel
import cz.josefadamcik.activityjournal.common.DateTimeProvider
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.model.ActivityRecordTimeParser
import org.threeten.bp.LocalDateTime

/**
 * Captures information form the whole flow of the activity creation.
 */
class AddActivityFlowModel(
        private val defaultTitle: String,
        private val currentTimeProvider: DateTimeProvider,
        private val startParser: ActivityRecordTimeParser
) : ViewModel() {

    var title: String? = null
    var time: String? = null
    var date: String? = null
    var duration: String? = null

    fun produceActivityRecord(): ActivityRecord {
        return ActivityRecord(
                if (title.isNullOrEmpty()) defaultTitle else title as String,
                processStartDateTime(),
                if (duration.isNullOrEmpty())
                    ActivityRecordDuration.Undergoing
                else ActivityRecordDuration.Done(duration?.toInt() as Int)

        )
    }



    private fun processStartDateTime(): LocalDateTime {
        return LocalDateTime.of(
                if (date.isNullOrEmpty())
                    currentTimeProvider.provideCurrentLocalDate()
                else
                    startParser.parseDateField(date as String),
                if (time.isNullOrEmpty())
                    currentTimeProvider.provideCurrentLocalTime()
                else
                    startParser.parseTimeField(time as String)
        )
    }
}
