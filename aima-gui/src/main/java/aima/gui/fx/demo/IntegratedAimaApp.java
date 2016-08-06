package aima.gui.fx.demo;

import aima.gui.demo.agent.TrivialVacuumProg;
import aima.gui.demo.logic.DPLLProg;
import aima.gui.demo.logic.FolProg;
import aima.gui.demo.logic.PLFCEntailsProg;
import aima.gui.demo.logic.PLResolutionProg;
import aima.gui.demo.logic.TTEntailsProg;
import aima.gui.demo.logic.WalkSatProg;
import aima.gui.demo.search.EightPuzzleProg;
import aima.gui.demo.search.MapColoringCSPProg;
import aima.gui.demo.search.NQueensProg;
import aima.gui.demo.search.NondeterministicVacuumEnvironmentProg;
import aima.gui.demo.search.TicTacToeProg;
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
public class IntegratedAimaApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		IntegratedAppPaneBuilder builder = new IntegratedAppPaneBuilder();
		defineContent(builder);
		BorderPane root = new BorderPane();
		builder.getResultFor(root, primaryStage);
		primaryStage.setScene(new Scene(root, 900, 600));
		primaryStage.show();
	}

	protected void defineContent(IntegratedAppPaneBuilder builder) {
		builder.defineTitle("Integrated FX AIMA App");
		builder.registerApp(SimulatedAnnealingMaximumFinderApp.class);
		builder.registerApp(GeneticMaximumFinderApp.class);
		builder.registerApp(NQueensSearchApp.class);
		builder.registerApp(ConnectFourApp.class);

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
