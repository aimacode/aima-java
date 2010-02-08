package aima.test.core.unit.logic.fol.inference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.Paramodulation;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.AtomicSentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ParamodulationTest {

	private Paramodulation paramodulation = null;

	@Before
	public void setUp() {
		paramodulation = new Paramodulation();
	}

	// Note: Based on:
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// Slide 31.
	@Test
	public void testSimpleExample() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addFunction("F");

		FOLParser parser = new FOLParser(domain);

		List<Literal> lits = new ArrayList<Literal>();
		AtomicSentence a1 = (AtomicSentence) parser.parse("P(F(x,B),x)");
		AtomicSentence a2 = (AtomicSentence) parser.parse("Q(x)");
		lits.add(new Literal(a1));
		lits.add(new Literal(a2));

		Clause c1 = new Clause(lits);

		lits.clear();
		a1 = (AtomicSentence) parser.parse("F(A,y) = y");
		a2 = (AtomicSentence) parser.parse("R(y)");
		lits.add(new Literal(a1));
		lits.add(new Literal(a2));

		Clause c2 = new Clause(lits);

		Set<Clause> paras = paramodulation.apply(c1, c2);
		Assert.assertEquals(2, paras.size());

		Iterator<Clause> it = paras.iterator();
		Assert.assertEquals("[P(B,A), Q(A), R(B)]", it.next().toString());
		Assert.assertEquals("[P(F(A,F(x,B)),x), Q(x), R(F(x,B))]", it.next()
				.toString());
	}

	@Test
	public void testMultipleTermEqualitiesInBothClausesExample() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addFunction("F");

		FOLParser parser = new FOLParser(domain);

		List<Literal> lits = new ArrayList<Literal>();
		AtomicSentence a1 = (AtomicSentence) parser.parse("F(C,x) = D");
		AtomicSentence a2 = (AtomicSentence) parser.parse("A = D");
		AtomicSentence a3 = (AtomicSentence) parser.parse("P(F(x,B),x)");
		AtomicSentence a4 = (AtomicSentence) parser.parse("Q(x)");
		AtomicSentence a5 = (AtomicSentence) parser.parse("R(C)");
		lits.add(new Literal(a1));
		lits.add(new Literal(a2));
		lits.add(new Literal(a3));
		lits.add(new Literal(a4));
		lits.add(new Literal(a5));

		Clause c1 = new Clause(lits);

		lits.clear();
		a1 = (AtomicSentence) parser.parse("F(A,y) = y");
		a2 = (AtomicSentence) parser.parse("F(B,y) = C");
		a3 = (AtomicSentence) parser.parse("R(y)");
		a4 = (AtomicSentence) parser.parse("R(A)");
		lits.add(new Literal(a1));
		lits.add(new Literal(a2));
		lits.add(new Literal(a3));
		lits.add(new Literal(a4));

		Clause c2 = new Clause(lits);

		Set<Clause> paras = paramodulation.apply(c1, c2);
		Assert.assertEquals(5, paras.size());

		Iterator<Clause> it = paras.iterator();
		Assert
				.assertEquals(
						"[F(B,B) = C, F(C,A) = D, A = D, P(B,A), Q(A), R(A), R(B), R(C)]",
						it.next().toString());
		Assert
				.assertEquals(
						"[F(A,F(C,x)) = D, F(B,F(C,x)) = C, A = D, P(F(x,B),x), Q(x), R(F(C,x)), R(A), R(C)]",
						it.next().toString());
		Assert
				.assertEquals(
						"[F(A,B) = B, F(C,B) = D, A = D, P(C,B), Q(B), R(A), R(B), R(C)]",
						it.next().toString());
		Assert
				.assertEquals(
						"[F(F(B,y),x) = D, F(A,y) = y, A = D, P(F(x,B),x), Q(x), R(y), R(A), R(C)]",
						it.next().toString());
		Assert
				.assertEquals(
						"[F(B,y) = C, F(C,x) = D, F(D,y) = y, P(F(x,B),x), Q(x), R(y), R(A), R(C)]",
						it.next().toString());
	}

	@Test
	public void testBypassReflexivityAxiom() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addPredicate("P");
		domain.addFunction("F");

		FOLParser parser = new FOLParser(domain);

		List<Literal> lits = new ArrayList<Literal>();
		AtomicSentence a1 = (AtomicSentence) parser.parse("P(y, F(A,y))");
		lits.add(new Literal(a1));

		Clause c1 = new Clause(lits);

		lits.clear();
		a1 = (AtomicSentence) parser.parse("x = x");
		lits.add(new Literal(a1));

		Clause c2 = new Clause(lits);

		Set<Clause> paras = paramodulation.apply(c1, c2);
		Assert.assertEquals(0, paras.size());
	}

	@Test
	public void testNegativeTermEquality() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addPredicate("P");
		domain.addFunction("F");

		FOLParser parser = new FOLParser(domain);

		List<Literal> lits = new ArrayList<Literal>();
		AtomicSentence a1 = (AtomicSentence) parser.parse("P(y, F(A,y))");
		lits.add(new Literal(a1));

		Clause c1 = new Clause(lits);

		lits.clear();
		a1 = (AtomicSentence) parser.parse("F(x,B) = x");
		lits.add(new Literal(a1, true));

		Clause c2 = new Clause(lits);

		Set<Clause> paras = paramodulation.apply(c1, c2);
		Assert.assertEquals(0, paras.size());
	}
}
