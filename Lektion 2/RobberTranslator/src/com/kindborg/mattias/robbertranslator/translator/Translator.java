package com.kindborg.mattias.robbertranslator.translator;

public class Translator {
	
	private final Exceptions exceptions;
	
	public Translator() {
		// Add exceptions to the robber language rules
		exceptions = new Exceptions();
		exceptions.add("h", "hot");
		exceptions.add("x", "koksos");
		exceptions.add("ck", "kokkok");
	}
	
	public String toRobberLanguage(String text) {
		OngoingTranslation ongoingTranslation = new OngoingTranslation(text);
		
		while (ongoingTranslation.hasMoreCharacters()) {
			translateCurrentCharacterToRobberLanguage(ongoingTranslation);
		}
		
		return ongoingTranslation.getTranslatedText();
	}
	
	public String fromRobberLanguage(String text) {
		OngoingTranslation ongoingTranslation = new OngoingTranslation(text);
		
		while (ongoingTranslation.hasMoreCharacters()) {
			translateCurrentCharacterFromRobberLanguage(ongoingTranslation);
		}
		
		return ongoingTranslation.getTranslatedText();
	}
	
	private void translateCurrentCharacterToRobberLanguage(OngoingTranslation ongoingTranslation) {
		char currentCharacter = ongoingTranslation.getCurrentCharacter();
		
		String partialText;
		String translatedPartialText;
		
		if (!Character.isLetter(currentCharacter) || LanguageRules.isVowel(currentCharacter)) {
			// Non-letters and vowels are not translated in Robber Language
			partialText = Character.toString(currentCharacter);
			translatedPartialText = partialText;
		}
		else if (exceptions.matchesPartialText(ongoingTranslation.getCurrentString()))
		{
			// Consonant matches exception 
			Exceptions.Exception exception = exceptions.getForPartialText(ongoingTranslation.getCurrentString());
			partialText = exception.getText();
			translatedPartialText = exception.getRobberText();
		}
		else {
			// Normal translation of consonant
			partialText = Character.toString(currentCharacter);
			translatedPartialText = translateToRobberLanguage(currentCharacter);
		}
		
		// Append translated text
		ongoingTranslation.addPartialTranslation(partialText, translatedPartialText);
	}
	
	private void translateCurrentCharacterFromRobberLanguage(OngoingTranslation ongoingTranslation) {
		char currentCharacter = ongoingTranslation.getCurrentCharacter();
		
		String partialText;
		String translatedPartialText;
		
		if (!Character.isLetter(currentCharacter) || LanguageRules.isVowel(currentCharacter)) {
			// Non-letters and vowels are not translated in Robber Language
			partialText = Character.toString(currentCharacter);
			translatedPartialText = partialText;
		}
		else if (exceptions.matchesPartialTranslatedText(ongoingTranslation.getCurrentString()))
		{
			// Consonant matches exception 
			Exceptions.Exception exception = exceptions.getForPartialTranslatedText(ongoingTranslation.getCurrentString());
			partialText = exception.getRobberText();
			translatedPartialText = exception.getText();
		}
		else {
			// Normal translation of consonant
			partialText = translateToRobberLanguage(currentCharacter);
			translatedPartialText = Character.toString(currentCharacter);
		}
		
		// Append translated text
		ongoingTranslation.addPartialTranslation(partialText, translatedPartialText);
	}
		
	private static String translateToRobberLanguage(char consonant) {
		return String.format("%so%s", consonant, consonant);
	}
}
