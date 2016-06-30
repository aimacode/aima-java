package aima.test.core.unit.logic.firstorder.parsing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.firstorder.domain.DomainFactory;
import aima.core.logic.basic.firstorder.domain.FOLDomain;
import aima.core.logic.basic.firstorder.parsing.FOLLexer;
import aima.core.logic.basic.firstorder.parsing.FOLParser;
import aima.core.logic.basic.firstorder.parsing.ast.ConnectedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Constant;
import aima.core.logic.basic.firstorder.parsing.ast.Function;
import aima.core.logic.basic.firstorder.parsing.ast.NotSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Predicate;
import aima.core.logic.basic.firstorder.parsing.ast.QuantifiedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.TermEquality;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * @author Anurag Rai
 */
public class FOLParserTest {
	FOLLexer lexer;

	FOLParser parser;

	@Before
	public void setUp() {
		FOLDomain domain = DomainFactory.crusadesDomain();

		lexer = new FOLLexer(domain);
		parser = new FOLParser(lexer);
	}

	@Test
	public void testParseSimpleVariable() {
		parser.setUpToParse("x");
		Term v = parser.parseVariable();
		Assert.assertEquals(v, new Variable("x"));
	}

	@Test
	public void testParseIndexedVariable() {
		parser.setUpToParse("x1");
		Term v = parser.parseVariable();
		Assert.assertEquals(v, new Variable("x1"));
	}

	@Test(expected = RuntimeException.class)
	public void testNotAllowedParseLeadingIndexedVariable() {
		parser.setUpToParse("1x");
		parser.parseVariable();
	}

	@Test
	public void testParseSimpleConstant() {
		parser.setUpToParse("John");
		Term c = parser.parseConstant();
		Assert.assertEquals(c, new Constant("John"));
	}

	@Test
	public void testParseFunction() {
		parser.setUpToParse("BrotherOf(John)");
		Term f = parser.parseFunction();
		Assert.assertEquals(f, getBrotherOfFunction(new Constant("John")));
	}

	@Test
	public void testParseMultiArityFunction() {
		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		Term f = parser.parseFunction();
		Assert.assertEquals(f, getLegsOfFunction());
		Assert.assertEquals(3, ((Function) f).getTerms().size());
	}

	@Test
	public void testPredicate() {
		// parser.setUpToParse("King(John)");
		Predicate p = (Predicate) parser.parse("King(John)");
		Assert.assertEquals(p, getKingPredicate(new Constant("John")));
	}

	@Test
	public void testTermEquality() {
		try {
			TermEquality te = (TermEquality) parser
					.parse("BrotherOf(John) = EnemyOf(Saladin)");
			Assert.assertEquals(te, new TermEquality(
					getBrotherOfFunction(new Constant("John")),
					getEnemyOfFunction()));
		} catch (RuntimeException e) {
			Assert.fail("RuntimeException thrown");
		}
	}

	@Test
	public void testTermEquality2() {
		try {
			TermEquality te = (TermEquality) parser
					.parse("BrotherOf(John) = x)");
			Assert.assertEquals(te, new TermEquality(
					getBrotherOfFunction(new Constant("John")), new Variable(
							"x")));
		} catch (RuntimeException e) {
			Assert.fail("RuntimeException thrown");
		}
	}

	@Test
	public void testNotSentence() {
		NotSentence ns = (NotSentence) parser
				.parse("~ BrotherOf(John) = EnemyOf(Saladin)");
		Assert.assertEquals(ns.getNegated(), new TermEquality(
				getBrotherOfFunction(new Constant("John")),
				getEnemyOfFunction()));
	}

	@Test
	public void testSimpleParanthizedSentence() {
		Sentence ps = parser.parse("(~ King(John))");
		Assert.assertEquals(ps, new NotSentence(getKingPredicate(new Constant(
				"John"))));
	}

	@Test
	public void testExtraParanthizedSentence() {
		Sentence ps = parser.parse("(((~ King(John))))");
		Assert.assertEquals(ps, new NotSentence(getKingPredicate(new Constant(
				"John"))));
	}

	@Test
	public void testParseComplexParanthizedSentence() {
		Sentence ps = parser.parse("(~ BrotherOf(John) = EnemyOf(Saladin))");
		Assert.assertEquals(ps, new NotSentence(new TermEquality(
				getBrotherOfFunction(new Constant("John")),
				getEnemyOfFunction())));
	}

	@Test
	public void testParseSimpleConnectedSentence() {
		Sentence ps = parser.parse("(King(John) & ~ King(Richard))");

		Assert.assertEquals(ps, new ConnectedSentence("&",
				getKingPredicate(new Constant("John")), new NotSentence(
						getKingPredicate(new Constant("Richard")))));

		ps = parser.parse("(King(John) & King(Saladin))");
		Assert.assertEquals(ps, new ConnectedSentence("&",
				getKingPredicate(new Constant("John")),
				getKingPredicate(new Constant("Saladin"))));
	}

	@Test
	public void testComplexConnectedSentence1() {
		Sentence ps = parser
				.parse("((King(John) & ~ King(Richard)) | King(Saladin))");

		Assert.assertEquals(ps, new ConnectedSentence("|",
				new ConnectedSentence("&", getKingPredicate(new Constant(
						"John")), new NotSentence(
						getKingPredicate(new Constant("Richard")))),
				getKingPredicate(new Constant("Saladin"))));
	}

	@Test
	public void testQuantifiedSentenceWithSingleVariable() {
		Sentence qs = parser.parse("FORALL x  King(x)");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		Assert.assertEquals(qs, new QuantifiedSentence("FORALL", vars,
				getKingPredicate(new Variable("x"))));
	}

	@Test
	public void testQuantifiedSentenceWithTwoVariables() {
		Sentence qs = parser
				.parse("EXISTS x,y  (King(x) & BrotherOf(x) = y)");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		vars.add(new Variable("y"));
		ConnectedSentence cse = new ConnectedSentence("&",
				getKingPredicate(new Variable("x")), new TermEquality(
						getBrotherOfFunction(new Variable("x")), new Variable(
								"y")));
		Assert.assertEquals(qs, new QuantifiedSentence("EXISTS", vars, cse));
	}

	@Test
	public void testQuantifiedSentenceWithPathologicalParanthising() {
		Sentence qs = parser
				.parse("(( (EXISTS x,y  (King(x) & (BrotherOf(x) = y)) ) ))");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		vars.add(new Variable("y"));
		ConnectedSentence cse = new ConnectedSentence("&",
				getKingPredicate(new Variable("x")), new TermEquality(
						getBrotherOfFunction(new Variable("x")), new Variable(
								"y")));
		Assert.assertEquals(qs, new QuantifiedSentence("EXISTS", vars, cse));
	}

	@Test
	public void testParseMultiArityFunctionEquality() {
		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		Term f = parser.parseFunction();

		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		Term f2 = parser.parseFunction();
		Assert.assertEquals(f, f2);
		Assert.assertEquals(3, ((Function) f).getTerms().size());
	}

	@Test
	public void testConnectedImplication() {
		parser = new FOLParser(DomainFactory.weaponsDomain());
		parser.parse("((Missile(m) & Owns(Nono,m)) => Sells(West , m ,Nono))");
	}

	//
	// PRIVATE METHODS
	//
	private Function getBrotherOfFunction(Term t) {
		List<Term> l = new ArrayList<Term>();
		l.add(t);
		return new Function("BrotherOf", l);
	}

	private Function getEnemyOfFunction() {
		List<Term> l = new ArrayList<Term>();
		l.add(new Constant("Saladin"));
		return new Function("EnemyOf", l);
	}

	private Function getLegsOfFunction() {
		List<Term> l = new ArrayList<Term>();
		l.add(new Constant("John"));
		l.add(new Constant("Saladin"));
		l.add(new Constant("Richard"));
		return new Function("LegsOf", l);
	}

	private Predicate getKingPredicate(Term t) {
		List<Term> l = new ArrayList<Term>();
		l.add(t);
		return new Predicate("King", l);
	}
}