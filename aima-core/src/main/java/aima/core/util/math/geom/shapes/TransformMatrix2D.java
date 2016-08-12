package aima.core.util.math.geom.shapes;

import aima.core.util.Util;

/**
 * Implements a transformation matrix for two-dimensional geometry.<br/>
 * This is based on the svg standard, but does not implement the {@code skewX} and {@code skewY} operations.
 * See <a href="https://www.w3.org/TR/SVG/coords.html#TransformMatrixDefined">w3c&reg; SVG TransformMatrix definition</a> for more information about these matrices.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 */
public class TransformMatrix2D {

	/**
	 * This is the unity/identity matrix:<br/>
	 * <code>[ 1 0 0 ]</code><br/>
	 * <code>[ 0 0 0 ]</code><br/>
	 * <code>[ 0 0 1 ]</code><br/>
	 */
	public static final TransformMatrix2D UNITY_MATRIX = new TransformMatrix2D(1.0d,0.0d,0.0d,1.0d,0.0d,0.0d);
	
	private final double a, b, c, d, e, f;
	
	/**
	 * Creates a new transformation matrix according to the delivered parameters in the form:<br/>
	 * <code>[ a c e ]</code><br/>
	 * <code>[ b d f ]</code><br/>
	 * <code>[ 0 0 1 ]</code><br/>
	 */
	@SuppressWarnings("javadoc")
	private TransformMatrix2D(double a, double b, double c, double d, double e, double f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}
	
	/**
	 * Produces a transformation matrix representing a translation operation.
	 * @param x the X element of the translate.
	 * @param y the Y element of the translate.
	 * @return the new transform matrix.
	 */
	public static TransformMatrix2D translate(double x, double y) {
		return new TransformMatrix2D(1.0d,0.0d,0.0d,1.0d,x,y);
	}
	
	/**
	 * Produces a transformation matrix representing a scaling operation.
	 * @param x the X element of the scale.
	 * @param y the Y element of the scale.
	 * @return the new transform matrix.
	 */
	public static TransformMatrix2D scale(double x, double y) {
		return new TransformMatrix2D(x,0.0d,0.0d,y,0.0d,0.0d);
	}
	
	/**
	 * Produces a transformation matrix representing a rotation operation around the origin of the coordinate system.
	 * @param alpha the angle of the rotation in radians.
	 * @return the new transform matrix.
	 */
	public static TransformMatrix2D rotate(double alpha) {
		final double sin = Math.sin(alpha);
		final double cos = Math.cos(alpha);
		return new TransformMatrix2D(cos,sin,-sin,cos,0.0d,0.0d);
	}
	
	/**
	 * Multiplies this matrix with another transformation matrix.
	 * @param matrix the other matrix to be multiplied with.
	 * @return The new transform matrix.
	 */
	public TransformMatrix2D multiply(TransformMatrix2D matrix) {
		return new TransformMatrix2D(a*matrix.a+c*matrix.b,b*matrix.a+d*matrix.b,a*matrix.c+c*matrix.d,b*matrix.c+d*matrix.d,a*matrix.e+c*matrix.f+e,b*matrix.e+d*matrix.f+f);
	}
	
	/**
	 * Calculates the determinant of this transformation matrix.
	 * @return the determinant.
	 */
	public double determinant() {
		return a*d-b*c;
	}
	
	/**
	 * Calculates the inverse of this transformation matrix.
	 * See <a href="http://mathworld.wolfram.com/MatrixInverse.html">Wolfram mathworld</a> for more information.
	 * @return The new transform matrix.
	 */
	public TransformMatrix2D inverse() {
		if(this == UNITY_MATRIX) return UNITY_MATRIX;
		final double determinant = determinant();
		if(determinant == 0.0d) return null;
		return new TransformMatrix2D(d/determinant,-b/determinant,-c/determinant,a/determinant,(c*f-d*e)/determinant,(b*e-a*f)/determinant);
	}
	
	/**
	 * Multiplies this transformation matrix with a given point.<br/>
	 * For a multiplication in two-dimensional Cartesian space the third field is set to 1:
	 * <pre><code> [ x_new ]   [ a c e ]   [ x_old ]
	 * [ y_new ] = [ b d f ] * [ y_old ]
	 * [   1   ]   [ 0 0 1 ]   [   1   ]</code></pre>
	 * @param point the {@link Point2D} to be transformed by this matrix.
	 * @return the new transformed point.
	 */
	public Point2D multiply(Point2D point) {
		final double xNew = point.getX()*a+point.getY()*c+e,
					 yNew = point.getX()*b+point.getY()*d+f;
		return new Point2D(xNew,yNew);
	}

	/**
	 * Compares this matrix to another transformation matrix.
	 * 
	 * @param op2 the {@link TransformMatrix2D} to be compared to this matrix.
	 * @return true if both matrices are identical.
	 */
	public boolean equals(TransformMatrix2D op2) {
		if(op2 == null) return false;
		return Util.compareDoubles(this.a,op2.a) && Util.compareDoubles(this.b,op2.b) && Util.compareDoubles(this.c,op2.c) && Util.compareDoubles(this.d,op2.d) && Util.compareDoubles(this.e,op2.e) && Util.compareDoubles(this.f,op2.f);
	}
	
	/**
	 * Compares this matrix to another object.
	 * 
	 * @param o the object to be compared to this matrix.
	 * @return true if the object is identical to this matrix.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof TransformMatrix2D)
			return this.equals((TransformMatrix2D) o);
		return false;
	}
}
