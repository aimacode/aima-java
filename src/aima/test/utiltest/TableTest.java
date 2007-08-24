/*
 * Created on Jul 31, 2005
 *
 */
package aima.test.utiltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.util.Table;

/**
 * @author Ravi Mohan
 * 
 */

public class TableTest extends TestCase {
	private Table<String, String, Integer> table;

	@Override
	public void setUp() {
		List<String> rowHeaders = new ArrayList<String>();
		List<String> columnHeaders = new ArrayList<String>();

		rowHeaders.add("row1");
		rowHeaders.add("ravi");
		rowHeaders.add("peter");

		columnHeaders.add("col1");
		columnHeaders.add("iq");
		columnHeaders.add("age");
		table = new Table<String, String, Integer>(rowHeaders, columnHeaders);

	}

	public void testTableInitialization() {
		assertNull(table.get("ravi", "iq"));
		table.set("ravi", "iq", 50);
		int i = table.get("ravi", "iq");
		assertEquals(50, i);
	}

	public void testNullAccess() {
		// No value yet assigned
		assertNull(table.get("row1", "col2"));
		table.set("row1", "col1", 1);
		assertEquals(1, (int) table.get("row1", "col1"));
		// Check null returned if column does not exist
		assertNull(table.get("row1", "col2"));
		// Check null returned if row does not exist
		assertNull(table.get("row2", "col1"));
	}

}
