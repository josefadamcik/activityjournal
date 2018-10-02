package cz.josefadamcik.activityjournal.screens.addactivity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError

import cz.josefadamcik.activityjournal.R

import kotlinx.android.synthetic.main.fragment_add_activity_time.*

/**
 *
 */
class AddActivityTimeFragment : Fragment(), Step {

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
        return inflater.inflate(R.layout.fragment_add_activity_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            listener?.onCancelFlow()
        }

        button_add.setOnClickListener {
            fillDataIIntoActivitFlow()
            listener?.onAddActivityTimeFinished()
        }

        input_duration.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(view: Editable?) {
            }

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text.isNullOrBlank()) {
                    button_add.text = resources.getString(R.string.start_tracking_finish_button)
                } else {
                    button_add.text = resources.getString(R.string.add_activity_finish_button)
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw IllegalStateException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onSelected() {}
    override fun verifyStep(): VerificationError? {
        fillDataIIntoActivitFlow()
        return null
    }

    private fun fillDataIIntoActivitFlow() {
        addActivityFlow.time = input_time.text.toString()
        addActivityFlow.date = input_date.text.toString()
        addActivityFlow.duration = input_duration.text.toString()
    }

    override fun onError(error: VerificationError) {}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onAddActivityTimeFinished()
        fun onCancelFlow()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = AddActivityTimeFragment()
    }
}
