package cz.josefadamcik.activityjournal.screens.addactivity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import cz.josefadamcik.activityjournal.DateTimeProvider
import cz.josefadamcik.activityjournal.DateTimeProviderImpl

import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.model.ActivityRecord
import kotlinx.android.synthetic.main.fragment_add_actitity_flow.*

/**
 *
 */
class AddActivityFlowFragment : Fragment(), AddActivityTitleFragment.OnFragmentInteractionListener,
        AddActivityTimeFragment.OnFragmentInteractionListener, StepperLayout.StepperListener {
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

        stepperLayout.adapter = AddActivityFlowStepperAdapter(childFragmentManager, context!!)
        stepperLayout.setListener(this)
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

        stepperLayout.currentStepPosition = 1
    }

    override fun onAddActivityTimeFinished(enteredTime: String, enteredDate: String, enteredDuration: String) {
        addActivityFlow?.apply {
            time = enteredTime
            date = enteredDate
            duration = enteredDuration
            listener?.onAddActivityTimeFinished(produceActivityRecord())
        }
   }

    //stepper
    override fun onStepSelected(newStepPosition: Int) {

    }

    //stepper
    override fun onError(verificationError: VerificationError?) {

    }

    //stepper
    override fun onReturn() {

    }

    //stepper
    override fun onCompleted(completeButton: View?) {

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
