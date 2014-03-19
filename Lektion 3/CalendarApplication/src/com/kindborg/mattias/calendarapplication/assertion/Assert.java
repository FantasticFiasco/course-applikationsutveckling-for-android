package com.kindborg.mattias.calendarapplication.assertion;

import android.text.*;

/**
 * Class containing assertion methods used to assert either method arguments or class state.
 */
public class Assert {

	public static void assertTrue(boolean condition) {
		assertTrue(condition, "Condition must be true.");
	}
	
	public static void assertTrue(boolean condition, String errorMessage) {
		if (!condition) {
			throw new RuntimeException(errorMessage);
		}
	}
	
	public static void assertFalse(boolean condition) {
		assertFalse(condition, "Condition must be false.");
	}
	
	public static void assertFalse(boolean condition, String errorMessage) {
		if (condition) {
			throw new RuntimeException(errorMessage);
		}
	}
	
	public static void assertNull(Object object) {
		assertNull(object, "Object must be null.");
	}
	
	public static void assertNull(Object object, String errorMessage) {
		if (object != null) {
			throw new RuntimeException(errorMessage);
		}
	}
	
	public static void assertNotNull(Object object) {
		assertNotNull(object, "Object must be separtate from null.");
	}
	
	public static void assertNotNull(Object object, String errorMessage) {
		if (object == null) {
			throw new RuntimeException(errorMessage);
		}
	}
	
	public static void assertNotNullOrEmpty(String text) {
		assertNotNullOrEmpty(text, "Text must be separtate from null or empty.");
	}
	
	public static void assertNotNullOrEmpty(String text, String errorMessage) {
		if (TextUtils.isEmpty(text)) {
			throw new RuntimeException(errorMessage);
		}
	}
}