package aima.gui.fx.applications.search.local;

import java.util.function.Function;

/**
 * Defines functions for local search experiments and a bounding box to limit
 * the search space and support visualization.
 * 
 * @author Ruediger Lunde
 */
public class Functions {
	public static double minX = -10;
	public static double maxX = 10;
	public static double minY = 0;
	public static double maxY = 1;

	public static Function<Double, Double> f1 = num -> (Math.sin(num) + 1) / 2;
	public static Function<Double, Double> f2 = num -> (Math.sin(num * 0.1) + 1) / 2;
	public static Function<Double, Double> f3 = num -> ((Math.sin(num - 2) / (Math.abs(num - 2) + 1) + 1) / 2);
}
