# TODO for the project


## WIP todo (short-term)


- [X] timeline - ability to finish undergoing activity _@done (2018-10-11 12:28)_
    - [X] add a finish button _@done (2018-10-02 15:11)_
    - [X] react to a press _@done (2018-10-11 11:44)_
    - [X] The item should have correct duration when finished. _@done (2018-10-11 12:28)_
- [X] Refactor in DI  _@done (2018-10-11 14:41)_
- [X] Refactor AddActivityFlowTest in a way that it won't depend on timeline's functionality. _@done (2018-10-12 10:31)_

- [ ] prevent data loss when the device is rotated
    - [ ] add activity flow 
        - [ ] when rotated, it should stay on the same step
        - [ ] when rotatet, it sohuld not crash when we move between steps
    - [X] timeline - content should be present after rotation _@done (2018-10-12 10:57)_

- [ ] get rid of the communication between nested fragments and activity
- [ ] improper handling of fragments -> data and commands are sent from main activy directy to instance


- [ ] what about date / time validation?
- [ ] what about date formats?
- [ ] timeline - display dates relatively ("yesterday" and so on)
- [ ] add activity - use chooses the category
- [ ] main activity contains state of the application
- [ ] prevent data loos when the application is killed
- [ ] prevent data loos when the user quits the application
- [ ] use nice dialogues for date and time
- [X] refactor tests from mainactivitytest class to more classes _@done (2018-10-12 10:38)_
- [X] list of activities in the timeiline _@done (2018-10-12 10:38)_
- [X] remove addActivityFlow_titleCanBeEnteredAndTheNewRecordDisplayed2 _@done (2018-10-12 10:37)_

## TODO archive


- [X] find a way how te refactor mainactivity in a way, that we will be able to test sorting of activity recods. _@done (2018-10-02 14:22)_
- [X] timeline - activities are sorted by date and time, but undergoing activities are always at the beginning of the list _@done (2018-10-02 14:22)_
- [X] use proper representation of dates and times _@done (2018-10-02 14:23)_

## Project screens - long term TODO

- [X] project and testing setup _@done (2018-09-24 20:43)_

- [ ] **timeline - show activities, alow interactions with them.**
- [ ] **add activity - allow user to add an activity with category, title,start, duration. The activity might be in undergoing state, which means that it started in the past and don't have duration yet and needs to be finished by users interaction. Acivity is displayed once**

- [ ] activity detail
- [ ] edit activity
- [ ] category list
- [ ] add/edit category
- [ ] settings / export / backup
- [ ] about


