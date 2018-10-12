# TODO for the project


## WIP todo (short-term)


- [X] timeline - ability to finish undergoing activity _@done (2018-10-11 12:28)_
    - [X] add a finish button _@done (2018-10-02 15:11)_
    - [X] react to a press _@done (2018-10-11 11:44)_
    - [X] The item should have correct duration when finished. _@done (2018-10-11 12:28)_
- [X] Refactor in DI  _@done (2018-10-11 14:41)_
- [X] Refactor AddActivityFlowTest in a way that it won't depend on timeline's functionality. _@done (2018-10-12 10:31)_


- [ ] what about date / time validation?
- [ ] what about date formats?
- [ ] timeline - display dates relatively ("yesterday" and so on)
- [ ] get rid of the communication between nested fragments and activity
- [ ] add activity - use chooses the category
- [ ] remove addActivityFlow_titleCanBeEnteredAndTheNewRecordDisplayed2
- [ ] improper handling of fragments -> data and commands are sent from main activy directy to instance
- [ ] main activity contains state of the application
- [ ] add activity -  solve problem with rotation (a crash, because the dependency for nested fragments is not set)
- [ ] prevent data loss when the device is rotated
- [ ] prevent data loos when the application is killed
- [ ] prevent data loos when the user quits the application
- [ ] use nice dialogues for date and time
- [ ] list of activities in the timeiline
- [ ] refactor tests from mainactivitytest class to more classes

## TODO archive


- [X] find a way how te refactor mainactivity in a way, that we will be able to test sorting of activity recods. _@done (2018-10-02 14:22)_
- [X] timeline - activities are sorted by date and time, but undergoing activities are always at the beginning of the list _@done (2018-10-02 14:22)_
- [X] use proper representation of dates and times _@done (2018-10-02 14:23)_

## Project screens - long term TODO

- [X] project and testing setup _@done (2018-09-24 20:43)_

- [ ] **timeline - show activities, alow interactions with them.**
- [ ] **add activity - allow user to add an activity with category, title,start, duration. The activity might be in undergoing state, which means that it started in the past and don't have duration yet and needs to be finished by users interaction. Acivity is displayed once **

- [ ] activity detail
- [ ] edit activity
- [ ] category list
- [ ] add/edit category
- [ ] settings / export / backup
- [ ] about


