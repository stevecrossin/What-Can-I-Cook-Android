# What Can I Cook - Android Application #

This is a working directory for an Android application that will form the substance for our assignment
a Deakin University subject - SIT305. The application is coded in Java.

Project by Steve Crossin: SID217109001 and Trong Hieu Lam: SID218738651

The purpose of the application will be to provide the end user with recipes that they can cook, by selecting
from a list of ingredients that they already have at home in their pantry. It will cater to a variety of different
cuisines and dietary requirements.

All features have been fully implemented and an overview of the progress it took to get the application from start to completion can be found in the changelog.txt file.

The github link for the project can be found at: https://github.com/stevecrossin/What-Can-I-Cook
The application has also been published to the Google Play Store: https://play.google.com/store/apps/details?id=com.stevecrossin.whatcanicook
A basic website has also been created for the application here: https://what-can-i-cook.flycricket.io

## Getting Started ##

As this is an Android application, it can be run two different ways.

The GitHub repo can be cloned or downloaded in a ZIP format from GitHub and compiled in Android Studio and then run on an emulator, or a physical device.

You can do so by performing this operation.
```
$ git://github.com/stevecrossin/What-Can-I-Cook.git
```
Alternatively, the application can be run directly onto a physical device by transferring the app-release.apk onto the device, which is found in the /app/release folder.

Lastly, you can download the latest release of the application from [Google Play](https://play.google.com/store/apps/details?id=com.stevecrossin.whatcanicook)

## Directory structure ##
The root of the github project contains:

app folder - contains all source files for the app. Broken down further, inside app/main contains:

Java files (java/com/stevecrossin/whatcanicook): contains all source code for the application. Further broken down into:
* adapter: contains the code for all the elements that make adapters and viewholders that are contained in the applicaton function
* entities: contains the information for each database table in the application, and the SQL operations for each database
* other: contains java files not elsewhere classified, which includes the splash screen, the utlility to populate databases on app load, and code relating to the login state of the app and the function to hash the password
* screens: contains the files that dictates how each activity in the application functions and the operations of those activites

res folder - contains all layout XML files for the app, colours, strings, styles and drawable objects. Also contains a raw directory which holds all read only CSV and text files.

Additionally, the root of the github project contains:
* bugs.txt - a list of all bugs that were encountered while developing the application. All were fixed, and method to fix in most cases were noted for future reference.
* changelog.txt - list of all changes made in the application
* todo.txt - a list of all changes that needed to be made as the app progressed
* userstories.txt - definition of some desired behaviour that the end user should be able to perform

## Permission ##
Unless permission is otherwise provided by myself, this code is NOT free to be reused or modified for any purpose. Please contact me
at stevecrossin@gmail.com if you wish to re-use or otherwise modify this code.
