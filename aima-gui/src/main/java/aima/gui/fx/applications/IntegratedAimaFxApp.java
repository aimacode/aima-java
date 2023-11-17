package aima.gui.fx.applications;

import aima.gui.demo.agent.MapAgentDemo;
import aima.gui.demo.agent.NondeterministicVacuumEnvironmentDemo;
import aima.gui.demo.agent.TrivialVacuumDemo;
import aima.gui.demo.agent.WumpusAgentDemo;
import aima.gui.demo.learning.LearningDemo;
import aima.gui.demo.logic.*;
import aima.gui.demo.search.*;
import aima.gui.fx.applications.agent.RouteFindingAgentApp;
import aima.gui.fx.applications.agent.VacuumAgentApp;
import aima.gui.fx.applications.agent.WumpusAgentApp;
import aima.gui.fx.applications.logic.PlanningProblemApp;
import aima.gui.fx.applications.search.*;
import aima.gui.fx.applications.search.games.ConnectFourApp;
import aima.gui.fx.applications.search.games.EightPuzzleApp;
import aima.gui.fx.applications.search.games.SimpleSudokuApp;
import aima.gui.fx.applications.search.games.TicTacToeApp;
import aima.gui.fx.applications.search.local.GeneticMaximumFinderApp;
import aima.gui.fx.applications.search.local.GeneticMaximumFinderDemo;
import aima.gui.fx.applications.search.local.SimulatedAnnealingMaximumFinderApp;
import aima.gui.fx.framework.IntegratedAppBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

// VM options (Java>8): --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml

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
		IntegratedAppBuilder builder = new IntegratedAppBuilder();
		builder.defineTitle("Integrated AIMA3e FX App");
		builder.defineSceneSize(1200, 800);
		defineContent(builder);
		builder.getResultFor(primaryStage);
		primaryStage.show();
	}

	public static void defineContent(IntegratedAppBuilder builder) {
		builder.registerApp(VacuumAgentApp.class);
		builder.registerApp(RouteFindingAgentApp.class);
		builder.registerApp(WumpusAgentApp.class);

		builder.registerApp(MapColoringCspApp.class);
		builder.registerApp(NQueensCspApp.class);
		builder.registerApp(NQueensSearchApp.class);
		builder.registerApp(SudokuCspApp.class);

		builder.registerApp(EightPuzzleApp.class);
		builder.registerApp(TicTacToeApp.class);
		builder.registerApp(ConnectFourApp.class);
		builder.registerApp(SimpleSudokuApp.class);

		builder.registerApp(SimulatedAnnealingMaximumFinderApp.class);
		builder.registerApp(GeneticMaximumFinderApp.class);

		builder.registerApp(PlanningProblemApp.class);

		builder.registerDemo(NQueensCspDemo.class);
		builder.registerDemo(GeneticMaximumFinderDemo.class);

		builder.registerDemo(TrivialVacuumDemo.class);
		builder.registerDemo(NondeterministicVacuumEnvironmentDemo.class);
		builder.registerDemo(MapAgentDemo.class);
		builder.registerDemo(WumpusAgentDemo.class);
		builder.registerDemo(NQueensSearchDemo.class);
		builder.registerDemo(EightPuzzleDemo.class);
		builder.registerDemo(TicTacToeDemo.class);
		builder.registerDemo(NQueensDemo.class);
		builder.registerDemo(MapColoringCspDemo.class);
		builder.registerDemo(SudokuCspDemo.class);

		builder.registerDemo(TTEntailsDemo.class);
		builder.registerDemo(PlFcEntailsDemo.class);
		builder.registerDemo(PlResolutionDemo.class);
		builder.registerDemo(DpllDemo.class);
		builder.registerDemo(WalkSatDemo.class);
		builder.registerDemo(FolDemo.class);
		builder.registerDemo(PlanningProblemDemo.class);

		// builder.registerDemo(ProbabilityDemo.class); // to slow.
		builder.registerDemo(LearningDemo.class); // to slow.
	}

}
