package aima.test.unit.logic.firstorder.parsing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.basic.firstorder.domain.FOLDomain;
import aima.core.logic.basic.firstorder.parsing.ast.ConnectedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Constant;
import aima.core.logic.basic.firstorder.parsing.ast.FOLNode;
import aima.core.logic.basic.firstorder.parsing.ast.Function;
import aima.core.logic.basic.firstorder.parsing.ast.NotSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Predicate;
import aima.core.logic.basic.firstorder.parsing.ast.QuantifiedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.TermEquality;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;
import aima.extra.logic.firstorder.parser.FirstOrderLogicLexer;
import aima.extra.logic.firstorder.parser.FirstOrderLogicParser;
import aima.extra.logic.firstorder.parser.FirstOrderVisitor;

/**
 * @author Ravi Mohan
 * @author Anurag Rai
 */
public class FOLParserTest {
	
	FirstOrderLogicParser parser;
	
	@Test
	public void testAtomicSentenceTrueParse() {
		FOLNode v = parseToSentence("King(John)");
		Assert.assertEquals(v, getKingPredicate(new Constant("John")));
	}

	@Test
	public void testTermEquality() {
		try {
			TermEquality te = (TermEquality) parseToSentence("BrotherOf(John) = EnemyOf(Saladin)");
			Assert.assertEquals(te, new TermEquality(getBrotherOfFunction(new Constant("John")), getEnemyOfFunction()));
		} catch (Exception e) {
			Assert.fail("Exception thrown");
		}
	}

	@Test
	public void testNotSentence() {
		NotSentence ns = (NotSentence) parseToSentence("~ BrotherOf(John) = EnemyOf(Saladin)");
		Assert.assertEquals(ns.getNegated(),
				new TermEquality(getBrotherOfFunction(new Constant("John")), getEnemyOfFunction()));
		
		//checking auto-population of domain after parsing
		FOLDomain domain = parser.getDomain();
		Set<String> functions  = new HashSet<>();
		functions.add("BrotherOf");
		functions.add("EnemyOf");
		Set<String> predicate  = new HashSet<>();
		Set<String> constants  = new HashSet<>();
		constants.add("John");
		constants.add("Saladin");
		
		Assert.assertEquals(domain.getFunctions(),functions);
		Assert.assertEquals(domain.getPredicates(),predicate);
		Assert.assertEquals(domain.getConstants(),constants);
	}

	@Test
	public void testSimpleParanthizedSentence() {
		Sentence ps = (Sentence) parseToSentence("(~ King(John))");
		Assert.assertEquals(ps, new NotSentence(getKingPredicate(new Constant("John"))));
	}

	@Test
	public void testExtraParanthizedSentence() {
		Sentence ps = (Sentence) parseToSentence("(((~ King(John))))");
		Assert.assertEquals(ps, new NotSentence(getKingPredicate(new Constant("John"))));
	}

	@Test
	public void testParseComplexParanthizedSentence() {
		Sentence ps = (Sentence) parseToSentence("(~ BrotherOf(John) = EnemyOf(Saladin))");
		Assert.assertEquals(ps,
				new NotSentence(new TermEquality(getBrotherOfFunction(new Constant("John")), getEnemyOfFunction())));
		
		//checking auto-population of domain after parsing
		FOLDomain domain = parser.getDomain();
		Set<String> functions  = new HashSet<>();
		functions.add("BrotherOf");
		functions.add("EnemyOf");
		Set<String> predicate  = new HashSet<>();
		Set<String> constants  = new HashSet<>();
		constants.add("John");
		constants.add("Saladin");
		Assert.assertEquals(domain.getFunctions(),functions);
		Assert.assertEquals(domain.getPredicates(),predicate);
		Assert.assertEquals(domain.getConstants(),constants);
	}

	@Test
	public void testParseSimpleConnectedSentence() {
		Sentence ps = (Sentence) parseToSentence("(King(John) & ~ King(Richard))");

		Assert.assertEquals(ps, new ConnectedSentence("&", getKingPredicate(new Constant("John")),
				new NotSentence(getKingPredicate(new Constant("Richard")))));

		ps = (Sentence) parseToSentence("(King(John) & King(Saladin))");
		Assert.assertEquals(ps, new ConnectedSentence("&", getKingPredicate(new Constant("John")),
				getKingPredicate(new Constant("Saladin"))));
	}

	@Test
	public void testComplexConnectedSentence1() {
		Sentence ps = (Sentence) parseToSentence("((King(John) & ~ King(Richard)) | King(Saladin))");

		Assert.assertEquals(ps,
				new ConnectedSentence("|",
						new ConnectedSentence("&", getKingPredicate(new Constant("John")),
								new NotSentence(getKingPredicate(new Constant("Richard")))),
						getKingPredicate(new Constant("Saladin"))));
		
		//checking auto-population of domain after parsing
		FOLDomain domain = parser.getDomain();
		Set<String> functions  = new HashSet<>();
		Set<String> predicate  = new HashSet<>();
		predicate.add("King");
		Set<String> constants  = new HashSet<>();
		constants.add("John");
		constants.add("Richard");
		constants.add("Saladin");
		
		Assert.assertEquals(domain.getFunctions(),functions);
		Assert.assertEquals(domain.getPredicates(),predicate);
		Assert.assertEquals(domain.getConstants(),constants);
	}

	@Test
	public void testQuantifiedSentenceWithSingleVariable() {
		Sentence qs = (Sentence) parseToSentence("FORALL x  King(x)");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		Assert.assertEquals(qs, new QuantifiedSentence("FORALL", vars, getKingPredicate(new Variable("x"))));
		
		//checking auto-population of domain after parsing
		FOLDomain domain = parser.getDomain();
		Set<String> functions  = new HashSet<>();
		Set<String> predicate  = new HashSet<>();
		predicate.add("King");
		Set<String> constants  = new HashSet<>();
		
		Assert.assertEquals(domain.getFunctions(),functions);
		Assert.assertEquals(domain.getPredicates(),predicate);
		Assert.assertEquals(domain.getConstants(),constants);
	}

	@Test
	public void testQuantifiedSentenceWithTwoVariables() {
		Sentence qs = (Sentence) parseToSentence("EXISTS x,y  (King(x) & BrotherOf(x) = y)");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		vars.add(new Variable("y"));
		ConnectedSentence cse = new ConnectedSentence("&", getKingPredicate(new Variable("x")),
				new TermEquality(getBrotherOfFunction(new Variable("x")), new Variable("y")));
		Assert.assertEquals(qs, new QuantifiedSentence("EXISTS", vars, cse));
		
		//checking auto-population of domain after parsing
		FOLDomain domain = parser.getDomain();
		Set<String> functions  = new HashSet<>();
		functions.add("BrotherOf");
		Set<String> predicate  = new HashSet<>();
		predicate.add("King");
		Set<String> constants  = new HashSet<>();
		
		Assert.assertEquals(domain.getFunctions(),functions);
		Assert.assertEquals(domain.getPredicates(),predicate);
		Assert.assertEquals(domain.getConstants(),constants);
	}

	@Test
	public void testQuantifiedSentenceWithPathologicalParanthising() {
		Sentence qs = (Sentence) parseToSentence("(( (EXISTS x,y  (King(x) & (BrotherOf(x) = y)) ) ))");
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("x"));
		vars.add(new Variable("y"));
		ConnectedSentence cse = new ConnectedSentence("&", getKingPredicate(new Variable("x")),
				new TermEquality(getBrotherOfFunction(new Variable("x")), new Variable("y")));
		Assert.assertEquals(qs, new QuantifiedSentence("EXISTS", vars, cse));
		
		//checking auto-population of domain after parsing
		FOLDomain domain = parser.getDomain();
		Set<String> functions  = new HashSet<>();
		functions.add("BrotherOf");
		Set<String> predicate  = new HashSet<>();
		predicate.add("King");
		Set<String> constants  = new HashSet<>();
		
		Assert.assertEquals(domain.getFunctions(),functions);
		Assert.assertEquals(domain.getPredicates(),predicate);
		Assert.assertEquals(domain.getConstants(),constants);
	}
	/*
	 * @Test public void testParseMultiArityFunctionEquality() {
	 * parser.setUpToParse("LegsOf(John,Saladin,Richard)"); Term f =
	 * parser.parseFunction();
	 * 
	 * parser.setUpToParse("LegsOf(John,Saladin,Richard)"); Term f2 =
	 * parser.parseFunction(); Assert.assertEquals(f, f2);
	 * Assert.assertEquals(3, ((Function) f).getTerms().size()); }
	 * 
	 * @Test public void testConnectedImplication() { parser = new
	 * FOLParser(DomainFactory.weaponsDomain()); parser.parse(
	 * "((Missile(m) & Owns(Nono,m)) => Sells(West , m ,Nono))"); }
	 */

	//
	// PRIVATE METHODS
	//
		
	private FOLNode parseToSentence(String stringToBeParsed) {
		FirstOrderLogicLexer lexer = new FirstOrderLogicLexer(new ANTLRInputStream(stringToBeParsed));
		TokenStream tokens = new CommonTokenStream(lexer);
		parser = new FirstOrderLogicParser(tokens);

		ParseTree tree = parser.parse();
		FOLNode node = new FirstOrderVisitor().visit(tree, parser);
		return node;
	}

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

//	private Function getLegsOfFunction() {
//		List<Term> l = new ArrayList<Term>();
//		l.add(new Constant("John"));
//		l.add(new Constant("Saladin"));
//		l.add(new Constant("Richard"));
//		return new Function("LegsOf", l);
//	}

	private Predicate getKingPredicate(Term t) {
		List<Term> l = new ArrayList<Term>();
		l.add(t);
		return new Predicate("King", l);
	}
}
