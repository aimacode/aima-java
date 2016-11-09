package aima.gui.fx.applications.search;

import aima.core.environment.nqueens.AttackingPairsHeuristic;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
import aima.gui.fx.views.NQueensViewCtrl;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;


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
	private NQueensSearchDemo experiment;

	public NQueensSearchApp() {
		experiment = new NQueensSearchDemo();
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

		List<Parameter> params = createParameters();

		SimulationPaneBuilder builder = new SimulationPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(stateView);
		builder.defineInitMethod(this::initialize);
		builder.defineSimMethod(this::simulate);
		simPaneCtrl = builder.getResultFor(root);
		simPaneCtrl.setParam(SimulationPaneCtrl.PARAM_SIM_SPEED, 1);

		return root;
	}

	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_STRATEGY,
				"Depth-First Search (incremental)", "Breadth-First Search (incremental)",
				"Iterative Deepening Search",
				"Greedy Best-First Search (attacking pair heuristic)", "A* search (attacking pair heuristic)",
				"Hill Climbing", "Simulated Annealing", "Genetic Algorithm");
		Parameter p2 = new Parameter(PARAM_BOARD_SIZE, 4, 8, 16, 32, 64);
		p2.setDefaultValueIndex(1);
		Parameter p3 = new Parameter(PARAM_INIT_CONFIG, "FirstRow", "Random");
		p3.setDependency(PARAM_STRATEGY, "Iterative Deepening Search",
				"Greedy Best-First Search (attacking pair heuristic)", "A* search (attacking pair heuristic)",
				"Hill Climbing", "Simulated Annealing", "Genetic Algorithm");
		return Arrays.asList(p1, p2, p3);
	}

	/** Displays the initialized board on the state view. */
	@Override
	public void initialize() {
		experiment.setBoardSize(simPaneCtrl.getParamAsInt(PARAM_BOARD_SIZE));
		Object strategy = simPaneCtrl.getParamValue(PARAM_STRATEGY);
		Config config;
		if (Arrays.asList("Depth-First Search (incremental)", "Breadth-First Search (incremental)", "Genetic Algorithm")
                .contains(strategy))
			config = Config.EMPTY;
		else if (simPaneCtrl.getParamValue(PARAM_INIT_CONFIG).equals("Random"))
			config = Config.QUEEN_IN_EVERY_COL;
		else
			config = Config.QUEENS_IN_FIRST_ROW;
		experiment.initExperiment(config);
		stateViewCtrl.update(experiment.getBoard());
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}

	/** Starts the experiment. */
	public void simulate() {
		Object strategy = simPaneCtrl.getParamValue(PARAM_STRATEGY);
		if (strategy.equals("Depth-First Search (incremental)"))
			experiment.startExperiment(new DepthFirstSearch(new TreeSearch()));
		else if (strategy.equals("Breadth-First Search (incremental)"))
			experiment.startExperiment(new BreadthFirstSearch(new TreeSearch()));
		else if (strategy.equals("Iterative Deepening Search"))
			experiment.startExperiment(new IterativeDeepeningSearch());
		else if (strategy.equals("Greedy Best-First Search (attacking pair heuristic)"))
			experiment.startExperiment(new GreedyBestFirstSearch(new GraphSearch(), new AttackingPairsHeuristic()));
        else if (strategy.equals("A* search (attacking pair heuristic)"))
            experiment.startExperiment(new AStarSearch(new GraphSearch(), new AttackingPairsHeuristic()));
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
		stateViewCtrl.update(board);
		simPaneCtrl.setStatus(metrics.toString());
	}
}
