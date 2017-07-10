package aimax.osm.gui.fx.applications;

import aima.gui.fx.framework.IntegrableApplication;
import aimax.osm.data.DataResource;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.Track;
import aimax.osm.gui.fx.viewer.MapPaneCtrl;
import aimax.osm.routing.RouteCalculator;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Extendable application for route planning based on a real map of the city of Ulm.
 *
 * @author Ruediger Lunde
 */
public class RoutePlannerOsmApp extends IntegrableApplication {

    public static void main(String[] args) {
        launch(args);
    }

    private Button clearBtn;
    private ComboBox<String> taskCombo;
    private Button calcBtn;
    private Label statusLabel;
    private MapPaneCtrl mapPaneCtrl;

    private RouteCalculator routeCalculator;


    @Override
    public String getTitle() {
        return "OSM Route Planner App";
    }

    /**
     * Simple pane to control the game.
     */
    @Override
    public Pane createRootPane() {

        routeCalculator = createRouteCalculator();

        BorderPane root = new BorderPane();

        ToolBar toolBar = new ToolBar();
        clearBtn = new Button("Clear");
        clearBtn.setOnAction(ev -> initialize());

        taskCombo = new ComboBox<>();
        taskCombo.getItems().addAll(routeCalculator.getTaskSelectionOptions());
        taskCombo.getSelectionModel().select(0);

        calcBtn = new Button("Calculate Route");
        calcBtn.setOnAction(ev -> calculateRoute());
        toolBar.getItems().addAll(clearBtn, new Separator(), taskCombo, calcBtn);
        root.setTop(toolBar);

        StackPane mapPane = new StackPane();
        mapPaneCtrl = new MapPaneCtrl(mapPane);
        mapPaneCtrl.getMap().addMapDataEventListener(ev -> updateEnabledState());
        mapPaneCtrl.loadMap(DataResource.getUlmFileResource());

        root.setCenter(mapPane);

        statusLabel = new Label();
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setFont(Font.font(16));
        root.setBottom(statusLabel);
        return root;
    }

    /**
     * Factory method for the routing component. Subclasses can override it and
     * provide more advanced routing algorithms.
     */
    protected RouteCalculator createRouteCalculator() {
        return new RouteCalculator();
    }


    @Override
    public void initialize() {
        mapPaneCtrl.getMap().clearMarkersAndTracks();
        statusLabel.setText("");
    }

    @Override
    public void cleanup() {
        // nothing to do here...
    }

    /** Starts route generation after the calculate button has been pressed. */
    public void calculateRoute() {
        OsmMap map = mapPaneCtrl.getMap();
        List<Position> positions = routeCalculator.calculateRoute(
                map.getMarkers(), map, taskCombo.getSelectionModel().getSelectedIndex());
        mapPaneCtrl.getMap().createTrack("Route", positions);
        statusLabel.setText(getTrackInfo(mapPaneCtrl.getMap().getTrack("Route")));
    }

    /**
     * Enables the Calculate button if at least two markers are set.
     */
    protected void updateEnabledState() {
        calcBtn.setDisable(mapPaneCtrl.getMap().getMarkers().size() < 2);
    }

    protected String getTrackInfo(Track track) {
        List<MapNode> nodes = track.getNodes();
        DecimalFormat f1 = new DecimalFormat("#0.00");
        double km = Position.getTrackLengthKM(nodes);
        String info = track.getName() + ": Length " + f1.format(km)
                + " km";
        if (nodes.size() == 2) {
            DecimalFormat f2 = new DecimalFormat("#000");
            MapNode m1 = nodes.get(nodes.size() - 2);
            MapNode m2 = nodes.get(nodes.size() - 1);
            int course = new Position(m1).getCourseTo(m2);
            info += "; Direction " + f2.format(course);
        }
        return info;
    }
}