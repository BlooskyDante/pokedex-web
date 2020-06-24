package com.pokedex.rest.util.validation;

import java.util.function.Predicate;

public abstract class ValidationPredicate {

    private ValidationPredicate() {
        // Abstract class - not meant to be instantiated
    }

    private static final String EMPTY_STRING = "";


    public static Predicate<String> validateNonEmptyString = str -> str != null && !str.equals(EMPTY_STRING);
}
