package aima.gui.fx.applications.search;

import java.util.Arrays;
import java.util.List;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.SolutionStrategy;
import aima.core.search.csp.Variable;
import aima.core.search.csp.examples.NQueensCSP;
import aima.core.search.framework.Metrics;
import aima.core.util.datastructure.XYLocation;
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
 * Integrable application which demonstrates how different CSP solution
 * strategies solve the N-Queens problem.
 *
 * @author Ruediger Lunde
 *
 */
public class CspNQueensApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public final static String PARAM_STRATEGY = "strategy";
	public final static String PARAM_MRV = "mrv";
	public final static String PARAM_DEG = "deg";
	public final static String PARAM_AC3 = "ac3";
	public final static String PARAM_LCV = "lcv";

	public final static String PARAM_BOARD_SIZE = "boardSize";

	private NQueensViewCtrl stateViewCtrl;
	private SimulationPaneCtrl simPaneCtrl;
	CSP csp;
	SolutionStrategy solver;
	ProgressAnalyzer progressAnalyzer = new ProgressAnalyzer();

	@Override
	public String getTitle() {
		return "CSP N-Queens App";
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
		simPaneCtrl.setParam(SimulationPaneCtrl.PARAM_SIM_SPEED, 0);

		return root;
	}

	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_STRATEGY, "Min-Conflicts", "Backtracking");
		Parameter p2 = new Parameter(PARAM_MRV, true, false);
		Parameter p3 = new Parameter(PARAM_DEG, true, false);
		Parameter p4 = new Parameter(PARAM_AC3, true, false);
		Parameter p5 = new Parameter(PARAM_LCV, true, false);
		p2.setDependency(PARAM_STRATEGY, "Backtracking");
		p3.setDependency(PARAM_STRATEGY, "Backtracking");
		p4.setDependency(PARAM_STRATEGY, "Backtracking");
		p5.setDependency(PARAM_STRATEGY, "Backtracking");
		Parameter p6 = new Parameter(PARAM_BOARD_SIZE, 4, 8, 16, 32, 64);
		p6.setDefaultValueIndex(1);
		return Arrays.asList(p1, p2, p3, p4, p5, p6);
	}

	/** Displays the initialized board on the state view. */
	@Override
	public void initialize() {
		csp = new NQueensCSP(simPaneCtrl.getParamAsInt(PARAM_BOARD_SIZE));
		Object strategy = simPaneCtrl.getParamValue(PARAM_STRATEGY);
		if (strategy.equals("Min-Conflicts"))
			solver = new MinConflictsStrategy(1000);
		else if (strategy.equals("Backtracking")) {
			boolean mrv = (Boolean) simPaneCtrl.getParamValue(PARAM_MRV);
			boolean deg = (Boolean) simPaneCtrl.getParamValue(PARAM_DEG);
			boolean ac3 = (Boolean) simPaneCtrl.getParamValue(PARAM_AC3);
			boolean lcv = (Boolean) simPaneCtrl.getParamValue(PARAM_LCV);
			solver = new ImprovedBacktrackingStrategy(mrv, deg, ac3, lcv);
		}
		solver.addCSPStateListener(progressAnalyzer);
		progressAnalyzer.reset();
		stateViewCtrl.update(new NQueensBoard(csp.getVariables().size()));
		simPaneCtrl.setStatus("");
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}

	/** Starts the experiment. */
	public void simulate() {
		Assignment solution = solver.solve(csp);
		if (solution != null) {
			NQueensBoard board = getBoard(solution);
			stateViewCtrl.update(board);
		}
		simPaneCtrl.setStatus(progressAnalyzer.getResults().toString());
	}

	private NQueensBoard getBoard(Assignment assignment) {
		NQueensBoard board = new NQueensBoard(csp.getVariables().size());
		for (Variable var : assignment.getVariables()) {
			int col = Integer.parseInt(var.getName().substring(1)) - 1;
			int row = ((int) assignment.getAssignment(var)) - 1;
			board.addQueenAt(new XYLocation(col, row));
		}
		return board;
	}

	/**
	 * Caution: While the background thread should be slowed down, updates of
	 * the GUI have to be done in the GUI thread!
	 */
	private void updateStateView(NQueensBoard board) {
		Platform.runLater(() -> stateViewCtrl.update(board));
		simPaneCtrl.waitAfterStep();
	}

	protected class ProgressAnalyzer implements CSPStateListener {
		private int assignmentCount = 0;
		private int domainCount = 0;

		@Override
		public void stateChanged(Assignment assignment, CSP csp) {
			updateStateView(getBoard(assignment));
			++assignmentCount;
		}

		@Override
		public void stateChanged(CSP csp) {
			++domainCount;
		}

		public void reset() {
			assignmentCount = 0;
			domainCount = 0;
		}

		public Metrics getResults() {
			Metrics result = new Metrics();
			result.set("assignmentChanges", assignmentCount);
			if (domainCount != 0)
				result.set("domainChanges", domainCount);
			return result;
		}
	}
}
