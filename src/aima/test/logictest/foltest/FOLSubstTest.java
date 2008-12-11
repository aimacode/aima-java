/*
 * Created on Sep 20, 2004
 *
 */
package aima.test.logictest.foltest;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;
import aima.logic.fol.SubstVisitor;
import aima.logic.fol.domain.DomainFactory;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */
public class FOLSubstTest extends TestCase {
	
	private FOLParser parser;
	private SubstVisitor sv;

	@Override
	public void setUp() {
		parser = new FOLParser(DomainFactory.crusadesDomain());
		sv = new SubstVisitor();
	}

	public void testSubstSingleVariableSucceedsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x)");
		Sentence expectedAfterSubst = parser.parse(" King(John) ");
		Sentence expectedAfterSubstCopy = (Sentence) expectedAfterSubst.copy();

		assertEquals(expectedAfterSubst, expectedAfterSubstCopy);
		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("King(x)"));
	}

	public void testSubstSingleVariableFailsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x)");
		Sentence expectedAfterSubst = parser.parse(" King(x) ");
		
		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("King(x)"));
	}

	public void testMultipleVariableSubstitutionWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x,y)");
		Sentence expectedAfterSubst = parser.parse(" King(John ,England) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("England"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("King(x,y)"));
	}

	public void testMultipleVariablePartiallySucceedsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x,y)");
		Sentence expectedAfterSubst = parser.parse(" King(John ,y) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("z"), new Constant("England"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("King(x,y)"));
	}

	public void testSubstSingleVariableSucceedsWithTermEquality() {
		Sentence beforeSubst = parser.parse("BrotherOf(x) = EnemyOf(y)");
		Sentence expectedAfterSubst = parser
				.parse("BrotherOf(John) = EnemyOf(Saladin)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(beforeSubst, parser.parse("BrotherOf(x) = EnemyOf(y)"));
	}

	public void testSubstSingleVariableSucceedsWithTermEquality2() {
		Sentence beforeSubst = parser.parse("BrotherOf(John) = x)");
		Sentence expectedAfterSubst = parser.parse("BrotherOf(John) = Richard");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("Richard"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("BrotherOf(John) = x)"), beforeSubst);
	}

	public void testSubstWithUniversalQuantifierAndSngleVariable() {
		Sentence beforeSubst = parser.parse("FORALL x King(x))");
		Sentence expectedAfterSubst = parser.parse("King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("FORALL x King(x))"), beforeSubst);
	}

	public void testSubstWithUniversalQuantifierAndZeroVariablesMatched() {
		Sentence beforeSubst = parser.parse("FORALL x King(x))");
		Sentence expectedAfterSubst = parser.parse("FORALL x King(x)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("FORALL x King(x))"), beforeSubst);
	}

	public void testSubstWithUniversalQuantifierAndOneOfTwoVariablesMatched() {
		Sentence beforeSubst = parser.parse("FORALL x,y King(x,y))");
		Sentence expectedAfterSubst = parser.parse("FORALL x King(x,John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("FORALL x,y King(x,y))"), beforeSubst);
	}

	public void testSubstWithExistentialQuantifierAndSngleVariable() {
		Sentence beforeSubst = parser.parse("EXISTS x King(x))");
		Sentence expectedAfterSubst = parser.parse("King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("EXISTS x King(x)"), beforeSubst);
	}

	public void testSubstWithNOTSentenceAndSngleVariable() {
		Sentence beforeSubst = parser.parse("NOT King(x))");
		Sentence expectedAfterSubst = parser.parse("NOT King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("NOT King(x))"), beforeSubst);
	}

	public void testConnectiveANDSentenceAndSngleVariable() {
		Sentence beforeSubst = parser
				.parse("EXISTS x ( King(x) AND BrotherOf(x) = EnemyOf(y) )");
		Sentence expectedAfterSubst = parser
				.parse("( King(John) AND BrotherOf(John) = EnemyOf(Saladin) )");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser
				.parse("EXISTS x ( King(x) AND BrotherOf(x) = EnemyOf(y) )"),
				beforeSubst);
	}

	public void testParanthisedSingleVariable() {
		Sentence beforeSubst = parser.parse("((( King(x))))");
		Sentence expectedAfterSubst = parser.parse("King(John) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		assertEquals(expectedAfterSubst, afterSubst);
		assertEquals(parser.parse("((( King(x))))"), beforeSubst);
	}
}