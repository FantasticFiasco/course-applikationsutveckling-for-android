package com.kindborg.mattias.mediaplayingrobbertranslator.translator;

public class LanguageRules {

    private static final String VOWELS = "aeiouy���";

    public static boolean isVowel(char c) {
        return VOWELS.indexOf(Character.toLowerCase(c)) >= 0;
    }
}
