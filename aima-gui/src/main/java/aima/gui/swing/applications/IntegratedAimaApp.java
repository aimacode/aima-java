package aima.gui.swing.applications;

import aima.gui.demo.agent.NondeterministicVacuumEnvironmentDemo;
import aima.gui.demo.agent.TrivialVacuumDemo;
import aima.gui.demo.learning.LearningDemo;
import aima.gui.demo.logic.DpllDemo;
import aima.gui.demo.logic.FolDemo;
import aima.gui.demo.logic.PlFcEntailsDemo;
import aima.gui.demo.logic.PlResolutionDemo;
import aima.gui.demo.logic.TTEntailsDemo;
import aima.gui.demo.logic.WalkSatDemo;
import aima.gui.demo.probability.ProbabilityDemo;
import aima.gui.demo.search.EightPuzzleDemo;
import aima.gui.demo.search.MapColoringCspDemo;
import aima.gui.demo.search.NQueensDemo;
import aima.gui.demo.search.TicTacToeDemo;
import aima.gui.swing.applications.agent.VacuumApp;
import aima.gui.swing.applications.agent.map.RouteFindingAgentApp;
import aima.gui.swing.applications.search.csp.MapColoringApp;
import aima.gui.swing.applications.search.games.ConnectFourApp;
import aima.gui.swing.applications.search.games.EightPuzzleApp;
import aima.gui.swing.applications.search.games.NQueensApp;
import aima.gui.swing.applications.search.games.TicTacToeApp;
import aima.gui.swing.applications.robotics.MonteCarloLocalizationApp;

/**
 * The all-in-one demo application. Shows everything within one frame.
 * 
 * @author Ruediger Lunde
 */
public class IntegratedAimaApp {

	/** Registers agent applications and command line demos. */
	public static void defineContent(AimaDemoFrame frame) {
		frame.addApp(VacuumApp.class);
		frame.addApp(RouteFindingAgentApp.class);
		frame.addApp(EightPuzzleApp.class);
		frame.addApp(NQueensApp.class);
		frame.addApp(TicTacToeApp.class);
		frame.addApp(ConnectFourApp.class);
		frame.addApp(MapColoringApp.class);

		frame.addApp(MonteCarloLocalizationApp.class);
		
		frame.addDemo(TrivialVacuumDemo.class);
		
		frame.addDemo(EightPuzzleDemo.class);
		frame.addDemo(TicTacToeDemo.class);
		frame.addDemo(NQueensDemo.class);
		frame.addDemo(MapColoringCspDemo.class);
		frame.addDemo(NondeterministicVacuumEnvironmentDemo.class);

		frame.addDemo(TTEntailsDemo.class);
		frame.addDemo(PlFcEntailsDemo.class);
		frame.addDemo(PlResolutionDemo.class);
		frame.addDemo(DpllDemo.class);
		frame.addDemo(WalkSatDemo.class);
		frame.addDemo(FolDemo.class);

		frame.addDemo(ProbabilityDemo.class);

		frame.addDemo(LearningDemo.class);
	}

	/** Starts the application. */
	public static void main(String[] args) {
		AimaDemoFrame frame = new AimaDemoFrame();
		defineContent(frame);
		frame.setSize(1000, 700);
		frame.setVisible(true);
	}
}
