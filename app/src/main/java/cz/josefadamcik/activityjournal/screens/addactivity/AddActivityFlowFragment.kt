package cz.josefadamcik.activityjournal.screens.addactivity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cz.josefadamcik.activityjournal.DateTimeProvider
import cz.josefadamcik.activityjournal.DateTimeProviderImpl

import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.model.ActivityRecord


/**
 *
 */
class AddActivityFlowFragment : Fragment(), AddActivityTitleFragment.OnFragmentInteractionListener,
        AddActivityTimeFragment.OnFragmentInteractionListener {
    private var listener: OnFragmentInteractionListener? = null

    private var addActivityFlow : AddActivityFlow? = null
    private val dateTimeProvider : DateTimeProvider = DateTimeProviderImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivityFlow = AddActivityFlow(getString(R.string.add_activity_default_title), dateTimeProvider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_actitity_flow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                    .replace(R.id.flow_content, AddActivityTitleFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onAddActivityTitleFinished(title: String) {
        // second step finished
        addActivityFlow?.title = title

        childFragmentManager.beginTransaction()
                .replace(R.id.flow_content, AddActivityTimeFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    override fun onAddActivityTimeFinished(enteredTime: String, enteredDate: String, enteredDuration: String) {
        addActivityFlow?.apply {
            time = enteredTime
            date = enteredDate
            duration = enteredDuration
            listener?.onAddActivityTimeFinished(produceActivityRecord())
        }
   }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onAddActivityTimeFinished(activityRecord: ActivityRecord)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AddActivityFlowFragment.
         */
        @JvmStatic
        fun newInstance() = AddActivityFlowFragment()
    }
}
