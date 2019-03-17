package com.stevecrossin.whatcanicook.screens;

import java.util.Arrays;

/* Example code taken from https://examples.javacodegeeks.com/core-java/lang/string/java-string-split-example/, to help solve
the problem on how to split strings e.g. in ingredients.csv or intolerances.csv, will not be the final code used, here as
placeholder only.
 */

public class SplitExample {

    public static void main(String[] args) {

        String str = "abdc:psdv:sdvosdv:dfpbkdd";

        // split the array using ':' as a delimiter
        String[] parts = str.split(":");

        System.out.println("Using : as a delimiter " + Arrays.toString(parts));

        // split the array using 'd' as a delimiter
        parts = str.split("d");
        System.out.println(Arrays.toString(parts));

        String str2 = "This is a string to tokenize";

        // tokenize the string into words simply by splitting with " "
        parts = str2.split(" ");
        System.out.println(Arrays.toString(parts));
    }
}