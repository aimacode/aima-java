package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import aima.logic.fol.domain.FOLDomain;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ClauseTest extends TestCase {

	public void testImmutable() {
		Clause c = new Clause();

		assertFalse(c.isImmutable());

		c.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));

		c.setImmutable();

		assertTrue(c.isImmutable());

		try {
			c.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));

			fail("Should have thrown an IllegalStateException");
		} catch (IllegalStateException ise) {
			// Ok, Expected
		}

		try {
			c.addPositiveLiteral(new Predicate("Pred3", new ArrayList<Term>()));

			fail("Should have thrown an IllegalStateException");
		} catch (IllegalStateException ise) {
			// Ok, Expected
		}
	}

	public void testIsEmpty() {
		Clause c1 = new Clause();
		assertTrue(c1.isEmpty());

		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertFalse(c1.isEmpty());

		Clause c2 = new Clause();
		assertTrue(c2.isEmpty());

		c2.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertFalse(c2.isEmpty());

		Clause c3 = new Clause();
		assertTrue(c3.isEmpty());

		c3.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c3.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		// Should be empty as they resolved with each other
		assertFalse(c3.isEmpty());
		
		c3.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c3.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertFalse(c3.isEmpty());
	}

	public void testIsHornClause() {
		Clause c1 = new Clause();
		assertFalse(c1.isHornClause());
		
		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertTrue(c1.isHornClause());
		
		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertTrue(c1.isHornClause());

		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		assertTrue(c1.isHornClause());
		c1.addNegativeLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		assertTrue(c1.isHornClause());

		c1.addPositiveLiteral(new Predicate("Pred5", new ArrayList<Term>()));
		assertFalse(c1.isHornClause());
	}
	
	public void testIsDefiniteClause() {
		Clause c1 = new Clause();
		assertFalse(c1.isDefiniteClause());
		
		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertFalse(c1.isDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertTrue(c1.isDefiniteClause());

		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		assertTrue(c1.isDefiniteClause());
		c1.addNegativeLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		assertTrue(c1.isDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred5", new ArrayList<Term>()));
		assertFalse(c1.isDefiniteClause());
	}
	
	public void testIsAtomicClause() {
		Clause c1 = new Clause();
		assertFalse(c1.isAtomicClause());
		
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertTrue(c1.isAtomicClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertFalse(c1.isAtomicClause());


		c1 = new Clause();
		assertFalse(c1.isAtomicClause());
		
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertTrue(c1.isAtomicClause());

		c1.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertFalse(c1.isAtomicClause());
		
		c1 = new Clause();
		assertFalse(c1.isAtomicClause());
		
		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertFalse(c1.isAtomicClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertFalse(c1.isAtomicClause());
	}
	
	public void testIsImplicationDefiniteClause() {
		Clause c1 = new Clause();
		assertFalse(c1.isImplicationDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertFalse(c1.isImplicationDefiniteClause());

		c1.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertTrue(c1.isImplicationDefiniteClause());
		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		assertTrue(c1.isImplicationDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		assertFalse(c1.isImplicationDefiniteClause());
	}
	
	public void testBinaryResolvents() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("Pred1");
		domain.addPredicate("Pred2");
		domain.addPredicate("Pred3");
		domain.addPredicate("Pred4");

		FOLKnowledgeBase kb = new FOLKnowledgeBase(domain);
		
		Clause c1 = new Clause();
		
		// Ensure that resolving to self when empty returns an empty clause
		assertNotNull(c1.binaryResolvents(kb, c1));
		assertEquals(1, c1.binaryResolvents(kb, c1).size());
		assertTrue(c1.binaryResolvents(kb, c1).iterator().next().isEmpty());
		
		// Check if resolve with self to an empty clause
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(kb, c1));
		assertEquals(1, c1.binaryResolvents(kb, c1).size());
		assertTrue(c1.binaryResolvents(kb, c1).iterator().next().isEmpty());
		
		// Check if try to resolve with self and no resolvents
		c1 = new Clause();
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertEquals(0, c1.binaryResolvents(kb, c1).size());
		
		c1 = new Clause();
		Clause c2 = new Clause();
		// Ensure that two empty clauses resolve to an empty clause
		assertNotNull(c1.binaryResolvents(kb, c2));
		assertEquals(1, c1.binaryResolvents(kb, c2).size());
		assertTrue(c1.binaryResolvents(kb, c2).iterator().next().isEmpty());
		assertNotNull(c2.binaryResolvents(kb, c1));
		assertEquals(1, c2.binaryResolvents(kb, c1).size());
		assertTrue(c2.binaryResolvents(kb, c1).iterator().next().isEmpty());
		
		// Enusre the two complementary clauses resolve
		// to the empty clause
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(kb, c2));
		assertEquals(1, c1.binaryResolvents(kb, c2).size());
		assertTrue(c1.binaryResolvents(kb, c2).iterator().next().isEmpty());
		assertNotNull(c2.binaryResolvents(kb, c1));
		assertEquals(1, c2.binaryResolvents(kb, c1).size());
		assertTrue(c2.binaryResolvents(kb, c1).iterator().next().isEmpty());	
		
		// Ensure that two clauses that have two complementaries
		// resolve with two resolvents
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(kb, c2));
		assertEquals(2, c1.binaryResolvents(kb, c2).size());
		assertNotNull(c2.binaryResolvents(kb, c1));
		assertEquals(2, c2.binaryResolvents(kb, c1).size());
		
		// Ensure two clauses that factor are not
		// considered resolved
		c1 = new Clause();
		c2 = new Clause();
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		c2.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(kb, c2));
		assertEquals(0, c1.binaryResolvents(kb, c2).size());
		assertNotNull(c2.binaryResolvents(kb, c1));
		assertEquals(0, c2.binaryResolvents(kb, c1).size());	
		
		// Ensure the resolvent is a subset of the originals
		c1 = new Clause();
		c2 = new Clause();
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		c2.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(kb, c2));
		assertNotNull(c2.binaryResolvents(kb, c1));
		assertEquals(1, c1.binaryResolvents(kb, c2).iterator().next()
				.getNumberPositiveLiterals());
		assertEquals(1, c1.binaryResolvents(kb, c2).iterator().next()
				.getNumberNegativeLiterals());
		assertEquals(1, c2.binaryResolvents(kb, c1).iterator().next()
				.getNumberPositiveLiterals());
		assertEquals(1, c2.binaryResolvents(kb, c1).iterator().next()
				.getNumberNegativeLiterals());
	}
	
	public void testBinaryResolventsOrderDoesNotMatter() {
		// This is a regression test, to ensure
		// the ordering of resolvents does not matter.
		// If the order ends up mattering, then likely
		// a problem was introduced in the Clause class
		// unifier, or related class.

		// Set up the initial set of clauses based on the
		// loves animal domain as it contains functions
		// new clauses will always be created (i.e. is an
		// infinite universe of discourse).
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory
				.lovesAnimalDomain());

		kb
				.tell("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");
		kb
				.tell("FORALL x (EXISTS y (Animal(y) AND Kills(x, y)) => FORALL z NOT(Loves(z, x)))");
		kb.tell("FORALL x (Animal(x) => Loves(Jack, x))");
		kb.tell("(Kills(Jack, Tuna) OR Kills(Curiosity, Tuna))");
		kb.tell("Cat(Tuna)");
		kb.tell("FORALL x (Cat(x) => Animal(x))");
		
		Set<Clause> clauses = new LinkedHashSet<Clause>();
		clauses.addAll(kb.getAllClauses());

		Set<Clause> newClauses = new LinkedHashSet<Clause>();
		long maxRunTime = 30 * 1000; // 30 seconds
		long finishTime = System.currentTimeMillis() + maxRunTime;
		do {
			clauses.addAll(newClauses);
			newClauses.clear();
			Clause[] clausesA = new Clause[clauses.size()];
			clauses.toArray(clausesA);
			for (int i = 0; i < clausesA.length; i++) {
				Clause cI = clausesA[i];
				for (int j = 0; j < clausesA.length; j++) {
					Clause cJ = clausesA[j];

					newClauses.addAll(cI.getFactors(kb));
					newClauses.addAll(cJ.getFactors(kb));

					Set<Clause> cIresolvents = cI.binaryResolvents(kb, cJ);
					Set<Clause> cJresolvents = cJ.binaryResolvents(kb, cI);
					if (!cIresolvents.equals(cJresolvents)) {
						fail("Ordering of binary resolvents has become important, which should not be the case");
					}
					
					newClauses.addAll(cIresolvents);
					
					if (System.currentTimeMillis() > finishTime) {
						break;
					}
				}
				if (System.currentTimeMillis() > finishTime) {
					break;
				}
			}
		} while (System.currentTimeMillis() < finishTime);
	}
	
	public void testHashCode() {
		Term cons1 = new Constant("C1");
		Term cons2 = new Constant("C2");
		Term var1 = new Variable("v1");
		List<Term> pts1 = new ArrayList<Term>();
		List<Term> pts2 = new ArrayList<Term>();
		pts1.add(cons1);
		pts1.add(cons2);
		pts1.add(var1);
		pts2.add(cons2);
		pts2.add(cons1);
		pts2.add(var1);
		
		Clause c1 = new Clause();
		Clause c2 = new Clause();
		assertEquals(c1.hashCode(), c2.hashCode());
		
		c1.addNegativeLiteral(new Predicate("Pred1", pts1));
		assertNotSame(c1.hashCode(), c2.hashCode());
		c2.addNegativeLiteral(new Predicate("Pred1", pts1));
		assertEquals(c1.hashCode(), c2.hashCode());
		
		c1.addPositiveLiteral(new Predicate("Pred1", pts1));
		assertNotSame(c1.hashCode(), c2.hashCode());
		c2.addPositiveLiteral(new Predicate("Pred1", pts1));
		assertEquals(c1.hashCode(), c2.hashCode());
	}
	
	public void testEquals() {
		Term cons1 = new Constant("C1");
		Term cons2 = new Constant("C2");
		Term var1 = new Variable("v1");
		List<Term> pts1 = new ArrayList<Term>();
		List<Term> pts2 = new ArrayList<Term>();
		pts1.add(cons1);
		pts1.add(cons2);
		pts1.add(var1);
		pts2.add(cons2);
		pts2.add(cons1);
		pts2.add(var1);
		
		Clause c1 = new Clause();
		Clause c2 = new Clause();
		assertTrue(c1.equals(c1));
		assertTrue(c2.equals(c2));
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		
		// Check negatives
		c1.addNegativeLiteral(new Predicate("Pred1", pts1));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new Predicate("Pred1", pts1));		
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		
		c1.addNegativeLiteral(new Predicate("Pred2", pts2));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new Predicate("Pred2", pts2));		
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		// Check same but added in different order
		c1.addNegativeLiteral(new Predicate("Pred3", pts1));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.addNegativeLiteral(new Predicate("Pred4", pts1));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new Predicate("Pred4", pts1));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new Predicate("Pred3", pts1));
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		
		// Check positives
		c1.addPositiveLiteral(new Predicate("Pred1", pts1));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new Predicate("Pred1", pts1));		
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		
		c1.addPositiveLiteral(new Predicate("Pred2", pts2));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new Predicate("Pred2", pts2));		
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		// Check same but added in different order
		c1.addPositiveLiteral(new Predicate("Pred3", pts1));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.addPositiveLiteral(new Predicate("Pred4", pts1));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new Predicate("Pred4", pts1));
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new Predicate("Pred3", pts1));
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
	}
	
	public void testNonTrivialFactors() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addPredicate("P");
		domain.addPredicate("Q");

		FOLParser parser = new FOLParser(domain);
		
		FOLKnowledgeBase kb = new FOLKnowledgeBase(domain);

		// p(x,y), q(a,b), ¬p(b,a), q(y,x)
		List<Predicate> posLits = new ArrayList<Predicate>();
		List<Predicate> negLits = new ArrayList<Predicate>();
		posLits.add((Predicate) parser.parse("P(x,y)"));
		posLits.add((Predicate) parser.parse("Q(A,B)"));
		negLits.add((Predicate) parser.parse("P(B,A)"));
		posLits.add((Predicate) parser.parse("Q(y,x)"));

		Clause c = new Clause(posLits, negLits);
		assertEquals("[{~P(B,A),P(B,A),Q(A,B)}]", c.getNonTrivialFactors(kb)
				.toString());
		
		// p(x,y), q(a,b), ¬p(b,a), ¬q(y,x)
		posLits.clear();
		negLits.clear();
		posLits.add((Predicate) parser.parse("P(x,y)"));
		posLits.add((Predicate) parser.parse("Q(A,B)"));
		negLits.add((Predicate) parser.parse("P(B,A)"));
		negLits.add((Predicate) parser.parse("Q(y,x)"));

		c = new Clause(posLits, negLits);
		assertEquals("[]", c.getNonTrivialFactors(kb).toString());
		
		// p(x,f(y)), p(g(u),x), p(f(y),u)
		posLits.clear();
		negLits.clear();
		posLits.add((Predicate) parser.parse("P(x,F(y))"));
		posLits.add((Predicate) parser.parse("P(G(u),x)"));
		posLits.add((Predicate) parser.parse("P(F(y),u)"));

		c = new Clause(posLits, negLits);
		assertEquals("[{P(G(F(v0)),F(v0)),P(F(v0),F(v0))}]", c
				.getNonTrivialFactors(kb)
				.toString());
		
		// p(g(x)), q(x), p(f(a)), p(x), p(g(f(x))), q(f(a))
		posLits.clear();
		negLits.clear();
		posLits.add((Predicate) parser.parse("P(G(x))"));
		posLits.add((Predicate) parser.parse("Q(x)"));
		posLits.add((Predicate) parser.parse("P(F(A))"));
		posLits.add((Predicate) parser.parse("P(x)"));
		posLits.add((Predicate) parser.parse("P(G(F(x)))"));
		posLits.add((Predicate) parser.parse("Q(F(A))"));

		c = new Clause(posLits, negLits);
		assertEquals("[{P(G(F(A))),P(F(A)),P(G(F(F(A)))),Q(F(A))}]", c
				.getNonTrivialFactors(kb).toString());
	}
}
