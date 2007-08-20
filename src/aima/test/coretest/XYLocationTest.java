/*
 * Created by IntelliJ IDEA.
 * User: rrmohan
 * Date: Jan 19, 2003
 * Time: 1:53:09 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package aima.test.coretest;

import junit.framework.TestCase;
import aima.basic.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class XYLocationTest extends TestCase {
	public XYLocationTest(String name) {
		super(name);
	}

	public void testXYLocationAtributeSettingOnConstruction() {
		XYLocation loc = new XYLocation(3, 4);
		assertEquals(3, loc.getXCoOrdinate());
		assertEquals(4, loc.getYCoOrdinate());
	}

	public void testEquality() {
		XYLocation loc1 = new XYLocation(3, 4);
		XYLocation loc2 = new XYLocation(3, 4);
		assertEquals(loc1, loc2);
	}
}
