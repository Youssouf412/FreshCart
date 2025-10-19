package com.fresh.cart.helpers;

public class StringHelper {
    public static boolean regexEmailValidationPattern(String email) {
        String regex = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})";

        return email.matches(regex);
    }
}
