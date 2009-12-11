package aima.test.core.unit.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.util.datastructure.FIFOQueueTest;
import aima.test.core.unit.util.datastructure.LIFOQueueTest;
import aima.test.core.unit.util.datastructure.TableTest;
import aima.test.core.unit.util.datastructure.XYLocationTest;
import aima.test.core.unit.util.math.MixedRadixNumberTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { FIFOQueueTest.class, LIFOQueueTest.class,
		TableTest.class, XYLocationTest.class, MixedRadixNumberTest.class,
		SetOpsTest.class, UtilTest.class })
public class UtilTestSuite {

}
