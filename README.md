# What Can I Cook - Android Application #

This is a working directory for an Android application, coded in Java.

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
* [licenses.txt](https://github.com/stevecrossin/What-Can-I-Cook/blob/master/licenses.txt) - a list of all resources used in this application and the underlying copyrights and permissions for the use of those resources
* [todo.txt](https://github.com/stevecrossin/What-Can-I-Cook/blob/master/todo.txt) - a list of all changes that needed to be made as the app progressed
* [userstories.txt](https://github.com/stevecrossin/What-Can-I-Cook/blob/master/userstories.txt) - definition of some desired behaviour that the end user should be able to perform
[/publishinginfo](https://github.com/stevecrossin/What-Can-I-Cook/tree/master/publishinginfo) - contains all the images for the Play Store listing, and the text for the listing as well (though redundant as the app is already published)

## Core Features ##
The below list details the core features of the application, a rough outline of their purpose and how they function.

### Initial load and login ###
On initial load of the application, a Splash screen loads. In the background of the application, it will call the DBPopulator using RXjava to read from the read-only CSV files in permanent storage into the applications room database. Once it has completed, it will hand over to the Login class.

The Login class defines the behaviour the application performs on load. The class first checks if there is any user flagged in the database as logged in, and if so, will exit to the Main Activity. Otherwise, the user is presented with a username/password prompt, and based on their input, determines if their input is valid, and if the user is an existing user (and then will validate if details entered match saved credentials, and reject login if they do not), otherwise will create them as a new user.

Once login is successful, it will load the contents of the users stored dietary intolerances and saved pantry ingredients, which are stored in a JSON format, into the applications room DB, and then exit to the MainActivity

### Main Activity ###
MainActivity is the landing page for the user after the Login class has finished. It's primary purpose is to navigate to other scenes. Buttons exist on the application to navigate to the CategoryPicker class and activity, and MainActivity will through an OnClick listener determine what dish the end user clicked (e.g. breakfast, lunch) and pass that via intent to the next class.

### CategoryPicker ###
CategoryPicker is the main class which allows the user to select ingredients that they would like to find recipes for. It displays the list of categories as dedfined in the database in rows in a RecyclerView, and utilises three files in the adapters folder - CategoryViewAdapter, CategoryViewHolder and CategoryIngredientsAutoCompleteAdapter to hold data to be shown in the view - the latter of which populates the AutoCompleteTextView at the bottom of the activity.

If a user clicks on any of the rows in the recyclerview, the name of that category will be passed via intent to IngredientsPicker.

From this screen, the user can also add ingredients through the AutoComplete text box. If the user types anything in the ingredients autocomplete box, they will be presented with ingredients in the database that contains their entry (e.g. typing "chicken" will return "chicken breast", "chicken gravy", "chicken drumsticks" etc. 

Through an onClick method, the user can then click on these to add them, which will add them to the view in "MyIngredients"

### IngredientsPicker & MyIngredients ###
IngredientsPicker: navigated to when the user clicks on a category from CategoryPicker. Similar to that class, it utilises a RecyclerView and two adapter files - IngredientViewAdapter and IngredientViewHolder to hold data to be shown in the view. 

Each ingredient is contained in a row and has a listener which adds the ingredient to the MyIngredients class, by changing the ingredient to "selected" in the database.

MyIngredients: Calls the ingredients database for all ingredients that have "ingredientSelected=1" and doesn't match any dietary intolerances, and displays these in the view. The user can remove individual ingredients by clicking the X button.

When the user clicks "Find Recipes" it will pass the list of ingredients to the Recipes class.

### Recipes & RecipeDetails ###
Recipes: contains the results of a database query to find recipes, based on the ingredients that the user has selected they have. It binds each recipe to a row in a recyclerview.

The class will determine if any recipes exist in the database that contain at least one ingredient the user has selected, and then if there are multiple results, order the results by recipe that has the most of their ingredients. The user can also opt to see recipes with ONLY the ingredients they have by clicking a switch.

The class is also aware of missing ingredients that the user does NOT have to prepare the dish, and will display these in the view holder. Additionally, the class has a view which shows the missing ingredients that exist in the database and suggest these ingredients to the end user. 

If the user clicks any of these missing ingredients (noting they also have it, e.g. oil) the results will be updated, and possibly re-organised, to always show recipes that have the most ingredients at the top of the recyclerview.

Finally, from this view, the user can save recipes (by clicking the save icon, which sets isSaved for that recipe in the recipe database to TRUE), and can navigate to the details of the recipe when the user clicks the row. When they click, the Recipe class passes via intent the details of the recipe to the RecipeDetails class.

RecipeDetails: shows the details of the clicked recipe. Gets the details of the recipe, such as recipe name, recipe ingredients, recipe steps, and recipe image (if applicable) via intent from the Recipes class, and populates the activity with these contents.

End users can also share recipes via an onClick method which calls the android.content.Intent.ACTION_SEND method, which will take the content in the view and convert to plain text, and then give the user the option to share the content.

### Dietary Preferences ###
This class contains all the possible dietary preferences for the end user. The database is populated when the application is started, and intolerances are stored in CSV files sorted by category. If the user is an existing user and has set intolerances before, these will be loaded in the onLoginSuccess method in Login.class and marked active in the Intolerances database.

When a user in this activity ticks, or unticks an intolerance, the ingredients, and recipes that contain these ingredients, will be marked selectable/unselectable in the database.

Intolerances are also taken from the java file and saved as a JSON file so when the user logs out, the intolerances list is clear for the next user, and their saved intolerances will be restored when they re-login.

### Saved & Custom Recipes ###
From the Recipes screen, the user is able to save recipes they want to re-visit later. The user is also able to add their own custom recipes, which will be saved in the database as a custom recipe.

The user will also be able to delete or unsave recipes from the view, and the application calls a database method to check whether the recipe in question is custom or saved, and either delete the record from the database, or mark it as saved = false.

### Pantry ###
This class contains the ingredients in the pantry for the specified end. If the user is an existing user and has saved  pantry ingredients before, these will be loaded in the onLoginSuccess method in Login.class.

From this screen, the user can add ingredients to their pantry by typing ingredients in the ingredients autocomplete box. Once they do so, they will be presented with ingredients in the database that contains their entry (e.g. typing "chicken" will return "chicken breast", "chicken gravy", "chicken drumsticks" etc. 

Through an onClick method, the user can then click on these to add them to their pantry, and can remove them by clicking the X. 

Pantry ingredients are also taken from the java file and saved as a JSON file so when the user logs out, the pantry is clear for the next user, and their pantry will be restored when they re-login.

### Logs & About ###
The About screen can be navigated from the MainActivity, and reads from a text file (about.txt) and displays that information in the textView.

The Log screen can be navigated from here, which shows any log messages that were inserted into the Log database (entities/LogRecords) through try/catch statements in the application. The user can opt to clear the logs.

### Google Ads ###
Google Advertisments, in the banner format, has been implemented upon several screens in the application. As ads may not always be served to the end user, this view is set to wrap_content, so the AdView will only be shown if an ad is served by adRequest.

## Having problems? ##
Please feel free to submit issues with any bugs or other unforseen issues you experience with the application. While this project has been tested and checked carefully to ensure the master branch is bug free, unknown bugs or exceptions could possibly exist. If you have any problems, please open an issue describing the problem and how to reproduce it, and we'll be sure to take a look at it.

## Permission ##
Unless permission is otherwise provided by myself, this code is NOT free to be reused or modified for commercial purposes, but may be utilised for self-learning or educational purposes, If you are in doubt on re-using this code, please contact me
at stevecrossin@gmail.com
