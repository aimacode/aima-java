package aima.test.unit.logic.firstorder;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.firstorder.Unifier;
import aima.core.logic.basic.firstorder.domain.FOLDomain;
import aima.core.logic.basic.firstorder.parsing.ast.Constant;
import aima.core.logic.basic.firstorder.parsing.ast.Function;
import aima.core.logic.basic.firstorder.parsing.ast.Predicate;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.TermEquality;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;
import aima.extra.logic.firstorder.parser.FirstOrderLogicLexer;
import aima.extra.logic.firstorder.parser.FirstOrderLogicParser;
import aima.extra.logic.firstorder.parser.FirstOrderVisitor;


/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class UnifierTest {

	private Unifier unifier;
	private Map<Variable, Term> theta;
	
	@Before
	public void setUp() {
		//parser = new FOLParser(DomainFactory.knowsDomain());
		unifier = new Unifier();
		theta = new Hashtable<Variable, Term>();
	}
	
	@Test
	public void testFailureIfThetaisNull() {
		Variable var = new Variable("x");
		Sentence sentence = parseToSentence("Knows(x)");
		theta = null;
		Map<Variable, Term> result = unifier.unify(var, sentence, theta);
		Assert.assertNull(result);
	}

	@Test
	public void testUnificationFailure() {
		Variable var = new Variable("x");
		Sentence sentence = parseToSentence("Knows(y)");
		theta = null;
		Map<Variable, Term> result = unifier.unify(var, sentence, theta);
		Assert.assertNull(result);
	}

	@Test
	public void testThetaPassedBackIfXEqualsYBothVariables() {
		Variable var1 = new Variable("x");
		Variable var2 = new Variable("x");

		theta.put(new Variable("dummy"), new Variable("dummy"));
		Map<Variable, Term> result = unifier.unify(var1, var2, theta);
		Assert.assertEquals(theta, result);
		Assert.assertEquals(1, theta.keySet().size());
		Assert.assertTrue(theta.containsKey(new Variable("dummy")));
	}

	@Test
	public void testVariableEqualsConstant() {
		Variable var1 = new Variable("x");
		Constant constant = new Constant("John");

		Map<Variable, Term> result = unifier.unify(var1, constant, theta);
		Assert.assertEquals(theta, result);
		Assert.assertEquals(1, theta.keySet().size());
		Assert.assertTrue(theta.keySet().contains(var1));
		Assert.assertEquals(constant, theta.get(var1));
	}

	@Test
	public void testSimpleVariableUnification() {
		Variable var1 = new Variable("x");
		List<Term> terms1 = new ArrayList<Term>();
		terms1.add(var1);
		Predicate p1 = new Predicate("King", terms1); // King(x)

		List<Term> terms2 = new ArrayList<Term>();
		terms2.add(new Constant("John"));
		Predicate p2 = new Predicate("King", terms2); // King(John)

		Map<Variable, Term> result = unifier.unify(p1, p2, theta);
		Assert.assertEquals(theta, result);
		Assert.assertEquals(1, theta.keySet().size());
		Assert.assertTrue(theta.keySet().contains(new Variable("x"))); // x =
		Assert.assertEquals(new Constant("John"), theta.get(var1)); // John
	}

	@Test
	public void testKnows1() {
		Sentence query = parseToSentence("Knows(John,x)");
		Sentence johnKnowsJane = parseToSentence("Knows(John,Jane)");
		Map<Variable, Term> result = unifier.unify(query, johnKnowsJane, theta);
		Assert.assertEquals(theta, result);
		Assert.assertTrue(theta.keySet().contains(new Variable("x"))); // x =
		Assert.assertEquals(new Constant("Jane"), theta.get(new Variable("x"))); // Jane
	}

	@Test
	public void testKnows2() {
		Sentence query = parseToSentence("Knows(John,x)");
		Sentence johnKnowsJane = parseToSentence("Knows(y,Bill)");
		Map<Variable, Term> result = unifier.unify(query, johnKnowsJane, theta);

		Assert.assertEquals(2, result.size());

		Assert.assertEquals(new Constant("Bill"), theta.get(new Variable("x"))); // x
		// =
		// Bill
		Assert.assertEquals(new Constant("John"), theta.get(new Variable("y"))); // y
		// =
		// John
	}

	@Test
	public void testKnows3() {
		Sentence query = parseToSentence("Knows(John,x)");
		Sentence johnKnowsJane = parseToSentence("Knows(y,Mother(y))");
		Map<Variable, Term> result = unifier.unify(query, johnKnowsJane, theta);

		Assert.assertEquals(2, result.size());

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("John"));
		Function mother = new Function("Mother", terms);
		Assert.assertEquals(mother, theta.get(new Variable("x")));
		Assert.assertEquals(new Constant("John"), theta.get(new Variable("y")));
	}

	@Test
	public void testKnows5() {
		Sentence query = parseToSentence("Knows(John,x)");
		Sentence johnKnowsJane = parseToSentence("Knows(y,z)");
		Map<Variable, Term> result = unifier.unify(query, johnKnowsJane, theta);

		Assert.assertEquals(2, result.size());

		Assert.assertEquals(new Variable("z"), theta.get(new Variable("x"))); // x
		// =
		// z
		Assert.assertEquals(new Constant("John"), theta.get(new Variable("y"))); // y
		// =
		// John
	}

	@Test
	public void testCascadedOccursCheck() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("P");
		domain.addFunction("F");
		domain.addFunction("SF0");
		domain.addFunction("SF1");
		//FOLParser parser = new FOLParser(domain);

		Sentence s1 = parseToSentence("P(SF1(v2),v2)");
		Sentence s2 = parseToSentence("P(v3,SF0(v3))");
		Map<Variable, Term> result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("P(v1,SF0(v1),SF0(v1),SF0(v1),SF0(v1))");
		s2 = parseToSentence("P(v2,SF0(v2),v2,     v3,     v2)");
		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("P(v1,   F(v2),F(v2),F(v2),v1,      F(F(v1)),F(F(F(v1))),v2)");
		s2 = parseToSentence("P(F(v3),v4,   v5,   v6,   F(F(v5)),v4,      F(v3),      F(F(v5)))");
		result = unifier.unify(s1, s2);

		Assert.assertNull(result);
	}

	/**
	 * From: TPTP:LCL418-1 Am performing an incorrect unification for:
	 * [is_a_theorem
	 * (equivalent(equivalent(c1744,c1743),equivalent(c1742,c1743))),
	 * is_a_theorem(equivalent(equivalent(c1752,c1751),c1752))]
	 * 
	 * which is giving the following substitution:
	 * 
	 * subst={c1744=equivalent(c1742,c1743), c1743=c1751,
	 * c1752=equivalent(c1742,c1751)}
	 * 
	 * which is incorrect as c1743 in the first function term needs to be c1751
	 * as this is the second substitution.
	 */
	@Test
	public void testBadCascadeSubstitution_LCL418_1() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("ISATHEOREM");
		domain.addFunction("EQUIVALENT");
		//FOLParser parser = new FOLParser(domain);

		Sentence s1 = parseToSentence("ISATHEOREM(EQUIVALENT(EQUIVALENT(c1744,c1743),EQUIVALENT(c1742,c1743)))");
		Sentence s2 = parseToSentence("ISATHEOREM(EQUIVALENT(EQUIVALENT(c1752,c1751),c1752))");
		Map<Variable, Term> result = unifier.unify(s1, s2);

		Assert.assertEquals(
				"{c1744=EQUIVALENT(c1742,c1751), c1743=c1751, c1752=EQUIVALENT(c1742,c1751)}",
				result.toString());
	}

	@Test
	public void testAdditionalVariableMixtures() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addPredicate("P");

		//FOLParser parser = new FOLParser(domain);

		// Test Cascade Substitutions handled correctly
		Sentence s1 = parseToSentence("P(z, x)");
		Sentence s2 = parseToSentence("P(x, a)");
		Map<Variable, Term> result = unifier.unify(s1, s2);

		Assert.assertEquals("{z=a, x=a}", result.toString());

		s1 = parseToSentence("P(x, z)");
		s2 = parseToSentence("P(a, x)");
		result = unifier.unify(s1, s2);

		Assert.assertEquals("{x=a, z=a}", result.toString());

		s1 = parseToSentence("P(w, w, w)");
		s2 = parseToSentence("P(x, y, z)");
		result = unifier.unify(s1, s2);

		Assert.assertEquals("{w=z, x=z, y=z}", result.toString());

		s1 = parseToSentence("P(x, y, z)");
		s2 = parseToSentence("P(w, w, w)");
		result = unifier.unify(s1, s2);

		Assert.assertEquals("{x=w, y=w, z=w}", result.toString());

		s1 = parseToSentence("P(x, B, F(y))");
		s2 = parseToSentence("P(A, y, F(z))");
		result = unifier.unify(s1, s2);

		Assert.assertEquals("{x=A, y=B, z=B}", result.toString());

		s1 = parseToSentence("P(F(x,B), G(y),         F(z,A))");
		s2 = parseToSentence("P(y,      G(F(G(w),w)), F(w,z))");
		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("P(F(G(A)), x,    F(H(z,z)), H(y,    G(w)))");
		s2 = parseToSentence("P(y,       G(z), F(v     ), H(F(w), x   ))");
		result = unifier.unify(s1, s2);

		Assert.assertEquals(
				"{y=F(G(A)), x=G(G(A)), v=H(G(A),G(A)), w=G(A), z=G(A)}",
				result.toString());
	}

	@Test
	public void testTermEquality() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addFunction("Plus");

		//FOLParser parser = new FOLParser(domain);

		TermEquality te1 = (TermEquality) parseToSentence("x = x");
		TermEquality te2 = (TermEquality) parseToSentence("x = x");

		// Both term equalities the same,
		// should unify but no substitutions.
		Map<Variable, Term> result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		// Different variable names but should unify.
		te1 = (TermEquality) parseToSentence("x1 = x1");
		te2 = (TermEquality) parseToSentence("x2 = x2");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("{x1=x2}", result.toString());

		// Test simple unification with reflexivity axiom
		te1 = (TermEquality) parseToSentence("x1 = x1");
		te2 = (TermEquality) parseToSentence("Plus(A,B) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals("{x1=Plus(A,B)}", result.toString());

		// Test more complex unification with reflexivity axiom
		te1 = (TermEquality) parseToSentence("x1 = x1");
		te2 = (TermEquality) parseToSentence("Plus(A,B) = Plus(A,z1)");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("{x1=Plus(A,B), z1=B}", result.toString());

		// Test reverse of previous unification with reflexivity axiom
		// Should still be the same.
		te1 = (TermEquality) parseToSentence("x1 = x1");
		te2 = (TermEquality) parseToSentence("Plus(A,z1) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("{x1=Plus(A,B), z1=B}", result.toString());

		// Test with nested terms
		te1 = (TermEquality) parseToSentence("Plus(Plus(Plus(A,B),B, A)) = Plus(Plus(Plus(A,B),B, A))");
		te2 = (TermEquality) parseToSentence("Plus(Plus(Plus(A,B),B, A)) = Plus(Plus(Plus(A,B),B, A))");

		result = unifier.unify(te1, te2);

		Assert.assertNotNull(result);

		Assert.assertEquals(0, result.size());

		// Simple term equality unification fails
		te1 = (TermEquality) parseToSentence("Plus(A,B) = Plus(B,A)");
		te2 = (TermEquality) parseToSentence("Plus(A,B) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		Assert.assertNull(result);
	}

	@Test
	public void testNOTSentence() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		//FOLParser parser = new FOLParser(domain);

		Sentence s1 = parseToSentence("NOT(P(A))");
		Sentence s2 = parseToSentence("NOT(P(A))");

		Map<Variable, Term> result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parseToSentence("NOT(P(A))");
		s2 = parseToSentence("NOT(P(B))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("NOT(P(A))");
		s2 = parseToSentence("NOT(P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new Constant("A"), result.get(new Variable("x")));
	}

	@Test
	public void testConnectedSentence() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		//FOLParser parser = new FOLParser(domain);

		Sentence s1 = parseToSentence("(P(A) & P(B))");
		Sentence s2 = parseToSentence("(P(A) & P(B))");

		Map<Variable, Term> result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parseToSentence("(P(A) & P(B))");
		s2 = parseToSentence("(P(A) & P(C))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("(P(A) & P(B))");
		s2 = parseToSentence("(P(A) & P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new Constant("B"), result.get(new Variable("x")));

		s1 = parseToSentence("(P(A) | P(B))");
		s2 = parseToSentence("(P(A) | P(B))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parseToSentence("(P(A) | P(B))");
		s2 = parseToSentence("(P(A) | P(C))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("(P(A) | P(B))");
		s2 = parseToSentence("(P(A) | P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new Constant("B"), result.get(new Variable("x")));

		s1 = parseToSentence("(P(A) => P(B))");
		s2 = parseToSentence("(P(A) => P(B))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parseToSentence("(P(A) => P(B))");
		s2 = parseToSentence("(P(A) => P(C))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("(P(A) => P(B))");
		s2 = parseToSentence("(P(A) => P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new Constant("B"), result.get(new Variable("x")));

		s1 = parseToSentence("(P(A) <=> P(B))");
		s2 = parseToSentence("(P(A) <=> P(B))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parseToSentence("(P(A) <=> P(B))");
		s2 = parseToSentence("(P(A) <=> P(C))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("(P(A) <=> P(B))");
		s2 = parseToSentence("(P(A) <=> P(x))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new Constant("B"), result.get(new Variable("x")));

		s1 = parseToSentence("((P(A) & P(B)) | (P(C) => (P(A) <=> P(C))))");
		s2 = parseToSentence("((P(A) & P(B)) | (P(C) => (P(A) <=> P(C))))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parseToSentence("((P(A) & P(B)) | (P(C) => (P(A) <=> P(C))))");
		s2 = parseToSentence("((P(A) & P(B)) | (P(C) => (P(A) <=> P(A))))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("((P(A) & P(B)) | (P(C) => (P(A) <=> P(C))))");
		s2 = parseToSentence("((P(A) & P(B)) | (P(C) => (P(A) <=> P(x))))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new Constant("C"), result.get(new Variable("x")));
	}

	@Test
	public void testQuantifiedSentence() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		//FOLParser parser = new FOLParser(domain);

		Sentence s1 = parseToSentence("FORALL x,y ((P(x) & P(A)) | (P(A) => P(y)))");
		Sentence s2 = parseToSentence("FORALL x,y ((P(x) & P(A)) | (P(A) => P(y)))");

		Map<Variable, Term> result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parseToSentence("FORALL x,y ((P(x) & P(A)) | (P(A) => P(y)))");
		s2 = parseToSentence("FORALL x   ((P(x) & P(A)) | (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("FORALL x,y ((P(x) & P(A)) | (P(A) => P(y)))");
		s2 = parseToSentence("FORALL x,y ((P(x) & P(A)) | (P(B) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("FORALL x,y ((P(x) & P(A)) | (P(A) => P(y)))");
		s2 = parseToSentence("FORALL x,y ((P(A) & P(A)) | (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new Constant("A"), result.get(new Variable("x")));

		//
		s1 = parseToSentence("EXISTS x,y ((P(x) & P(A)) | (P(A) => P(y)))");
		s2 = parseToSentence("EXISTS x,y ((P(x) & P(A)) | (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

		s1 = parseToSentence("EXISTS x,y ((P(x) & P(A)) | (P(A) => P(y)))");
		s2 = parseToSentence("EXISTS x   ((P(x) & P(A)) | (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("EXISTS x,y ((P(x) & P(A)) | (P(A) => P(y)))");
		s2 = parseToSentence("EXISTS x,y ((P(x) & P(A)) | (P(B) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNull(result);

		s1 = parseToSentence("EXISTS x,y ((P(x) & P(A)) | (P(A) => P(y)))");
		s2 = parseToSentence("EXISTS x,y ((P(A) & P(A)) | (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(new Constant("A"), result.get(new Variable("x")));
	}
	
	private Sentence parseToSentence(String stringToBeParsed) {
		FirstOrderLogicLexer lexer = new FirstOrderLogicLexer(new ANTLRInputStream(stringToBeParsed));
		TokenStream tokens = new CommonTokenStream(lexer);
		FirstOrderLogicParser parser = new FirstOrderLogicParser(tokens);

		ParseTree tree = parser.parse();
		Sentence node = (Sentence) new FirstOrderVisitor().visit(tree, parser);
		return node;
	}
}
