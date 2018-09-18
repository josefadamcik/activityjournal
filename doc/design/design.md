# ActivityJournal - design

More detailed coverage is caputered in [series of articles on my website](https://josef-adamcik.cz/programming/activity-journal-app-idea.html).

## My use case

I tend to track some of my daily activities in different ways. 

- I use [a  mobile application (Sportractive)](http://sportractive.com) that tracks my runs and sends data to [smashrun.com](https://smashrun.com)
- Google fit also tracks my running and adds other episodes of movement it detects.
- I use toggle.com in order to track remaining activities:
    - yoga exercises
    - study and self-improvement
    - work on personal projects
- And of course, I always track tasks done during work but let's put those aside for now.
- Recently I tried to create a journal but at the moment I only track my yoga sessions and keep some information about the exercises I did.

I want still use the Sportractive application for the tracking of my runs, but It would be nice to have a place to keep track of all my activities in order to be able to analyze how I spend my time. It would be nice to have a mobile application for it, wouldn't it? 

## My goals for the project

The primary goals are: 

1. training. It's always good to train your skills, 
2. experimenting with new technologies and approaches without the risk that I would create some future problem for some "real" application.

For the project, I would like to:

- continue with Kotlin and deepen my knowledge of the language,
- use Android Components (which came out after I started TrackOnTrakt),
- avoid RxJava, focus more on Android Components and maybe try to use coroutines from Kotlin,
- use MVVM, maybe with DataBinding (I am actually bit afraid to use DataBinding, but I'll see),
- Clean Architecture, perhaps,
- practice TDD

## Outline of the project

**Name**: Private activity journal

**App statement**: Helps you to keep a record of various daily activities.

**Use cases - basic features**:

1. A user chooses an activity to track, starts tracking and when the activity ends, he stops tracking. The activity is added to his timeline. Activities have a category. There can be multiple activities tracked simultaneously (e.g. commuting and listening to a podcast).
2. The user adds an activity that happened in the past.
3. The user manages (edits/removes) activities on his timeline.
4. The user can add notes and sentiment (mood picked) to his activities at any time.
5. The user can explore daily, weekly and monthly statistics of his activities. He can filter activities by categories.
6. The user can export his data in some useful format (CSV, JSON).

**More features**

- Backup up & sync - data are backed up and/or synchronized via Gdrive or Dropbox
- Activity templates: user can add templates for activities, that would allow to track them easier.
- Activity fields: user can add a field with a name and a numerical value to every activity or template. There can be more of those in every activity. It's possible to display those values in graphs (trends). Those can be used for tracking of counts of particular exercises in a workout.
- Auto activities - there might by automatically tracked activities based on various information sources. 
    - APPs and APIs, the application can fetch data from others applications and APIs, where possible and reasonable. It may, for example, get activity info from Google Fit.
    - geofencing -> e.g. the application detects that the user is present in his office, it will add an activity "in work"
    - observing notifications -> listening to podcasts


**Constraints**

The application stores data only in the device. There is no API, no backend, no web app. The only exception will be the Dropbox backup/sync mentioned above. But all data are completely controlled by the user.


## UX design

### Purpose of this phase

The purpose of this part is to explore some ideas about the application's user interface and create some initial notion of the product I am going to build. This design phase is not supposed to be thorough and the resulting design won't be final. I am going to practice iterative development[^1] and TDD and decisions will be made as the development progresses. 

However, it's good to have some starting point, some vision which would allow me to asses the size of the project and would help me to start development.

### Constraints and assumptions

As this is not a regular project, but a training project and I am a single person with a limited amount of time, I am going to simplify things as much as I can. We can say, that we are going to design some kind of MVP, which I can further improve in the future.

So I am going to:

- focus mainly on "basic features" of the application as listed in [the previous article]({% post_url 2018-08-27-activity-journal-app-idea %}#outline-of-the-project)
- use the Material design as a design framework and build UI preferably from [the material design components](https://material.io/design/),
- attempt to avoid complex custom views that would require a vast amount of effort to create,
- use a pen and paper for hand-drawn diagrams and wireframes, so excuse my drawing[^3] and handwriting.


I recommend the dear reader to [glance over features in the first article]({% post_url 2018-08-27-activity-journal-app-idea %}#outline-of-the-project) before we continue. 

### Navigation flow

This is just a simple draft of a navigation graph that crudely captures screens and navigation paths between them. This may even change before we reach the end of this article.


![flow diagram](wireframes/flow_out.jpeg)

A user starts on the [Timeline](#timeline) where undergoing and past activities are displayed. They can easily [add new activity](#add-activity-flow) or [display detail of existing one](#activity-detail-and-edit). They can also show some kind of [navigation](#navigation) where they can access the rest of the application. You may notice that I automatically added a "settings" screen but later realized, that since backup and settings (both candidates for presence on the settings screen) might have dedicated screens for themselves, there won't be anything left for the settings screen.

### Timeline

The timeline is the most important screen in the application and the user will spend most of his time there. It's also the hardest screen to design because there are so many options. I suppose that the task of designing this screen might occupy a whole UX team in some company for a long time.

Let's summarize the requirements:

- It displays activities ordered by the time.
- Activities might be undergoing (happening right now) or in the past.
- Every activity has title and category (represented by a colourised icon), the time when it was started, and a duration.
- If an activity has a note or a mood (an emoticon) attached, the user should be also able to see them.
- The user can start [add a new activity](#add-activity-flow) and [display/edit an activity](#activity-detail-and-edit) interactions from this screen.
- The user also needs an access to [the menu](#navigation).
- The user should be able to easily navigate a long period of time.
- We want to be able to track **multiple activities in parallel**.


The last one is an important requirement. It complicates our situation and limits our design options. Is this requirement really needed? And do we need an unlimited number of parallel activities or would it be valid to limit the amount somehow?

There are possible use cases for 2 parallel activities:

- travelling (driving/reading) and some other (in such situation meaningful) activity. E.g. commute on public transport and reading or listening to a podcast).
- Sport (e.g. running) and listening to a podcast or an audiobook

And what about 3 parallel activities? Honestly, I wasn't able to come out with a possible use case. Therefore it seems we can limit the maximum number of parallel activities to 2.

So how should be the timeline presented? And how parallel activities should be presented? Those are some of the options I was considering:

**Option 1:** Literally display a horizontal timeline and visually attach activities to it using lines. One point on a timeline may lead to several boxes representing several parallel activities. It's hard to imagine, how it actually should look. I attempted to sketch a few variants but nothing looked right.

**Option 2:** Show a calendar-like schedule, imagine Google calendar switched to the "day" view. That means that a particular amount of time is represented by a fixed space on the display. Activities that are parallel are displayed next to each other horizontally and occupy less width than activities that are not overlapping with any other. That would mean a lot of white space. There might be some clever way how to collapse free time and keep clarity.

**Option 3:** Use [cards from material design](https://material.io/design/components/cards.html) and display them one after each other. There might be subheaders which would help the user to orient in time. Since we know, that it's possible to limit the maximal amount of parallel activities to 2, we might be able to fit two cards next to each other in order to indicate their parallel run. I sketched the idea but I didn't like how it looked when the activities were overlapping partially.

I spent some time thinking this one through and then I remembered: I am trying to find a simple MVP-like solution, which can be built with basic components. That's why I chose the option 3 and decided to abandon the idea, that the timeline somehow reflect the fact that some activities overlap. So: activities will be represented by full-width cards. The activities, which are in progress, will be always at the beginning.


Wireframes are included in the [wireframes directory](wireframes/).

## Add activity flow

I decided to use simple wizard-like flow with a horizontal stepper. The user can return back and change values for each step. 

Wireframes are included in the [wireframes directory](wireframes/).

## Activity detail and edit


The big question in this part was if the detail screen should also act as an edit screen. In the end, I decided to separate those two. There will be a detail screen with an edit action and a specialized edit screen. I sketched two slightly different variants of the detail screen and I am not fully decided which one I prefer (I might end up with another variant that combines those two) so I have included both of them.

Wireframes are included in the [wireframes directory](wireframes/).

## Navigation

Navigation can be as simple as standard Navigation Drawer. I decided to join backup & sync to a single item. You may remember that they were separated [in the navigation flow](#navigation-flow). This is one of those details which are easy to change 

Wireframes are included in the [wireframes directory](wireframes/).


## Categories

The user can manage (create, update, delete) categories. There is a simple list of categories accessible from the navigation drawer. Tap on a row displays screen with an editable detail for the category. The category edit screen is almost identical to the add category screen. The add category screen can be also accessed from the first screen of the [add activity flow](#add-activity-flow).

Selection of colour and icon is done through simple modal dialogues. There will be a fixed palette of colours and a fixed set of icons.

Wireframes are included in the [wireframes directory](wireframes/).

## Remaining screens: Backup, Sync, About

I skipped those screens for the time being. I may draw some wireframes once I get to implementing them. But from my current perspective, those screens are marginal.
