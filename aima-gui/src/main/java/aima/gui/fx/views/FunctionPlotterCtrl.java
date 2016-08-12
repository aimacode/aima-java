package aima.gui.fx.views;

import java.util.Optional;
import java.util.function.Function;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Controller class which provides functionality for using a canvas as function
 * plotter.
 * 
 * @author Ruediger Lunde
 */
public class FunctionPlotterCtrl {
	private Canvas canvas;
	private Optional<Function<Double, Double>> function = Optional.empty();
	private double minX;
	private double maxX = 1;
	private double minY;
	private double maxY = 1;

	public FunctionPlotterCtrl(Canvas canvas) {
		this.canvas = canvas;
		canvas.widthProperty().addListener((obs, o, n) -> update());
		canvas.heightProperty().addListener((obs, o, n) -> update());
	}

	public Optional<Function<Double, Double>> getFunction() {
		return function;
	}

	public void setFunction(Function<Double, Double> function) {
		this.function = Optional.of(function);
		update();
	}

	public void setLimits(double minX, double maxX, double minY, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;

	}

	public void update() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		gc.setStroke(Color.GRAY);
		gc.strokeLine(0.01 * canvas.getWidth(), yToScreen(0), canvas.getWidth() * 0.99, yToScreen(0));
		gc.strokeLine(xToScreen(0), 0.01 * canvas.getHeight(), xToScreen(0), 0.99 * canvas.getHeight());
		gc.strokeLine(xToScreen(1), yToScreen(0) - 5, xToScreen(1), yToScreen(0) + 5);
		gc.strokeLine(xToScreen(0) - 5, yToScreen(1), xToScreen(0) + 5, yToScreen(1));

		if (function.isPresent()) {
			gc.setStroke(Color.BLACK);
			double lastVal = function.get().apply(minX);
			for (int i = 1; i < canvas.getWidth(); i++) {
				double newVal = function.get().apply(screenToX(i));
				gc.strokeLine(i - 1, yToScreen(lastVal), i, yToScreen(newVal));
				lastVal = newVal;
			}
		}
	}

	public void setMarker(double x, Optional<Paint> fill) {
		if (function.isPresent()) {
			double y = function.get().apply(x);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.setStroke(Color.RED);
			if (fill.isPresent()) {
				gc.setFill(fill.get());
				gc.fillOval(xToScreen(x) - 10, yToScreen(y) - 10, 20, 20);
			} else
				gc.strokeOval(xToScreen(x) - 10, yToScreen(y) - 10, 20, 20);
		}
	}

	/* Returns pixel width. */
	public double getDeltaX() {
		return (maxX - minX) / canvas.getWidth();
	}

	private double yToScreen(double y) {
		return (1.1 - (y - minY) / (maxY - minY)) * canvas.getHeight() * 0.8;
	}

	private double screenToX(double xScreen) {
		return xScreen / canvas.getWidth() * (maxX - minX) + minX;
	}

	private double xToScreen(double x) {
		return (x - minX) * canvas.getWidth() / (maxX - minX);
	}
}
