package com.kindborg.mattias.mediaplayingrobbertranslator.translator;

public class OngoingTranslation {

    private final String text;
    private final StringBuilder translatedText;

    private int currentPosition;

    public OngoingTranslation(String text) {
        this.text = text.toLowerCase();
        translatedText = new StringBuilder();
    }

    public boolean hasMoreCharacters() {
        return currentPosition < text.length();
    }

    public char getCurrentCharacter() {
        return text.charAt(currentPosition);
    }

    public String getCurrentString() {
        return text.substring(currentPosition);
    }

    public void addPartialTranslation(String partialText, String translatedPartialText) {
        currentPosition += partialText.length();
        translatedText.append(translatedPartialText);
    }

    public String getTranslatedText() {
        return translatedText.toString();
    }
}