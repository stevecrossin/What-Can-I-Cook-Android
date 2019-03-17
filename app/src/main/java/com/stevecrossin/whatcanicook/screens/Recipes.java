package com.stevecrossin.whatcanicook.screens;

//This class handles all the recipes functions for this application, including reading the recipes and providing recipe results
public class Recipes {
    String recipename;
    String recipeingredients;//ingredients
    String recipesteps;


    //Load recipes into memory from csv file, apply filters.
    public void loadRecipes() {
        /*
        This method will need to
        1. Load all recipes from recipes.csv stored in permanent storage
        2. Update the contents of the recipes room database with any new entries in the csv
       */
    }

    //Perform queries with ingredients data to find recipe results
    public void findRecipes() {
       /*
        This method will perform the following actions to check for recipe results
        1. Take the parcel passed by the IngredientPicker.searchRecipe class
        2. Query the ingredients provided against the recipes database, and check each row's ingredients column.
        Data in the ingredients column is stored in this format, separated by semicolons.
        "(chicken, 200, grams); (oil, 50, ml)"

        The code will parse the ingredients names in that row and compare them to the selected ingredients passed.

        If all ingredients are available, the recipe will be flagged as a valid ingredient, and will be passed to the next
        function for display once query complete.

        If some ingredients are available, the alternative ingredient for missing ingredients will be selected.
        If the alternative ingredient matches the recipe ingredient, this recipe will be noted as possible to make
        using an alternative and passed to the next function to display once query complete

        If, even using alternative ingredients, the recipe cannot be reproduced, the recipe will be skipped and the
        query will move onto the next row.

        This method will continue until all rows in the table have been queried
       */
    }

    //Display recipe results
    public void displayRecipes() {
       /*
       This method will handle outputting recipe results to display to the user.
       Once the findRecipes method is complete, recipe results will be sorted and displayed, in order of
       most number of ingredients used. Recipes that can be used using ingredients the user has, where the recipe
       calls for a different ingredient, will be returned in the same priority, by most primary
       ingredients used.

       The recyclerview of the reciperesults will be updated, and output will be need to be presented in the following format:

       Recipe Name, IngredientPicker user has e.g. "Roast chicken with potatoes, uses your chicken, potatoes and olive oil"
       If a substituite is recommended, it will instead state
       "Recipe Name, IngredientPicker they have, "You can substituite your X instead of Y"
       e.g. "Roast chicken with potatoes, uses your chicken and potatoes. You can use your canola oil instead of olive oil"
       */
    }

    //Display details of recipe when the recipe row is clicked
    public void recipeInfo() {
        /*
        When the recipe row is clicked, a query will be run on the recipes database to extract the
        recipe name, recipe ingredients including quantities and recipe steps, for that selected recipe.
        This data will then be passed to the Recipe Details scene and displayed in a scrollable text view
        */
    }


    //Reset ingredients to defaults
    public void resetIngredients() {
        /*
        This method is executed when the "Start Over" button is clicked on the "Recipe Details" activity.
        As the selected ingredients need to be reset to defaults, this button will perform a database update on
        the ingredients database table to set all ingredientselected fields back to false, which will have the effect
        of clearing the "My IngredientPicker" list.
         */
    }
}
