package cz.josefadamcik.activityjournal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.josefadamcik.activityjournal.common.ui.BackButtonPressConsumer
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, TimelineFragment.newInstance())
                    .commit()
            supportFragmentManager.executePendingTransactions()
        }
    }

    override fun onNavigationToAddActivityRecord() {
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AddActivityFlowFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    override fun onAddActivityFlowFinished() {
        supportFragmentManager.popBackStack()
    }

    override fun onFinishForActivityRecordClicked() {
        findTimelineFragment()?.refreshList()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(android.R.id.content)
        if (fragment is BackButtonPressConsumer && fragment.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    private fun findTimelineFragment(): TimelineFragment? {
        return supportFragmentManager.findFragmentById(android.R.id.content) as TimelineFragment?
    }

    private fun findAddActivityFlowFragment(): AddActivityFlowFragment? {
        return supportFragmentManager.findFragmentById(android.R.id.content) as AddActivityFlowFragment?
    }

    override fun onAddActivityTimeFinished() {
        findAddActivityFlowFragment()?.onAddActivityTimeFinished()
    }

    override fun requestMoveToNextStep() {
        findAddActivityFlowFragment()?.requestMoveToNextStep()
    }

    override fun onCancelFlow() {
        supportFragmentManager.popBackStack()
    }
}
