package aima.test.core.unit.logic.fol;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */
public class SubstVisitorTest {

	private FOLParser parser;
	private SubstVisitor sv;

	@Before
	public void setUp() {
		parser = new FOLParser(DomainFactory.crusadesDomain());
		sv = new SubstVisitor();
	}

	@Test
	public void testSubstSingleVariableSucceedsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x)");
		Sentence expectedAfterSubst = parser.parse(" King(John) ");
		Sentence expectedAfterSubstCopy = expectedAfterSubst.copy();

		Assert.assertEquals(expectedAfterSubst, expectedAfterSubstCopy);
		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser.parse("King(x)"));
	}

	@Test
	public void testSubstSingleVariableFailsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x)");
		Sentence expectedAfterSubst = parser.parse(" King(x) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser.parse("King(x)"));
	}

	@Test
	public void testMultipleVariableSubstitutionWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x,y)");
		Sentence expectedAfterSubst = parser.parse(" King(John ,England) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("England"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser.parse("King(x,y)"));
	}

	@Test
	public void testMultipleVariablePartiallySucceedsWithPredicate() {
		Sentence beforeSubst = parser.parse("King(x,y)");
		Sentence expectedAfterSubst = parser.parse(" King(John ,y) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("z"), new Constant("England"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser.parse("King(x,y)"));
	}

	@Test
	public void testSubstSingleVariableSucceedsWithTermEquality() {
		Sentence beforeSubst = parser.parse("BrotherOf(x) = EnemyOf(y)");
		Sentence expectedAfterSubst = parser
				.parse("BrotherOf(John) = EnemyOf(Saladin)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parser
				.parse("BrotherOf(x) = EnemyOf(y)"));
	}

	@Test
	public void testSubstSingleVariableSucceedsWithTermEquality2() {
		Sentence beforeSubst = parser.parse("BrotherOf(John) = x)");
		Sentence expectedAfterSubst = parser.parse("BrotherOf(John) = Richard");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("Richard"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("BrotherOf(John) = x)"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndSngleVariable() {
		Sentence beforeSubst = parser.parse("FORALL x King(x))");
		Sentence expectedAfterSubst = parser.parse("King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("FORALL x King(x))"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndZeroVariablesMatched() {
		Sentence beforeSubst = parser.parse("FORALL x King(x))");
		Sentence expectedAfterSubst = parser.parse("FORALL x King(x)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("FORALL x King(x))"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndOneOfTwoVariablesMatched() {
		Sentence beforeSubst = parser.parse("FORALL x,y King(x,y))");
		Sentence expectedAfterSubst = parser.parse("FORALL x King(x,John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("FORALL x,y King(x,y))"), beforeSubst);
	}

	@Test
	public void testSubstWithExistentialQuantifierAndSngleVariable() {
		Sentence beforeSubst = parser.parse("EXISTS x King(x))");
		Sentence expectedAfterSubst = parser.parse("King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);

		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("EXISTS x King(x)"), beforeSubst);
	}

	@Test
	public void testSubstWithNOTSentenceAndSngleVariable() {
		Sentence beforeSubst = parser.parse("NOT King(x))");
		Sentence expectedAfterSubst = parser.parse("NOT King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("NOT King(x))"), beforeSubst);
	}

	@Test
	public void testConnectiveANDSentenceAndSngleVariable() {
		Sentence beforeSubst = parser
				.parse("EXISTS x ( King(x) AND BrotherOf(x) = EnemyOf(y) )");
		Sentence expectedAfterSubst = parser
				.parse("( King(John) AND BrotherOf(John) = EnemyOf(Saladin) )");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser
				.parse("EXISTS x ( King(x) AND BrotherOf(x) = EnemyOf(y) )"),
				beforeSubst);
	}

	@Test
	public void testParanthisedSingleVariable() {
		Sentence beforeSubst = parser.parse("((( King(x))))");
		Sentence expectedAfterSubst = parser.parse("King(John) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parser.parse("((( King(x))))"), beforeSubst);
	}
}