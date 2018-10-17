package cz.josefadamcik.activityjournal.screens.addactivity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.di.sharedViewModelForNestFragment
import kotlinx.android.synthetic.main.fragment_add_activity_title.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.ViewModelStoreOwnerDefinition
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.core.content.ContextCompat.getSystemService



/**
 *
 */
class AddActivityTitleFragment : Fragment(), Step {
    private var listener: OnFragmentInteractionListener? = null

    private val addActivityFlowModel: AddActivityFlowModel by sharedViewModelForNestFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_activity_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            listener?.onCancelFlow()
        }

        input_title.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                fillAddActivityFlow()
                addActivityFlowModel.moveToNextStep()
                true
            } else {
                false
            }
        }

        forceKeyboardPresent()
    }

    private fun forceKeyboardPresent() {
        input_title.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(input_title, SHOW_IMPLICIT)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
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

    override fun onSelected() {
    }
    override fun verifyStep(): VerificationError? {
        fillAddActivityFlow()
        return null
    }

    private fun fillAddActivityFlow() {
        addActivityFlowModel.title = input_title.text.toString()
    }

    override fun onError(error: VerificationError) {}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onCancelFlow()
    }

}
