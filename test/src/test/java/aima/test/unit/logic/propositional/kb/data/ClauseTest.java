package aima.test.unit.logic.propositional.kb.data;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.kb.data.Literal;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.util.Util;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class ClauseTest {

	private final Literal LITERAL_P     = new Literal(new PropositionSymbol("P"));
	private final Literal LITERAL_NOT_P = new Literal(new PropositionSymbol("P"), false);
	private final Literal LITERAL_Q     = new Literal(new PropositionSymbol("Q"));
	private final Literal LITERAL_NOT_Q = new Literal(new PropositionSymbol("Q"), false);
	private final Literal LITERAL_R     = new Literal(new PropositionSymbol("R"));
	
	@Test
	public void testAlwaysFalseLiteralsExcludedOnConstruction() {
		Clause clause = new Clause();
		Assert.assertEquals(0, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P);
		Assert.assertEquals(1, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.FALSE));
		Assert.assertEquals(1, clause.getNumberLiterals());
		Assert.assertEquals(Util.createSet(LITERAL_P), clause.getLiterals());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.TRUE, false));
		Assert.assertEquals(1, clause.getNumberLiterals());
		Assert.assertEquals(Util.createSet(LITERAL_P), clause.getLiterals());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.FALSE, false));
		Assert.assertEquals(2, clause.getNumberLiterals());
		Assert.assertEquals(Util.createSet(LITERAL_P, new Literal(PropositionSymbol.FALSE, false)), clause.getLiterals());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.TRUE));
		Assert.assertEquals(2, clause.getNumberLiterals());
		Assert.assertEquals(Util.createSet(LITERAL_P, new Literal(PropositionSymbol.TRUE)), clause.getLiterals());
	}
	
	@Test
	public void testIsFalse() {
		Clause clause = new Clause();
		Assert.assertTrue(clause.isFalse());
		
		clause = new Clause(LITERAL_P);
		Assert.assertFalse(clause.isFalse());
	}
	
	@Test
	public void testIsEmpty() {
		Clause clause = new Clause();
		Assert.assertTrue(clause.isEmpty());
		
		clause = new Clause(LITERAL_P);
		Assert.assertFalse(clause.isEmpty());
	}
	
	@Test
	public void testIsUnitClause() {
		Clause clause = new Clause();
		Assert.assertFalse(clause.isUnitClause());
		
		clause = new Clause(LITERAL_P);
		Assert.assertTrue(clause.isUnitClause());
		
		clause = new Clause(LITERAL_P, LITERAL_Q);
		Assert.assertFalse(clause.isUnitClause());
	}
	
	@Test
	public void testIsDefiniteClause() {
		Clause clause = new Clause();
		Assert.assertFalse(clause.isDefiniteClause());
		
		clause = new Clause(LITERAL_P);
		Assert.assertTrue(clause.isDefiniteClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_Q);
		Assert.assertTrue(clause.isDefiniteClause());
		
		clause = new Clause(LITERAL_P, LITERAL_Q);
		Assert.assertFalse(clause.isDefiniteClause());
	}
	
	@Test
	public void testIsImplicationDefiniteClause() {
		Clause clause = new Clause();
		Assert.assertFalse(clause.isImplicationDefiniteClause());
		
		clause = new Clause(LITERAL_P);
		Assert.assertFalse(clause.isImplicationDefiniteClause());
		
		clause = new Clause(LITERAL_NOT_P);
		Assert.assertFalse(clause.isImplicationDefiniteClause());
		
		clause = new Clause(LITERAL_P, LITERAL_Q);
		Assert.assertFalse(clause.isImplicationDefiniteClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_Q);
		Assert.assertTrue(clause.isImplicationDefiniteClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_P, LITERAL_NOT_Q);
		Assert.assertTrue(clause.isImplicationDefiniteClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_P, LITERAL_Q, LITERAL_NOT_Q);
		Assert.assertFalse(clause.isImplicationDefiniteClause());
	}
	
	@Test
	public void testIsHornClause() {
		Clause clause = new Clause();
		Assert.assertFalse(clause.isHornClause());
		
		clause = new Clause(LITERAL_P);
		Assert.assertTrue(clause.isHornClause());
		
		clause = new Clause(LITERAL_NOT_P);
		Assert.assertTrue(clause.isHornClause());
		
		clause = new Clause(LITERAL_P, LITERAL_Q);
		Assert.assertFalse(clause.isHornClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_Q);
		Assert.assertTrue(clause.isHornClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_P, LITERAL_NOT_Q);
		Assert.assertTrue(clause.isHornClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_P, LITERAL_Q, LITERAL_NOT_Q);
		Assert.assertFalse(clause.isHornClause());
	}
	
	@Test
	public void testIsGoalClause() {
		Clause clause = new Clause();
		Assert.assertFalse(clause.isGoalClause());
		
		clause = new Clause(LITERAL_P);
		Assert.assertFalse(clause.isGoalClause());
		
		clause = new Clause(LITERAL_NOT_P);
		Assert.assertTrue(clause.isGoalClause());
		
		clause = new Clause(LITERAL_P, LITERAL_Q);
		Assert.assertFalse(clause.isGoalClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_Q);
		Assert.assertFalse(clause.isGoalClause());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_P, LITERAL_NOT_Q);
		Assert.assertFalse(clause.isGoalClause());
		
		clause = new Clause(LITERAL_NOT_P, LITERAL_NOT_Q);
		Assert.assertTrue(clause.isGoalClause());
	}
	
	@Test
	public void testIsTautology() {
		Clause clause = new Clause();
		Assert.assertFalse(clause.isTautology());
		
		clause = new Clause(LITERAL_P);
		Assert.assertFalse(clause.isTautology());
		
		clause = new Clause(LITERAL_NOT_P);
		Assert.assertFalse(clause.isTautology());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.TRUE), LITERAL_R);
		Assert.assertTrue(clause.isTautology());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.FALSE, false), LITERAL_R);
		Assert.assertTrue(clause.isTautology());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.TRUE, false), LITERAL_R);
		Assert.assertFalse(clause.isTautology());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.FALSE), LITERAL_R);
		Assert.assertFalse(clause.isTautology());
		
		clause = new Clause(LITERAL_P, LITERAL_Q, LITERAL_R, LITERAL_NOT_Q);
		Assert.assertTrue(clause.isTautology());
		
		clause = new Clause(LITERAL_P, LITERAL_Q, LITERAL_R);
		Assert.assertFalse(clause.isTautology());
	}
	
	@Test
	public void testGetNumberLiterals() {
		Clause clause = new Clause();
		Assert.assertEquals(0, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P);
		Assert.assertEquals(1, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.FALSE));
		Assert.assertEquals(1, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.TRUE, false));
		Assert.assertEquals(1, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.FALSE, false));
		Assert.assertEquals(2, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P, new Literal(PropositionSymbol.TRUE));
		Assert.assertEquals(2, clause.getNumberLiterals());

		clause = new Clause(LITERAL_P, LITERAL_P);
		Assert.assertEquals(1, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P, LITERAL_Q);
		Assert.assertEquals(2, clause.getNumberLiterals());
		
		clause = new Clause(LITERAL_P, LITERAL_Q, LITERAL_R);
		Assert.assertEquals(3, clause.getNumberLiterals());
	}
	
	@Test
	public void testGetNumberPositiveLiterals() {
		Clause clause = new Clause();
		Assert.assertEquals(0, clause.getNumberPositiveLiterals());
		
		clause = new Clause(LITERAL_P);
		Assert.assertEquals(1, clause.getNumberPositiveLiterals());
		
		clause = new Clause(LITERAL_NOT_P);
		Assert.assertEquals(0, clause.getNumberPositiveLiterals());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_P, LITERAL_Q);
		Assert.assertEquals(2, clause.getNumberPositiveLiterals());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_Q, LITERAL_R);
		Assert.assertEquals(2, clause.getNumberPositiveLiterals());
	}
	
	@Test
	public void testGetNumberNegativeLiterals() {
		Clause clause = new Clause();
		Assert.assertEquals(0, clause.getNumberNegativeLiterals());
		
		clause = new Clause(LITERAL_P);
		Assert.assertEquals(0, clause.getNumberNegativeLiterals());
		
		clause = new Clause(LITERAL_NOT_P);
		Assert.assertEquals(1, clause.getNumberNegativeLiterals());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_P, LITERAL_Q);
		Assert.assertEquals(1, clause.getNumberNegativeLiterals());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_Q, LITERAL_R);
		Assert.assertEquals(1, clause.getNumberNegativeLiterals());
		
		clause = new Clause(LITERAL_P, LITERAL_NOT_P, LITERAL_NOT_Q);
		Assert.assertEquals(2, clause.getNumberNegativeLiterals());
	}
	
	@Test
	public void testGetLiterals() {
		Clause clause = new Clause();
		Assert.assertEquals(new HashSet<Literal>(), clause.getLiterals());
		
		clause = new Clause(LITERAL_P);
		Assert.assertEquals(Util.createSet(LITERAL_P), clause.getLiterals());

		clause = new Clause(LITERAL_P, LITERAL_NOT_Q, LITERAL_R);
		Assert.assertEquals(Util.createSet(LITERAL_P, LITERAL_NOT_Q, LITERAL_R), clause.getLiterals());
	}
	
	@Test
	public void testGetPositiveSymbols() {
		Clause clause = new Clause();
		Assert.assertEquals(new HashSet<PropositionSymbol>(), clause.getPositiveSymbols());
		
		clause = new Clause(LITERAL_P);
		Assert.assertEquals(Util.createSet(new PropositionSymbol("P")), clause.getPositiveSymbols());

		clause = new Clause(LITERAL_P, LITERAL_NOT_Q, LITERAL_R);
		Assert.assertEquals(Util.createSet(new PropositionSymbol("P"), new PropositionSymbol("R")), clause.getPositiveSymbols());
	}
	
	@Test
	public void testGetNegativeSymbols() {
		Clause clause = new Clause();
		Assert.assertEquals(new HashSet<PropositionSymbol>(), clause.getNegativeSymbols());
		
		clause = new Clause(LITERAL_NOT_P);
		Assert.assertEquals(Util.createSet(new PropositionSymbol("P")), clause.getNegativeSymbols());

		clause = new Clause(LITERAL_NOT_P, LITERAL_NOT_Q, LITERAL_R);
		Assert.assertEquals(Util.createSet(new PropositionSymbol("P"), new PropositionSymbol("Q")), clause.getNegativeSymbols());
	}
	
	@Test
	public void testToString() {
		Clause clause = new Clause();
		Assert.assertEquals("{}", clause.toString());
		
		clause = new Clause(LITERAL_P);
		Assert.assertEquals("{P}", clause.toString());
		
		clause = new Clause(LITERAL_P, LITERAL_Q, LITERAL_R);
		Assert.assertEquals("{P, Q, R}", clause.toString());

		clause = new Clause(LITERAL_NOT_P, LITERAL_NOT_Q, LITERAL_R);
		Assert.assertEquals("{~P, ~Q, R}", clause.toString());
	}
	
	@Test
	public void testEquals() {
		Clause clause1 = new Clause();
		Clause clause2 = new Clause();
		Assert.assertTrue(clause1.equals(clause2));
		
		clause1 = new Clause(LITERAL_P);
		clause2 = new Clause(LITERAL_P);
		Assert.assertTrue(clause1.equals(clause2));
		
		clause1 = new Clause(LITERAL_P, LITERAL_Q);
		clause2 = new Clause(LITERAL_P, LITERAL_Q);
		Assert.assertTrue(clause1.equals(clause2));
		
		clause1 = new Clause(LITERAL_R, LITERAL_P, LITERAL_Q);
		clause2 = new Clause(LITERAL_P, LITERAL_Q, LITERAL_R);
		Assert.assertTrue(clause1.equals(clause2));
		
		clause1 = new Clause(LITERAL_P);
		clause2 = new Clause(LITERAL_Q);
		Assert.assertFalse(clause1.equals(clause2));
		
		clause1 = new Clause(LITERAL_P, LITERAL_Q);
		clause2 = new Clause(LITERAL_P, LITERAL_R);
		Assert.assertFalse(clause1.equals(clause2));
		
		clause1 = new Clause(LITERAL_P);
		Assert.assertFalse(clause1.equals(LITERAL_P));
	}
	
	@Test
	public void testHashCode() {
		Clause clause1 = new Clause();
		Clause clause2 = new Clause();
		Assert.assertTrue(clause1.hashCode() == clause2.hashCode());
		
		clause1 = new Clause(LITERAL_P);
		clause2 = new Clause(LITERAL_P);
		Assert.assertTrue(clause1.hashCode() == clause2.hashCode());
		
		clause1 = new Clause(LITERAL_P, LITERAL_Q);
		clause2 = new Clause(LITERAL_P, LITERAL_Q);
		Assert.assertTrue(clause1.hashCode() == clause2.hashCode());
		
		clause1 = new Clause(LITERAL_R, LITERAL_P, LITERAL_Q);
		clause2 = new Clause(LITERAL_P, LITERAL_Q, LITERAL_R);
		Assert.assertTrue(clause1.hashCode() == clause2.hashCode());
		
		clause1 = new Clause(LITERAL_P);
		clause2 = new Clause(LITERAL_Q);
		Assert.assertFalse(clause1.hashCode() == clause2.hashCode());
		
		clause1 = new Clause(LITERAL_P, LITERAL_Q);
		clause2 = new Clause(LITERAL_P, LITERAL_R);
		Assert.assertFalse(clause1.hashCode() == clause2.hashCode());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testLiteralsImmutable() {
		Clause clause = new Clause(LITERAL_P);
		clause.getLiterals().add(LITERAL_Q);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testPostivieSymbolsImmutable() {
		Clause clause = new Clause(LITERAL_P);
		clause.getPositiveSymbols().add(new PropositionSymbol("Q"));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testNegativeSymbolsImmutable() {
		Clause clause = new Clause(LITERAL_P);
		clause.getNegativeSymbols().add(new PropositionSymbol("Q"));
	}
	
}
