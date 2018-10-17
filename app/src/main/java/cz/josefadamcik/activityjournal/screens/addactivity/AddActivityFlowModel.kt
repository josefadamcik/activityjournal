package cz.josefadamcik.activityjournal.screens.addactivity

import android.database.Observable
import androidx.lifecycle.ViewModel
import cz.josefadamcik.activityjournal.common.DateTimeProvider
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import cz.josefadamcik.activityjournal.model.ActivityRecordTimeParser
import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import org.threeten.bp.LocalDateTime
import java.util.*
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty

/**
 * Captures information form the whole flow of the activity creation.
 */
class AddActivityFlowModel(
        private val defaultTitle: String,
        private val currentTimeProvider: DateTimeProvider,
        private val startParser: ActivityRecordTimeParser,
        private val activityRecordsRepository: ActivityRecordsRepository
) : ViewModel() {

    //TODO: this is plainly wrong
    var onCurrentStepChangeRequired: (step: Int) -> Unit = {}
    var title: String? = null
    var time: String? = null
    var date: String? = null
    var duration: String? = null
    public var currentStep: Int by Delegates.observable(0,
            onChange = {_, _, newValue ->
                onCurrentStepChangeRequired(newValue)
            })
            private set


    fun produceActivityRecord(): ActivityRecord {
        return ActivityRecord(
                if (title.isNullOrEmpty()) defaultTitle else title as String,
                processStartDateTime(),
                if (duration.isNullOrEmpty())
                    ActivityRecordDuration.Undergoing
                else ActivityRecordDuration.Done(duration?.toInt() as Int)

        )
    }

    fun produceState(): Int = currentStep
    fun restoreState(step: Int) {
        currentStep = step
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


    fun storeNewActivityRecord() {
        activityRecordsRepository.add(produceActivityRecord())
    }


    fun moveToNextStep() {
        currentStep++
    }

    fun onStepSelected(newStepPosition: Int) {
        currentStep = newStepPosition
    }
}
