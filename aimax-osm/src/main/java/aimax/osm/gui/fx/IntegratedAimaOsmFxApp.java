package aimax.osm.gui.fx;

import aima.gui.fx.applications.IntegratedAimaFxApp;
import aima.gui.fx.framework.IntegratedAppPaneBuilder;
import aimax.osm.gui.fx.applications.OsmLRTAStarAgentApp;
import aimax.osm.gui.fx.applications.OsmRouteFindingAgentApp;
import aimax.osm.gui.fx.applications.OsmRoutePlannerApp;
import aimax.osm.gui.fx.applications.OsmAgentBaseApp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Integrated application which provides access to all JavaFX applications
 * (...App) and command line demos (...Demo) which are currently available in
 * the AIMA-GUI and AIMA-OSM project.
 * 
 * @author Ruediger Lunde
 */
public class IntegratedAimaOsmFxApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		IntegratedAppPaneBuilder builder = new IntegratedAppPaneBuilder();
		builder.defineTitle("Integrated AIMA3e OSM FX App");
		defineContent(builder);
		BorderPane root = new BorderPane();
		builder.getResultFor(root, primaryStage);
		primaryStage.setScene(new Scene(root, 1200, 800));
		primaryStage.show();
	}

	public static void defineContent(IntegratedAppPaneBuilder builder) {
		IntegratedAimaFxApp.defineContent(builder);

		builder.registerApp(OsmRoutePlannerApp.class);
		builder.registerApp(OsmAgentBaseApp.class);
		builder.registerApp(OsmRouteFindingAgentApp.class);
		builder.registerApp(OsmLRTAStarAgentApp.class);
	}
}
