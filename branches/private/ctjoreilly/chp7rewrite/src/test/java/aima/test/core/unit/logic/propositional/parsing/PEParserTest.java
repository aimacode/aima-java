package aima.test.core.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
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
		UnarySentence sen = (UnarySentence) parser.parse("~ AIMA");
		Assert.assertEquals(UnarySentence.class, sen.getClass());
	}

	@Test
	public void testBinarySentenceParse() {
		BinarySentence sen = (BinarySentence) parser
				.parse("PETER  &  NORVIG");
		Assert.assertEquals(BinarySentence.class, sen.getClass());
	}

	@Test
	public void testComplexSentenceParse() {
		BinarySentence sen = (BinarySentence) parser
				.parse("(NORVIG | AIMA | LISP) & TRUE");
		Assert.assertEquals(BinarySentence.class, sen.getClass());

		sen = (BinarySentence) parser
				.parse("((NORVIG | AIMA | LISP) & (((LISP => COOL))))");
		Assert.assertEquals(BinarySentence.class, sen.getClass());
		Assert.assertEquals(
				" ( (NORVIG | AIMA | LISP  ) &  ( LISP => COOL ) )",
				sen.toString());

		String s = "((~ (P & Q ))  & ((~ (R & S))))";
		sen = (BinarySentence) parser.parse(s);
		Assert.assertEquals(
				" (  ( ~  ( P & Q ) ) &  ( ~ ( R & S ) )  )",
				sen.toString());

		s = "((P & Q) | (S & T))";
		sen = (BinarySentence) parser.parse(s);
		Assert.assertEquals(" (  ( P & Q ) |  ( S & T ) )", sen.toString());
		Assert.assertEquals("|", sen.getConnective().getSymbol());

		s = "(~ ((P & Q) => (S & T)))";
		UnarySentence nsen = (UnarySentence) parser.parse(s);
		// assertEquals("=>",sen.getOperator());
		s = "(~ (P <=> (S & T)))";
		nsen = (UnarySentence) parser.parse(s);
		Assert.assertEquals(" ( ~  ( P <=>  ( S & T ) ) ) ",
				nsen.toString());

		s = "(P <=> (S & T))";
		sen = (BinarySentence) parser.parse(s);

		s = "(P => Q)";
		sen = (BinarySentence) parser.parse(s);

		s = "((P & Q) => R)";
		sen = (BinarySentence) parser.parse(s);
	}
}
