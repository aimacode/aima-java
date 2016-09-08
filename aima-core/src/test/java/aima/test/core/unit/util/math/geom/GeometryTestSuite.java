package aima.test.core.unit.util.math.geom;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import aima.test.core.unit.util.math.geom.shapes.Circle2DTest;
import aima.test.core.unit.util.math.geom.shapes.Ellipse2DTest;
import aima.test.core.unit.util.math.geom.shapes.Line2DTest;
import aima.test.core.unit.util.math.geom.shapes.Polyline2DTest;
import aima.test.core.unit.util.math.geom.shapes.Rect2DTest;
import aima.test.core.unit.util.math.geom.shapes.Vector2DTest;

/**
 * Test suite for the {@code aima.core.util.math.geom} package.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@RunWith(Suite.class)
@SuiteClasses({SVGGroupParserTest.class, Vector2DTest.class, Line2DTest.class, Rect2DTest.class, Polyline2DTest.class, Circle2DTest.class, Ellipse2DTest.class })
public class GeometryTestSuite {

}
