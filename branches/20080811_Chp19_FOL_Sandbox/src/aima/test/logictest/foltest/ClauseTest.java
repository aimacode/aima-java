package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;

import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;
import junit.framework.TestCase;

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
	
	public void testFactorClause() {
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

		// Check negative literals
		c1.addNegativeLiteral(new Predicate("Pred1", pts1));
		assertEquals(1, c1.getNegativeLiterals().size());
		
		c1.addNegativeLiteral(new Predicate("Pred1", pts2));
		assertEquals(2, c1.getNegativeLiterals().size());
		
		c1.addNegativeLiteral(new Predicate("Pred1", pts1));
		assertEquals(2, c1.getNegativeLiterals().size());
		c1.addNegativeLiteral(new Predicate("Pred1", pts2));
		assertEquals(2, c1.getNegativeLiterals().size());
		
		c1.addNegativeLiteral(new Predicate("Pred2", pts1));
		assertEquals(3, c1.getNegativeLiterals().size());
		
		c1.addNegativeLiteral(new Predicate("Pred2", pts2));
		assertEquals(4, c1.getNegativeLiterals().size());
		
		c1.addNegativeLiteral(new Predicate("Pred2", pts1));
		assertEquals(4, c1.getNegativeLiterals().size());
		c1.addNegativeLiteral(new Predicate("Pred2", pts2));
		assertEquals(4, c1.getNegativeLiterals().size());
		
		// Check positive literals
		c1.addPositiveLiteral(new Predicate("Pred1", pts1));
		assertEquals(1, c1.getPositiveLiterals().size());
		
		c1.addPositiveLiteral(new Predicate("Pred1", pts2));
		assertEquals(2, c1.getPositiveLiterals().size());
		
		c1.addPositiveLiteral(new Predicate("Pred1", pts1));
		assertEquals(2, c1.getPositiveLiterals().size());
		c1.addPositiveLiteral(new Predicate("Pred1", pts2));
		assertEquals(2, c1.getPositiveLiterals().size());
		
		c1.addPositiveLiteral(new Predicate("Pred2", pts1));
		assertEquals(3, c1.getPositiveLiterals().size());
		
		c1.addPositiveLiteral(new Predicate("Pred2", pts2));
		assertEquals(4, c1.getPositiveLiterals().size());
		
		c1.addPositiveLiteral(new Predicate("Pred2", pts1));
		assertEquals(4, c1.getPositiveLiterals().size());
		c1.addPositiveLiteral(new Predicate("Pred2", pts2));
		assertEquals(4, c1.getPositiveLiterals().size());	
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
}
