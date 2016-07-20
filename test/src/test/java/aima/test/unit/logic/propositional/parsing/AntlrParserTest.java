package aima.test.unit.logic.propositional.parsing;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.extra.logic.propositional.parser.PropositionalLogicLexer;
import aima.extra.logic.propositional.parser.PropositionalLogicParser;
import aima.extra.logic.propositional.parser.PropositionalVisitor;

/*
 * @author Anurag Rai
 */
public class AntlrParserTest {
	
	private Sentence sentence = null;
	private String   expected = null;
	
	@Test
	public void testAtomicSentenceTrueParse() {
			
		sentence = parseToSentence("true");	
		expected = prettyPrintF("True");
		
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
		
		sentence = parseToSentence("(true)");
		expected = prettyPrintF("True");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
		
		sentence = parseToSentence("((true))");
		expected = prettyPrintF("True");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testAtomicSentenceFalseParse() {
		sentence = parseToSentence("faLse");
		expected = prettyPrintF("False");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testAtomicSentenceSymbolParse() {
		sentence = parseToSentence("AIMA");
		expected = prettyPrintF("AIMA");
		Assert.assertTrue(sentence.isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testNotSentenceParse() {
		sentence = parseToSentence("~ AIMA");
		expected = prettyPrintF("~AIMA");
		Assert.assertTrue(sentence.isNotSentence());
		Assert.assertEquals(expected, sentence.toString());
	}
	
	@Test
	public void testDoubleNegation() {
		sentence = parseToSentence("~~AIMA");
		expected = prettyPrintF("~~AIMA");
		Assert.assertTrue(sentence.isNotSentence());
		Assert.assertTrue(sentence.getSimplerSentence(0).isNotSentence());
		Assert.assertTrue(sentence.getSimplerSentence(0).getSimplerSentence(0).isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testBinarySentenceParse() {
		sentence = parseToSentence("PETER  &  NORVIG");
		expected = prettyPrintF("PETER & NORVIG");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertTrue(sentence.getSimplerSentence(0).isPropositionSymbol());
		Assert.assertTrue(sentence.getSimplerSentence(1).isPropositionSymbol());
		Assert.assertEquals(expected, sentence.toString());
	}

	@Test
	public void testComplexSentenceParse() {		
		sentence = parseToSentence("(NORVIG | AIMA | LISP) & TRUE");
		expected = prettyPrintF("(NORVIG | AIMA | LISP) & True");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parseToSentence("((NORVIG | AIMA | LISP) & (((LISP => COOL))))");
		expected = prettyPrintF("(NORVIG | AIMA | LISP) & (LISP => COOL)");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parseToSentence("((~ (P & Q ))  & ((~ (R & S))))");
		expected = prettyPrintF("~(P & Q) & ~(R & S)");
		Assert.assertEquals(expected, sentence.toString());

		sentence = parseToSentence("((P & Q) | (S & T))");
		expected = prettyPrintF("P & Q | S & T");
		Assert.assertTrue(sentence.isOrSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parseToSentence("(~ ((P & Q) => (S & T)))");
		expected = prettyPrintF("~(P & Q => S & T)");
		Assert.assertTrue(sentence.isNotSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parseToSentence("(~ (P <=> (S & T)))");
		expected = prettyPrintF("~(P <=> S & T)");
		Assert.assertTrue(sentence.isNotSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parseToSentence("(P <=> (S & T))");
		expected = prettyPrintF("P <=> S & T");
		Assert.assertTrue(sentence.isBiconditionalSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parseToSentence("(P => Q)");
		expected = prettyPrintF("P => Q");
		Assert.assertTrue(sentence.isImplicationSentence());
		Assert.assertEquals(expected, sentence.toString());

		sentence = parseToSentence("((P & Q) => R)");
		expected = prettyPrintF("P & Q => R");
		Assert.assertTrue(sentence.isImplicationSentence());
		Assert.assertEquals(expected, sentence.toString());
	}
	
	@Test
	public void testSquareBracketsParse() {
		// Instead of
		sentence = parseToSentence("[NORVIG | AIMA | LISP] & TRUE");
		expected = prettyPrintF("(NORVIG | AIMA | LISP) & True");
		Assert.assertEquals(expected, sentence.toString());
		
		// Alternating
		sentence = parseToSentence("[A | B | C] & D & [C | D & (F | G | H & [I | J])]");
		expected = prettyPrintF("(A | B | C) & D & (C | D & (F | G | H & (I | J)))");
		Assert.assertEquals(expected, sentence.toString());
	}
	
	@Test
	public void testMultipleSentences() {
		sentence = parseToSentence("(A & B) (C | D)");
		expected = prettyPrintF("A & B & (C | D)");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertEquals(expected, sentence.toString());
		
		sentence = parseToSentence("(A & B) (C & D)");
		expected = prettyPrintF("A & B & C & D");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertEquals(expected, sentence.toString());
		
		sentence = parseToSentence("(A & B) ~(C => D)");
		expected = prettyPrintF("A & B & ~(C => D)");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertEquals(expected, sentence.toString());
		/*
		sentence = parseToSentence("(A & B) (C | D) (E <=> F) (G & H | I)");
		expected = prettyPrintF("A & B & (C | D) & (E <=> F) & ( G & H | I)");
		Assert.assertTrue(sentence.isAndSentence());
		Assert.assertEquals(expected, sentence.toString());
		*/
	}
	
	
	@Test
	public void testParserException() {
		/*try {
			sentence = parseToSentence("");
			Assert.fail("A Parser Exception should have been thrown.");
		} catch (RuntimeException e) {
			//do nothing
		}*/
		try {
			sentence = parseToSentence("A & & B");
			Assert.fail("A Parser Exception should have been thrown.");		
		} catch (RuntimeException e) {
		}
		
		try {
			sentence = parseToSentence("A & (B & C &)");
			Assert.fail("A Parser Exception should have been thrown.");		
		} catch (RuntimeException e) {}
	}
	
	@Test
	public void testIssue72() {
		// filter1 AND filter2 AND filter3 AND filter4
		sentence = parseToSentence("filter1 & filter2 & filter3 & filter4");
		expected = prettyPrintF("filter1 & filter2 & filter3 & filter4");
		Assert.assertEquals(expected, sentence.toString());
		
		// (filter1 AND filter2) AND (filter3 AND filter4)
		sentence = parseToSentence("(filter1 & filter2) & (filter3 & filter4)");
		expected = prettyPrintF("filter1 & filter2 & filter3 & filter4");
		Assert.assertEquals(expected, sentence.toString());

		// ((filter1 AND filter2) AND (filter3 AND filter4))
		sentence = parseToSentence("((filter1 & filter2) & (filter3 & filter4))");
		expected = prettyPrintF("filter1 & filter2 & filter3 & filter4");
		Assert.assertEquals(expected, sentence.toString());
	}
	
	private String prettyPrintF(String prettyPrintedFormula) {
		Sentence s = parseToSentence(prettyPrintedFormula);
		
		Assert.assertEquals("The pretty print formula should parse and print the same.", prettyPrintedFormula, ""+s);
		
		return prettyPrintedFormula;
	}
	
	private Sentence parseToSentence( String stringToBeParsed ) {
		ANTLRInputStream input = new ANTLRInputStream(stringToBeParsed);
		PropositionalLogicLexer lexer = new PropositionalLogicLexer(input);
		TokenStream tokens = new CommonTokenStream(lexer);
		PropositionalLogicParser parser = new PropositionalLogicParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new ExceptionThrowingErrorListener());
		parser.setErrorHandler(new ExceptionThrowingErrorHandler());
		ParseTree tree = parser.parse();
		Sentence s = new PropositionalVisitor().visit(tree);
		return s;
	}
}
