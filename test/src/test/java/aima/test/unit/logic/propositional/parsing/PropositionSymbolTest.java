package aima.test.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;

public class PropositionSymbolTest {

	@Test
	public void test_isAlwaysTrueSymbol() {
		Assert.assertTrue(PropositionSymbol.isAlwaysTrueSymbol("True"));
		Assert.assertTrue(PropositionSymbol.isAlwaysTrueSymbol("tRue"));
		Assert.assertTrue(PropositionSymbol.isAlwaysTrueSymbol("trUe"));
		Assert.assertTrue(PropositionSymbol.isAlwaysTrueSymbol("truE"));
		Assert.assertTrue(PropositionSymbol.isAlwaysTrueSymbol("TRUE"));
		Assert.assertTrue(PropositionSymbol.isAlwaysTrueSymbol("true"));
		//
		Assert.assertFalse(PropositionSymbol.isAlwaysTrueSymbol("Tru3"));
		Assert.assertFalse(PropositionSymbol.isAlwaysTrueSymbol("True "));
		Assert.assertFalse(PropositionSymbol.isAlwaysTrueSymbol(" True"));
	}
	
	@Test
	public void test_isAlwaysFalseSymbol() {
		Assert.assertTrue(PropositionSymbol.isAlwaysFalseSymbol("False"));
		Assert.assertTrue(PropositionSymbol.isAlwaysFalseSymbol("fAlse"));
		Assert.assertTrue(PropositionSymbol.isAlwaysFalseSymbol("faLse"));
		Assert.assertTrue(PropositionSymbol.isAlwaysFalseSymbol("falSe"));
		Assert.assertTrue(PropositionSymbol.isAlwaysFalseSymbol("falsE"));
		Assert.assertTrue(PropositionSymbol.isAlwaysFalseSymbol("FALSE"));
		Assert.assertTrue(PropositionSymbol.isAlwaysFalseSymbol("false"));
		//
		Assert.assertFalse(PropositionSymbol.isAlwaysFalseSymbol("Fals3"));
		Assert.assertFalse(PropositionSymbol.isAlwaysFalseSymbol("False "));
		Assert.assertFalse(PropositionSymbol.isAlwaysFalseSymbol(" False"));
	}
	
	@Test
	public void test_isPropositionSymbol() {
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("True"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("False"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("A"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("A1"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("A_1"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("a"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("a1"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("A_1"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("_"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("_1"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("_1_2"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("$"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("$1"));
		Assert.assertTrue(PropositionSymbol.isPropositionSymbol("$1_1"));
		
		// Commas not allowed (only legal java identifier characters).
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A1,2"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol(" A"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A "));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A B"));
	}
	
	@Test
	public void test_isPropositionSymbolDoesNotContainConnectiveChars() {
		// '~', '&', '|', '=', '<', '>'
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("~"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("&"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("|"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("="));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("<"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol(">"));
		
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A~"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A&"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A|"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A="));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A<"));
		Assert.assertFalse(PropositionSymbol.isPropositionSymbol("A>"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction() {
		new PropositionSymbol("A_1,2");
	}
}
