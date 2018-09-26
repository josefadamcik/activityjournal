package cz.josefadamcik.activityjournal.screens.addactivity

import com.natpryce.hamkrest.and
import org.junit.Test
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isNullOrEmptyString
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import cz.josefadamcik.activityjournal.DateTimeProvider
import cz.josefadamcik.activityjournal.model.ActivityRecord
import org.junit.Before

class AddActivityFlowTest {
    private val defaultTitle = "default title"
    private val currentTime = "12:00"
    private val currentDate = "12.10.2018"


    private val currentDateTimeProvider = mock<DateTimeProvider> {
        on { provideCurrentTime() } doReturn currentTime
        on { provideCurrentDate() } doReturn currentDate
    }

    private lateinit var flow : AddActivityFlow

    @Before
    fun setUp() {
        flow = AddActivityFlow(defaultTitle, currentDateTimeProvider)
    }

    @Test
    fun nullTitle_defaultTitleIsProvided() {
        flow.title = null

        val activityRecord = flow.produceActivityRecord()

        assertThatDefaultTitleWasUsed(activityRecord)
    }

    @Test
    fun emptyTitle_defaultTitleIsProvided() {
        flow.title = ""

        val activityRecord = flow.produceActivityRecord()

        assertThatDefaultTitleWasUsed(activityRecord)
    }

    @Test
    fun nullTime_currentTimeIsUsed() {
        flow.time = null

        val activityRecord = flow.produceActivityRecord()

        assertThatCurrentTimeWasUsed(activityRecord)
    }

    @Test
    fun emptyDate_currentTimeIsUsed() {
        flow.date = ""

        val activityRecord = flow.produceActivityRecord()

        assertThatCurrentDateWasUsed(activityRecord)
    }

    @Test
    fun nullDate_currentTimeIsUsed() {
        flow.date = null

        val activityRecord = flow.produceActivityRecord()

        assertThatCurrentDateWasUsed(activityRecord)
    }

    @Test
    fun emptyTime_currentTimeIsUsed() {
        flow.time = ""

        val activityRecord = flow.produceActivityRecord()

        assertThatCurrentTimeWasUsed(activityRecord)
    }

    private fun assertThatDefaultTitleWasUsed(activityRecord: ActivityRecord) {
        assert.that(activityRecord.title, !isNullOrEmptyString and equalTo(defaultTitle))
    }

    private fun assertThatCurrentTimeWasUsed(activityRecord: ActivityRecord) {
        assert.that(activityRecord.time, !isNullOrEmptyString and equalTo(currentTime))
    }

    private fun assertThatCurrentDateWasUsed(activityRecord: ActivityRecord) {
        assert.that(activityRecord.date, !isNullOrEmptyString and equalTo(currentDate))
    }


}