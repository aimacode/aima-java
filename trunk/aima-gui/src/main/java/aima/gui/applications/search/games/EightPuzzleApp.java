package aima.gui.applications.search.games;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JButton;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.environment.eightpuzzle.EightPuzzleFunctionFactory;
import aima.core.environment.eightpuzzle.EightPuzzleGoalTest;
import aima.core.environment.eightpuzzle.ManhattanHeuristicFunction;
import aima.core.environment.eightpuzzle.MisplacedTilleHeuristicFunction;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.util.datastructure.XYLocation;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppEnvironmentView;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;
import aima.gui.framework.SimpleAgentApp;
import aima.gui.framework.SimulationThread;

/**
 * Simple graphical 8-puzzle game application. It demonstrates the performance
 * of different search strategies.
 * 
 * @author R. Lunde
 */
public class EightPuzzleApp extends SimpleAgentApp {

	/** Returns a <code>EightPuzzleView</code> instance. */
	public AgentAppEnvironmentView createEnvironmentView() {
		return new EightPuzzleView();
	}

	/** Returns a <code>EightPuzzleFrame</code> instance. */
	@Override
	public AgentAppFrame createFrame() {
		return new EightPuzzleFrame();
	}

	/** Returns a <code>EightPuzzleController</code> instance. */
	@Override
	public AgentAppController createController() {
		return new EightPuzzleController();
	}

	
	// ///////////////////////////////////////////////////////////////
	// main method

	/**
	 * Starts the application.
	 */
	public static void main(String args[]) {
		new EightPuzzleApp().startApplication();
	}

	
	// ///////////////////////////////////////////////////////////////
	// some inner classes

	/**
	 * Adds some selectors to the base class and adjusts its size.
	 */
	static class EightPuzzleFrame extends AgentAppFrame {
		public static String ENV_SEL = "EnvSelection";
		public static String SEARCH_SEL = "SearchSelection";

		/** List of supported search algorithms. */
		static Search[] SEARCH_ALGOS = new Search[] {
				new DepthLimitedSearch(9),
				new IterativeDeepeningSearch(),
				new GreedyBestFirstSearch(new GraphSearch(),
						new MisplacedTilleHeuristicFunction()),
				new GreedyBestFirstSearch(new GraphSearch(),
						new ManhattanHeuristicFunction()),
				new AStarSearch(new GraphSearch(),
						new MisplacedTilleHeuristicFunction()),
				new AStarSearch(new GraphSearch(),
						new ManhattanHeuristicFunction()),
				new SimulatedAnnealingSearch(new ManhattanHeuristicFunction()) };
		/** List of names corresponding to the list of search algorithms. */
		static String[] SEARCH_NAMES = new String[] {
				"Depth Limited Search (9)", "Iterative Deepening Search",
				"Greedy Best First Search (MisplacedTileHeursitic)",
				"Greedy Best First Search (ManhattanHeursitic)",
				"AStar Search (MisplacedTileHeursitic)",
				"AStar Search (ManhattanHeursitic)",
				"Simulated Annealing Search" };

		public EightPuzzleFrame() {
			setEnvView(new EightPuzzleView());
			setSelectors(new String[] { ENV_SEL, SEARCH_SEL }, new String[] {
					"Select Environment", "Select Search" });
			setSelectorItems(ENV_SEL, new String[] { "Three Moves", "Random 1",
					"Extreme" }, 0);
			setSelectorItems(SEARCH_SEL, SEARCH_NAMES, 0);
			setTitle("Eight Puzzle Application");
			setSize(800, 400);
		}
	}

	/**
	 * Displays the informations provided by a <code>EightPuzzleEnvironment</code> on
	 * a panel using 2D-graphics.
	 */
	static class EightPuzzleView extends AgentAppEnvironmentView implements ActionListener {

		JButton[] tileButtons;

		EightPuzzleView() {
			setLayout(new GridLayout(3, 3));
			Font f = new java.awt.Font(Font.SANS_SERIF, Font.PLAIN, 32);
			tileButtons = new JButton[9];
			for (int i = 0; i < 9; i++) {
				JButton tile = new JButton("");
				tile.setFont(f);
				tile.addActionListener(this);
				tileButtons[i] = tile;
				add(tile);
			}
		}

		@Override
		public void setEnvironment(Environment env) {
			super.setEnvironment(env);
			showState();
		}
		
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

		/** Displays the board state by labeling and coloring the tile buttons. */
		void showState() {
			int[] vals = ((EightPuzzleEnvironment) env).getBoard().getBoard();
			for (int i = 0; i < 9; i++) {
				tileButtons[i].setBackground(vals[i] == 0 ? Color.LIGHT_GRAY : Color.WHITE);
				tileButtons[i].setText(vals[i] == 0 ? "" : Integer.toString(vals[i]));
			}
		}
		
		/**
		 * When the user presses tile buttons the board state is modified accordingly.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			for (int i = 0; i < 9; i++)
				if (ae.getSource() == tileButtons[i]) {
					EightPuzzleEnvironment e = (EightPuzzleEnvironment) env;
					XYLocation locGap = e.getBoard().getLocationOf(0);
					if (locGap.getXCoOrdinate() == i / 3) {
						if (locGap.getYCoOrdinate() == i%3-1)
							e.executeAction(null, EightPuzzleBoard.RIGHT);
						else if (locGap.getYCoOrdinate() == i%3+1)
							e.executeAction(null, EightPuzzleBoard.LEFT);
					} else if (locGap.getYCoOrdinate() == i % 3) {
						if (locGap.getXCoOrdinate() == i/3-1)
							e.executeAction(null, EightPuzzleBoard.DOWN);
						else if (locGap.getXCoOrdinate() == i/3+1)
							e.executeAction(null, EightPuzzleBoard.UP);
					}
					showState();
				}
		}	
	}

	/**
	 * Defines how to react on standard simulation button events.
	 */
	static class EightPuzzleController extends AgentAppController implements EnvironmentView {

		protected EightPuzzleEnvironment env = null;
		protected SearchAgent agent = null;

		/** Prepares next simulation. */
		@Override
		public void clear() {
			prepare(null);
		}

		/**
		 * Creates an eight puzzle environment and clears the current search agent.
		 */
		@Override
		public void prepare(String changedSelector) {
			AgentAppFrame.SelectionState selState = frame.getSelection();
			EightPuzzleBoard board = null;
			switch (selState.getValue(EightPuzzleFrame.ENV_SEL)) {
			case 0: // three moves
				board = new EightPuzzleBoard(new int[]{1, 2, 5, 3, 4, 0, 6, 7, 8});
				break;
			case 1: // random 1
				board = new EightPuzzleBoard(new int[]{1, 4, 2, 7, 5, 8, 3, 0, 6});
				break;
			case 2: // extreme
				board = new EightPuzzleBoard(new int[]{0, 8, 7, 6, 5, 4, 3, 2, 1});
				break;
			}
			env = new EightPuzzleEnvironment(board);
			agent = null;
			env.addEnvironmentView(this);
			frame.getEnvView().setEnvironment(env);
		}

		/**
		 * Creates a new search agent and adds it to the current environment if
		 * necessary.
		 */
		void addAgent() {
			if (agent == null) {
				int sel = frame.getSelection().getValue(EightPuzzleFrame.SEARCH_SEL);
				Search search = EightPuzzleFrame.SEARCH_ALGOS[sel];
				Problem problem = new Problem(env.getBoard(),
						EightPuzzleFunctionFactory.getActionsFunction(),
						EightPuzzleFunctionFactory.getResultFunction(),
						new EightPuzzleGoalTest());
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
			return agent == null || !agent.isDone();
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

		/**
		 * Clears the current search agent after the user has modified the board state.
		 * (indicated by parameter agent == null).
		 */
		@Override
		public void agentActed(Agent agent, Action action,
				EnvironmentState resultingState) {
			if (agent == null) {
				this.agent = null;
				frame.updateEnabledState();
			}
		}

		/** Does nothing. */
		@Override
		public void agentAdded(Agent agent, EnvironmentState resultingState) {
		}

		/** Does nothing. */
		@Override
		public void notify(String msg) {
		}
	}

	/** Simple environment maintaining just the current board state. */
	static class EightPuzzleEnvironment extends AbstractEnvironment {
		EightPuzzleBoard board;

		EightPuzzleEnvironment(EightPuzzleBoard board) {
			this.board = board;
		}
		
		protected EightPuzzleBoard getBoard() {
			return board;
		}

		/** Executes the provided action and returns null. */
		@Override
		public EnvironmentState executeAction(Agent agent, Action action) {
			if (action == EightPuzzleBoard.UP)
				board.moveGapUp();
			else if (action == EightPuzzleBoard.DOWN)
				board.moveGapDown();
			else if (action == EightPuzzleBoard.LEFT)
				board.moveGapLeft();
			else if (action == EightPuzzleBoard.RIGHT)
				board.moveGapRight();
			if (agent == null)
				updateEnvironmentViewsAgentActed(agent, action, null);
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
