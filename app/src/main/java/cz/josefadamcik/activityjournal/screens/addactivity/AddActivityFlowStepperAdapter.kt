package cz.josefadamcik.activityjournal.screens.addactivity

import android.content.Context

import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

import androidx.annotation.IntRange
import androidx.fragment.app.FragmentManager

class AddActivityFlowStepperAdapter(
    fm: FragmentManager,
    context: Context,
    private val addActivityModelFlow: AddActivityModelFlow
) : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step {
        return if (position == 0) {
            AddActivityTitleFragment().apply {
                this.addActivityModelFlow = this@AddActivityFlowStepperAdapter.addActivityModelFlow
            }
        } else {
            AddActivityTimeFragment().apply {
                this.addActivityModelFlow = this@AddActivityFlowStepperAdapter.addActivityModelFlow
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        // Override this method to set Step title for the Tabs, not necessary for other stepper types
        return StepViewModel.Builder(context)
                .setTitle("") // can be a CharSequence instead
                .create()
    }
}
