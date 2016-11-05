package aimax.osm.gui.fx.viewer;

import aimax.osm.viewer.UColor;
import aimax.osm.viewer.UnifiedImageBuilder;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;

/**
 * Specialized image builder for the FX canvas.
 * 
 * @author Ruediger Lunde
 */
public class FXImageBuilder implements UnifiedImageBuilder<Canvas> {

	private Canvas result;
	private GraphicsContext gc;
	boolean areaFillMode;

	@Override
	public void initialize(Canvas canvas) {
		result = canvas;
		gc = canvas.getGraphicsContext2D();
		initStroke();
	}

	/** Returns the width of the image under construction. */
	@Override
	public int getWidth() { return (int) result.getWidth(); }

	/** Returns the height of the image under construction. */
	@Override
	public int getHeight() {
		return (int) result.getHeight();
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		gc.strokeLine(x1, y1, x2, y2);
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		initStroke();
		if (areaFillMode)
			gc.fillRect(x, y, width, height);
		else
			gc.strokeRect(x, y, width, height);

	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		initStroke();
		if (areaFillMode)
			gc.fillOval(x, y, width, height);
		else
			gc.strokeOval(x, y, width, height);

	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		double[] xPts = new double[nPoints];
		double[] yPts = new double[nPoints];
		for (int i = 0; i < nPoints; i++) {
			xPts[i] = xPoints[i];
			yPts[i] = yPoints[i];
		}
		initStroke();
		gc.strokePolyline(xPts, yPts, nPoints);

	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		double[] xPts = new double[nPoints];
		double[] yPts = new double[nPoints];
		for (int i = 0; i < nPoints; i++) {
			xPts[i] = xPoints[i];
			yPts[i] = yPoints[i];
		}
		initStroke();
		if (areaFillMode)
			gc.fillPolygon(xPts, yPts, nPoints);
		else
			gc.strokePolygon(xPts, yPts, nPoints);

	}

	@Override
	public void drawString(String text, int x, int y) {
		gc.fillText(text, x, y);

	}

	@Override
	public Canvas getResult() {
		return result;
	}

	@Override
	public void setColor(UColor color) {
		gc.setStroke(Color.rgb(color.getRed(), color.getGreen(),
				color.getBlue(), color.getAlpha() / 255.0));
		gc.setFill(Color.rgb(color.getRed(), color.getGreen(),
				color.getBlue(), color.getAlpha() / 255.0));
	}

	@Override
	public void setLineStyle(boolean dashed, float width) {
		gc.setLineWidth(width);
		gc.setLineDashes(new double[] {width * 2, width * 2});
		gc.setLineJoin(StrokeLineJoin.ROUND);
	}

	@Override
	public void setAreaFilled(boolean value) {
		areaFillMode = value;

	}

	@Override
	public void setFontSize(float size) {
		gc.setFont(new Font(gc.getFont().getName(), size));
	}

	@Override
	public float getFontSize() { return (float) gc.getFont().getSize();
	}

	private void initStroke() {
		//gc.setLineWidth(1);
		gc.setLineDashes(null);
		gc.setLineJoin(StrokeLineJoin.ROUND);
	}
}
