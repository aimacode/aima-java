package aimax.osm.gui.fx;

import aima.gui.fx.applications.IntegratedAimaFxApp;
import aima.gui.fx.framework.IntegratedAppBuilder;
import aimax.osm.gui.fx.applications.OnlineAgentOsmApp;
import aimax.osm.gui.fx.applications.ExtendedRouteFindingAgentOsmApp;
import aimax.osm.gui.fx.applications.RoutePlannerOsmApp;
import aimax.osm.gui.fx.applications.RouteFindingAgentOsmApp;
import javafx.application.Application;
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
		IntegratedAppBuilder builder = new IntegratedAppBuilder();
		builder.defineTitle("Integrated AIMA3e OSM FX App");
		builder.defineSceneSize(1200, 800);
		defineContent(builder);
		builder.getResultFor(primaryStage);
		primaryStage.show();
	}

	public static void defineContent(IntegratedAppBuilder builder) {
		IntegratedAimaFxApp.defineContent(builder);

		builder.registerApp(RoutePlannerOsmApp.class);
		builder.registerApp(RouteFindingAgentOsmApp.class);
		builder.registerApp(ExtendedRouteFindingAgentOsmApp.class);
		builder.registerApp(OnlineAgentOsmApp.class);
	}
}
