package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.logic.fol.domain.FOLDomain;
import aima.logic.fol.inference.Demodulation;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.TermEquality;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class DemodulationTest extends TestCase {
	
	private Demodulation demodulation = null;

	public void setUp() {
		demodulation = new Demodulation();
	}
	
	// Note: Based on:
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// Slide 22.
	public void testSimpleAtomicExamples() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addPredicate("P");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addFunction("J");

		FOLParser parser = new FOLParser(domain);
		
		Predicate expression = (Predicate) parser
				.parse("P(A,F(B,G(A,H(B)),C),D)");
		TermEquality assertion = (TermEquality) parser.parse("B = E");
		
		Predicate altExpression = (Predicate) demodulation.apply(assertion,
				expression);
		
		assertFalse(expression.equals(altExpression));
		assertEquals("P(A,F(E,G(A,H(B)),C),D)", altExpression.toString());
		
		altExpression = (Predicate) demodulation
				.apply(assertion, altExpression);

		assertEquals("P(A,F(E,G(A,H(E)),C),D)", altExpression.toString());

		assertion = (TermEquality) parser.parse("G(x,y) = J(x)");

		altExpression = (Predicate) demodulation.apply(assertion, expression);

		assertEquals("P(A,F(B,J(A),C),D)", altExpression.toString());
	}
	
	// Note: Based on:
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// Slide 23.
	public void testSimpleAtomicNonExample() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addPredicate("P");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addFunction("J");

		FOLParser parser = new FOLParser(domain);

		Predicate expression = (Predicate) parser.parse("P(A,G(x,B),C)");
		TermEquality assertion = (TermEquality) parser.parse("G(A,y) = J(y)");

		Predicate altExpression = (Predicate) demodulation.apply(assertion,
				expression);

		assertNull(altExpression);
	}
	
	public void testSimpleClauseExamples() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("W");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addFunction("J");

		FOLParser parser = new FOLParser(domain);

		List<Literal> lits = new ArrayList<Literal>();
		Predicate p1 = (Predicate) parser.parse("Q(z, G(D,B))");
		Predicate p2 = (Predicate) parser.parse("P(x, G(A,C))");
		Predicate p3 = (Predicate) parser.parse("W(z,x,u,w,y)");
		lits.add(new Literal(p1));
		lits.add(new Literal(p2));
		lits.add(new Literal(p3));

		Clause clExpression = new Clause(lits);

		TermEquality assertion = (TermEquality) parser.parse("G(x,y) = x");

		Clause altClExpression = demodulation.apply(assertion, clExpression);
		
		assertEquals("[P(x,G(A,C)), Q(z,D), W(z,x,u,w,y)]", altClExpression
				.toString());

		altClExpression = demodulation.apply(assertion, altClExpression);

		assertEquals("[P(x,A), Q(z,D), W(z,x,u,w,y)]", altClExpression
				.toString());
	}

	public void testSimpleClauseNonExample() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addPredicate("P");
		domain.addFunction("F");

		FOLParser parser = new FOLParser(domain);

		List<Literal> lits = new ArrayList<Literal>();
		Predicate p1 = (Predicate) parser.parse("P(y, F(A,y))");
		lits.add(new Literal(p1));

		Clause clExpression = new Clause(lits);

		TermEquality assertion = (TermEquality) parser.parse("F(x,B) = C");

		Clause altClExpression = demodulation.apply(assertion, clExpression);

		assertNull(altClExpression);
	}
	
	public void testBypassReflexivityAxiom() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addPredicate("P");
		domain.addFunction("F");

		FOLParser parser = new FOLParser(domain);

		List<Literal> lits = new ArrayList<Literal>();
		Predicate p1 = (Predicate) parser.parse("P(y, F(A,y))");
		lits.add(new Literal(p1));

		Clause clExpression = new Clause(lits);

		TermEquality assertion = (TermEquality) parser.parse("x = x");

		Clause altClExpression = demodulation.apply(assertion, clExpression);

		assertNull(altClExpression);
	}
}
