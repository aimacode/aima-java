package aima.test.core.unit.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */
public class ListTest {

	@Test
	public void testListOfSymbolsClone() {
		ArrayList<Symbol> l = new ArrayList<Symbol>();
		l.add(new Symbol("A"));
		l.add(new Symbol("B"));
		l.add(new Symbol("C"));
		List<Symbol> l2 = (List<Symbol>) l.clone();
		l2.remove(new Symbol("B"));
		Assert.assertEquals(3, l.size());
		Assert.assertEquals(2, l2.size());
	}

	@Test
	public void testListRemove() {
		List<Integer> one = new ArrayList<Integer>();
		one.add(new Integer(1));
		Assert.assertEquals(1, one.size());
		one.remove(0);
		Assert.assertEquals(0, one.size());
	}
}
