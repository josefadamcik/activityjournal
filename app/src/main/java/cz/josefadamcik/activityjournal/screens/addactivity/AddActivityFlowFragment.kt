package cz.josefadamcik.activityjournal.screens.addactivity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import cz.josefadamcik.activityjournal.*
import cz.josefadamcik.activityjournal.common.DateTimeProvider
import cz.josefadamcik.activityjournal.common.ui.BackButtonPressConsumer

import cz.josefadamcik.activityjournal.model.ActivityRecordsRepository
import kotlinx.android.synthetic.main.fragment_add_actitity_flow.*
import org.koin.android.ext.android.inject

/**
 *
 */
class AddActivityFlowFragment : Fragment(), AddActivityTitleFragment.OnFragmentInteractionListener,
        AddActivityTimeFragment.OnFragmentInteractionListener, StepperLayout.StepperListener,
        BackButtonPressConsumer {
    private var listener: OnFragmentInteractionListener? = null

    private val addActivityModelFlow: AddActivityModelFlow by inject()
    private val dateTimeProvider: DateTimeProvider by inject()
    private val activityRecordsRepository by inject<ActivityRecordsRepository>()
    private lateinit var stepperAdapter: AddActivityFlowStepperAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stepperAdapter = AddActivityFlowStepperAdapter(childFragmentManager, context!!, addActivityModelFlow)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_actitity_flow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stepperLayout.adapter = stepperAdapter
        stepperLayout.setListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun requestMoveToNextStep() {
        stepperLayout.currentStepPosition = 1
    }

    override fun onCancelFlow() {
        // nop, see MainActivity#onCancelFlow
    }

    override fun onAddActivityTimeFinished() {
        activityRecordsRepository.add(addActivityModelFlow.produceActivityRecord())
        listener?.onAddActivityFlowFinished()
    }

    override fun onBackPressed(): Boolean {
        return if (stepperLayout.currentStepPosition > 0) {
            stepperLayout.onBackClicked()
            true
        } else {
            false
        }
    }

    // stepper
    override fun onStepSelected(newStepPosition: Int) {
    }

    // stepper
    override fun onError(verificationError: VerificationError?) {
    }

    // stepper
    override fun onReturn() {
    }

    // stepper
    override fun onCompleted(completeButton: View?) {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onAddActivityFlowFinished()
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
