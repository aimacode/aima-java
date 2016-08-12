package aima.gui.swing.demo;

import aima.gui.demo.agent.NondeterministicVacuumEnvironmentProg;
import aima.gui.demo.agent.TrivialVacuumProg;
import aima.gui.demo.learning.LearningProg;
import aima.gui.demo.logic.DPLLProg;
import aima.gui.demo.logic.FolProg;
import aima.gui.demo.logic.PLFCEntailsProg;
import aima.gui.demo.logic.PLResolutionProg;
import aima.gui.demo.logic.TTEntailsProg;
import aima.gui.demo.logic.WalkSatProg;
import aima.gui.demo.probability.ProbabilityProg;
import aima.gui.demo.search.EightPuzzleProg;
import aima.gui.demo.search.MapColoringCSPProg;
import aima.gui.demo.search.NQueensProg;
import aima.gui.demo.search.TicTacToeProg;
import aima.gui.swing.demo.agent.VacuumApp;
import aima.gui.swing.demo.agent.map.RouteFindingAgentApp;
import aima.gui.swing.demo.search.csp.MapColoringApp;
import aima.gui.swing.demo.search.games.ConnectFourApp;
import aima.gui.swing.demo.search.games.EightPuzzleApp;
import aima.gui.swing.demo.search.games.NQueensApp;
import aima.gui.swing.demo.search.games.TicTacToeApp;
import aima.gui.swing.demo.robotics.MonteCarloLocalizationApp;

/**
 * The all-in-one demo application. Shows everything within one frame.
 * 
 * @author Ruediger Lunde
 */
public class AimaDemoApp {

	/** Registers agent applications and console program demos. */
	public static void registerDemos(AimaDemoFrame frame) {
		frame.addApp(VacuumApp.class);
		frame.addApp(RouteFindingAgentApp.class);
		frame.addApp(EightPuzzleApp.class);
		frame.addApp(NQueensApp.class);
		frame.addApp(TicTacToeApp.class);
		frame.addApp(ConnectFourApp.class);
		frame.addApp(MapColoringApp.class);

		frame.addApp(MonteCarloLocalizationApp.class);
		
		frame.addProg(TrivialVacuumProg.class);
		
		frame.addProg(EightPuzzleProg.class);
		frame.addProg(TicTacToeProg.class);
		frame.addProg(NQueensProg.class);
		frame.addProg(MapColoringCSPProg.class);
		frame.addProg(NondeterministicVacuumEnvironmentProg.class);

		frame.addProg(TTEntailsProg.class);
		frame.addProg(PLFCEntailsProg.class);
		frame.addProg(PLResolutionProg.class);
		frame.addProg(DPLLProg.class);
		frame.addProg(WalkSatProg.class);
		frame.addProg(FolProg.class);

		frame.addProg(ProbabilityProg.class);

		frame.addProg(LearningProg.class);
	}

	/** Starts the demo. */
	public static void main(String[] args) {
		AimaDemoFrame frame = new AimaDemoFrame();
		registerDemos(frame);
		frame.setSize(1000, 700);
		frame.setVisible(true);
	}
}
