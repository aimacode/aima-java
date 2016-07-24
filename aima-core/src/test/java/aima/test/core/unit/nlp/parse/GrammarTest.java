package aima.test.core.unit.nlp.parse;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import aima.core.nlp.parsing.grammars.ProbUnrestrictedGrammar;
import aima.core.nlp.parsing.grammars.Rule;

public class GrammarTest {
	
	ProbUnrestrictedGrammar g; ProbUnrestrictedGrammar g2;
	
	@Before
	public void setup() {
		g = new ProbUnrestrictedGrammar(); // reset grammar before each test
	}

	@Test
	public void testValidRule() {
		Rule invalidR = new Rule( null, new ArrayList<String>(Arrays.asList("W","Z")),(float)0.5);
		Rule validR   = new Rule( new ArrayList<String>(Arrays.asList("W")), 
								  new ArrayList<String>(Arrays.asList("a","s")),(float)0.5);
		Rule validR2  = new Rule( new ArrayList<String>(Arrays.asList("W")), null, (float)0.5);
		assertFalse( g.validRule(invalidR));
		assertTrue(  g.validRule(validR));
		assertTrue(  g.validRule(validR2));
		
	}
	
	/**
	 * Grammar should not allow a rule of the form 
	 * null -> X, where X is a combo of variables and terminals
	 */
	@Test
	public void testRejectNullLhs() {
		Rule r = new Rule( new ArrayList<String>() , new ArrayList<String>(), (float)0.50); // test completely null rule
		// test only null lhs
		Rule r2 = new Rule( null, new ArrayList<String>(Arrays.asList("W","Z")),(float)0.50);
		assertFalse( g.addRule(r));
		assertFalse( g.addRule(r2));
	}
	
	/**
	 * Grammar (unrestricted) should accept all the rules in the test,
	 * as they have non-null left hand sides
	 */
	@Test
	public void testAcceptValidRules() {
		Rule unrestrictedRule = new Rule( new ArrayList<String>(Arrays.asList("A","a","A","B")),
										  new ArrayList<String>(Arrays.asList("b","b","A","C")),(float)0.50);
		Rule contextSensRule =  new Rule( new ArrayList<String>(Arrays.asList("A","a","A")),
				  						  new ArrayList<String>(Arrays.asList("b","b","A","C")),(float)0.50);
		Rule contextFreeRule = new Rule( new ArrayList<String>(Arrays.asList("A")),
				  						  new ArrayList<String>(Arrays.asList("b","b","A","C")),(float)0.50);
		Rule regularRule     = new Rule( new ArrayList<String>(Arrays.asList("A")),
				  						 new ArrayList<String>(Arrays.asList("b","C")),(float)0.50);
		Rule nullRHSRule     = new Rule( new ArrayList<String>(Arrays.asList("A","B")), null ,(float)0.50);
		// try adding these rules in turn
		assertTrue( g.addRule( unrestrictedRule ));
		assertTrue( g.addRule( contextSensRule  ));
		assertTrue( g.addRule( contextFreeRule  ));
		assertTrue( g.addRule( regularRule      ));
		assertTrue( g.addRule( nullRHSRule      ));
	}
	
	/**
	 * Test that Grammar class correctly updates its 
	 * list of variables and terminals when a new rule is added
	 */
	@Test 
	public void testUpdateVarsAndTerminals() {
		// add a rule that has variables and terminals not 
		// already in the grammar
		g.addRule( new Rule( new ArrayList<String>(Arrays.asList("Z")),
					 		  new ArrayList<String>(Arrays.asList("z","Z")),(float)0.50));
		assertTrue( g.terminals.contains("z") && !g.terminals.contains("Z"));
		assertTrue( g.vars.contains("Z") && !g.vars.contains("z"));
	}
	
	@Test
	public void testIsVariable() {
		assertTrue(  ProbUnrestrictedGrammar.isVariable("S"));
		assertTrue(  ProbUnrestrictedGrammar.isVariable("SSSSS"));
		assertFalse( ProbUnrestrictedGrammar.isVariable("s"));
		assertFalse( ProbUnrestrictedGrammar.isVariable("tttt"));
	}
	
	@Test
	public void testIsTerminal() {
		assertTrue(  ProbUnrestrictedGrammar.isTerminal("x"));
		assertTrue(  ProbUnrestrictedGrammar.isTerminal("xxxxx"));
		assertFalse( ProbUnrestrictedGrammar.isTerminal("X"));
		assertFalse( ProbUnrestrictedGrammar.isTerminal("XXXXXX"));
	}
}
