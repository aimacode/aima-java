package aima.gui.fx.applications.search.local;

import java.util.*;
import java.util.function.Function;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
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
	private TaskExecutionPaneCtrl taskPaneCtrl;

	private Random random = new Random();
	private SimulatedAnnealingSearch<Double, Action> search;

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
		
		TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(canvas);
		builder.defineInitMethod(this::initialize);
		builder.defineTaskMethod(this::startExperiment);
		taskPaneCtrl = builder.getResultFor(root);
		taskPaneCtrl.setParam(TaskExecutionPaneCtrl.PARAM_EXEC_SPEED, 1);
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
		funcPlotterCtrl.setFunction((Function<Double, Double>) taskPaneCtrl.getParamValue(PARAM_FUNC_SELECT));
	}

	@Override
	public void cleanup() {
		taskPaneCtrl.cancelExecution();
	}

	/** Starts the experiment. */
	@SuppressWarnings("unchecked")
	public void startExperiment() {

		List<Action> actions = new ArrayList<>(1);
		actions.add(new DynamicAction("Move"));
		Problem<Double, Action> problem = new GeneralProblem<>(getRandomState(), s -> actions, (s, a) -> getSuccessor(s), s -> false);
		Function<Double, Double> func = (Function<Double, Double>) taskPaneCtrl.getParamValue(PARAM_FUNC_SELECT);
		Scheduler scheduler = new Scheduler(taskPaneCtrl.getParamAsInt(PARAM_K),
				taskPaneCtrl.getParamAsDouble(PARAM_LAMBDA), taskPaneCtrl.getParamAsInt(PARAM_MAX_ITER));
		search = new SimulatedAnnealingSearch<>(n -> 1 - func.apply(n.getState()), scheduler);
		search.addNodeListener(n -> updateStateView(n.getState()));
		search.findActions(problem);
		updateStateView(search.getLastState());
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
	protected Double getSuccessor(Double state) {
		double result =  state;
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
		taskPaneCtrl.waitAfterStep();
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
			taskPaneCtrl.setStatus(search.getMetrics().toString());
		} else {
			taskPaneCtrl.setStatus("");
		}
	}
}
