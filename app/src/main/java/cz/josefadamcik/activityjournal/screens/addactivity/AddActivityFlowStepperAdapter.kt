package cz.josefadamcik.activityjournal.screens.addactivity

import android.content.Context

import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

import androidx.annotation.IntRange
import androidx.fragment.app.FragmentManager

class AddActivityFlowStepperAdapter(
    fm: FragmentManager,
    context: Context
) : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step =
            if (position == 0) AddActivityTitleFragment() else AddActivityTimeFragment()

    override fun getCount(): Int {
        return 2
    }

    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        // Override this method to set Step title for the Tabs, not necessary for other stepper types
        return when (position) {
            0 -> StepViewModel.Builder(context)
                    .setTitle("") // can be a CharSequence instead
                    .setEndButtonVisible(true)
                    .create()
            1 -> StepViewModel.Builder(context)
                    .setTitle("") // can be a CharSequence instead
                    .setEndButtonVisible(false)
                    .create()
            else -> StepViewModel.Builder(context)
                    .setTitle("") // can be a CharSequence instead
                    .setEndButtonVisible(true)
                    .create()
        }

    }
}
