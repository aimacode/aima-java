package aima.gui.fx.demo;

import aima.gui.fx.demo.search.CspMapColoringApp;
import aima.gui.fx.demo.search.games.EightPuzzleApp;
import aima.gui.fx.demo.search.games.TicTacToeApp;
import aima.gui.prog.agent.NondeterministicVacuumEnvironmentProg;
import aima.gui.prog.agent.TrivialVacuumProg;
import aima.gui.prog.logic.DPLLProg;
import aima.gui.prog.logic.FolProg;
import aima.gui.prog.logic.PLFCEntailsProg;
import aima.gui.prog.logic.PLResolutionProg;
import aima.gui.prog.logic.TTEntailsProg;
import aima.gui.prog.logic.WalkSatProg;
import aima.gui.prog.search.EightPuzzleProg;
import aima.gui.prog.search.MapColoringCSPProg;
import aima.gui.prog.search.NQueensProg;
import aima.gui.prog.search.TicTacToeProg;
import aima.gui.fx.demo.agent.VacuumAgentApp;
import aima.gui.fx.demo.agent.RouteFindingAgentApp;
import aima.gui.fx.demo.search.NQueensSearchApp;
import aima.gui.fx.demo.search.NQueensSearchProg;
import aima.gui.fx.demo.search.games.ConnectFourApp;
import aima.gui.fx.demo.search.local.GeneticMaximumFinderApp;
import aima.gui.fx.demo.search.local.GeneticMaximumFinderProg;
import aima.gui.fx.demo.search.local.SimulatedAnnealingMaximumFinderApp;
import aima.gui.fx.framework.IntegratedAppPaneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Integrated application which provides access to all JavaFX demos which are
 * currently available in the AIMA-GUI project.
 * 
 * @author Ruediger Lunde
 */
public class IntegratedAimaFxApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		IntegratedAppPaneBuilder builder = new IntegratedAppPaneBuilder();
		builder.defineTitle("Integrated AIMA FX App");
		defineContent(builder);
		BorderPane root = new BorderPane();
		builder.getResultFor(root, primaryStage);
		primaryStage.setScene(new Scene(root, 1200, 800));
		primaryStage.show();
	}

	public static void defineContent(IntegratedAppPaneBuilder builder) {
		builder.registerApp(VacuumAgentApp.class);
		builder.registerApp(RouteFindingAgentApp.class);

		builder.registerApp(CspMapColoringApp.class);
		builder.registerApp(NQueensSearchApp.class);

		builder.registerApp(EightPuzzleApp.class);
		builder.registerApp(ConnectFourApp.class);
		builder.registerApp(TicTacToeApp.class);

		builder.registerApp(SimulatedAnnealingMaximumFinderApp.class);
		builder.registerApp(GeneticMaximumFinderApp.class);
		
		builder.registerProg(GeneticMaximumFinderProg.class);
		builder.registerProg(NQueensSearchProg.class);

		builder.registerProg(TrivialVacuumProg.class);

		builder.registerProg(EightPuzzleProg.class);
		builder.registerProg(TicTacToeProg.class);
		builder.registerProg(NQueensProg.class);
		builder.registerProg(MapColoringCSPProg.class);
		builder.registerProg(NondeterministicVacuumEnvironmentProg.class);

		builder.registerProg(TTEntailsProg.class);
		builder.registerProg(PLFCEntailsProg.class);
		builder.registerProg(PLResolutionProg.class);
		builder.registerProg(DPLLProg.class);
		builder.registerProg(WalkSatProg.class);
		builder.registerProg(FolProg.class);

		// builder.registerProg(ProbabilityProg.class); // to slow.

		// builder.registerProg(LearningProg.class); // to slow.
	}

}
