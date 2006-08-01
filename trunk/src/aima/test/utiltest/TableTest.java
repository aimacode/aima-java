/*
 * Created on Jul 31, 2005
 *
 */
package aima.test.utiltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.util.Table;

public class TableTest extends TestCase {
	private Table<String, String, Integer> table;

	public void setUp() {
		List<String> rowHeaders = new ArrayList<String>();
		List<String> columnHeaders = new ArrayList<String>();

		rowHeaders.add("ravi");
		rowHeaders.add("peter");

		columnHeaders.add("iq");
		columnHeaders.add("age");
		table = new Table<String, String, Integer>(rowHeaders, columnHeaders);

	}

	public void testTableInitialization() {
		assertNull(table.get("ravi","iq"));
		table.set("ravi","iq",50);
		int i = table.get("ravi","iq");
		assertEquals(50,i);
	}

}
