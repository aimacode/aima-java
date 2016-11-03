package aimax.osm.gui.fx.applications;

import aimax.osm.data.DataResource;
import aimax.osm.gui.fx.viewer.MapPaneCtrl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Locale;

import static javafx.scene.paint.Color.*;

/**
 * Created by rlunde on 03.11.2016.
 */
public class OsmViewerApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // indicates progress when reading large maps (for testing only)
        // Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
        // Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);

        Locale.setDefault(Locale.US);

        StackPane mapPane = new StackPane();
        MapPaneCtrl mapPaneCtrl = new MapPaneCtrl(mapPane);
        mapPaneCtrl.loadMap(DataResource.getULMFileResource());

        BorderPane root = new BorderPane();
        root.setCenter(mapPane);
        root.setBottom(new Button("XYZ"));
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Osm Viewer App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
