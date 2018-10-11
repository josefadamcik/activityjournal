package cz.josefadamcik.activityjournal.test

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import cz.josefadamcik.activityjournal.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers

fun assertToolbarTitle(title: String) {
    Espresso.onView(Matchers.allOf(
            Matchers.instanceOf(TextView::class.java),
            ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar)),
            ViewMatchers.isCompletelyDisplayed(),
            ViewMatchers.withText(title)))
            .check(ViewAssertions.matches(ViewMatchers.withText(title)))
}

fun assertOnTimeline() {
    assertToolbarTitle("Timeline")
}

fun onRecyclerViewRowAtPositionCheck(recyclerViewId: Int, position: Int, itemMatcher: Matcher<View>) {
    Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
    Espresso.onView(withRecyclerView(recyclerViewId)
            .atPosition(position))
            .check(ViewAssertions.matches(itemMatcher))
}

fun actClickOnNavUpButton() {
    Espresso.onView(Matchers.allOf(
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.toolbar)),
            ViewMatchers.isCompletelyDisplayed(),
            ViewMatchers.withClassName(Matchers.endsWith("AppCompatImageButton"))
    )).perform(ViewActions.click())
}
