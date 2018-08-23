# Google Tasks - Unofficial Implementation

## About
This project is my custom attempt to re-create Google Tasks Android App.
It's not my intention to create the whole app with all the features. I'm focusing mainly on basic use cases like creating tasks lists, tasks, and subtasks and Material Design 2.0 implementation used in UI.

## Screenshots
![Screenshots](https://github.com/jurajkusnier/google-tasks/raw/master/screenshots/screenshot.png)

## Disclaimer
* **Work in progress** - a lot of features aren't implemented yet

## Features
* [x] Room database
* [x] Tasks List Bottom drawer  
* [x] Add/Edit/Remove Tasks List
* [ ] _Add/Edit/Remove Task_ (not implemented yet)
* [ ] _Add/Edit/Remove Subtask_ (not implemented yet)

## Codebase
The codebase is structured to packages and decoupled using MVVM patterns. App uses local storage only. It uses an SQLite database to save task list, tasks, and subtasks and shared preferences to save everything else. I implemented Room Persistence Library to get an abstract layer over the database. 
The app consists of one activity and a couple of fragments for individual use cases.   

## Libraries
 * **Android Architecture Components** - https://developer.android.com/topic/libraries/architecture/
 * **Material Design 2.0** - https://material.io/develop/android/
 * **RxJava** - https://github.com/ReactiveX/RxJava
 * **Room** - https://developer.android.com/topic/libraries/architecture/room
