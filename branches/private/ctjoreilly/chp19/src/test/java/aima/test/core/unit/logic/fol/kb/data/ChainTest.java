package aima.test.core.unit.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ChainTest {

	@Test
	public void testIsEmpty() {
		Chain c = new Chain();

		Assert.assertTrue(c.isEmpty());

		c.addLiteral(new Literal(new Predicate("P", new ArrayList<Term>())));

		Assert.assertFalse(c.isEmpty());

		List<Literal> lits = new ArrayList<Literal>();

		lits.add(new Literal(new Predicate("P", new ArrayList<Term>())));

		c = new Chain(lits);

		Assert.assertFalse(c.isEmpty());
	}

	@Test
	public void testContrapositives() {
		List<Chain> conts;
		Literal p = new Literal(new Predicate("P", new ArrayList<Term>()));
		Literal notq = new Literal(new Predicate("Q", new ArrayList<Term>()),
				true);
		Literal notr = new Literal(new Predicate("R", new ArrayList<Term>()),
				true);

		Chain c = new Chain();

		conts = c.getContrapositives();
		Assert.assertEquals(0, conts.size());

		c.addLiteral(p);
		conts = c.getContrapositives();
		Assert.assertEquals(0, conts.size());

		c.addLiteral(notq);
		conts = c.getContrapositives();
		Assert.assertEquals(1, conts.size());
		Assert.assertEquals("<~Q(),P()>", conts.get(0).toString());

		c.addLiteral(notr);
		conts = c.getContrapositives();
		Assert.assertEquals(2, conts.size());
		Assert.assertEquals("<~Q(),P(),~R()>", conts.get(0).toString());
		Assert.assertEquals("<~R(),P(),~Q()>", conts.get(1).toString());
	}
}
