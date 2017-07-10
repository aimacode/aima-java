package aimax.osm.gui.fx.applications;

import aimax.osm.data.DataResource;
import aimax.osm.gui.fx.viewer.FXImageBuilder;
import aimax.osm.viewer.UnifiedMapDrawer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Locale;

/**
 * Simple demo application which shows a stage with the
 * map of Ulm, provided that a file with name ulm.osm is found
 * in the resource/maps path. No interactions supported here.
 *
 * @author Ruediger Lunde
 */
public class SimpleMapViewerOsmApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // indicates progress when reading large maps (for testing only)
        // Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
        // Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);

        Locale.setDefault(Locale.US);

        UnifiedMapDrawer<Canvas> mapDrawer = new UnifiedMapDrawer<>(new FXImageBuilder());
        mapDrawer.loadMap(DataResource.getUlmFileResource());

        Canvas canvas = new Canvas(600, 400);
        mapDrawer.drawMap(canvas, true);
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, Color.BLACK);

        primaryStage.setTitle("Simple Map Viewer OSM App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
