package aima.test.core.unit.nlp.parse;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import aima.core.nlp.data.lexicons.LexiconExamples;
import aima.core.nlp.parsing.Lexicon;
import aima.core.nlp.parsing.grammars.Rule;

public class LexiconTest {

	Lexicon l;
	Lexicon wumpusLex;
	
	@Before
	public void setUp() {
		l = new Lexicon();
		wumpusLex = LexiconExamples.buildWumpusLex();
	}
	
	@Test
	public void testAddEntry() {
		l.addEntry("EXAMPLE", "word", (float)0.10);
		assertTrue( l.containsKey("EXAMPLE"));
		assertEquals( l.get("EXAMPLE").size(), 1 );
	}
	
	@Test
	public void testAddEntryExistingCategory() {
		l.addEntry("EXAMPLE", "word", (float)0.10);
		l.addEntry("EXAMPLE", "second", (float)0.90);
		assertTrue( l.containsKey("EXAMPLE"));
		assertTrue( l.keySet().size() == 1 );
		assertTrue( l.get("EXAMPLE").get(1).getWord().equals("second"));

	}
	
	@Test
	public void testAddLexWords() {
		String key = "EXAMPLE";
		l.addLexWords( key, "stench", "0.05", "breeze", "0.10", "wumpus", "0.15");
		assertTrue( l.get(key).size() == 3);
		assertTrue( l.get(key).get(0).getWord().equals("stench"));
		
	}
	
	@Test
	public void testAddLexWordsWithInvalidArgs() {
		String key = "EXAMPLE";
		assertFalse( l.addLexWords( key, "stench", "0.05", "breeze"));
		assertFalse( l.containsKey(key));
	}
	
	@Test 
	public void testGetTerminalRules() {
		String key1 = "A"; String key2 = "B"; String key3 = "C";
		l.addLexWords(key1, "apple","0.25","alpha","0.5","arrow","0.25");
		l.addLexWords(key2, "ball","0.25","bench","0.25","blue","0.25","bell","0.25");
		l.addLexWords(key3, "carrot","0.25","canary","0.5","caper","0.25");
		ArrayList<Rule> rules1 = l.getTerminalRules(key1);
		ArrayList<Rule> rules2 = l.getTerminalRules(key2);
		ArrayList<Rule> rules3 = l.getTerminalRules(key3);
		assertEquals( rules1.size(), 3 );
		assertEquals( rules1.get(0).rhs.get(0), "apple");
		assertEquals( rules2.size(), 4 );
		assertEquals( rules2.get(3).rhs.get(0), "bell");
		assertEquals( rules3.size(), 3);
		assertEquals( rules3.get(1).lhs.get(0), "C");
	}
	
	@Test 
	public void testGetAllTerminalRules() {
		String key1 = "A"; String key2 = "B"; String key3 = "C";
		l.addLexWords(key1, "apple","0.25","alpha","0.5","arrow","0.25");
		l.addLexWords(key2, "ball","0.25","bench","0.25","blue","0.25","bell","0.25");
		l.addLexWords(key3, "carrot","0.25","canary","0.5","caper","0.25");
		ArrayList<Rule> allRules = l.getAllTerminalRules();
		assertEquals( allRules.size(), 10 );
		assertTrue( allRules.get(0).rhs.get(0).equals("apple") ||
					allRules.get(0).rhs.get(0).equals("ball") ||
					allRules.get(0).rhs.get(0).equals("carrot"));
	}
	
	

}
