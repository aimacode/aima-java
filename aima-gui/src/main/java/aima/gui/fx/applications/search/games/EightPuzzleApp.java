package aima.gui.fx.applications.search.games;

import aima.core.agent.Action;
import aima.core.environment.eightpuzzle.BidirectionalEightPuzzleProblem;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.environment.eightpuzzle.EightPuzzleFunctions;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Node;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.BidirectionalSearch;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.util.Tasks;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
import aima.gui.fx.views.EightPuzzleViewCtrl;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.*;


/**
 * Integrable application which demonstrates how different search strategies
 * solve the Eight Puzzle problem.
 *
 * @author Ruediger Lunde
 *
 */
public class EightPuzzleApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	/** List of supported search algorithm names. */
	protected static List<String> SEARCH_NAMES = new ArrayList<>();
	/** List of supported search algorithms. */
	protected static List<SearchForActions<EightPuzzleBoard, Action>> SEARCH_ALGOS = new ArrayList<>();

	private EightPuzzleBoard board;

	/** Adds a new item to the list of supported search algorithms. */
	public static void addSearchAlgorithm(String name, SearchForActions<EightPuzzleBoard, Action> algo) {
		SEARCH_NAMES.add(name);
		SEARCH_ALGOS.add(algo);
	}

	static {
		addSearchAlgorithm("Breadth First Search (Graph Search)", new BreadthFirstSearch<>(new GraphSearch<>()));
		addSearchAlgorithm("Breadth First Search (Bidirectional Search)",
				new BreadthFirstSearch<>(new BidirectionalSearch<>()));
		addSearchAlgorithm("Depth Limited Search (9)", new DepthLimitedSearch<>(9));
		addSearchAlgorithm("Iterative Deepening Search", new IterativeDeepeningSearch<>());
		addSearchAlgorithm("Greedy Best First Search (MisplacedTileHeuristic)",
				new GreedyBestFirstSearch<>(new GraphSearch<>(),
						EightPuzzleFunctions.createMisplacedTileHeuristicFunction()));
		addSearchAlgorithm("Greedy Best First Search (ManhattanHeuristic)",
				new GreedyBestFirstSearch<>(new GraphSearch<>(),
						EightPuzzleFunctions.createManhattanHeuristicFunction()));
		addSearchAlgorithm("AStar Search (MisplacedTileHeuristic)",
				new AStarSearch<>(new GraphSearch<>(), EightPuzzleFunctions.createMisplacedTileHeuristicFunction()));
		addSearchAlgorithm("AStar Search (ManhattanHeuristic)",
				new AStarSearch<>(new GraphSearch<>(), EightPuzzleFunctions.createManhattanHeuristicFunction()));
		addSearchAlgorithm("Simulated Annealing Search (ManhattanHeuristic)",
				new SimulatedAnnealingSearch<>(EightPuzzleFunctions.createManhattanHeuristicFunction(),
						new Scheduler(20, 0.05, 200)));
	}


	public final static String PARAM_INIT_CONF = "initConf";
	public final static String PARAM_STRATEGY = "strategy";

	private EightPuzzleViewCtrl stateViewCtrl;
	private TaskExecutionPaneCtrl simPaneCtrl;

	public EightPuzzleApp() {
	}

	@Override
	public String getTitle() {
		return "Eight Puzzle App";
	}

	/**
	 * Defines state view, parameters, and call-back functions and calls the
	 * simulation pane builder to create layout and controller objects.
	 */
	@Override
	public Pane createRootPane() {
		BorderPane root = new BorderPane();

		StackPane stateView = new StackPane();
		stateViewCtrl = new EightPuzzleViewCtrl(stateView);

		List<Parameter> params = createParameters();

		TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(stateView);
		builder.defineInitMethod(this::initialize);
		builder.defineTaskMethod(this::startExperiment);
		simPaneCtrl = builder.getResultFor(root);

		return root;
	}

	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_INIT_CONF, "Three Moves", "Medium", "Extreme", "Random");
		Parameter p2 = new Parameter(PARAM_STRATEGY, (Object[]) SEARCH_NAMES.toArray());
		return Arrays.asList(p1, p2);
	}

	/** Displays the initialized board on the state view. */
	@Override
	public void initialize() {
		board = null;
		switch (simPaneCtrl.getParamValueIndex(PARAM_INIT_CONF)) {
			case 0: // three moves
				board = new EightPuzzleBoard(new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8 });
				break;
			case 1: // medium
				board = new EightPuzzleBoard(new int[] { 1, 4, 2, 7, 5, 8, 3, 0, 6 });
				break;
			case 2: // extreme
				board = new EightPuzzleBoard(new int[] { 0, 8, 7, 6, 5, 4, 3, 2, 1 });
				break;
			case 3: // random
				board = new EightPuzzleBoard(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });
				Random r = new Random(System.currentTimeMillis());
				for (int i = 0; i < 200; i++) {
					switch (r.nextInt(4)) {
						case 0:
							board.moveGapUp();
							break;
						case 1:
							board.moveGapDown();
							break;
						case 2:
							board.moveGapLeft();
							break;
						case 3:
							board.moveGapRight();
							break;
					}
				}
		}
		stateViewCtrl.initialize(board);
	}

	@Override
	public void cleanup() {
		simPaneCtrl.cancelExecution();
	}

	/** Starts the experiment. */
	public void startExperiment() {
		int strategyIdx = simPaneCtrl.getParamValueIndex(PARAM_STRATEGY);

		Problem<EightPuzzleBoard, Action> problem = new BidirectionalEightPuzzleProblem(board);
		SearchForActions<EightPuzzleBoard, Action> search = SEARCH_ALGOS.get(strategyIdx);
		Optional<List<Action>> actions = search.findActions(problem);
		if (actions.isPresent()) {
			for (Action action : actions.get()) {
				if (action == EightPuzzleBoard.UP)
					board.moveGapUp();
				else if (action == EightPuzzleBoard.DOWN)
					board.moveGapDown();
				else if (action == EightPuzzleBoard.LEFT)
					board.moveGapLeft();
				else if (action == EightPuzzleBoard.RIGHT)
					board.moveGapRight();
				Metrics m = new Metrics();
				m.set("manhattanHeuristic", EightPuzzleFunctions.createManhattanHeuristicFunction().applyAsDouble
						(new Node<>(board)));
				updateStateView(m);
				if (Tasks.currIsCancelled())
					break;
				simPaneCtrl.waitAfterStep();
			}
		}
		updateStateView(search.getMetrics());
	}

	/**
	 * Caution: While the background thread should be slowed down, updates of
	 * the GUI have to be done in the GUI thread!
	 */
	private void updateStateView(Metrics metrics) {
		Platform.runLater(() -> updateStateViewLater(metrics));
		simPaneCtrl.waitAfterStep();
	}

	/**
	 * Must be called by the GUI thread!
	 */
	private void updateStateViewLater(Metrics metrics) {
		stateViewCtrl.update();
		if (metrics != null)
			simPaneCtrl.setStatus(metrics.toString());
	}
}
