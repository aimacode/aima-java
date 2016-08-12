package aima.test.core.unit.robotics.impl.simple;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

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
public class SimpleTests {

}
