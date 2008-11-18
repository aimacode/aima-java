package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.logic.fol.kb.data.Chain;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ChainTest extends TestCase {
	
	public void testIsEmpty() {
		Chain c = new Chain();
		
		assertTrue(c.isEmpty());
		
		c.addLiteral(new Literal(new Predicate("P", new ArrayList<Term>())));
		
		assertFalse(c.isEmpty());
		
		List<Literal> lits = new ArrayList<Literal>();

		lits.add(new Literal(new Predicate("P", new ArrayList<Term>())));

		c = new Chain(lits);

		assertFalse(c.isEmpty());
	}
	
	public void testContrapositives() {
		List<Chain> conts;
		Literal p = new Literal(new Predicate("P", new ArrayList<Term>()));
		Literal notq = new Literal(new Predicate("Q", new ArrayList<Term>()),
				true);
		Literal notr = new Literal(new Predicate("R", new ArrayList<Term>()),
				true);

		Chain c = new Chain();

		conts = c.getContrapositives();
		assertEquals(0, conts.size());

		c.addLiteral(p);
		conts = c.getContrapositives();
		assertEquals(0, conts.size());

		c.addLiteral(notq);
		conts = c.getContrapositives();
		assertEquals(1, conts.size());
		assertEquals("<~Q(),P()>", conts.get(0).toString());
		
		c.addLiteral(notr);
		conts = c.getContrapositives();
		assertEquals(2, conts.size());
		assertEquals("<~Q(),P(),~R()>", conts.get(0).toString());
		assertEquals("<~R(),P(),~Q()>", conts.get(1).toString());
	}
}
