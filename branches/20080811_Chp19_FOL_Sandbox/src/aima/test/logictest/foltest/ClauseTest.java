package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import aima.logic.fol.CNFConverter;
import aima.logic.fol.StandardizeApartIndexicalFactory;
import aima.logic.fol.domain.DomainFactory;
import aima.logic.fol.domain.FOLDomain;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.CNF;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.AtomicSentence;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ClauseTest extends TestCase {
	
	public void setUp() {
		StandardizeApartIndexicalFactory.flush();
	}

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
	
	public void testIsUnitClause() {
		Clause c1 = new Clause();
		assertFalse(c1.isUnitClause());
		
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertTrue(c1.isUnitClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertFalse(c1.isUnitClause());


		c1 = new Clause();
		assertFalse(c1.isUnitClause());
		
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertTrue(c1.isUnitClause());

		c1.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertFalse(c1.isUnitClause());
		
		c1 = new Clause();
		assertFalse(c1.isUnitClause());
		
		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertTrue(c1.isUnitClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertFalse(c1.isUnitClause());
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
		
		Clause c1 = new Clause();
		
		// Ensure that resolving to self when empty returns an empty clause
		assertNotNull(c1.binaryResolvents(c1));
		assertEquals(1, c1.binaryResolvents(c1).size());
		assertTrue(c1.binaryResolvents(c1).iterator().next().isEmpty());
		
		// Check if resolve with self to an empty clause
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(c1));
		assertEquals(1, c1.binaryResolvents(c1).size());
		// i.e. resolving a tautology with a tautology gives you
		// back a tautology.
		assertEquals("[~Pred1(), Pred1()]", c1.binaryResolvents(c1).iterator()
				.next().toString());
		
		// Check if try to resolve with self and no resolvents
		c1 = new Clause();
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertEquals(0, c1.binaryResolvents(c1).size());
		
		c1 = new Clause();
		Clause c2 = new Clause();
		// Ensure that two empty clauses resolve to an empty clause
		assertNotNull(c1.binaryResolvents(c2));
		assertEquals(1, c1.binaryResolvents(c2).size());
		assertTrue(c1.binaryResolvents(c2).iterator().next().isEmpty());
		assertNotNull(c2.binaryResolvents(c1));
		assertEquals(1, c2.binaryResolvents(c1).size());
		assertTrue(c2.binaryResolvents(c1).iterator().next().isEmpty());
		
		// Enusre the two complementary clauses resolve
		// to the empty clause
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(c2));
		assertEquals(1, c1.binaryResolvents(c2).size());
		assertTrue(c1.binaryResolvents(c2).iterator().next().isEmpty());
		assertNotNull(c2.binaryResolvents(c1));
		assertEquals(1, c2.binaryResolvents(c1).size());
		assertTrue(c2.binaryResolvents(c1).iterator().next().isEmpty());	
		
		// Ensure that two clauses that have two complementaries
		// resolve with two resolvents
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(c2));
		assertEquals(2, c1.binaryResolvents(c2).size());
		assertNotNull(c2.binaryResolvents(c1));
		assertEquals(2, c2.binaryResolvents(c1).size());
		
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
		assertNotNull(c1.binaryResolvents(c2));
		assertEquals(0, c1.binaryResolvents(c2).size());
		assertNotNull(c2.binaryResolvents(c1));
		assertEquals(0, c2.binaryResolvents(c1).size());	
		
		// Ensure the resolvent is a subset of the originals
		c1 = new Clause();
		c2 = new Clause();
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		c2.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		assertNotNull(c1.binaryResolvents(c2));
		assertNotNull(c2.binaryResolvents(c1));
		assertEquals(1, c1.binaryResolvents(c2).iterator().next()
				.getNumberPositiveLiterals());
		assertEquals(1, c1.binaryResolvents(c2).iterator().next()
				.getNumberNegativeLiterals());
		assertEquals(1, c2.binaryResolvents(c1).iterator().next()
				.getNumberPositiveLiterals());
	    assertEquals(1, c2.binaryResolvents(c1).iterator().next()
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

					newClauses.addAll(cI.getFactors());
					newClauses.addAll(cJ.getFactors());

					Set<Clause> cIresolvents = cI.binaryResolvents(cJ);
					Set<Clause> cJresolvents = cJ.binaryResolvents(cI);
					if (!cIresolvents.equals(cJresolvents)) {
						System.err.println("cI=" + cI);
						System.err.println("cJ=" + cJ);
						System.err.println("cIR=" + cIresolvents);
						System.err.println("cJR=" + cJresolvents);				
						fail("Ordering of binary resolvents has become important, which should not be the case");
					}
					
					for (Clause r : cIresolvents) {
						newClauses.addAll(r.getFactors());
					}
					
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
	
	public void testEqualityBinaryResolvents() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");

		FOLParser parser = new FOLParser(domain);

		// B = A
		Clause c1 = new Clause();
		c1.addPositiveLiteral((AtomicSentence) parser.parse("B = A"));

		Clause c2 = new Clause();
		c2.addNegativeLiteral((AtomicSentence) parser.parse("B = A"));
		c2.addPositiveLiteral((AtomicSentence) parser.parse("B = A"));

		Set<Clause> resolvents = c1.binaryResolvents(c2);

		assertEquals(1, resolvents.size());
		assertEquals("[[B = A]]", resolvents.toString());
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
	
	public void testSimpleEquals() {
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
	
	public void testComplexEquals() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");		
		domain.addConstant("D");
		domain.addPredicate("P");
		domain.addPredicate("Animal");
		domain.addPredicate("Kills");
		domain.addFunction("F");
		domain.addFunction("SF0");

		FOLParser parser = new FOLParser(domain);

		CNFConverter cnfConverter = new CNFConverter(parser);
		Sentence s1 = parser.parse("((x1 = y1 AND y1 = z1) => x1 = z1)");
		Sentence s2 = parser.parse("((x2 = y2 AND F(y2) = z2) => F(x2) = z2)");
		CNF cnf1 = cnfConverter.convertToCNF(s1);
		CNF cnf2 = cnfConverter.convertToCNF(s2);
		
		Clause c1 = cnf1.getConjunctionOfClauses().get(0);
		Clause c2 = cnf2.getConjunctionOfClauses().get(0);

		assertFalse(c1.equals(c2));
		
		s1 = parser.parse("((x1 = y1 AND y1 = z1) => x1 = z1)");
		s2 = parser.parse("((x2 = y2 AND y2 = z2) => x2 = z2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);
	
		assertTrue(c1.equals(c2));
		
		s1 = parser.parse("((x1 = y1 AND y1 = z1) => x1 = z1)");
		s2 = parser.parse("((y2 = z2 AND x2 = y2) => x2 = z2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);
		
		assertTrue(c1.equals(c2));
		
		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((x2 = y2 AND y2 = z2) AND z2 = r2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		assertTrue(c1.equals(c2));
		
		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((z2 = r2 AND y2 = z2) AND x2 = y2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		assertTrue(c1.equals(c2));
		
		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((x2 = y2 AND y2 = z2) AND z2 = y2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		assertFalse(c1.equals(c2));
		
		s1 = parser
				.parse("(((((x1 = y1 AND y1 = z1) AND z1 = r1) AND r1 = q1) AND q1 = s1) => x1 = r1)");
		s2 = parser
				.parse("(((((x2 = y2 AND y2 = z2) AND z2 = r2) AND r2 = q2) AND q2 = s2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		assertTrue(c1.equals(c2));
		
		s1 = parser
				.parse("((((NOT(Animal(c1920)) OR NOT(Animal(c1921))) OR NOT(Kills(c1922,c1920))) OR NOT(Kills(c1919,c1921))) OR NOT(Kills(SF0(c1922),SF0(c1919))))");
		s2 = parser
				.parse("((((NOT(Animal(c1929)) OR NOT(Animal(c1928))) OR NOT(Kills(c1927,c1929))) OR NOT(Kills(c1930,c1928))) OR NOT(Kills(SF0(c1930),SF0(c1927))))");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		assertTrue(c1.equals(c2));
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

		// p(x,y), q(a,b), ¬p(b,a), q(y,x)
		Clause c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(x,y)"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(A,B)"));
		c.addNegativeLiteral((Predicate) parser.parse("P(B,A)"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(y,x)"));

		assertEquals("[[~P(B,A), P(B,A), Q(A,B)]]", c.getNonTrivialFactors()
				.toString());
		
		// p(x,y), q(a,b), ¬p(b,a), ¬q(y,x)
		c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(x,y)"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(A,B)"));
		c.addNegativeLiteral((Predicate) parser.parse("P(B,A)"));
		c.addNegativeLiteral((Predicate) parser.parse("Q(y,x)"));

		assertEquals("[]", c.getNonTrivialFactors().toString());
		
		// p(x,f(y)), p(g(u),x), p(f(y),u)
		c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(x,F(y))"));
		c.addPositiveLiteral((Predicate) parser.parse("P(G(u),x)"));
		c.addPositiveLiteral((Predicate) parser.parse("P(F(y),u)"));

		// Should be: [{P(F(c#),F(c#)),P(G(F(c#)),F(c#))}]
		c = c.getNonTrivialFactors().iterator().next();
		Literal p = c.getPositiveLiterals().get(0);
		assertEquals("P", p.getAtomicSentence().getSymbolicName());
		Function f = (Function) p.getAtomicSentence().getArgs().get(0);
		assertEquals("F", f.getFunctionName());
		Variable v = (Variable) f.getTerms().get(0);
		f = (Function) p.getAtomicSentence().getArgs().get(1);
		assertEquals("F", f.getFunctionName());
		assertEquals(v, f.getTerms().get(0));
			
		//
		p = c.getPositiveLiterals().get(1);
		f = (Function) p.getAtomicSentence().getArgs().get(0);
		assertEquals("G", f.getFunctionName());
		f = (Function) f.getTerms().get(0);
		assertEquals("F", f.getFunctionName());
		assertEquals(v, f.getTerms().get(0));
		f = (Function) p.getAtomicSentence().getArgs().get(1);
		assertEquals("F", f.getFunctionName());
		assertEquals(v, f.getTerms().get(0));

		
		// p(g(x)), q(x), p(f(a)), p(x), p(g(f(x))), q(f(a))
		c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(G(x))"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(x)"));
		c.addPositiveLiteral((Predicate) parser.parse("P(F(A))"));
		c.addPositiveLiteral((Predicate) parser.parse("P(x)"));
		c.addPositiveLiteral((Predicate) parser.parse("P(G(F(x)))"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(F(A))"));

		assertEquals("[[P(F(A)), P(G(F(F(A)))), P(G(F(A))), Q(F(A))]]", c
				.getNonTrivialFactors().toString());
	}
	
	// Note: Tests derived from:
	// http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
	// page 16.
	public void testIsTautology() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addFunction("F");

		FOLParser parser = new FOLParser(domain);

		// {p(f(a)),~p(f(a))}
		Clause c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(F(A))"));
		assertFalse(c.isTautology());
		c.addNegativeLiteral((Predicate) parser.parse("P(F(A))"));
		assertTrue(c.isTautology());
		
		// {p(x),q(y),~q(y),r(z)}
		c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(x)"));
		assertFalse(c.isTautology());
		c.addPositiveLiteral((Predicate) parser.parse("Q(y)"));
		assertFalse(c.isTautology());
		c.addNegativeLiteral((Predicate) parser.parse("Q(y)"));
		assertTrue(c.isTautology());
		c.addPositiveLiteral((Predicate) parser.parse("R(z)"));
		assertTrue(c.isTautology());
		
		// {~p(a),p(x)}
		c = new Clause();
		c.addNegativeLiteral((Predicate) parser.parse("P(A)"));
		assertFalse(c.isTautology());
		c.addPositiveLiteral((Predicate) parser.parse("P(x)"));
		assertFalse(c.isTautology());
	}
}
