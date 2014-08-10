package aima.test.core.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.common.LexerException;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.ParserException;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class PLParserTest {
	private PLParser parser   = null;
	private Sentence sentence = null;
	private String   expected = null;

	@Before
	public void setUp() {
		parser = new PLParser();
	}

	@Test
	public void testAtomicSentenceTrueParse() {
		sentence = parser.parse("true");
		expected = prettyPrintF("True");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
		
		sentence = parser.parse("(true)");
		expected = prettyPrintF("True");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
		
		sentence = parser.parse("((true))");
		expected = prettyPrintF("True");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testAtomicSentenceFalseParse() {
		sentence = parser.parse("faLse");
		expected = prettyPrintF("False");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testAtomicSentenceSymbolParse() {
		sentence = parser.parse("AIMA");
		expected = prettyPrintF("AIMA");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testNotSentenceParse() {
		sentence = parser.parse("~ AIMA");
		expected = prettyPrintF("~AIMA");
		Assert.assertTrue(sentence.isNotSentence());
		Assert.assertEquals(expected, sentence.toString());
	}
	
	@Test
	public void testDoubleNegation() {
		sentence = parser.parse("~~AIMA");
		expected = prettyPrintF("~~AIMA");
		Assert.assertTrue(sentence.isNotSentence());
		Assert.assertTrue(sentence.getSimplerSentence(0).isNotSentence());
		Assert.assertTrue(sentence.getSimplerSentence(0).getSimplerSentence(0).isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testBinarySentenceParse() {
		sentence = parser.parse("PETER  &  NORVIG");
		expected = prettyPrintF("PETER & NORVIG");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertTrue(sentence.getSimplerSentence(0).isPropositionSymbol());
		Assert.assertTrue(sentence.getSimplerSentence(1).isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testComplexSentenceParse() {		
		sentence = parser.parse("(NORVIG | AIMA | LISP) & TRUE");
		expected = prettyPrintF("(NORVIG | AIMA | LISP) & True");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parser.parse("((NORVIG | AIMA | LISP) & (((LISP => COOL))))");
		expected = prettyPrintF("(NORVIG | AIMA | LISP) & (LISP => COOL)");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parser.parse("((~ (P & Q ))  & ((~ (R & S))))");
		expected = prettyPrintF("~(P & Q) & ~(R & S)");
		Assert.assertEquals(expected, sentence.toString());

		sentence = parser.parse("((P & Q) | (S & T))");
		expected = prettyPrintF("P & Q | S & T");
		Assert.assertTrue(sentence.isOrSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parser.parse("(~ ((P & Q) => (S & T)))");
		expected = prettyPrintF("~(P & Q => S & T)");
		Assert.assertTrue(sentence.isNotSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parser.parse("(~ (P <=> (S & T)))");
		expected = prettyPrintF("~(P <=> S & T)");
		Assert.assertTrue(sentence.isNotSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parser.parse("(P <=> (S & T))");
		expected = prettyPrintF("P <=> S & T");
		Assert.assertTrue(sentence.isBiconditionalSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parser.parse("(P => Q)");
		expected = prettyPrintF("P => Q");
		Assert.assertTrue(sentence.isImplicationSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parser.parse("((P & Q) => R)");
		expected = prettyPrintF("P & Q => R");
		Assert.assertTrue(sentence.isImplicationSentence());
		Assert.assertEquals(expected, sentence.toString());
	}
	
	@Test
	public void testSquareBracketsParse() {
		// Instead of
		sentence = parser.parse("[NORVIG | AIMA | LISP] & TRUE");
		expected = prettyPrintF("(NORVIG | AIMA | LISP) & True");
		Assert.assertEquals(expected, sentence.toString());
		
		// Alternating
		sentence = parser.parse("[A | B | C] & D & [C | D & (F | G | H & [I | J])]");
		expected = prettyPrintF("(A | B | C) & D & (C | D & (F | G | H & (I | J)))");
		Assert.assertEquals(expected, sentence.toString());
	}
	
	@Test
	public void testParserException() {
		try {
			sentence = parser.parse("");
			Assert.fail("A Parser Exception should have been thrown.");
		} catch (ParserException pex) {
			Assert.assertEquals(0, pex.getProblematicTokens().size());
		}
		
		try {
			sentence = parser.parse("A A1.2");
			Assert.fail("A Parser Exception should have been thrown.");
		} catch (ParserException pex) {
			Assert.assertEquals(0, pex.getProblematicTokens().size());
			Assert.assertTrue(pex.getCause() instanceof LexerException);
			Assert.assertEquals(4, ((LexerException)pex.getCause()).getCurrentPositionInInputExceptionThrown());
		}
		
		try {
			sentence = parser.parse("A & & B");
			Assert.fail("A Parser Exception should have been thrown.");		
		} catch (ParserException pex) {
			Assert.assertEquals(1, pex.getProblematicTokens().size());			
			Assert.assertTrue(pex.getProblematicTokens().get(0).getType() == LogicTokenTypes.CONNECTIVE);
			Assert.assertEquals(4, pex.getProblematicTokens().get(0).getStartCharPositionInInput());
		}
		
		try {
			sentence = parser.parse("A & (B & C &)");
			Assert.fail("A Parser Exception should have been thrown.");		
		} catch (ParserException pex) {
			Assert.assertEquals(1, pex.getProblematicTokens().size());			
			Assert.assertTrue(pex.getProblematicTokens().get(0).getType() == LogicTokenTypes.CONNECTIVE);
			Assert.assertEquals(11, pex.getProblematicTokens().get(0).getStartCharPositionInInput());
		}
	}
	
	@Test
	public void testIssue72() {
		// filter1 AND filter2 AND filter3 AND filter4
		sentence = parser.parse("filter1 & filter2 & filter3 & filter4");
		expected = prettyPrintF("filter1 & filter2 & filter3 & filter4");
		Assert.assertEquals(expected, sentence.toString());
		
		// (filter1 AND filter2) AND (filter3 AND filter4)
		sentence = parser.parse("(filter1 & filter2) & (filter3 & filter4)");
		expected = prettyPrintF("filter1 & filter2 & filter3 & filter4");
		Assert.assertEquals(expected, sentence.toString());

		// ((filter1 AND filter2) AND (filter3 AND filter4))
		sentence = parser.parse("((filter1 & filter2) & (filter3 & filter4))");
		expected = prettyPrintF("filter1 & filter2 & filter3 & filter4");
		Assert.assertEquals(expected, sentence.toString());
	}
	
	private String prettyPrintF(String prettyPrintedFormula) {
		Sentence s = parser.parse(prettyPrintedFormula);
		
		Assert.assertEquals("The pretty print formula should parse and print the same.", prettyPrintedFormula, ""+s);
		
		return prettyPrintedFormula;
	}
}
