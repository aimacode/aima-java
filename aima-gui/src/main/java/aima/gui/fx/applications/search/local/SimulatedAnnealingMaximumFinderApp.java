package aima.gui.fx.applications.search.local;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.search.framework.problem.Problem;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
import aima.gui.fx.views.FunctionPlotterCtrl;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


/**
 * Demonstrates, how simulated annealing can be used, to find maximums in
 * mathematical functions. Different parameter settings can be tried out and
 * progress shown for each iteration.
 * 
 * @author Ruediger Lunde
 */
public class SimulatedAnnealingMaximumFinderApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public final static String PARAM_FUNC_SELECT = "funcSelect";
	public final static String PARAM_K = "k";
	public final static String PARAM_LAMBDA = "lambda";
	public final static String PARAM_MAX_ITER = "maxIter";

	protected FunctionPlotterCtrl funcPlotterCtrl;
	private SimulationPaneCtrl simPaneCtrl;

	private Random random = new Random();
	private SimulatedAnnealingSearch search;

	@Override
	public String getTitle() {
		return "Simulated Annealing Maximum Finder App";
	}

	/**
	 * Defines state view, parameters, and call-back functions and calls the
	 * simulation pane builder to create layout and controller objects.
	 */
	@Override
	public Pane createRootPane() {
		BorderPane root = new BorderPane();

		Canvas canvas = new Canvas();
		funcPlotterCtrl = new FunctionPlotterCtrl(canvas);
		funcPlotterCtrl.setLimits(Functions.minX, Functions.maxX, Functions.minY, Functions.maxY);
		List<Parameter> params = createParameters();
		
		SimulationPaneBuilder builder = new SimulationPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(canvas);
		builder.defineInitMethod(this::initialize);
		builder.defineSimMethod(this::simulate);
		simPaneCtrl = builder.getResultFor(root);
		simPaneCtrl.setParam(SimulationPaneCtrl.PARAM_SIM_SPEED, 1);
		return root;
	}

	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_FUNC_SELECT);
		p1.setValues(Functions.f1, Functions.f2, Functions.f3);
		p1.setValueNames("f1", "f2", "f3");
		Parameter p2 = new Parameter(PARAM_K, 1, 20, 100);
		p2.setDefaultValueIndex(1);
		Parameter p3 = new Parameter(PARAM_LAMBDA, 0.01, 0.05, 0.1, 0.5);
		p3.setDefaultValueIndex(1);
		Parameter p4 = new Parameter(PARAM_MAX_ITER, 100, 500, 1000);
		p4.setDefaultValueIndex(1);
		return Arrays.asList(p1, p2, p3, p4);
	}
	
	
	
	/** Displays the selected function on the state view. */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		funcPlotterCtrl.setFunction((Function<Double, Double>) simPaneCtrl.getParamValue(PARAM_FUNC_SELECT));
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}

	/** Starts the experiment. */
	@SuppressWarnings("unchecked")
	public void simulate() {

		Set<Action> actions = new HashSet<>();
		actions.add(new DynamicAction("Move"));
		Problem problem = new Problem(getRandomState(), s -> actions, (s, a) -> getSuccessor(s), s -> false);
		Function<Double, Double> func = (Function<Double, Double>) simPaneCtrl.getParamValue(PARAM_FUNC_SELECT);
		Scheduler scheduler = new Scheduler(simPaneCtrl.getParamAsInt(PARAM_K),
				simPaneCtrl.getParamAsDouble(PARAM_LAMBDA), simPaneCtrl.getParamAsInt(PARAM_MAX_ITER));
		search = new SimulatedAnnealingSearch(s -> 1 - func.apply((Double) s), scheduler);
		search.getNodeExpander().addNodeListener(n -> updateStateView(n.getState()));
		search.findActions(problem);
		updateStateView(search.getLastSearchState());
	}

	/** Creates a random initial state for the maximum search problem. */
	private Double getRandomState() {
		return random.nextDouble() * (Functions.maxX - Functions.minX) + Functions.minX;
	}

	/**
	 * Successor function for maximum search problem. Just one successor is
	 * presented which corresponds to one mutation step in the genetic algorithm
	 * for numbers.
	 */
	protected Double getSuccessor(Object state) {
		double result = (Double) state;
		double r = random.nextDouble() - 0.5;
		result += r * r * r * (Functions.maxX - Functions.minX) / 2;
		if (result < Functions.minX)
			result = Functions.minX;
		else if (result > Functions.maxX)
			result = Functions.maxX;
		// result += (random.nextInt(3) - 1) * funcPlotter.getDeltaX();
		return result;
	}

	/**
	 * Caution: While the background thread should be slowed down, updates of
	 * the GUI have to be done in the GUI thread!
	 */
	private void updateStateView(Object state) {
		Platform.runLater(() -> updateStateViewLater(state));
		simPaneCtrl.waitAfterStep();
	}

	/**
	 * Must be called by the GUI thread!
	 */
	private void updateStateViewLater(Object state) {
		funcPlotterCtrl.update();
		if (state instanceof Double) {
			double temp = search.getMetrics().getDouble(SimulatedAnnealingSearch.METRIC_TEMPERATURE);
			Paint fill = Color.RED;
			if (temp < 1)
				fill = Color.rgb((int) (255 * temp), 0, (int) (255 * (1 - temp)));
			funcPlotterCtrl.setMarker((Double) state, Optional.of(fill));
			simPaneCtrl.setStatus(search.getMetrics().toString());
		} else {
			simPaneCtrl.setStatus("");
		}
	}
}
