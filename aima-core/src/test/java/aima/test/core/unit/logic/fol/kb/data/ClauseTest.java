package aima.test.core.unit.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ClauseTest {

	@Before
	public void setUp() {
		StandardizeApartIndexicalFactory.flush();
	}

	@Test
	public void testImmutable() {
		Clause c = new Clause();

		Assert.assertFalse(c.isImmutable());

		c.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));

		c.setImmutable();

		Assert.assertTrue(c.isImmutable());

		try {
			c.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));

			Assert.fail("Should have thrown an IllegalStateException");
		} catch (IllegalStateException ise) {
			// Ok, Expected
		}

		try {
			c.addPositiveLiteral(new Predicate("Pred3", new ArrayList<Term>()));

			Assert.fail("Should have thrown an IllegalStateException");
		} catch (IllegalStateException ise) {
			// Ok, Expected
		}
	}

	@Test
	public void testIsEmpty() {
		Clause c1 = new Clause();
		Assert.assertTrue(c1.isEmpty());

		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertFalse(c1.isEmpty());

		Clause c2 = new Clause();
		Assert.assertTrue(c2.isEmpty());

		c2.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertFalse(c2.isEmpty());

		Clause c3 = new Clause();
		Assert.assertTrue(c3.isEmpty());

		c3.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c3.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		// Should be empty as they resolved with each other
		Assert.assertFalse(c3.isEmpty());

		c3.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c3.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertFalse(c3.isEmpty());
	}

	@Test
	public void testIsHornClause() {
		Clause c1 = new Clause();
		Assert.assertFalse(c1.isHornClause());

		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertTrue(c1.isHornClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertTrue(c1.isHornClause());

		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		Assert.assertTrue(c1.isHornClause());
		c1.addNegativeLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		Assert.assertTrue(c1.isHornClause());

		c1.addPositiveLiteral(new Predicate("Pred5", new ArrayList<Term>()));
		Assert.assertFalse(c1.isHornClause());
	}

	@Test
	public void testIsDefiniteClause() {
		Clause c1 = new Clause();
		Assert.assertFalse(c1.isDefiniteClause());

		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertFalse(c1.isDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertTrue(c1.isDefiniteClause());

		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		Assert.assertTrue(c1.isDefiniteClause());
		c1.addNegativeLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		Assert.assertTrue(c1.isDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred5", new ArrayList<Term>()));
		Assert.assertFalse(c1.isDefiniteClause());
	}

	@Test
	public void testIsUnitClause() {
		Clause c1 = new Clause();
		Assert.assertFalse(c1.isUnitClause());

		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertTrue(c1.isUnitClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertFalse(c1.isUnitClause());

		c1 = new Clause();
		Assert.assertFalse(c1.isUnitClause());

		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertTrue(c1.isUnitClause());

		c1.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertFalse(c1.isUnitClause());

		c1 = new Clause();
		Assert.assertFalse(c1.isUnitClause());

		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertTrue(c1.isUnitClause());

		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertFalse(c1.isUnitClause());
	}

	@Test
	public void testIsImplicationDefiniteClause() {
		Clause c1 = new Clause();
		Assert.assertFalse(c1.isImplicationDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertFalse(c1.isImplicationDefiniteClause());

		c1.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertTrue(c1.isImplicationDefiniteClause());
		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		Assert.assertTrue(c1.isImplicationDefiniteClause());

		c1.addPositiveLiteral(new Predicate("Pred4", new ArrayList<Term>()));
		Assert.assertFalse(c1.isImplicationDefiniteClause());
	}

	@Test
	public void testBinaryResolvents() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("Pred1");
		domain.addPredicate("Pred2");
		domain.addPredicate("Pred3");
		domain.addPredicate("Pred4");

		Clause c1 = new Clause();

		// Ensure that resolving to self when empty returns an empty clause
		Assert.assertNotNull(c1.binaryResolvents(c1));
		Assert.assertEquals(1, c1.binaryResolvents(c1).size());
		Assert.assertTrue(c1.binaryResolvents(c1).iterator().next().isEmpty());

		// Check if resolve with self to an empty clause
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertNotNull(c1.binaryResolvents(c1));
		Assert.assertEquals(1, c1.binaryResolvents(c1).size());
		// i.e. resolving a tautology with a tautology gives you
		// back a tautology.
		Assert.assertEquals("[~Pred1(), Pred1()]", c1.binaryResolvents(c1)
				.iterator().next().toString());

		// Check if try to resolve with self and no resolvents
		c1 = new Clause();
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertEquals(0, c1.binaryResolvents(c1).size());

		c1 = new Clause();
		Clause c2 = new Clause();
		// Ensure that two empty clauses resolve to an empty clause
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertEquals(1, c1.binaryResolvents(c2).size());
		Assert.assertTrue(c1.binaryResolvents(c2).iterator().next().isEmpty());
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(1, c2.binaryResolvents(c1).size());
		Assert.assertTrue(c2.binaryResolvents(c1).iterator().next().isEmpty());

		// Enusre the two complementary clauses resolve
		// to the empty clause
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertEquals(1, c1.binaryResolvents(c2).size());
		Assert.assertTrue(c1.binaryResolvents(c2).iterator().next().isEmpty());
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(1, c2.binaryResolvents(c1).size());
		Assert.assertTrue(c2.binaryResolvents(c1).iterator().next().isEmpty());

		// Ensure that two clauses that have two complementaries
		// resolve with two resolvents
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		c2.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertEquals(2, c1.binaryResolvents(c2).size());
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(2, c2.binaryResolvents(c1).size());

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
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertEquals(0, c1.binaryResolvents(c2).size());
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(0, c2.binaryResolvents(c1).size());

		// Ensure the resolvent is a subset of the originals
		c1 = new Clause();
		c2 = new Clause();
		c1.addPositiveLiteral(new Predicate("Pred1", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		c1.addNegativeLiteral(new Predicate("Pred3", new ArrayList<Term>()));
		c2.addPositiveLiteral(new Predicate("Pred2", new ArrayList<Term>()));
		Assert.assertNotNull(c1.binaryResolvents(c2));
		Assert.assertNotNull(c2.binaryResolvents(c1));
		Assert.assertEquals(1, c1.binaryResolvents(c2).iterator().next()
				.getNumberPositiveLiterals());
		Assert.assertEquals(1, c1.binaryResolvents(c2).iterator().next()
				.getNumberNegativeLiterals());
		Assert.assertEquals(1, c2.binaryResolvents(c1).iterator().next()
				.getNumberPositiveLiterals());
		Assert.assertEquals(1, c2.binaryResolvents(c1).iterator().next()
				.getNumberNegativeLiterals());
	}

	@Test
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
						Assert
								.fail("Ordering of binary resolvents has become important, which should not be the case");
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

	@Test
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

		Assert.assertEquals(1, resolvents.size());
		Assert.assertEquals("[[B = A]]", resolvents.toString());
	}

	@Test
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
		Assert.assertEquals(c1.hashCode(), c2.hashCode());

		c1.addNegativeLiteral(new Predicate("Pred1", pts1));
		Assert.assertNotSame(c1.hashCode(), c2.hashCode());
		c2.addNegativeLiteral(new Predicate("Pred1", pts1));
		Assert.assertEquals(c1.hashCode(), c2.hashCode());

		c1.addPositiveLiteral(new Predicate("Pred1", pts1));
		Assert.assertNotSame(c1.hashCode(), c2.hashCode());
		c2.addPositiveLiteral(new Predicate("Pred1", pts1));
		Assert.assertEquals(c1.hashCode(), c2.hashCode());
	}

	@Test
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
		Assert.assertTrue(c1.equals(c1));
		Assert.assertTrue(c2.equals(c2));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));

		// Check negatives
		c1.addNegativeLiteral(new Predicate("Pred1", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new Predicate("Pred1", pts1));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));

		c1.addNegativeLiteral(new Predicate("Pred2", pts2));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new Predicate("Pred2", pts2));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));
		// Check same but added in different order
		c1.addNegativeLiteral(new Predicate("Pred3", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c1.addNegativeLiteral(new Predicate("Pred4", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new Predicate("Pred4", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addNegativeLiteral(new Predicate("Pred3", pts1));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));

		// Check positives
		c1.addPositiveLiteral(new Predicate("Pred1", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new Predicate("Pred1", pts1));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));

		c1.addPositiveLiteral(new Predicate("Pred2", pts2));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new Predicate("Pred2", pts2));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));
		// Check same but added in different order
		c1.addPositiveLiteral(new Predicate("Pred3", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c1.addPositiveLiteral(new Predicate("Pred4", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new Predicate("Pred4", pts1));
		Assert.assertFalse(c1.equals(c2));
		Assert.assertFalse(c2.equals(c1));
		c2.addPositiveLiteral(new Predicate("Pred3", pts1));
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c2.equals(c1));
	}

	@Test
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

		Assert.assertFalse(c1.equals(c2));

		s1 = parser.parse("((x1 = y1 AND y1 = z1) => x1 = z1)");
		s2 = parser.parse("((x2 = y2 AND y2 = z2) => x2 = z2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser.parse("((x1 = y1 AND y1 = z1) => x1 = z1)");
		s2 = parser.parse("((y2 = z2 AND x2 = y2) => x2 = z2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((x2 = y2 AND y2 = z2) AND z2 = r2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((z2 = r2 AND y2 = z2) AND x2 = y2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser.parse("(((x1 = y1 AND y1 = z1) AND z1 = r1) => x1 = r1)");
		s2 = parser.parse("(((x2 = y2 AND y2 = z2) AND z2 = y2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertFalse(c1.equals(c2));

		s1 = parser
				.parse("(((((x1 = y1 AND y1 = z1) AND z1 = r1) AND r1 = q1) AND q1 = s1) => x1 = r1)");
		s2 = parser
				.parse("(((((x2 = y2 AND y2 = z2) AND z2 = r2) AND r2 = q2) AND q2 = s2) => x2 = r2)");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));

		s1 = parser
				.parse("((((NOT(Animal(c1920)) OR NOT(Animal(c1921))) OR NOT(Kills(c1922,c1920))) OR NOT(Kills(c1919,c1921))) OR NOT(Kills(SF0(c1922),SF0(c1919))))");
		s2 = parser
				.parse("((((NOT(Animal(c1929)) OR NOT(Animal(c1928))) OR NOT(Kills(c1927,c1929))) OR NOT(Kills(c1930,c1928))) OR NOT(Kills(SF0(c1930),SF0(c1927))))");
		cnf1 = cnfConverter.convertToCNF(s1);
		cnf2 = cnfConverter.convertToCNF(s2);

		c1 = cnf1.getConjunctionOfClauses().get(0);
		c2 = cnf2.getConjunctionOfClauses().get(0);

		Assert.assertTrue(c1.equals(c2));
	}

	@Test
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

		// p(x,y), q(a,b), p(b,a), q(y,x)
		Clause c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(x,y)"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(A,B)"));
		c.addNegativeLiteral((Predicate) parser.parse("P(B,A)"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(y,x)"));

		Assert.assertEquals("[[~P(B,A), P(B,A), Q(A,B)]]", c
				.getNonTrivialFactors().toString());

		// p(x,y), q(a,b), p(b,a), q(y,x)
		c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(x,y)"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(A,B)"));
		c.addNegativeLiteral((Predicate) parser.parse("P(B,A)"));
		c.addNegativeLiteral((Predicate) parser.parse("Q(y,x)"));

		Assert.assertEquals("[]", c.getNonTrivialFactors().toString());

		// p(x,f(y)), p(g(u),x), p(f(y),u)
		c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(x,F(y))"));
		c.addPositiveLiteral((Predicate) parser.parse("P(G(u),x)"));
		c.addPositiveLiteral((Predicate) parser.parse("P(F(y),u)"));

		// Should be: [{P(F(c#),F(c#)),P(G(F(c#)),F(c#))}]
		c = c.getNonTrivialFactors().iterator().next();
		Literal p = c.getPositiveLiterals().get(0);
		Assert.assertEquals("P", p.getAtomicSentence().getSymbolicName());
		Function f = (Function) p.getAtomicSentence().getArgs().get(0);
		Assert.assertEquals("F", f.getFunctionName());
		Variable v = (Variable) f.getTerms().get(0);
		f = (Function) p.getAtomicSentence().getArgs().get(1);
		Assert.assertEquals("F", f.getFunctionName());
		Assert.assertEquals(v, f.getTerms().get(0));

		//
		p = c.getPositiveLiterals().get(1);
		f = (Function) p.getAtomicSentence().getArgs().get(0);
		Assert.assertEquals("G", f.getFunctionName());
		f = (Function) f.getTerms().get(0);
		Assert.assertEquals("F", f.getFunctionName());
		Assert.assertEquals(v, f.getTerms().get(0));
		f = (Function) p.getAtomicSentence().getArgs().get(1);
		Assert.assertEquals("F", f.getFunctionName());
		Assert.assertEquals(v, f.getTerms().get(0));

		// p(g(x)), q(x), p(f(a)), p(x), p(g(f(x))), q(f(a))
		c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(G(x))"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(x)"));
		c.addPositiveLiteral((Predicate) parser.parse("P(F(A))"));
		c.addPositiveLiteral((Predicate) parser.parse("P(x)"));
		c.addPositiveLiteral((Predicate) parser.parse("P(G(F(x)))"));
		c.addPositiveLiteral((Predicate) parser.parse("Q(F(A))"));

		Assert.assertEquals("[[P(F(A)), P(G(F(F(A)))), P(G(F(A))), Q(F(A))]]",
				c.getNonTrivialFactors().toString());
	}

	// Note: Tests derived from:
	// http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
	// page 16.
	@Test
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
		Assert.assertFalse(c.isTautology());
		c.addNegativeLiteral((Predicate) parser.parse("P(F(A))"));
		Assert.assertTrue(c.isTautology());

		// {p(x),q(y),~q(y),r(z)}
		c = new Clause();
		c.addPositiveLiteral((Predicate) parser.parse("P(x)"));
		Assert.assertFalse(c.isTautology());
		c.addPositiveLiteral((Predicate) parser.parse("Q(y)"));
		Assert.assertFalse(c.isTautology());
		c.addNegativeLiteral((Predicate) parser.parse("Q(y)"));
		Assert.assertTrue(c.isTautology());
		c.addPositiveLiteral((Predicate) parser.parse("R(z)"));
		Assert.assertTrue(c.isTautology());

		// {~p(a),p(x)}
		c = new Clause();
		c.addNegativeLiteral((Predicate) parser.parse("P(A)"));
		Assert.assertFalse(c.isTautology());
		c.addPositiveLiteral((Predicate) parser.parse("P(x)"));
		Assert.assertFalse(c.isTautology());
	}

	// Note: Tests derived from:
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture12.pdf
	// slides 17 and 18.
	@Test
	public void testSubsumes() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addConstant("F");
		domain.addConstant("G");
		domain.addConstant("H");
		domain.addConstant("I");
		domain.addConstant("J");
		domain.addPredicate("P");
		domain.addPredicate("Q");

		FOLParser parser = new FOLParser(domain);

		// Example
		// {~p(a,b),q(c)}
		Clause psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C)"));
		// {~p(x,y)}
		Clause phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(x,y)"));

		Assert.assertTrue(phi.subsumes(psi));
		// Non-Example
		// {~p(x,b),q(x)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(x,B)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(x)"));
		// {~p(a,y)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(A,y)"));
		// Reason for Non-Example:
		// {p(b,b)}
		// {~q(b)}
		Assert.assertFalse(phi.subsumes(psi));

		//
		// Additional Examples

		// Non-Example
		// {~p(x,b),q(z)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(x,B)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(z)"));
		// {~p(a,y)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(A,y)"));

		Assert.assertFalse(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(w,z),q(c)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(w,z)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C)"));
		// {~p(x,y),~p(a,b)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(x,y)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Non-Example
		// {~p(v,b),~p(w,z),q(c)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(v,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(w,z)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C)"));
		// {~p(x,y),~p(a,b)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(x,y)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));

		Assert.assertFalse(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(c)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C)"));
		// {~p(i,j)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),q(c)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(E,F)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C)"));
		// {~p(e,f),~p(a,b),~p(c,d)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(E,F)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(c)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C)"));
		// {~p(i,j),~p(c,d)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Non-Example
		// {~p(a,b),~p(x,d),~p(e,f),~p(g,h),~p(i,j),q(c)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(x,D)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C)"));
		// {~p(i,j),~p(c,d)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));

		Assert.assertFalse(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(c)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C)"));
		// {~p(i,j),~p(a,x)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(A,x)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(a,b),q(c,d),q(e,f)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(A,B)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C,D)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(E,F)"));
		// {~p(i,j),~p(a,b),q(e,f),q(a,b)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		phi.addPositiveLiteral((Predicate) parser.parse("Q(E,F)"));
		phi.addPositiveLiteral((Predicate) parser.parse("Q(A,B)"));

		Assert.assertTrue(phi.subsumes(psi));

		// Non-Example
		// {~p(a,b),~p(c,d),~p(e,f),~p(g,h),~p(i,j),q(a,b),q(c,d),q(e,f)}
		psi = new Clause();
		psi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(C,D)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(E,F)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(G,H)"));
		psi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(A,B)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(C,D)"));
		psi.addPositiveLiteral((Predicate) parser.parse("Q(E,F)"));
		// {~p(i,j),~p(a,b),q(e,f),q(a,b)}
		phi = new Clause();
		phi.addNegativeLiteral((Predicate) parser.parse("P(I,J)"));
		phi.addNegativeLiteral((Predicate) parser.parse("P(A,B)"));
		phi.addPositiveLiteral((Predicate) parser.parse("Q(E,A)"));
		phi.addPositiveLiteral((Predicate) parser.parse("Q(A,B)"));

		Assert.assertFalse(phi.subsumes(psi));
	}
}
