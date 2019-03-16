package com.stevecrossin.whatcanicook.screens;

/*
This class covers the Pantry screen and database. Ingredients added here will be stored in a Room Database
called Pantry, with ingredients selected by the user displayed
 */
public class Pantry {
    String pantryingredient;

    /**
     * This will load the contents of the pantry database
     */

    public void loadPantry() {
        /*
        This method will be called when a user clicks the "My Pantry" button, or is otherwise navigated back to the pantry scene through the app
        1. Perform a query of the pantry database, and get all records in the database
        2. Pass that data to the pantry scene, and update the recyclerview with the items in the database
        */
    }

    /**
     * This is the method to add ingredients to the pantry. Steps to code it are below.
     */

    private void addPantryItem() {
        /*
        This method will be called when a user clicks the "Add Ingredients" button from the Pantry activity. The following steps need to occur
        1. The UI will navigate from the "Pantry" activity to a modified view of the Add Ingredients activity normally used for recipe searches.
        The label at the top needs to be updated to "Add Ingredients"
        The ingredientselected method as noted in Ingredients.java will be duplicated partially here, when a user selects an ingredient, it will be added to a list
        in the background.
        Once the user has selected all their ingredients and clicks "Check My Ingredients", the user will be taken to the "My Ingredients".
        If any ingredients selected are already in the pantry, an alert or some notification needs to be presented notifying the user of this.
        activity and be presented with their ingredients. The button presented will say "save ingredients to pantry" instead of "Find Recipes".
        2. The app will then take the strings for each individual ingredient, and add them to the pantry database, one per line, in the background.
        3. An exception handler needs to be coded to account for the possiblity the user will not add any ingredients.
        4. The application will then navigate back to the "My Pantry" activity, which will then refresh with the loadPantry method.
        */
    }

    /**
     * This method will will delete an ingredient from the users pantry list
     */

    public void deletePantryItem() {
        /*
        This method will be called when a user selects an ingredient in the pantry activity and clicks the delete cross.
        When this done, it will need to perform a few actions
        1. Take the name of the ingredient marked for deletion
        2. Find that ingredient in the Pantry database, and delete its entry
        3. Remove the ingredient from the list in the pantry activity
        */
    }
}