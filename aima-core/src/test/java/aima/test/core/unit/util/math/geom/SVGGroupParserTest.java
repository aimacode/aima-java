package aima.test.core.unit.util.math.geom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import aima.core.util.math.geom.IGroupParser;
import aima.core.util.math.geom.SVGGroupParser;
import aima.core.util.math.geom.shapes.Circle2D;
import aima.core.util.math.geom.shapes.Ellipse2D;
import aima.core.util.math.geom.shapes.IGeometric2D;
import aima.core.util.math.geom.shapes.Line2D;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Polyline2D;
import aima.core.util.math.geom.shapes.Rect2D;
/**
 * Test case for the {@code aima.core.util.math.geometry} package.
 * Tests valid implementation of the {@link IGroupParser} interface by {@link SVGGroupParser}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class SVGGroupParserTest {
	private final String file = "test.svg";
	private final String groupID = "obstacles";
	private SVGGroupParser testParser;
	private ArrayList<IGeometric2D> testGroup;
	private Point2D[] testVertices1 = new Point2D[]{new Point2D(100.0d,100.0d),new Point2D(150.0d,140.0d),new Point2D(120.0d,140.0d),new Point2D(80.0d,100.0d),new Point2D(55.0d,60.0d),new Point2D(85.0d,80.0d)};
	private Point2D[] testVertices2 = new Point2D[]{new Point2D(500.0d,150.0d),new Point2D(570.0d,200.0d),new Point2D(600.0d,236.0d),new Point2D(550.0d,300.0d),new Point2D(400.0d,220.0d),new Point2D(385.0d,80.0d)}; 
	
	@Before
	public void setUp() {
		testGroup = new ArrayList<IGeometric2D>();
		testGroup.ensureCapacity(10);
		testGroup.add(new Polyline2D(testVertices1, true));
		testGroup.add(new Line2D(15.0d,15.0d,200.0d,100.0d));
		testGroup.add(new Ellipse2D(new Point2D(300.0d,80.0d),60.0d,40.0d));
		testGroup.add(new Circle2D(new Point2D(180.0d,160.0d),20.0d));
		testGroup.add(new Rect2D(161.0d,200.0d,261.0d,250.0d));
		testGroup.add(new Polyline2D(testVertices2, true));
		testGroup.add(new Line2D(0.0d,0.0d,700.0d,0.0d));
		testGroup.add(new Line2D(700.0d,0.0d,700.0d,500.0d));
		testGroup.add(new Line2D(700.0d,500.0d,0.0d,500.0d));
		testGroup.add(new Line2D(0.0d,500.0d,0.0d,0.0d));
		testParser = new SVGGroupParser();
	}
	
	@Test
	public void testParse() {
		IGeometric2D x;
		IGeometric2D y;
		Iterator<IGeometric2D> j;
		try {
			j = testParser.parse(this.getClass().getResourceAsStream(file), groupID).iterator();
			Iterator<IGeometric2D> i = testGroup.iterator(); 
			while (i.hasNext()){
				x = i.next();
				y = j.next();
				if (x instanceof Circle2D && y instanceof Circle2D) {
					assertEquals("Circle: Center-X.",((Circle2D)(y)).getCenter().getX(), ((Circle2D)(x)).getCenter().getX(), 0.000005d);
					assertEquals("Circle: Center-Y.",((Circle2D)(y)).getCenter().getY(), ((Circle2D)(x)).getCenter().getY(), 0.000005d);
					assertEquals("Circle: Radius.",((Circle2D)(y)).getRadius(), ((Circle2D)(x)).getRadius(), 0.000005d);
				}
				else if (x instanceof Ellipse2D && y instanceof Ellipse2D) {
					assertEquals("Ellipse: Center-X.",((Ellipse2D)(y)).getCenter().getX(), ((Ellipse2D)(x)).getCenter().getX(), 0.000005d);
					assertEquals("Ellipse: Center-Y.",((Ellipse2D)(y)).getCenter().getY(), ((Ellipse2D)(x)).getCenter().getY(), 0.000005d);
					assertEquals("Ellipse: Horizontal length.",((Ellipse2D)(y)).getHorizontalLength(), ((Ellipse2D)(x)).getHorizontalLength(), 0.000005d);
					assertEquals("Ellipse: Vertical length.",((Ellipse2D)(y)).getVerticalLength(), ((Ellipse2D)(x)).getVerticalLength(), 0.000005d);
					assertEquals("Ellipse: Rotation angle.",((Ellipse2D)(y)).getAngle(), ((Ellipse2D)(x)).getAngle(), 0.000005d);
				}
				else if (x instanceof Line2D && y instanceof Line2D) {
					assertEquals("Line: Start-X.",((Line2D)(y)).getStart().getX(), ((Line2D)(x)).getStart().getX(), 0.000005d);
					assertEquals("Line: Start-Y.",((Line2D)(y)).getStart().getY(), ((Line2D)(x)).getStart().getY(), 0.000005d);
					assertEquals("Line: End-X.",((Line2D)(y)).getEnd().getX(), ((Line2D)(x)).getEnd().getX(), 0.000005d);
					assertEquals("Line: End-Y.",((Line2D)(y)).getEnd().getY(), ((Line2D)(x)).getEnd().getY(), 0.000005d);
				}
				else if (x instanceof Polyline2D && y instanceof Polyline2D) {
					if (((Polyline2D)x).getVertexes().length != ((Polyline2D)x).getVertexes().length) fail();
					else {
						for (int k = 0; k < ((Polyline2D)x).getVertexes().length; k++){
							assertEquals("Polygon: Vertex-X", ((Polyline2D)x).getVertexes()[k].getX(), ((Polyline2D)y).getVertexes()[k].getX(), 0.000005d);
							assertEquals("Polygon: Vertex-Y", ((Polyline2D)x).getVertexes()[k].getY(), ((Polyline2D)y).getVertexes()[k].getY(), 0.000005d);
						}
					}
				}
				else if (x instanceof Rect2D && y instanceof Rect2D){
					assertEquals("Line: UpperLeft-X.",((Rect2D)(y)).getUpperLeft().getX(), ((Rect2D)(x)).getUpperLeft().getX(), 0.000005d);
					assertEquals("Line: UpperLeft-Y.",((Rect2D)(y)).getUpperLeft().getY(), ((Rect2D)(x)).getUpperLeft().getY(), 0.000005d);
					assertEquals("Line: LowerRight-X.",((Rect2D)(y)).getLowerRight().getX(), ((Rect2D)(x)).getLowerRight().getX(), 0.000005d);
					assertEquals("Line: LowerRight-Y.",((Rect2D)(y)).getLowerRight().getY(), ((Rect2D)(x)).getLowerRight().getY(), 0.000005d);
				}
			}
			assertFalse("Both groups are the same length and contain only legal shapes.", i.hasNext() || j.hasNext());
		} catch (XMLStreamException e) {
			e.printStackTrace();
			fail();
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail();
		}
	}
}
