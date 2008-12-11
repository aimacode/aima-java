package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import aima.logic.fol.Unifier;
import aima.logic.fol.domain.DomainFactory;
import aima.logic.fol.domain.FOLDomain;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class UnifierTest extends TestCase {

	private FOLParser parser;
	private Unifier unifier;
	private Map<Variable, Term> theta;

	@Override
	public void setUp() {
		parser = new FOLParser(DomainFactory.knowsDomain());
		unifier = new Unifier();
		theta = new Hashtable<Variable, Term>();
	}

	public void testFailureIfThetaisNull() {
		Variable var = new Variable("x");
		Sentence sentence = parser.parse("Knows(x)");
		theta = null;
		Map<Variable, Term> result = unifier.unify(var, sentence, theta);
		assertNull(result);
	}

	public void testUnificationFailure() {
		Variable var = new Variable("x");
		Sentence sentence = parser.parse("Knows(y)");
		theta = null;
		Map<Variable, Term> result = unifier.unify(var, sentence, theta);
		assertNull(result);
	}

	public void testThetaPassedBackIfXEqualsYBothVariables() {
		Variable var1 = new Variable("x");
		Variable var2 = new Variable("x");

		theta.put(new Variable("dummy"), new Variable("dummy"));
		Map<Variable, Term> result = unifier.unify(var1, var2, theta);
		assertEquals(theta, result);
		assertEquals(1, theta.keySet().size());
		assertTrue(theta.containsKey(new Variable("dummy")));
	}

	public void testVariableEqualsConstant() {
		Variable var1 = new Variable("x");
		Constant constant = new Constant("John");

		Map<Variable, Term> result = unifier.unify(var1, constant, theta);
		assertEquals(theta, result);
		assertEquals(1, theta.keySet().size());
		assertTrue(theta.keySet().contains(var1));
		assertEquals(constant, theta.get(var1));
	}

	public void testSimpleVariableUnification() {
		Variable var1 = new Variable("x");
		List<Term> terms1 = new ArrayList<Term>();
		terms1.add(var1);
		Predicate p1 = new Predicate("King", terms1); // King(x)

		List<Term> terms2 = new ArrayList<Term>();
		terms2.add(new Constant("John"));
		Predicate p2 = new Predicate("King", terms2); // King(John)

		Map<Variable, Term> result = unifier.unify(p1, p2, theta);
		assertEquals(theta, result);
		assertEquals(1, theta.keySet().size());
		assertTrue(theta.keySet().contains(new Variable("x"))); // x =
		assertEquals(new Constant("John"), theta.get(var1)); // John
	}

	public void testKnows1() {
		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(John,Jane)");
		Map<Variable, Term> result = unifier.unify(query, johnKnowsJane, theta);
		assertEquals(theta, result);
		assertTrue(theta.keySet().contains(new Variable("x"))); // x =
		assertEquals(new Constant("Jane"), theta.get(new Variable("x"))); // Jane

	}

	public void testKnows2() {
		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(y,Bill)");
		Map<Variable, Term> result = unifier.unify(query, johnKnowsJane, theta);

		assertEquals(2, result.size());

		assertEquals(new Constant("Bill"), theta.get(new Variable("x"))); // x =
		// Bill
		assertEquals(new Constant("John"), theta.get(new Variable("y"))); // y =
		// John
	}

	public void testKnows3() {
		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(y,Mother(y))");
		Map<Variable, Term> result = unifier.unify(query, johnKnowsJane, theta);

		assertEquals(2, result.size());

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("John"));
		Function mother = new Function("Mother", terms);
		assertEquals(mother, theta.get(new Variable("x")));
		assertEquals(new Constant("John"), theta.get(new Variable("y")));
	}

	public void testKnows5() {
		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(y,z)");
		Map<Variable, Term> result = unifier.unify(query, johnKnowsJane, theta);

		assertEquals(2, result.size());

		assertEquals(new Variable("z"), theta.get(new Variable("x"))); // x = z
		assertEquals(new Constant("John"), theta.get(new Variable("y"))); // y =
		// John
	}

	public void testCascadedOccursCheck() {
		parser = new FOLParser(DomainFactory.lovesAnimalDomain());
		parser.getFOLDomain().addFunction("SF0");
		parser.getFOLDomain().addFunction("SF1");

		Sentence s1 = parser.parse("Loves(SF1(v2),v2)");
		Sentence s2 = parser.parse("Loves(v3,SF0(v3))");
		Map<Variable, Term> result = unifier.unify(s1, s2);

		assertNull(result);
	}

	public void testAdditionalVariableMixtures() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addPredicate("P");

		FOLParser parser = new FOLParser(domain);

		// Test Cascade Substitutions handled correctly
		Sentence s1 = parser.parse("P(z, x)");
		Sentence s2 = parser.parse("P(x, a)");
		Map<Variable, Term> result = unifier.unify(s1, s2);

		assertEquals("{z=a, x=a}", result.toString());

		s1 = parser.parse("P(x, z)");
		s2 = parser.parse("P(a, x)");
		result = unifier.unify(s1, s2);

		assertEquals("{x=a, z=a}", result.toString());

		s1 = parser.parse("P(w, w, w)");
		s2 = parser.parse("P(x, y, z)");
		result = unifier.unify(s1, s2);

		assertEquals("{w=z, x=z, y=z}", result.toString());

		s1 = parser.parse("P(x, y, z)");
		s2 = parser.parse("P(w, w, w)");
		result = unifier.unify(s1, s2);

		assertEquals("{x=w, y=w, z=w}", result.toString());

		s1 = parser.parse("P(x, B, F(y))");
		s2 = parser.parse("P(A, y, F(z))");
		result = unifier.unify(s1, s2);

		assertEquals("{x=A, y=B, z=B}", result.toString());

		s1 = parser.parse("P(F(x,B), G(y),         F(z,A))");
		s2 = parser.parse("P(y,      G(F(G(w),w)), F(w,z))");
		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("P(F(G(A)), x,    F(H(z,z)), H(y,    G(w)))");
		s2 = parser.parse("P(y,       G(z), F(v     ), H(F(w), x   ))");
		result = unifier.unify(s1, s2);

		assertEquals("{y=F(G(A)), x=G(G(A)), v=H(G(A),G(A)), w=G(A), z=G(A)}",
				result.toString());
	}

	public void testTermEquality() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addFunction("Plus");

		FOLParser parser = new FOLParser(domain);

		TermEquality te1 = (TermEquality) parser.parse("x = x");
		TermEquality te2 = (TermEquality) parser.parse("x = x");

		// Both term equalities the same,
		// should unify but no substitutions.
		Map<Variable, Term> result = unifier.unify(te1, te2);

		assertNotNull(result);
		assertEquals(0, result.size());

		// Different variable names but should unify.
		te1 = (TermEquality) parser.parse("x1 = x1");
		te2 = (TermEquality) parser.parse("x2 = x2");

		result = unifier.unify(te1, te2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("{x1=x2}", result.toString());

		// Test simple unification with reflexivity axiom
		te1 = (TermEquality) parser.parse("x1 = x1");
		te2 = (TermEquality) parser.parse("Plus(A,B) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		assertNotNull(result);

		assertEquals(1, result.size());
		assertEquals("{x1=Plus(A,B)}", result.toString());

		// Test more complex unification with reflexivity axiom
		te1 = (TermEquality) parser.parse("x1 = x1");
		te2 = (TermEquality) parser.parse("Plus(A,B) = Plus(A,z1)");

		result = unifier.unify(te1, te2);

		assertNotNull(result);

		assertEquals(2, result.size());
		assertEquals("{x1=Plus(A,B), z1=B}", result.toString());

		// Test reverse of previous unification with reflexivity axiom
		// Should still be the same.
		te1 = (TermEquality) parser.parse("x1 = x1");
		te2 = (TermEquality) parser.parse("Plus(A,z1) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		assertNotNull(result);

		assertEquals(2, result.size());
		assertEquals("{x1=Plus(A,B), z1=B}", result.toString());

		// Test with nested terms
		te1 = (TermEquality) parser
				.parse("Plus(Plus(Plus(A,B),B, A)) = Plus(Plus(Plus(A,B),B, A))");
		te2 = (TermEquality) parser
				.parse("Plus(Plus(Plus(A,B),B, A)) = Plus(Plus(Plus(A,B),B, A))");

		result = unifier.unify(te1, te2);

		assertNotNull(result);

		assertEquals(0, result.size());

		// Simple term equality unification fails
		te1 = (TermEquality) parser.parse("Plus(A,B) = Plus(B,A)");
		te2 = (TermEquality) parser.parse("Plus(A,B) = Plus(A,B)");

		result = unifier.unify(te1, te2);

		assertNull(result);
	}

	public void testNOTSentence() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		FOLParser parser = new FOLParser(domain);

		Sentence s1 = parser.parse("NOT(P(A))");
		Sentence s2 = parser.parse("NOT(P(A))");

		Map<Variable, Term> result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(0, result.size());

		s1 = parser.parse("NOT(P(A))");
		s2 = parser.parse("NOT(P(B))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("NOT(P(A))");
		s2 = parser.parse("NOT(P(x))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new Constant("A"), result.get(new Variable("x")));
	}

	public void testConnectedSentence() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		FOLParser parser = new FOLParser(domain);

		Sentence s1 = parser.parse("(P(A) AND P(B))");
		Sentence s2 = parser.parse("(P(A) AND P(B))");

		Map<Variable, Term> result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(0, result.size());

		s1 = parser.parse("(P(A) AND P(B))");
		s2 = parser.parse("(P(A) AND P(C))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("(P(A) AND P(B))");
		s2 = parser.parse("(P(A) AND P(x))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new Constant("B"), result.get(new Variable("x")));

		s1 = parser.parse("(P(A) OR P(B))");
		s2 = parser.parse("(P(A) OR P(B))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(0, result.size());

		s1 = parser.parse("(P(A) OR P(B))");
		s2 = parser.parse("(P(A) OR P(C))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("(P(A) OR P(B))");
		s2 = parser.parse("(P(A) OR P(x))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new Constant("B"), result.get(new Variable("x")));

		s1 = parser.parse("(P(A) => P(B))");
		s2 = parser.parse("(P(A) => P(B))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(0, result.size());

		s1 = parser.parse("(P(A) => P(B))");
		s2 = parser.parse("(P(A) => P(C))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("(P(A) => P(B))");
		s2 = parser.parse("(P(A) => P(x))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new Constant("B"), result.get(new Variable("x")));

		s1 = parser.parse("(P(A) <=> P(B))");
		s2 = parser.parse("(P(A) <=> P(B))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(0, result.size());

		s1 = parser.parse("(P(A) <=> P(B))");
		s2 = parser.parse("(P(A) <=> P(C))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("(P(A) <=> P(B))");
		s2 = parser.parse("(P(A) <=> P(x))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new Constant("B"), result.get(new Variable("x")));

		s1 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(C))))");
		s2 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(C))))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(0, result.size());

		s1 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(C))))");
		s2 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(A))))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(C))))");
		s2 = parser.parse("((P(A) AND P(B)) OR (P(C) => (P(A) <=> P(x))))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new Constant("C"), result.get(new Variable("x")));
	}

	public void testQuantifiedSentence() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addFunction("Plus");
		domain.addPredicate("P");

		FOLParser parser = new FOLParser(domain);

		Sentence s1 = parser
				.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		Sentence s2 = parser
				.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");

		Map<Variable, Term> result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(0, result.size());

		s1 = parser.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("FORALL x   ((P(x) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("FORALL x,y ((P(x) AND P(A)) OR (P(B) => P(y)))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("FORALL x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("FORALL x,y ((P(A) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new Constant("A"), result.get(new Variable("x")));

		//
		s1 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(0, result.size());

		s1 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("EXISTS x   ((P(x) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(B) => P(y)))");

		result = unifier.unify(s1, s2);

		assertNull(result);

		s1 = parser.parse("EXISTS x,y ((P(x) AND P(A)) OR (P(A) => P(y)))");
		s2 = parser.parse("EXISTS x,y ((P(A) AND P(A)) OR (P(A) => P(y)))");

		result = unifier.unify(s1, s2);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(new Constant("A"), result.get(new Variable("x")));
	}
}
