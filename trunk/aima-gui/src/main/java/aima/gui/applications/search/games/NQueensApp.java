package aima.gui.applications.search.games;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.environment.nqueens.PlaceQueenAction;
import aima.core.environment.nqueens.QueensToBePlacedHeuristic;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.util.datastructure.XYLocation;
import aima.gui.applications.search.games.EightPuzzleApp.EightPuzzleFrame;
import aima.gui.applications.search.games.EightPuzzleApp.EightPuzzleView;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppEnvironmentView;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;
import aima.gui.framework.SimpleAgentApp;
import aima.gui.framework.SimulationThread;

/**
 * Simple graphical 8-puzzle game application. It demonstrates the performance
 * of different search algorithms. Manual search by the user is also supported.
 * 
 * @author R. Lunde
 */
public class NQueensApp extends SimpleAgentApp {

	/** List of supported search algorithm names. */
	protected static List<String> SEARCH_NAMES = new ArrayList<String>();
	/** List of supported search algorithms. */
	protected static List<Search> SEARCH_ALGOS = new ArrayList<Search>();

	/** Adds a new item to the list of supported search algorithms. */
	public static void addSearchAlgorithm(String name, Search algo) {
		SEARCH_NAMES.add(name);
		SEARCH_ALGOS.add(algo);
	}

	static {
		addSearchAlgorithm("Depth Limited Search (9)",
				new DepthLimitedSearch(9));
		addSearchAlgorithm("Breadth First Search (Tree Search)",
				new BreadthFirstSearch(new TreeSearch()));
		addSearchAlgorithm("Depth First Search (Tree Search)",
				new DepthFirstSearch(new TreeSearch()));
		addSearchAlgorithm("Iterative Deepening Search",
				new IterativeDeepeningSearch());
		addSearchAlgorithm("AStar Search (QueensToBePlacedHeuristic)",
				new AStarSearch(new GraphSearch(),
						new QueensToBePlacedHeuristic()));
		addSearchAlgorithm("Hill Climbing Search", new HillClimbingSearch(
				new QueensToBePlacedHeuristic()));
		addSearchAlgorithm("Simulated Annealing Search",
				new SimulatedAnnealingSearch(new QueensToBePlacedHeuristic()));
	}

	/** Returns a <code>NQueensView</code> instance. */
	public AgentAppEnvironmentView createEnvironmentView() {
		return new NQueensView();
	}

	/** Returns a <code>NQueensFrame</code> instance. */
	@Override
	public AgentAppFrame createFrame() {
		return new NQueensFrame();
	}

	/** Returns a <code>NQueensController</code> instance. */
	@Override
	public AgentAppController createController() {
		return new NQueensController();
	}

	// ///////////////////////////////////////////////////////////////
	// main method

	/**
	 * Starts the application.
	 */
	public static void main(String args[]) {
		new NQueensApp().startApplication();
	}

	// ///////////////////////////////////////////////////////////////
	// some inner classes

	/**
	 * Adds some selectors to the base class and adjusts its size.
	 */
	protected static class NQueensFrame extends AgentAppFrame {
		private static final long serialVersionUID = 1L;
		public static String ENV_SEL = "EnvSelection";
		public static String SEARCH_SEL = "SearchSelection";

		public NQueensFrame() {
			setEnvView(new EightPuzzleView());
			setSelectors(new String[] { ENV_SEL, SEARCH_SEL }, new String[] {
					"Select Environment", "Select Search" });
			setSelectorItems(ENV_SEL, new String[] { "8 Queens", "16 Queens" },
					0);
			setSelectorItems(SEARCH_SEL, (String[]) SEARCH_NAMES
					.toArray(new String[] {}), 0);
			setTitle("N-Queens Application");
			setSize(700, 500);
		}
	}

	/**
	 * Displays the informations provided by a
	 * <code>NQueensEnvironment</code> on a panel.
	 */
	protected static class NQueensView extends AgentAppEnvironmentView
			implements ActionListener {
		private static final long serialVersionUID = 1L;
		protected JButton[] squareButtons;
		protected int currSize = -1;

		protected NQueensView() {
		}

		@Override
		public void setEnvironment(Environment env) {
			super.setEnvironment(env);
			showState();
		}

		/** Agent value null indicates a user initiated action. */
		@Override
		public void agentActed(Agent agent, Action action,
				EnvironmentState resultingState) {
			showState();
			notify((agent == null ? "User: " : "") + action.toString());
		}

		@Override
		public void agentAdded(Agent agent, EnvironmentState resultingState) {
			showState();
		}

		/**
		 * Displays the board state by labeling and coloring the square buttons.
		 */
		protected void showState() {
			NQueensBoard board = ((NQueensEnvironment) env).getBoard();
			if (currSize != board.getSize()) {
				currSize = board.getSize();
				removeAll();
				setLayout(new GridLayout(currSize, currSize));
				Font f = new java.awt.Font(Font.SANS_SERIF, Font.PLAIN,
						260 / currSize);
				squareButtons = new JButton[currSize * currSize];
				for (int i = 0; i < currSize * currSize; i++) {
					JButton square = new JButton("");
					square.setFont(f);
					square.setMargin(new Insets(0, 0, 0, 0));
					square.setBackground((i % currSize) % 2 == (i / currSize) % 2
							? Color.WHITE : Color.LIGHT_GRAY);
					square.addActionListener(this);
					squareButtons[i] = square;
					add(square);
				}
			}
			for (int i = 0; i < currSize * currSize; i++)
				squareButtons[i].setText("");
			for (XYLocation loc : board.getQueenPositions()) {
				JButton square = squareButtons
				[loc.getXCoOrdinate() + loc.getYCoOrdinate() * currSize];
				square.setForeground
				(board.isSquareUnderAttack(loc) ? Color.RED : Color.BLACK);
				square.setText("Q");
			}
			validate();
		}

		/**
		 * When the user presses square buttons the board state is modified
		 * accordingly.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			for (int i = 0; i < currSize*currSize; i++) {
				if (ae.getSource() == squareButtons[i]) {
					NQueensController contr = (NQueensController) getController();
					XYLocation loc = new XYLocation(i % currSize, i / currSize);
					contr.modifySquare(loc);
				}
			}
		}
	}

	/**
	 * Defines how to react on standard simulation button events.
	 */
	protected static class NQueensController extends AgentAppController {

		protected NQueensEnvironment env = null;
		protected SearchAgent agent = null;
		protected boolean boardDirty;

		/** Prepares next simulation. */
		@Override
		public void clear() {
			prepare(null);
		}

		/**
		 * Creates an n-queens environment and clears the current search agent.
		 */
		@Override
		public void prepare(String changedSelector) {
			AgentAppFrame.SelectionState selState = frame.getSelection();
			NQueensBoard board = null;
			switch (selState.getValue(EightPuzzleFrame.ENV_SEL)) {
			case 0: // three moves
				board = new NQueensBoard(8);
				break;
			case 1: // three moves
				board = new NQueensBoard(16);
				break;
			}
			env = new NQueensEnvironment(board);
			boardDirty = false;
			agent = null;
			frame.getEnvView().setEnvironment(env);
		}

		/**
		 * Creates a new search agent and adds it to the current environment if
		 * necessary.
		 */
		protected void addAgent() {
			if (agent == null) {
				int sel = frame.getSelection().getValue(
						EightPuzzleFrame.SEARCH_SEL);
				Search search = SEARCH_ALGOS.get(sel);
				Problem problem = new Problem(env.getBoard(),
						NQueensFunctionFactory.getActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace(); // hack...
				}
				env.addAgent(agent);
			}
		}

		/** Checks whether simulation can be started. */
		@Override
		public boolean isPrepared() {
			return (agent == null || !agent.isDone())
			&& (!boardDirty || env.getBoard().getNumberOfQueensOnBoard() == 0);
		}

		/** Starts simulation. */
		@Override
		public void run(MessageLogger logger) {
			logger.log("<simulation-log>");
			addAgent();
			try {
				while (!agent.isDone() && !frame.simulationPaused()) {
					Thread.sleep(500);
					env.step();
				}
			} catch (InterruptedException e) {
			}
			logger.log(getStatistics());
			logger.log("</simulation-log>\n");
		}

		/** Executes one simulation step. */
		@Override
		public void step(MessageLogger logger) {
			addAgent();
			env.step();
		}

		/** Updates the status of the frame after simulation has finished. */
		public void update(SimulationThread simulationThread) {
			if (simulationThread.isCanceled()) {
				frame.setStatus("Task canceled.");
			} else if (frame.simulationPaused()) {
				frame.setStatus("Task paused.");
			} else {
				frame.setStatus("Task completed.");
			}
		}

		/** Provides a text with statistical information about the last run. */
		private String getStatistics() {
			StringBuffer result = new StringBuffer();
			Properties properties = agent.getInstrumentation();
			Iterator<Object> keys = properties.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String property = properties.getProperty(key);
				result.append("\n" + key + " : " + property);
			}
			return result.toString();
		}

		public void modifySquare(XYLocation loc) {
			boardDirty = true;
			env.executeAction(null, new PlaceQueenAction
					(loc.getXCoOrdinate(), loc.getYCoOrdinate()));
			agent = null;
			frame.updateEnabledState();
		}
	}

	/** Simple environment maintaining just the current board state. */
	protected static class NQueensEnvironment extends AbstractEnvironment {
		NQueensBoard board;

		protected NQueensEnvironment(NQueensBoard board) {
			this.board = board;
		}

		protected NQueensBoard getBoard() {
			return board;
		}

		/**
		 * Executes the provided action and returns null. A place-queen
		 * action adds a queen at the specified position if the position
		 * is free, otherwise removes the existing queen at the position.
		 */
		@Override
		public EnvironmentState executeAction(Agent agent, Action action) {
			if (action instanceof PlaceQueenAction) {
				PlaceQueenAction act = (PlaceQueenAction) action;
				XYLocation loc = new XYLocation(act.getX(), act.getY());
				if (board.queenExistsAt(loc))
					board.removeQueenFrom(loc);
				else
					board.addQueenAt(loc);
				if (agent == null)
					updateEnvironmentViewsAgentActed(agent, action, null);
			}
			return null;
		}

		/** Returns null. */
		@Override
		public EnvironmentState getCurrentState() {
			return null;
		}

		/** Returns null. */
		@Override
		public Percept getPerceptSeenBy(Agent anAgent) {
			return null;
		}
	}
}
