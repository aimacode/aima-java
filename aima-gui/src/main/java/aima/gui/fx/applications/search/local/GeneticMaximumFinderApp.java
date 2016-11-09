package aima.gui.fx.applications.search.local;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import aima.core.search.local.Individual;
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
 * Demonstrates, how the genetic algorithm can be used, to find maximums in
 * mathematical functions. Different parameter settings can be tried out and
 * progress shown for each iteration. 
 * 
 * @author Ruediger Lunde
 */
public class GeneticMaximumFinderApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public final static String PARAM_FUNC_SELECT = "funcSelect";
	public final static String PARAM_MUT_PROB = "mutProb";
	public final static String PARAM_POPULATION = "population";
	public final static String PARAM_MAX_ITER = "maxIter";
	
	protected FunctionPlotterCtrl funcPlotterCtrl;
	private SimulationPaneCtrl simPaneCtrl;
	private GeneticMaximumFinderDemo experiment;

	@Override
	public String getTitle() {
		return "Genetic Maxium Finder App";
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
		
		return root;
	}
	
	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_FUNC_SELECT);
		p1.setValues(Functions.f1, Functions.f2, Functions.f3);
		p1.setValueNames("f1", "f2", "f3");
		Parameter p2 = new Parameter(PARAM_MUT_PROB, 0.0, 0.2, 0.5, 1.0);
		p2.setDefaultValueIndex(1);
		Parameter p3 = new Parameter(PARAM_POPULATION, 2, 10, 20, 100);
		p3.setDefaultValueIndex(2);
		Parameter p4 = new Parameter(PARAM_MAX_ITER, 100, 200, 400);
		p4.setDefaultValueIndex(0);
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
		experiment = new GeneticMaximumFinderDemo();
		experiment.setFunction((Function<Double, Double>) simPaneCtrl.getParamValue(PARAM_FUNC_SELECT));
		experiment.setMutationProb(simPaneCtrl.getParamAsDouble(PARAM_MUT_PROB));
		experiment.setPopulationSize(simPaneCtrl.getParamAsInt(PARAM_POPULATION));
		experiment.setMaxIterations(simPaneCtrl.getParamAsInt(PARAM_MAX_ITER));
		experiment.startExperiment(this::updateStateView);
	}

	/**
	 * Caution: While the background thread should be slowed down, updates of
	 * the GUI have to be done in the GUI thread!
	 */
	private void updateStateView(int itCount, Collection<Individual<Double>> gen) {
		Platform.runLater(() -> updateStateViewLater(itCount, gen));
		simPaneCtrl.waitAfterStep();
	}

	/**
	 * Must be called by the GUI thread!
	 */
	private void updateStateViewLater(int itCount, Collection<Individual<Double>> gen) {
		funcPlotterCtrl.update();
		if (gen != null) {
			for (Individual<Double> ind : gen) {
				Optional<Paint> fill = Optional.empty();
				if (ind.getDescendants() > 0)
					fill = Optional.of(Color.rgb(Math.max(255 - ind.getDescendants() * 20, 0), 0, 0));
				else
					fill = Optional.of(Color.RED.brighter());
				double x = ind.getRepresentation().get(0);
				funcPlotterCtrl.setMarker(x, fill);
			}
			simPaneCtrl.setStatus(experiment.getIterationInfo(itCount, gen));
		} else {
			simPaneCtrl.setStatus("");
		}
	}
}
