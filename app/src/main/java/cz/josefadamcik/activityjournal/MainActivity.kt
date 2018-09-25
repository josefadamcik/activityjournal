package cz.josefadamcik.activityjournal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * The main and only activity in the application.
 */
class MainActivity : AppCompatActivity(), TimelineFragment.OnFragmentInteractionListener,
        AddActivityTitleFragment.OnFragmentInteractionListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, TimelineFragment.newInstance())
                    .commit()
        }
    }

    override fun onNavigationToAddActivityRecord() {
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AddActivityTitleFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    override fun onAddActivityFinished(title: String) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.executePendingTransactions()
        val timelineFragment = supportFragmentManager.findFragmentById(android.R.id.content) as TimelineFragment
        timelineFragment.displayNewActivityRecord(title)

    }




}
