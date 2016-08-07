package aima.gui.fx.demo.search;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
import aima.gui.fx.views.NQueensViewCtrl;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


/**
 * Integrable application which demonstrates how different search strategies
 * solve the N-Queens problem.
 * 
 * @author Ruediger Lunde
 *
 */
public class NQueensSearchApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public final static String PARAM_STRATEGY = "strategy";
	public final static String PARAM_BOARD_SIZE = "boardSize";
	public final static String PARAM_INIT_CONFIG = "initConfig";

	private NQueensViewCtrl stateViewCtrl;
	private SimulationPaneCtrl simPaneCtrl;
	private NQueensSearchProg experiment;

	public NQueensSearchApp() {
		experiment = new NQueensSearchProg();
		experiment.addProgressTracer(this::updateStateView);
	}

	@Override
	public String getTitle() {
		return "N-Queens Search App";
	}

	/**
	 * Defines state view, parameters, and call-back functions and calls the
	 * simulation pane builder to create layout and controller objects.
	 */
	@Override
	public Pane createRootPane() {
		BorderPane root = new BorderPane();

		StackPane stateView = new StackPane();
		stateViewCtrl = new NQueensViewCtrl(stateView);

		Parameter[] params = createParameters();

		SimulationPaneBuilder builder = new SimulationPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(stateView);
		builder.defineInitMethod(this::initialize);
		builder.defineSimMethod(this::simulate);
		simPaneCtrl = builder.getResultFor(root);

		return root;
	}
	
	protected Parameter[] createParameters() {
		Parameter p1 = new Parameter(PARAM_STRATEGY, "Depth-First Search", "Hill Climbing", "Simulated Annealing",
				"Genetic Algorithm");
		Parameter p2 = new Parameter(PARAM_BOARD_SIZE, 8, 16, 32, 64);
		Parameter p3 = new Parameter(PARAM_INIT_CONFIG, "FirstRow", "Random");
		return new Parameter[] {p1, p2, p3};
	}

	/** Displays the selected function on the state view. */
	@Override
	public void initialize() {
		experiment.setBoardSize(simPaneCtrl.getParamAsInt(PARAM_BOARD_SIZE));
		Object strategy = simPaneCtrl.getParamValue(PARAM_STRATEGY);
		Config config;
		if (strategy.equals("Depth-First Search") || strategy.equals("Genetic Algorithm"))
			config = Config.EMPTY;
		else if (simPaneCtrl.getParamValue(PARAM_INIT_CONFIG).equals("Random"))
			config = Config.QUEEN_IN_EVERY_COL;
		else
			config = Config.QUEENS_IN_FIRST_ROW;
		experiment.initExperiment(config);
		stateViewCtrl.updateBoard(experiment.getBoard());
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}

	/** Starts the experiment. */
	public void simulate() {
		Object strategy = simPaneCtrl.getParamValue(PARAM_STRATEGY);
		if (strategy.equals("Depth-First Search"))
			experiment.startExperiment(new DepthFirstSearch(new TreeSearch()));
		else if (strategy.equals("Hill Climbing"))
			experiment.startHillClimbingExperiment();
		else if (strategy.equals("Simulated Annealing"))
			experiment.startSimulatedAnnealingExperiment();
		else if (strategy.equals("Genetic Algorithm"))
			experiment.startGenAlgoExperiment(simPaneCtrl.getParamValue(PARAM_INIT_CONFIG).equals("Random"));
	}

	/**
	 * Caution: While the background thread should be slowed down, updates of
	 * the GUI have to be done in the GUI thread!
	 */
	private void updateStateView(NQueensBoard board, Metrics metrics) {
		Platform.runLater(() -> updateStateViewLater(board, metrics));
		simPaneCtrl.waitAfterStep();
	}

	/**
	 * Must be called by the GUI thread!
	 */
	private void updateStateViewLater(NQueensBoard board, Metrics metrics) {
		stateViewCtrl.updateBoard(board);
		simPaneCtrl.setStatus(metrics.toString());
	}
}
