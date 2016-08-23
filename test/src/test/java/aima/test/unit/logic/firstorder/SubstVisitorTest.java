package aima.test.unit.logic.firstorder;

import java.util.LinkedHashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.firstorder.SubstVisitor;
import aima.core.logic.basic.firstorder.parsing.ast.Constant;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;
import aima.extra.logic.firstorder.parser.FirstOrderLogicLexer;
import aima.extra.logic.firstorder.parser.FirstOrderLogicParser;
import aima.extra.logic.firstorder.parser.FirstOrderVisitor;

/**
 * @author Ravi Mohan
 * @author Anurag Rai
 */
public class SubstVisitorTest {

	private SubstVisitor sv;

	@Before
	public void setUp() {
		//parser = new FirstOrderLogicParser(DomainFactory.crusadesDomain());
		sv = new SubstVisitor();
	}

	@Test
	public void testSubstSingleVariableSucceedsWithPredicate() {
		Sentence beforeSubst = parseToSentence("King(x)");
		Sentence expectedAfterSubst = parseToSentence(" King(John) ");
		Sentence expectedAfterSubstCopy = expectedAfterSubst.copy();

		Assert.assertEquals(expectedAfterSubst, expectedAfterSubstCopy);
		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parseToSentence("King(x)"));
	}

	@Test
	public void testSubstSingleVariableFailsWithPredicate() {
		Sentence beforeSubst = parseToSentence("King(x)");
		Sentence expectedAfterSubst = parseToSentence(" King(x) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parseToSentence("King(x)"));
	}

	@Test
	public void testMultipleVariableSubstitutionWithPredicate() {
		Sentence beforeSubst = parseToSentence("King(x,y)");
		Sentence expectedAfterSubst = parseToSentence(" King(John ,England) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("England"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parseToSentence("King(x,y)"));
	}

	@Test
	public void testMultipleVariablePartiallySucceedsWithPredicate() {
		Sentence beforeSubst = parseToSentence("King(x,y)");
		Sentence expectedAfterSubst = parseToSentence(" King(John ,y) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("z"), new Constant("England"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parseToSentence("King(x,y)"));
	}

	@Test
	public void testSubstSingleVariableSucceedsWithTermEquality() {
		Sentence beforeSubst = parseToSentence("BrotherOf(x) = EnemyOf(y)");
		Sentence expectedAfterSubst = parseToSentence("BrotherOf(John) = EnemyOf(Saladin)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(beforeSubst, parseToSentence("BrotherOf(x) = EnemyOf(y)"));
	}

	@Test
	public void testSubstSingleVariableSucceedsWithTermEquality2() {
		Sentence beforeSubst = parseToSentence("BrotherOf(John) = x");
		Sentence expectedAfterSubst = parseToSentence("BrotherOf(John) = Richard");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("Richard"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parseToSentence("BrotherOf(John) = x"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndSngleVariable() {
		Sentence beforeSubst = parseToSentence("FORALL x King(x)");
		Sentence expectedAfterSubst = parseToSentence("King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parseToSentence("FORALL x King(x)"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndZeroVariablesMatched() {
		Sentence beforeSubst = parseToSentence("FORALL x King(x)");
		Sentence expectedAfterSubst = parseToSentence("FORALL x King(x)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parseToSentence("FORALL x King(x)"), beforeSubst);
	}

	@Test
	public void testSubstWithUniversalQuantifierAndOneOfTwoVariablesMatched() {
		Sentence beforeSubst = parseToSentence("FORALL x,y King(x,y)");
		Sentence expectedAfterSubst = parseToSentence("FORALL x King(x,John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("y"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parseToSentence("FORALL x,y King(x,y)"), beforeSubst);
	}

	@Test
	public void testSubstWithExistentialQuantifierAndSngleVariable() {
		Sentence beforeSubst = parseToSentence("EXISTS x King(x)");
		Sentence expectedAfterSubst = parseToSentence("King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);

		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parseToSentence("EXISTS x King(x)"), beforeSubst);
	}

	@Test
	public void testSubstWithNOTSentenceAndSngleVariable() {
		Sentence beforeSubst = parseToSentence("~ King(x)");
		Sentence expectedAfterSubst = parseToSentence("~ King(John)");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parseToSentence("~ King(x)"), beforeSubst);
	}

	@Test
	public void testConnectiveANDSentenceAndSngleVariable() {
		Sentence beforeSubst = parseToSentence("EXISTS x ( King(x) & BrotherOf(x) = EnemyOf(y) )");
		Sentence expectedAfterSubst = parseToSentence("( King(John) & BrotherOf(John) = EnemyOf(Saladin) )");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));
		p.put(new Variable("y"), new Constant("Saladin"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parseToSentence("EXISTS x ( King(x) & BrotherOf(x) = EnemyOf(y) )"), beforeSubst);
	}

	@Test
	public void testParanthisedSingleVariable() {
		Sentence beforeSubst = parseToSentence("((( King(x) )))");
		Sentence expectedAfterSubst = parseToSentence("King(John) ");

		Map<Variable, Term> p = new LinkedHashMap<Variable, Term>();
		p.put(new Variable("x"), new Constant("John"));

		Sentence afterSubst = sv.subst(p, beforeSubst);
		Assert.assertEquals(expectedAfterSubst, afterSubst);
		Assert.assertEquals(parseToSentence("((( King(x))))"), beforeSubst);
	}
	
	private Sentence parseToSentence(String stringToBeParsed) {
		FirstOrderLogicLexer lexer = new FirstOrderLogicLexer(new ANTLRInputStream(stringToBeParsed));
		TokenStream tokens = new CommonTokenStream(lexer);
		FirstOrderLogicParser parser = new FirstOrderLogicParser(tokens);

		ParseTree tree = parser.parse();
		Sentence node = (Sentence) new FirstOrderVisitor().visit(tree,parser);
		return node;
	}
}
