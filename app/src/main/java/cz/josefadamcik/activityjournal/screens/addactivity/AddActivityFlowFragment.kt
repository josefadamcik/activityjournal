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
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val STATE_CURRENT_POSITION = "state_current_position"
/**
 *
 */
class AddActivityFlowFragment : Fragment(), AddActivityTitleFragment.OnFragmentInteractionListener,
        StepperLayout.StepperListener,
        BackButtonPressConsumer {
    private var listener: OnFragmentInteractionListener? = null

    private val addActivityFlowModel: AddActivityFlowModel by viewModel()
    private val dateTimeProvider: DateTimeProvider by inject()
    private val activityRecordsRepository by inject<ActivityRecordsRepository>()
    private lateinit var stepperAdapter: AddActivityFlowStepperAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stepperAdapter = AddActivityFlowStepperAdapter(childFragmentManager, context!!)
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
        if (savedInstanceState != null) {
            addActivityFlowModel.restoreState(savedInstanceState.getInt(STATE_CURRENT_POSITION, 0))
        }
        stepperLayout.currentStepPosition = addActivityFlowModel.currentStep
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
        addActivityFlowModel.onCurrentStepChangeRequired = { step ->
            if (stepperLayout.currentStepPosition !=  step) {
                stepperLayout.currentStepPosition = step
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        addActivityFlowModel.onCurrentStepChangeRequired = {} //TODO: leak or not?
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_CURRENT_POSITION, addActivityFlowModel.produceState())
    }



    override fun onCancelFlow() {
        // nop, see MainActivity#onCancelFlow
    }


    override fun onBackPressed(): Boolean {
        stepperLayout.onBackClicked()
        return true
    }

    // stepper
    override fun onStepSelected(newStepPosition: Int) {
        addActivityFlowModel.onStepSelected(newStepPosition)
    }

    // stepper
    override fun onError(verificationError: VerificationError?) {
    }

    // stepper
    override fun onReturn() {
        //Back was pressed but not handled or user pressed back button on the first step
        listener?.onCancelFlow()
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
        fun onCancelFlow()
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
