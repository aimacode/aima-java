package aima.test.core.unit.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.util.datastructure.TableTest;
import aima.test.core.unit.util.datastructure.XYLocationTest;
import aima.test.core.unit.util.math.MixedRadixNumberTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		TableTest.class, XYLocationTest.class, MixedRadixNumberTest.class,
		DisjointSetsTest.class, SetOpsTest.class, UtilTest.class })
public class UtilTestSuite {

}
