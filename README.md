# What Can I Cook - Android Application #

This is a working directory for an Android application, coded in Java. Project by Steve Crossin: SID217109001 and Trong Hieu Lam: SID218738651

The purpose of the application is to provide the end user with recipes that they can cook, by selecting
from a list of ingredients that they already have at home in their pantry. It will cater to a variety of different
dishes and dietary requirements, and also allows the end user to save recipes, add their own custom recipes, share recipes with other people and keep note of ingredients they have at home.

All features have been fully implemented and an overview of the progress it took to get the application from start to completion can be found in the changelog.txt file.

###
* The GitHub link for the project can be found [here](https://github.com/stevecrossin/What-Can-I-Cook)
* The application has also been published to [Google Play](https://play.google.com/store/apps/details?id=com.stevecrossin.whatcanicook)
* A [basic website](https://what-can-i-cook.flycricket.io) has also been created for the application here, to attract new users: 

## Getting Started ##

As this is an Android application, it can be run several different ways.

The GitHub repo can be cloned or downloaded in a ZIP format from GitHub and compiled in Android Studio and then run on an emulator, or a physical device.

You can do so by performing this operation.
```
$ git://github.com/stevecrossin/What-Can-I-Cook.git
```
Alternatively, the application can be run directly onto a physical device by transferring the app-release.apk onto the device, which is found in the /app/release folder.

Lastly, you can download the latest release of the application from [Google Play](https://play.google.com/store/apps/details?id=com.stevecrossin.whatcanicook)

## Directory structure ##
The root of the github project contains:

[/app](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app) - contains all source files for the app. Broken down further, it contains:

[/release](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app/release) - contains the application APK and Android App Bundle files.

[Java files](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app/src/main/java/com/stevecrossin/whatcanicook) - contains all source code for the application. Further broken down into:
* [/adapter](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app/src/main/java/com/stevecrossin/whatcanicook/adapter) - contains the code for all the elements that make adapters and viewholders that are contained in the applicaton function
* [entities](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app/src/main/java/com/stevecrossin/whatcanicook/entities) - contains the information for each database table in the application, and the SQL operations for each database
* [/other](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app/src/main/java/com/stevecrossin/whatcanicook/other) - contains java files not elsewhere classified, which includes the splash screen, the utlility to populate databases on app load, and code relating to the login state of the app and the function to hash the password
* [/roomdatabase](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app/src/main/java/com/stevecrossin/whatcanicook/roomdatabase) - contains the data repositories for the application and entities that exist in that database
* [/screens](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app/src/main/java/com/stevecrossin/whatcanicook/screens) - contains the files that dictates how each activity in the application functions and the operations of those activites

[/res folder](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/app/src/main/res) - contains all layout XML files for the app, colours, strings, styles and drawable objects. Also contains a raw directory which holds all read only CSV and text files.

Additionally, the root of the github project contains:
* [bugs.txt](https://github.com/stevecrossin/What-Can-I-Cook/blob/master/bugs.txt) - a list of all bugs that were encountered while developing the application. All were fixed, and method to fix in most cases were noted for future reference.
* [changelog.txt](https://github.com/stevecrossin/What-Can-I-Cook/blob/master/changelog.txt) - list of all changes made in the application
* [todo.txt](https://github.com/stevecrossin/What-Can-I-Cook/blob/master/todo.txt) - a list of all changes that needed to be made as the app progressed
* [userstories.txt](https://github.com/stevecrossin/What-Can-I-Cook/blob/master/userstories.txt) - definition of some desired behaviour that the end user should be able to perform

## Having problems? ##
Please feel free to submit issues with any bugs or other unforseen issues you experience with the application. While this project has been tested and checked carefully to ensure the master branch is bug free, unknown bugs or exceptions could possibly exist. If you have any problems, please open an issue describing the problem and how to reproduce it, and we'll be sure to take a look at it.

## Permission ##
Unless permission is otherwise provided by myself, this code is NOT free to be reused or modified for any purpose. Please contact me
at stevecrossin@gmail.com if you wish to re-use or otherwise modify this code.
