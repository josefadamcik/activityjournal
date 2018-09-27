package cz.josefadamcik.activityjournal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.screens.addactivity.AddActivityFlowFragment
import cz.josefadamcik.activityjournal.screens.addactivity.AddActivityTimeFragment
import cz.josefadamcik.activityjournal.screens.addactivity.AddActivityTitleFragment
import cz.josefadamcik.activityjournal.screens.timeline.TimelineFragment

/**
 * The main and only activity in the application.
 */
class MainActivity : AppCompatActivity(),
        TimelineFragment.OnFragmentInteractionListener,
        AddActivityFlowFragment.OnFragmentInteractionListener,
        AddActivityTimeFragment.OnFragmentInteractionListener,
        AddActivityTitleFragment.OnFragmentInteractionListener {

    private val activityRecordsList = mutableListOf<ActivityRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, TimelineFragment.newInstance())
                    .commit()
            supportFragmentManager.executePendingTransactions()
            findTimelineFragment()?.showRecords(activityRecordsList)
        }
    }

    override fun onNavigationToAddActivityRecord() {
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AddActivityFlowFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    override fun onAddActivityTimeFinished(activityRecord: ActivityRecord) {
        supportFragmentManager.popBackStackImmediate()
        activityRecordsList.add(activityRecord)
        findTimelineFragment()?.showRecords(activityRecordsList)
    }

    private fun findTimelineFragment(): TimelineFragment? {
        return supportFragmentManager.findFragmentById(android.R.id.content) as TimelineFragment?
    }

    private fun findAddActivityFlowFragment(): AddActivityFlowFragment? {
        return supportFragmentManager.findFragmentById(android.R.id.content) as AddActivityFlowFragment?
    }

    override fun onAddActivityTimeFinished(time: String, date: String, duration: String) {
        findAddActivityFlowFragment()?.onAddActivityTimeFinished(time, date, duration)
    }

    override fun onAddActivityTitleFinished(title: String) {
        findAddActivityFlowFragment()?.onAddActivityTitleFinished(title)
    }
}
