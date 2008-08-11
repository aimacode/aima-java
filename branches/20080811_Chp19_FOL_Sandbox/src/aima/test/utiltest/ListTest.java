/*
 * Created on Dec 4, 2004
 *
 */
package aima.test.utiltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.util.Util;

/**
 * @author Ravi Mohan
 * 
 */

public class ListTest extends TestCase {
	public void testListOfSymbolsCloneing() {
		List<Symbol> l = new ArrayList<Symbol>();
		l.add(new Symbol("A"));
		l.add(new Symbol("B"));
		l.add(new Symbol("C"));
		List l2 = (List) ((ArrayList) l).clone();
		l2.remove(new Symbol("B"));
		assertEquals(3, l.size());
		assertEquals(2, l2.size());
	}

	public void testModeFunction() {
		List<Integer> l = new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(2);
		l.add(3);
		int i = (Util.mode(l)).intValue();
		assertEquals(2, i);

		List<Integer> l2 = new ArrayList<Integer>();
		l2.add(1);
		i = (Util.mode(l2)).intValue();
		assertEquals(1, i);
	}

}
