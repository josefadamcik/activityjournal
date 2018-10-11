package cz.josefadamcik.activityjournal.screens.timeline

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import kotlinx.android.synthetic.main.fragment_timeline.*

/**
 *
 */
class TimelineFragment : Fragment(),  TimelineAdapter.Listener {
    private var listener: OnFragmentInteractionListener? = null
    private var listOfRecords: List<ActivityRecord> = emptyList()
    private var adapter: TimelineAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener { _ -> listener?.onNavigationToAddActivityRecord() }

        adapter = TimelineAdapter(LayoutInflater.from(context), this, listOfRecords)

        list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@TimelineFragment.adapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw IllegalStateException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onFinishClicked(position: Int, item: ActivityRecord) {
        listener?.onFinishForActivityRecordClicked(item)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun showRecords(list: List<ActivityRecord>) {
        listOfRecords = list
        adapter?.updateList(list)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onNavigationToAddActivityRecord()
        fun onFinishForActivityRecordClicked(item: ActivityRecord)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TimelineFragment.
         */
        @JvmStatic
        fun newInstance() = TimelineFragment()
    }
}
