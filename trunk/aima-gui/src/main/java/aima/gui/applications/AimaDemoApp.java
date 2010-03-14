package aima.gui.applications;

import aima.gui.applications.search.map.RouteFindingAgentApp;
import aima.gui.applications.vacuum.VacuumApp;
import aima.gui.demo.learning.LearningDemo;
import aima.gui.demo.logic.DPLLDemo;
import aima.gui.demo.logic.FolDemo;
import aima.gui.demo.logic.PLFCEntailsDemo;
import aima.gui.demo.logic.PLResolutionDemo;
import aima.gui.demo.logic.TTEntailsDemo;
import aima.gui.demo.logic.WalkSatDemo;
import aima.gui.demo.probability.ProbabilityDemo;
import aima.gui.demo.search.CSPDemo;
import aima.gui.demo.search.EightPuzzleDemo;
import aima.gui.demo.search.NQueensDemo;
import aima.gui.demo.search.TicTacToeDemo;

/**
 * The all-in-one demo application. Shows everything within one frame.
 * @author R. Lunde
 */
public class AimaDemoApp {

	/** Registers console program demos and applications. */
	public static void registerDemos(AimaDemoFrame frame) {
		frame.addDemo(EightPuzzleDemo.class);
		frame.addDemo(NQueensDemo.class);
		frame.addDemo(TicTacToeDemo.class);
		frame.addDemo(CSPDemo.class);
		
		frame.addDemo(TTEntailsDemo.class);
		frame.addDemo(PLFCEntailsDemo.class);
		frame.addDemo(PLResolutionDemo.class);
		frame.addDemo(DPLLDemo.class);
		frame.addDemo(WalkSatDemo.class);
		frame.addDemo(FolDemo.class);
		
		frame.addDemo(ProbabilityDemo.class);
		
		frame.addDemo(LearningDemo.class);
		
		frame.addApp(VacuumApp.class);
		frame.addApp(RouteFindingAgentApp.class);
	}
	
	/** Starts the demo. */
	public static void main(String[] args) {
		AimaDemoFrame frame = new AimaDemoFrame();
		registerDemos(frame);
		frame.setTitle("Artificial Intelligence a Modern Approach 3rd ed. Java Demos (AIMA3e-Java)");
		frame.setSize(1000, 600);
		frame.setVisible(true);
	}
}
