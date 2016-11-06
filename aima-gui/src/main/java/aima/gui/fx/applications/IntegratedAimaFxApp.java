package aima.gui.fx.applications;

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
import aima.gui.fx.applications.agent.RouteFindingAgentApp;
import aima.gui.fx.applications.agent.VacuumAgentApp;
import aima.gui.fx.applications.search.CspMapColoringApp;
import aima.gui.fx.applications.search.CspNQueensApp;
import aima.gui.fx.applications.search.CspNQueensDemo;
import aima.gui.fx.applications.search.NQueensSearchApp;
import aima.gui.fx.applications.search.NQueensSearchDemo;
import aima.gui.fx.applications.search.games.ConnectFourApp;
import aima.gui.fx.applications.search.games.EightPuzzleApp;
import aima.gui.fx.applications.search.games.TicTacToeApp;
import aima.gui.fx.applications.search.local.GeneticMaximumFinderApp;
import aima.gui.fx.applications.search.local.GeneticMaximumFinderDemo;
import aima.gui.fx.applications.search.local.SimulatedAnnealingMaximumFinderApp;
import aima.gui.fx.framework.IntegratedAppPaneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Integrated application which provides access to all JavaFX applications
 * (...App) and command line demos (...Demo) which are currently available in
 * the AIMA-GUI project.
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
		builder.defineTitle("Integrated AIMA3e FX App");
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
		builder.registerApp(CspNQueensApp.class);
		builder.registerApp(NQueensSearchApp.class);

		builder.registerApp(EightPuzzleApp.class);
		builder.registerApp(ConnectFourApp.class);
		builder.registerApp(TicTacToeApp.class);

		builder.registerApp(SimulatedAnnealingMaximumFinderApp.class);
		builder.registerApp(GeneticMaximumFinderApp.class);

		builder.registerDemo(CspNQueensDemo.class);
		builder.registerDemo(GeneticMaximumFinderDemo.class);

		builder.registerDemo(NQueensSearchDemo.class);
		builder.registerDemo(TrivialVacuumDemo.class);
		builder.registerDemo(EightPuzzleDemo.class);
		builder.registerDemo(TicTacToeDemo.class);
		builder.registerDemo(NQueensDemo.class);
		builder.registerDemo(MapColoringCspDemo.class);
		builder.registerDemo(NondeterministicVacuumEnvironmentDemo.class);

		builder.registerDemo(TTEntailsDemo.class);
		builder.registerDemo(PlFcEntailsDemo.class);
		builder.registerDemo(PlResolutionDemo.class);
		builder.registerDemo(DpllDemo.class);
		builder.registerDemo(WalkSatDemo.class);
		builder.registerDemo(FolDemo.class);

		// builder.registerDemo(ProbabilityProg.class); // to slow.
		// builder.registerDemo(LearningProg.class); // to slow.
	}

}
