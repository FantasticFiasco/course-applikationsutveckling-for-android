package com.kindborg.mattias.robbertranslator;

import static org.junit.Assert.*;

import org.junit.*;

import com.kindborg.mattias.robbertranslator.translator.Translator;

public class TranslatorTest {

	private Translator translator;
	
	@Before
	public void before() {
		translator = new Translator();
	}
	
	@Test
	public void oneWordToRobberLanguage() {
		// ACT
		String robberLanguageText = translator.toRobberLanguage("alla");
		
		// ASSERT
		assertEquals("alollola", robberLanguageText);
	}
	
	@Test
	public void twoWordsToRobberLanguage() {
		// ACT
		String robberLanguageText = translator.toRobberLanguage("alla ombord");
		
		// ASSERT
		assertEquals("alollola omomboborordod", robberLanguageText);
	}
	
	@Test
	public void threeWordsToRobberLanguage() {
		// ACT
		String robberLanguageText = translator.toRobberLanguage("Jag talar rövarspråket");
		
		// ASSERT
		assertEquals("jojagog totalolaror rorövovarorsospoproråkoketot", robberLanguageText);
	}
	
	@Test
	public void sentenceWithPunctuationToRobberLanguage() {
		// ACT
		String robberLanguageText = translator.toRobberLanguage("alla ombord.");
						
		// ASSERT
		assertEquals("alollola omomboborordod.", robberLanguageText);
	}
	
	@Test
	public void sentenceWithNumericalToRobberLanguage() {
		// ACT
		String robberLanguageText = translator.toRobberLanguage("3 kaniner");
								
		// ASSERT
		assertEquals("3 kokanoninoneror", robberLanguageText);
	}
	
	@Test
	public void wordWithHToRobberLanguage() {
		// ACT
		String robberLanguageText = translator.toRobberLanguage("hej");
										
		// ASSERT
		assertEquals("hotejoj", robberLanguageText);
	}
	
	@Test
	public void wordWithXToRobberLanguage() {
		// ACT
		String robberLanguageText = translator.toRobberLanguage("Sax");
												
		// ASSERT
		assertEquals("sosakoksos", robberLanguageText);
	}
	
	@Test
	public void wordWithCkToRobberLanguage() {
		// ACT
		String robberLanguageText = translator.toRobberLanguage("Tack");
														
		// ASSERT
		assertEquals("totakokkok", robberLanguageText);
	}
	
	@Test
	public void oneWordFromRobberLanguage() {
		// ACT
		String text = translator.fromRobberLanguage("alollola");
		
		// ASSERT
		assertEquals("alla", text);
	}
	
	@Test
	public void twoWordsFromRobberLanguage() {
		// ACT
		String text = translator.fromRobberLanguage("alollola omomboborordod");
		
		// ASSERT
		assertEquals("alla ombord", text);
	}
	
	@Test
	public void threeWordsFromRobberLanguage() {
		// ACT
		String text = translator.fromRobberLanguage("Jojagog totalolaror rorövovarorsospoproråkoketot");
		
		// ASSERT
		assertEquals("jag talar rövarspråket", text);
	}
	
	@Test
	public void sentenceWithPunctuationFromRobberLanguage() {
		// ACT
		String text = translator.fromRobberLanguage("alollola omomboborordod.");
						
		// ASSERT
		assertEquals("alla ombord.", text);
	}
	
	@Test
	public void sentenceWithNumericalFromRobberLanguage() {
		// ACT
		String text = translator.fromRobberLanguage("3 kokanoninoneror");
								
		// ASSERT
		assertEquals("3 kaniner", text);
	}
	
	@Test
	public void wordWithHFromRobberLanguage() {
		// ACT
		String text = translator.fromRobberLanguage("hotejoj");
										
		// ASSERT
		assertEquals("hej", text);
	}
	
	@Test
	public void wordWithXFromRobberLanguage() {
		// ACT
		String text = translator.fromRobberLanguage("Sosakoksos");
												
		// ASSERT
		assertEquals("sax", text);
	}
	
	@Test
	public void wordWithCkFromRobberLanguage() {
		// ACT
		String text = translator.fromRobberLanguage("Totakokkok");
														
		// ASSERT
		assertEquals("tack", text);
	}
}