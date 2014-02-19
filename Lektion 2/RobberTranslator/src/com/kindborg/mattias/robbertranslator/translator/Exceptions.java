package com.kindborg.mattias.robbertranslator.translator;

import java.util.*;

public class Exceptions {

	private final List<Exception> exceptions;
	
	public Exceptions() {
		exceptions = new ArrayList<Exception>();
	}
	
	public void add(String text, String translatedRobberLanguageText) {
		exceptions.add(new Exception(text, translatedRobberLanguageText));
	}
	
	public boolean matchesPartialText(String text) {
		return getForPartialText(text) != null;
	}
	
	public boolean matchesPartialTranslatedText(String text) {
		return getForPartialTranslatedText(text) != null;
	}
	
	public Exception getForPartialText(String text) {
		for (Exception exception : exceptions) {
			if (text.startsWith(exception.text)) {
				return exception;
			}
		}
		
		return null;
	}
	
	public Exception getForPartialTranslatedText(String text) {
		for (Exception exception : exceptions) {
			if (text.startsWith(exception.robberText)) {
				return exception;
			}
		}
		
		return null;
	}
	
	public static class Exception {
				
		private final String text;
		private final String robberText;
		
		public Exception(String text, String robberText) {
			this.text = text;
			this.robberText = robberText;
		}
		
		public String getText() {
			return text;
		}
		
		public String getRobberText() {
			return robberText;
		}
	}
}