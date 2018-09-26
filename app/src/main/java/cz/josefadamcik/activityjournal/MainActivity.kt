package cz.josefadamcik.activityjournal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.screens.addactivity.AddActivityFlow
import cz.josefadamcik.activityjournal.screens.addactivity.AddActivityTimeFragment
import cz.josefadamcik.activityjournal.screens.addactivity.AddActivityTitleFragment
import cz.josefadamcik.activityjournal.screens.timeline.TimelineFragment

/**
 * The main and only activity in the application.
 */
class MainActivity : AppCompatActivity(), TimelineFragment.OnFragmentInteractionListener,
        AddActivityTitleFragment.OnFragmentInteractionListener,
        AddActivityTimeFragment.OnFragmentInteractionListener {

    private val activityRecordsList = mutableListOf<ActivityRecord>()
    private val dateTimeProvider : DateTimeProvider = DateTimeProviderImpl()
    private var addActivityFlow : AddActivityFlow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, TimelineFragment.newInstance())
                    .commit()
            supportFragmentManager.executePendingTransactions()
            val timelineFragment = findTimelineFragment()
            timelineFragment.showRecords(activityRecordsList)
        }
    }

    override fun onNavigationToAddActivityRecord() {

        //start the add activity flow
        addActivityFlow = AddActivityFlow(getString(R.string.add_activity_default_title), dateTimeProvider)

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AddActivityTitleFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }


    override fun onAddActivityTitleFinished(title: String) {
        // second step finished
        addActivityFlow?.title = title

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AddActivityTimeFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    override fun onAddActivityTimeFinished(time: String, date: String, duration: String) {
        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.popBackStackImmediate()

        val timelineFragment = findTimelineFragment()
        addActivityFlow?.let {
            it.time = time
            it.date = date
            it.duration = duration
        }

        addActivityFlow?.apply {
            activityRecordsList.add(produceActivityRecord())
        }

        timelineFragment.showRecords(activityRecordsList)

    }

    private fun findTimelineFragment(): TimelineFragment {
        return supportFragmentManager.findFragmentById(android.R.id.content) as TimelineFragment
    }
}
