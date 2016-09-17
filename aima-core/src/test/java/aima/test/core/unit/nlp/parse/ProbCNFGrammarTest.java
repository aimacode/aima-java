package aima.test.core.unit.nlp.parse;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import aima.core.nlp.parsing.grammars.ProbCNFGrammar;
import aima.core.nlp.parsing.grammars.Rule;

/**
 * Test the class representing the Chomsky Normal Form grammar
 * @author Jonathon
 *
 */
public class ProbCNFGrammarTest {

	ProbCNFGrammar gEmpty;
	Rule validR; Rule invalidR;
	
	@Before
	public void setUp() {
		gEmpty = new ProbCNFGrammar();
		validR = new Rule( new ArrayList<String>(Arrays.asList("A")),
				new ArrayList<String>(Arrays.asList("Y","X")),(float)0.50);
		invalidR = new Rule( new ArrayList<String>(Arrays.asList("A")),
				  new ArrayList<String>(Arrays.asList("Y","X","Z")),(float)0.50); // too many RHS variables
	}
	
	@Test
	public void testAddValidRule() {
		assertTrue(gEmpty.addRule(validR));
	}
	
	@Test
	public void testAddInvalidRule() {
		assertFalse(gEmpty.addRule(invalidR));
	}
	
	@Test
	public void testValidRule() {
		assertTrue( gEmpty.validRule(validR));
		assertFalse( gEmpty.validRule(invalidR));
	}
	

}
