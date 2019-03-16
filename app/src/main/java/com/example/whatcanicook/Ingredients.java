package com.example.whatcanicook;

/**
 * This class handles the ingredients functions of the application. Test addition here only for purpose of checking commit signing. Test commit to working
 */

public class Ingredients {

    String ingredienttype;//Type of ingredient e.g meat, veg
    String ingredientname;//Name of ingredient
    String ingredientalternative;//Possible alternative ingredients e.g. canola to sunflower oil
    Boolean ingredientselectable = true;//Whether or not the ingredient is selectable. Defaults to true.
    Boolean ingredientselected = false;//Whether or not the ingredient has been selected for recipe searching. Defaults to false.

    /**
     * This will perform the initial load of the ingredients from the ingredients.csv file and its display in the Ingredients activity
     */
    public void loadIngredients() {
    /*
    This method will be performed in the background once the user navigates to the Ingredients chooser activity from the main app landing page.
    MainActivity will pass the dish option that was clicked (e.g. breakfast, dessert) and pass this to Ingredients activity, which will update the
    label at the top of the activity to "What's for breakfast/dinner/dessert etc.

    It also needs to
    1. Read all information from ingredients.csv, a read only document stored in permanent storage
    2. Append the ingredients room database with any new ingredients/delete any ingredients that are no longer present in the csv
    3. Perform query on intolerances database to determine which intolerances are currently active
    4. Update ingredientselectable column in ingredients database to false when ingredient matches exclusion criteria
    5. Query database for ingredients that are not excluded, and update recyclerview in ingredient chooser activity with this list.
    */
    }

    //Notes when ingredients have been selected by the user, and handles searching of ingredients with the text box
    public void ingredientSelected() {
        /*
        When a user navigates the ingredients chooser, they can either click on a category in the recyclerview, or search with a search box that filters the ingredients view,
        that can then be clicked. Each ingredient has its own row in the database, and has an onClick listener to determine if clicked.
        When any row is clicked, for that particular ingredient, the
        */
    }


    //Handles the display of the selected ingredients in the My Ingredients activity
    public void checkIngredients() {
        /*
        When the user clicks the "Check my ingredients" the UI will navigate to the My Ingredients activity,
        and the recyclerview in that activity will be updated with the list of selected ingredients, where ingredientselected = true

        Ingredients can be removed from the list individually, and when an ingredient is removed from the My Ingredients Activity,
        it will perform a background task to change the ingredientselected database field to false
        */
    }

    //Passes the selected ingredients to Recipe class
    public void searchRecipe() {
        /*
        Once the user clicks the "Find Recipes" button in the My Ingredients activity, the following actions need to occur
        1. Query the ingredients database for all ingredients where ingredientselectable = true, and ingredientselected = true with a getter method
        This will pull a list of all currently selected valid ingredients. As a secondary piece of information, if any selected ingredients have alternatives,
        this will be collected as well.
        2. Pass that information in a parcel to the Recipes activity, which will perform the recipe results
        */

    }

}

