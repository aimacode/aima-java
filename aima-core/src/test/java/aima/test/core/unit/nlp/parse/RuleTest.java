package aima.test.core.unit.nlp.parse;

import static org.junit.Assert.*;

import org.junit.Test;

import aima.core.nlp.parsing.grammars.Rule;

public class RuleTest {

	Rule testR;
	
	@Test
	public void testStringSplitConstructor() {
		testR = new Rule("A,B", "a,bb,c", (float)0.50);
		assertEquals( testR.lhs.size(), 2 );
		assertEquals( testR.rhs.size(), 3 );
		assertEquals( testR.lhs.get(1), "B");
		assertEquals( testR.rhs.get(2), "c");
	}
	
	@Test 
	public void testStringSplitConstructorOnEmptyStrings() {
		testR = new Rule("", "",(float)0.50);
		assertEquals( testR.lhs.size(), 0);
		assertEquals( testR.rhs.size(), 0);
	}
	
	@Test
	public void testStringSplitConstructorOnCommas() {
		testR = new Rule(",", ",",(float)0.50);
		assertEquals( testR.lhs.size(), 0);
		assertEquals( testR.rhs.size(), 0);
	}
	
	@Test( expected=IndexOutOfBoundsException.class)
	public void testStringSplitConstructorElementAccess() {
		testR = new Rule(",", "",(float)0.50);
		testR.lhs.get(0);
	}

}
