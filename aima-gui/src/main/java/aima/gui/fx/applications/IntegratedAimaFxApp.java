package aima.gui.fx.applications;

import aima.gui.fx.applications.search.CspMapColoringApp;
import aima.gui.fx.applications.search.games.EightPuzzleApp;
import aima.gui.fx.applications.search.games.TicTacToeApp;
import aima.gui.demo.agent.NondeterministicVacuumEnvironmentDemo;
import aima.gui.demo.agent.TrivialVacuumDemo;
import aima.gui.demo.logic.DpllDemo;
import aima.gui.demo.logic.FolDemo;
import aima.gui.demo.logic.PlFcEntailsDemo;
import aima.gui.demo.logic.PlResolutionDemo;
import aima.gui.demo.logic.TTEntailsDemo;
import aima.gui.demo.logic.WalkSatDemo;
import aima.gui.demo.search.EightPuzzleDemo;
import aima.gui.demo.search.MapColoringCspDemo;
import aima.gui.demo.search.NQueensDemo;
import aima.gui.demo.search.TicTacToeDemo;
import aima.gui.fx.applications.agent.VacuumAgentApp;
import aima.gui.fx.applications.agent.RouteFindingAgentApp;
import aima.gui.fx.applications.search.NQueensSearchApp;
import aima.gui.fx.applications.search.NQueensSearchDemo;
import aima.gui.fx.applications.search.games.ConnectFourApp;
import aima.gui.fx.applications.search.local.GeneticMaximumFinderApp;
import aima.gui.fx.applications.search.local.GeneticMaximumFinderDemo;
import aima.gui.fx.applications.search.local.SimulatedAnnealingMaximumFinderApp;
import aima.gui.fx.framework.IntegratedAppPaneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Integrated application which provides access to all JavaFX applications (...App) and command line demos (...Demo)
 * which are currently available in the AIMA-GUI project.
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
		
		builder.registerProg(GeneticMaximumFinderDemo.class);
		builder.registerProg(NQueensSearchDemo.class);

		builder.registerProg(TrivialVacuumDemo.class);

		builder.registerProg(EightPuzzleDemo.class);
		builder.registerProg(TicTacToeDemo.class);
		builder.registerProg(NQueensDemo.class);
		builder.registerProg(MapColoringCspDemo.class);
		builder.registerProg(NondeterministicVacuumEnvironmentDemo.class);

		builder.registerProg(TTEntailsDemo.class);
		builder.registerProg(PlFcEntailsDemo.class);
		builder.registerProg(PlResolutionDemo.class);
		builder.registerProg(DpllDemo.class);
		builder.registerProg(WalkSatDemo.class);
		builder.registerProg(FolDemo.class);

		// builder.registerProg(ProbabilityProg.class); // to slow.

		// builder.registerProg(LearningProg.class); // to slow.
	}

}
