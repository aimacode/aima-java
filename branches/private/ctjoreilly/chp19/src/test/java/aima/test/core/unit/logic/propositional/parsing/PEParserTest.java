package aima.test.core.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */
public class PEParserTest {
	private PEParser parser;

	@Before
	public void setUp() {
		parser = new PEParser();
	}

	@Test
	public void testAtomicSentenceTrueParse() {
		AtomicSentence sen = (AtomicSentence) parser.parse("true");
		Assert.assertEquals(TrueSentence.class, sen.getClass());
		sen = (AtomicSentence) parser.parse("(true)");
		Assert.assertEquals(TrueSentence.class, sen.getClass());
		sen = (AtomicSentence) parser.parse("((true))");
		Assert.assertEquals(TrueSentence.class, sen.getClass());
	}

	@Test
	public void testAtomicSentenceFalseParse() {
		AtomicSentence sen = (AtomicSentence) parser.parse("faLse");
		Assert.assertEquals(FalseSentence.class, sen.getClass());
	}

	@Test
	public void testAtomicSentenceSymbolParse() {
		AtomicSentence sen = (AtomicSentence) parser.parse("AIMA");
		Assert.assertEquals(Symbol.class, sen.getClass());
	}

	@Test
	public void testNotSentenceParse() {
		UnarySentence sen = (UnarySentence) parser.parse("NOT AIMA");
		Assert.assertEquals(UnarySentence.class, sen.getClass());
	}

	@Test
	public void testBinarySentenceParse() {
		BinarySentence sen = (BinarySentence) parser
				.parse("(PETER  AND  NORVIG)");
		Assert.assertEquals(BinarySentence.class, sen.getClass());
	}

	@Test
	public void testMultiSentenceAndParse() {
		MultiSentence sen = (MultiSentence) parser
				.parse("(AND  NORVIG AIMA LISP)");
		Assert.assertEquals(MultiSentence.class, sen.getClass());
	}

	@Test
	public void testMultiSentenceOrParse() {
		MultiSentence sen = (MultiSentence) parser
				.parse("(OR  NORVIG AIMA LISP)");
		Assert.assertEquals(MultiSentence.class, sen.getClass());
	}

	@Test
	public void testMultiSentenceBracketedParse() {
		MultiSentence sen = (MultiSentence) parser
				.parse("((OR  NORVIG AIMA LISP))");
		Assert.assertEquals(MultiSentence.class, sen.getClass());
	}

	@Test
	public void testComplexSentenceParse() {
		BinarySentence sen = (BinarySentence) parser
				.parse("((OR  NORVIG AIMA LISP) AND TRUE)");
		Assert.assertEquals(BinarySentence.class, sen.getClass());

		sen = (BinarySentence) parser
				.parse("((OR  NORVIG AIMA LISP) AND (((LISP => COOL))))");
		Assert.assertEquals(BinarySentence.class, sen.getClass());
		Assert.assertEquals(
				" ( ( OR NORVIG AIMA LISP  )  AND  ( LISP => COOL ) )", sen
						.toString());

		String s = "((NOT (P AND Q ))  AND ((NOT (R AND S))))";
		sen = (BinarySentence) parser.parse(s);
		Assert.assertEquals(
				" (  ( NOT  ( P AND Q ) )  AND  ( NOT  ( R AND S ) )  )", sen
						.toString());

		s = "((P AND Q) OR (S AND T))";
		sen = (BinarySentence) parser.parse(s);
		Assert
				.assertEquals(" (  ( P AND Q ) OR  ( S AND T ) )", sen
						.toString());
		Assert.assertEquals("OR", sen.getOperator());

		s = "(NOT ((P AND Q) => (S AND T)))";
		UnarySentence nsen = (UnarySentence) parser.parse(s);
		// assertEquals("=>",sen.getOperator());
		s = "(NOT (P <=> (S AND T)))";
		nsen = (UnarySentence) parser.parse(s);
		Assert.assertEquals(" ( NOT  ( P <=>  ( S AND T ) ) ) ", nsen
				.toString());

		s = "(P <=> (S AND T))";
		sen = (BinarySentence) parser.parse(s);

		s = "(P => Q)";
		sen = (BinarySentence) parser.parse(s);

		s = "((P AND Q) => R)";
		sen = (BinarySentence) parser.parse(s);
	}
}
