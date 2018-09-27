package cz.josefadamcik.activityjournal.test

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.viewpager.widget.ViewPager
import com.stepstone.stepper.test.idling.CustomViewPagerListener

/**
 * Adds some support to testing actions which are actually initiating a StepperLayout
 * interaction (stepper change).
 *
 * @param stepperLayoutOrParent - Pass the StepperLayout or its parent, use activityRule.findViewBy<>()
 *
 *
 * StepperLayout internally uses a ViewPager and espresso is not waiting for transitions to
 * finish (turning off animations on the device doesn't help). Solution is to register a custom
 * idling resource (see [https://stackoverflow.com/questions/31056918/wait-for-view-pager-animations-with-espresso/32763454#32763454] ). If properly integrated, problem is solved.
 *
 * The StepperLayoutLibrary has some testing support for this problem. It actually implements
 * the idling resource in the [com.stepstone.stepper.test.idling.CustomViewPagerListener].
 * There are also some ViewActions [com.stepstone.stepper.test.StepperNavigationActions]  provided,
 * but they cover only interactions with stepper's own UI components and cannot be used to test
 * other interactions.
 *
 * Based on implementation of [com.stepstone.stepper.test.StepperNavigationActions]
 * which is based on `ViewPagerActions.ViewPagerScrollAction` from espresso-contrib
*/
fun runWithStepperLayoutSupport(stepperLayoutOrParent: ViewGroup, action: () -> Unit) {
    val viewPager = stepperLayoutOrParent.findViewById<View>(com.stepstone.stepper.R.id.ms_stepPager) as ViewPager
    // Add a custom tracker listener
    val customListener = CustomViewPagerListener()
    viewPager.addOnPageChangeListener(customListener)

    // Note that we're running the following block in a try-finally construct. This
    // is needed since some of the actions are going to throw (expected) exceptions. If that
    // happens, we still need to clean up after ourselves to leave the system (Espresso) in a good
    // state.
    try {
        // Register our listener as idling resource so that Espresso waits until the
        // wrapped action results in the view pager getting to the STATE_IDLE state
        IdlingRegistry.getInstance().register(customListener)

        Espresso.onIdle()

        action.invoke()

        Espresso.onIdle()

        customListener.mNeedsIdle = true
        Espresso.onIdle()
        customListener.mNeedsIdle = false
    } finally {
        // Unregister our idling resource
        IdlingRegistry.getInstance().unregister(customListener)
        // And remove our tracker listener from ViewPager
        viewPager.removeOnPageChangeListener(customListener)
    }

}


