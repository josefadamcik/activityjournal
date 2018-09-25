package cz.josefadamcik.activityjournal

import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
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

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
