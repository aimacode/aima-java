package aima.test.core.unit.robotics;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import aima.test.core.unit.robotics.impl.simple.SimpleMoveTest;
import aima.test.core.unit.robotics.impl.simple.SimplePoseFactoryTest;
import aima.test.core.unit.robotics.impl.simple.SimplePoseTest;
import aima.test.core.unit.robotics.impl.simple.SimpleRangeReadingFactoryTest;
import aima.test.core.unit.robotics.impl.simple.SimpleRangeReadingTest;

/**
 * Test suite for the aima.core.robotics.impl.simple package.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@RunWith(Suite.class)
@SuiteClasses({SimpleRangeReadingTest.class, SimplePoseTest.class, SimpleMoveTest.class, SimpleRangeReadingFactoryTest.class, SimplePoseFactoryTest.class})
public class RoboticsTestSuite {

}
