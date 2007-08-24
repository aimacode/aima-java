/*
 * Created on Sep 18, 2004
 *
 */
package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.logic.fol.FOLDomain;
import aima.logic.fol.parsing.FOLLexer;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.ast.ConnectedSentence;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.NotSentence;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.QuantifiedSentence;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */

public class FOLParserTest extends TestCase {
	FOLLexer lexer;

	FOLParser parser;

	@Override
	public void setUp() {
		FOLDomain domain = DomainFactory.crusadesDomain();

		lexer = new FOLLexer(domain);
		parser = new FOLParser(lexer);

	}

	public void testParseSimpleVariable() {
		parser.setUpToParse("x");
		Term v = parser.parseVariable();
		assertEquals(v, new Variable("x"));
	}

	public void testParseSimpleConstant() {
		parser.setUpToParse("John");
		Term c = parser.parseConstant();
		assertEquals(c, new Constant("John"));
	}

	public void testParseFunction() {
		parser.setUpToParse("BrotherOf(John)");
		Term f = parser.parseFunction();
		assertEquals(f, getBrotherOfFunction("John"));
	}

	public void testParseMultiArityFunction() {
		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		Term f = parser.parseFunction();
		assertEquals(f, getLegsOfFunction());
		assertEquals(3, ((Function) f).getTerms().size());
	}

	public void testPredicate() {
		// parser.setUpToParse("King(John)");
		Predicate p = (Predicate) parser.parse("King(John)");
		assertEquals(p, getKingPredicate("John"));
	}

	public void testTermEquality() {
		try {
			TermEquality te = (TermEquality) parser
					.parse("BrotherOf(John) = EnemyOf(Saladin)");
			assertEquals(te, new TermEquality(getBrotherOfFunction("John"),
					getEnemyOfFunction()));
		} catch (RuntimeException e) {

		}

	}

	public void testTermEquality2() {
		try {
			TermEquality te = (TermEquality) parser
					.parse("BrotherOf(John) = x)");
			assertEquals(te, new TermEquality(getBrotherOfFunction("John"),
					new Variable("x")));
		} catch (RuntimeException e) {

		}

	}

	public void testNotSentence() {

		// parser.setUpToParse("NOT BrotherOf(John) = EnemyOf(Saladin)");
		NotSentence ns = (NotSentence) parser
				.parse("NOT BrotherOf(John) = EnemyOf(Saladin)");
		assertEquals(ns.getNegated(), new TermEquality(
				getBrotherOfFunction("John"), getEnemyOfFunction()));
	}

	public void testSimpleParanthizedSentence() {
		Sentence ps = parser.parse("(NOT King(John))");
		assertEquals(ps, new NotSentence(getKingPredicate("John")));
	}

	public void testExtraParanthizedSentence() {
		Sentence ps = parser.parse("(((NOT King(John))))");
		assertEquals(ps, new NotSentence(getKingPredicate("John")));
	}

	public void testParseComplexParanthizedSentence() {
		Sentence ps = parser.parse("(NOT BrotherOf(John) = EnemyOf(Saladin))");
		assertEquals(ps, new NotSentence(new TermEquality(
				getBrotherOfFunction("John"), getEnemyOfFunction())));
	}

	public void testParseSimpleConnectedSentence() {
		Sentence ps = parser.parse("(King(John) AND NOT King(Richard))");

		assertEquals(ps, new ConnectedSentence("AND", getKingPredicate("John"),
				new NotSentence(getKingPredicate("Richard"))));

		ps = parser.parse("(King(John) AND King(Saladin))");
		assertEquals(ps, new ConnectedSentence("AND", getKingPredicate("John"),
				getKingPredicate("Saladin")));
	}

	public void testComplexConnectedSentence1() {
		Sentence ps = parser
				.parse("((King(John) AND NOT King(Richard)) OR King(Saladin))");
		// Sentence ps = parser.parse("(King(John) AND King(Saladin))");
		assertEquals(ps, new ConnectedSentence("OR", new ConnectedSentence(
				"AND", getKingPredicate("John"), new NotSentence(
						getKingPredicate("Richard"))),
				getKingPredicate("Saladin")));
		// assertEquals()
	}

	public void testQuantifiedSentenceWithSingleVariable() {
		Sentence qs = parser.parse("FORALL x  King(x)");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		assertEquals(qs, new QuantifiedSentence("FORALL", vars,
				getKingPredicate("x")));
	}

	public void testQuantifiedSentenceWithTwoVariables() {
		Sentence qs = parser
				.parse("EXISTS x,y  (King(x) AND BrotherOf(x) = y)");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		vars.add(new Variable("y"));
		ConnectedSentence cse = new ConnectedSentence("AND",
				getKingPredicate("x"), new TermEquality(
						getBrotherOfFunction("x"), new Variable("y")));
		assertEquals(qs, new QuantifiedSentence("EXISTS", vars, cse));
	}

	public void testQuantifiedSentenceWithPathologicalParanthising() {
		Sentence qs = parser
				.parse("(( (EXISTS x,y  (King(x) AND (BrotherOf(x) = y)) ) ))");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		vars.add(new Variable("y"));
		ConnectedSentence cse = new ConnectedSentence("AND",
				getKingPredicate("x"), new TermEquality(
						getBrotherOfFunction("x"), new Variable("y")));
		assertEquals(qs, new QuantifiedSentence("EXISTS", vars, cse));

	}

	public Function getBrotherOfFunction(String who) {
		List<Term> l = new ArrayList<Term>();
		l.add(new Constant(who));
		return new Function("BrotherOf", l);
	}

	public Function getEnemyOfFunction() {
		List<Term> l = new ArrayList<Term>();
		l.add(new Constant("Saladin"));
		return new Function("EnemyOf", l);
	}

	public Function getLegsOfFunction() {
		List<Term> l = new ArrayList<Term>();
		l.add(new Constant("John"));
		l.add(new Constant("Saladin"));
		l.add(new Constant("Richard"));
		return new Function("LegsOf", l);
	}

	public Predicate getKingPredicate(String who) {
		List<Term> l = new ArrayList<Term>();
		l.add(new Constant(who));
		return new Predicate("King", l);
	}

	public void testParseMultiArityFunctionEquality() {
		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		Term f = parser.parseFunction();

		parser.setUpToParse("LegsOf(John,Saladin,Richard)");
		Term f2 = parser.parseFunction();
		assertEquals(f, f2);
		assertEquals(3, ((Function) f).getTerms().size());
	}

	public void testConnectedImplication() {
		parser = new FOLParser(DomainFactory.weaponsDomain());
		Sentence s = parser
				.parse("((Missile(m) AND Owns(NoNo,m)) => Sells(West , m ,NoNo))");
	}

}