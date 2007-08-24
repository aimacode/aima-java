/*
 * Created on Sep 15, 2003 by Ravi Mohan
 *
 */
package aima.test.logictest.prop.parser;

import junit.framework.TestCase;
import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.AtomicSentence;
import aima.logic.propositional.parsing.ast.BinarySentence;
import aima.logic.propositional.parsing.ast.FalseSentence;
import aima.logic.propositional.parsing.ast.MultiSentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.parsing.ast.TrueSentence;
import aima.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */

public class PEParserTest extends TestCase {
	private PEParser parser;

	@Override
	public void setUp() {
		parser = new PEParser();
	}

	public void testAtomicSentenceTrueParse() {
		AtomicSentence sen = (AtomicSentence) parser.parse("true");
		assertEquals(TrueSentence.class, sen.getClass());
		sen = (AtomicSentence) parser.parse("(true)");
		assertEquals(TrueSentence.class, sen.getClass());
		sen = (AtomicSentence) parser.parse("((true))");
		assertEquals(TrueSentence.class, sen.getClass());
	}

	public void testAtomicSentenceFalseParse() {
		AtomicSentence sen = (AtomicSentence) parser.parse("faLse");
		assertEquals(FalseSentence.class, sen.getClass());
	}

	public void testAtomicSentenceSymbolParse() {
		AtomicSentence sen = (AtomicSentence) parser.parse("AIMA");
		assertEquals(Symbol.class, sen.getClass());
	}

	public void testNotSentenceParse() {
		UnarySentence sen = (UnarySentence) parser.parse("NOT AIMA");
		assertEquals(UnarySentence.class, sen.getClass());
	}

	public void testBinarySentenceParse() {
		BinarySentence sen = (BinarySentence) parser
				.parse("(PETER  AND  NORVIG)");
		assertEquals(BinarySentence.class, sen.getClass());
	}

	public void testMultiSentenceAndParse() {
		MultiSentence sen = (MultiSentence) parser
				.parse("(AND  NORVIG AIMA LISP)");
		assertEquals(MultiSentence.class, sen.getClass());
	}

	public void testMultiSentenceOrParse() {
		MultiSentence sen = (MultiSentence) parser
				.parse("(OR  NORVIG AIMA LISP)");
		assertEquals(MultiSentence.class, sen.getClass());
	}

	public void testMultiSentenceBracketedParse() {
		MultiSentence sen = (MultiSentence) parser
				.parse("((OR  NORVIG AIMA LISP))");
		assertEquals(MultiSentence.class, sen.getClass());
	}

	public void testComplexSentenceParse() {
		BinarySentence sen = (BinarySentence) parser
				.parse("((OR  NORVIG AIMA LISP) AND TRUE)");
		assertEquals(BinarySentence.class, sen.getClass());

		sen = (BinarySentence) parser
				.parse("((OR  NORVIG AIMA LISP) AND (((LISP => COOL))))");
		assertEquals(BinarySentence.class, sen.getClass());
		assertEquals(" ( ( OR NORVIG AIMA LISP  )  AND  ( LISP => COOL ) )",
				sen.toString());

		String s = "((NOT (P AND Q ))  AND ((NOT (R AND S))))";
		sen = (BinarySentence) parser.parse(s);
		assertEquals(" (  ( NOT  ( P AND Q ) )  AND  ( NOT  ( R AND S ) )  )",
				sen.toString());

		s = "((P AND Q) OR (S AND T))";
		sen = (BinarySentence) parser.parse(s);
		assertEquals(" (  ( P AND Q ) OR  ( S AND T ) )", sen.toString());
		assertEquals("OR", sen.getOperator());

		s = "(NOT ((P AND Q) => (S AND T)))";
		UnarySentence nsen = (UnarySentence) parser.parse(s);
		// assertEquals("=>",sen.getOperator());
		s = "(NOT (P <=> (S AND T)))";
		nsen = (UnarySentence) parser.parse(s);
		assertEquals(" ( NOT  ( P <=>  ( S AND T ) ) ) ", nsen.toString());

		s = "(P <=> (S AND T))";
		sen = (BinarySentence) parser.parse(s);

		s = "(P => Q)";
		sen = (BinarySentence) parser.parse(s);

		s = "((P AND Q) => R)";
		sen = (BinarySentence) parser.parse(s);

	}
}
