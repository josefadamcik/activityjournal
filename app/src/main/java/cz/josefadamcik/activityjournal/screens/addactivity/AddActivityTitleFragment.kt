package cz.josefadamcik.activityjournal.screens.addactivity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import cz.josefadamcik.activityjournal.R
import kotlinx.android.synthetic.main.fragment_add_activity_title.*
/**
 *
 */
class AddActivityTitleFragment : Fragment(), Step {
    private var listener: OnFragmentInteractionListener? = null

    lateinit var addActivityFlow: AddActivityFlow

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
                listener?.requestMoveToNextStep()
                true
            } else {
                false
            }
        }
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
        addActivityFlow.title = input_title.text.toString()
    }

    override fun onError(error: VerificationError) {}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun requestMoveToNextStep()
        fun onCancelFlow()
    }

    companion object {
        fun newInstance() =
                AddActivityTitleFragment().apply {
                    arguments = Bundle()
                }
    }
}
