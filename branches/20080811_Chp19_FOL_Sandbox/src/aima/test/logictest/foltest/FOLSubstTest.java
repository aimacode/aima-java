/*
 * Created on Sep 20, 2004
 *
 */
package aima.test.logictest.foltest;

import java.util.Properties;

import junit.framework.TestCase;
import aima.logic.fol.SubstVisitor;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */

public class FOLSubstTest extends TestCase {
	private Sentence sentence;

	FOLParser parser;

	SubstVisitor sv;

	@Override
	public void setUp() {
		parser = new FOLParser(DomainFactory.crusadesDomain());
		sv = new SubstVisitor(parser);
	}

	public void testSubstSingleVariableSucceedsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x)");
		Sentence expectedAfterSubst = parser.parse(" King(John) ");
		Sentence expectedAfterSubstCopy = (Sentence) expectedAfterSubst.copy();

		assertEquals(expectedAfterSubst, expectedAfterSubstCopy);
		Properties p = new Properties();
		p.setProperty("x", "John");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("King(x)"));

	}

	public void testSubstSingleVariableFailsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x)");
		Sentence expectedAfterSubst = parser.parse(" King(x) ");

		Properties p = new Properties();
		p.setProperty("y", "John");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("King(x)"));

	}

	public void testMultipleVariableSubstitutionWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x,y)");
		Sentence expectedAfterSubst = parser.parse(" King(John ,England) ");

		Properties p = new Properties();
		p.setProperty("x", "John");
		p.setProperty("y", "England");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("King(x,y)"));

	}

	public void testMultipleVariablePartiallySucceedsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x,y)");
		Sentence expectedAfterSubst = parser.parse(" King(John ,y) ");

		Properties p = new Properties();
		p.setProperty("x", "John");
		p.setProperty("z", "England");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("King(x,y)"));

	}

	public void testSubstSingleVariableSucceedsWithTermEquality() {
		Sentence beforeSubst = parser.parse("BrotherOf(x) = EnemyOf(y)");
		Sentence expectedAfterSubst = parser
				.parse("BrotherOf(John) = EnemyOf(Saladin)");

		Properties p = new Properties();
		p.setProperty("x", "John");
		p.setProperty("y", "Saladin");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("BrotherOf(x) = EnemyOf(y)"));

	}

	public void testSubstSingleVariableSucceedsWithTermEquality2() {
		Sentence beforeSubst = parser.parse("BrotherOf(John) = x)");
		Sentence expectedAfterSubst = parser.parse("BrotherOf(John) = Richard");

		Properties p = new Properties();
		p.setProperty("x", "Richard");
		p.setProperty("y", "Saladin");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("BrotherOf(John) = x)"), beforeSubst);

	}

	public void testSubstWithUniversalQuantifierAndSngleVariable() {
		Sentence beforeSubst = parser.parse("FORALL x King(x))");
		Sentence expectedAfterSubst = parser.parse("King(John)");

		Properties p = new Properties();
		p.setProperty("x", "John");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("FORALL x King(x))"), beforeSubst);
	}

	public void testSubstWithUniversalQuantifierAndZeroVariablesMatched() {
		Sentence beforeSubst = parser.parse("FORALL x King(x))");
		Sentence expectedAfterSubst = parser.parse("FORALL x King(x)");

		Properties p = new Properties();
		p.setProperty("y", "John");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("FORALL x King(x))"), beforeSubst);
	}

	public void testSubstWithUniversalQuantifierAndOneOfTwoVariablesMatched() {
		Sentence beforeSubst = parser.parse("FORALL x,y King(x,y))");
		Sentence expectedAfterSubst = parser.parse("FORALL x King(x,John)");

		Properties p = new Properties();
		p.setProperty("y", "John");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("FORALL x King(x,John))"), beforeSubst);
	}

	public void testSubstWithExistentialQuantifierAndSngleVariable() {
		Sentence beforeSubst = parser.parse("EXISTS x King(x))");
		Sentence expectedAfterSubst = parser.parse("King(John)");

		Properties p = new Properties();
		p.setProperty("x", "John");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("EXISTS x King(x)"), beforeSubst);
	}

	public void testSubstWithNOTSentenceAndSngleVariable() {
		Sentence beforeSubst = parser.parse("NOT King(x))");
		Sentence expectedAfterSubst = parser.parse("NOT King(John)");

		Properties p = new Properties();
		p.setProperty("x", "John");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("NOT King(x))"), beforeSubst);
	}

	public void testConnectiveANDSentenceAndSngleVariable() {
		Sentence beforeSubst = parser
				.parse("EXISTS x ( King(x) AND BrotherOf(x) = EnemyOf(y) )");
		Sentence expectedAfterSubst = parser
				.parse("( King(John) AND BrotherOf(John) = EnemyOf(Saladin) )");

		Properties p = new Properties();
		p.setProperty("x", "John");
		p.setProperty("y", "Saladin");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser
				.parse("EXISTS x ( King(x) AND BrotherOf(x) = EnemyOf(y) )"),
				beforeSubst);
	}

	public void testParanthisedSngleVariable() {
		Sentence beforeSubst = parser.parse("((( King(x))))");
		Sentence expectedAfterSubst = parser.parse("King(John) ");

		Properties p = new Properties();
		p.setProperty("x", "John");

		Sentence afterSubst = sv.getSubstitutedSentence(beforeSubst, p);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("((( King(x))))"), beforeSubst);
	}

}