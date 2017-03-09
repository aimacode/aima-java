package aimax.osm.gui.fx.viewer;

import aimax.osm.data.OsmMap;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.WayRef;
import aimax.osm.data.impl.DefaultMap;
import aimax.osm.viewer.AbstractEntityRenderer;
import aimax.osm.viewer.CoordTransformer;
import aimax.osm.viewer.UnifiedMapDrawer;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by rlunde on 28.10.2016.
 */
public class MapPaneCtrl {

    private StackPane pane;
    private Canvas currCanvas;
    private Canvas osCanvas;

    private UnifiedMapDrawer<Canvas> mapDrawer;
    private boolean scaleToFit = true;

    public MapPaneCtrl(StackPane pane) {
        this.pane = pane;
        mapDrawer = new UnifiedMapDrawer<Canvas>(new FXImageBuilder(), createMap());
        mapDrawer.getMap().addMapDataEventListener(ev -> update());
        pane.widthProperty().addListener((obs, o, n) -> { scaleToFit = true; update(); });
        pane.heightProperty().addListener((obs, o, n) -> { scaleToFit = true; update(); });
        pane.setOnMouseEntered(ev -> {if (currCanvas != null) currCanvas.requestFocus();});
        pane.setOnMousePressed(ev -> handleMouseEvent(ev));
        pane.setOnMouseDragged(ev -> handleMouseEvent(ev));
        pane.setOnMouseClicked(ev -> handleMouseEvent(ev));
        pane.setOnScroll(ev -> handleScrollEvent(ev));
        pane.setOnKeyPressed(ev -> handleKeyEvent(ev));
        pane.setMinSize(0, 0);
    }

    protected OsmMap createMap() { return new DefaultMap(); }

    public OsmMap getMap() {
        return mapDrawer.getMap();
    }

    public void loadMap(InputStream stream) {
        mapDrawer.loadMap(stream);
        scaleToFit = true;
    }

    public void loadMap(File file) {
        mapDrawer.loadMap(file);
        scaleToFit = true;
    }

    public AbstractEntityRenderer getRenderer() {
        return mapDrawer.getRenderer();
    }

    public void setRenderer(AbstractEntityRenderer renderer) { mapDrawer.setRenderer(renderer); }

    public CoordTransformer getTransformer() { return mapDrawer.getTransformer(); }

    public void update() {
        Canvas canvas = osCanvas;
        if (canvas == null
                || Math.abs(canvas.getWidth() - pane.getWidth()) > 0.01
                || Math.abs(canvas.getHeight() - pane.getHeight()) > 0.1) {
            canvas = new Canvas(pane.getWidth(), pane.getHeight());
            canvas.setFocusTraversable(true);
        }
        mapDrawer.drawMap(canvas, scaleToFit);
        pane.getChildren().add(canvas);
        if (pane.getChildren().size() > 1)
            pane.getChildren().remove(0);
        osCanvas = currCanvas;
        currCanvas = canvas;
        scaleToFit = false;
    }

    /**
     * Multiples the current scale with the specified factor and adjusts the
     * view so that the objects shown at the specified view focus keep at their
     * position.
     */
    public void zoom(float factor, int focusX, int focusY) {
        getTransformer().zoom(factor, focusX, focusY);
//        paintPreview((int) ((1 - factor) * focusX),
//                (int) ((1 - factor) * focusY), factor);
        update();
    }

    public void multiplyDisplayFactorWith(float fac) {
        getRenderer().setDisplayFactor(getRenderer().getDisplayFactor() * fac);
        update();
    }

    /**
     * Adjusts the view.
     *
     * @param dx
     *            Number of pixels for horizontal shift.
     * @param dy
     *            Number of pixels for vertical shift.
     */
    public void adjust(double dx, double dy) {
        getTransformer().adjust((int) dx, (int) dy);
        //paintPreview(dx, dy, 1f);
        update();
    }

    private double xDrag;
    private double yDrag;
    private boolean dragActive;

    protected void handleMouseEvent(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            xDrag = event.getX();
            yDrag = event.getY();
            dragActive = false;
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            adjust(event.getX() - xDrag, event.getY() - yDrag);
            xDrag = event.getX();
            yDrag = event.getY();
            dragActive = true;
        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED && !dragActive) {
            if (event.getButton() == MouseButton.PRIMARY) {
                CoordTransformer tr = getTransformer();
                getMap().addMarker(tr.lat((int) event.getY()), tr.lon((int) event.getX()));
            } else if (event.getButton() == MouseButton.SECONDARY) {
                getMap().clearMarkersAndTracks();
            } else if (event.getButton() == MouseButton.MIDDLE) {
                MapNode mNode = getRenderer().getNextNode((int) event.getX(), (int) event.getY());
                if (mNode != null)
                    showMapEntityInfoDialog(mNode, true);
            }
        }
    }

    protected void handleScrollEvent(ScrollEvent event) {
        float fac = event.getDeltaY() > 0 ? 1.2f : 1.0f / 1.2f;
        if (event.isControlDown())
            multiplyDisplayFactorWith(fac);
        else
            zoom(fac, (int) event.getX(), (int) event.getY());
    }

    protected void handleKeyEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT)
            adjust(30, 0);
        else if (event.getCode() == KeyCode.RIGHT)
            adjust(-30, 0);
        else if (event.getCode() == KeyCode.UP)
            adjust(0, 30);
        else if (event.getCode() == KeyCode.DOWN)
            adjust(0, -30);
        else if (event.getCode() == KeyCode.PLUS)
            if (event.isControlDown())
                multiplyDisplayFactorWith(1.5f);
            else
                zoom(1.5f, (int) pane.getWidth() / 2, (int) pane.getHeight() / 2);
        else if (event.getCode() == KeyCode.MINUS)
            if (event.isControlDown())
                multiplyDisplayFactorWith(1.0f/1.5f);
            else
                zoom(0.7f, (int) pane.getWidth() / 2, (int) pane.getHeight() / 2);
        else
            return;
        event.consume();
        currCanvas.requestFocus(); // hack...
    }



    /**
     * Finds the visible entity next to the specified view coordinates and shows
     * informations about it.
     *
     * @param debug
     *            Enables a more detailed view.
     */
    private void showMapEntityInfoDialog(MapEntity entity, boolean debug) {
        List<MapEntity> entities = new ArrayList<MapEntity>();
        if (entity.getName() != null || entity.getAttributes().length > 0
                || debug)
            entities.add(entity);
        if (entity instanceof MapNode) {
            MapNode mNode = (MapNode) entity;
            for (WayRef ref : mNode.getWayRefs()) {
                MapEntity me = ref.getWay();
                if (me.getName() != null || me.getAttributes().length > 0
                        || debug)
                    entities.add(me);
            }
        }
        boolean done = false;
        for (int i = 0; i < entities.size() && !done; i++) {
            MapEntity me = entities.get(i);
            String header = (me.getName() != null) ? me.getName() : "";
            StringBuilder content = new StringBuilder();
            if (debug)
                header += " (" + ((me instanceof MapNode) ? "Node " : "Way ")
                        + me.getId() + ")";
            if (me instanceof MapNode) {
                content.append("Lat: ")
                        .append(((MapNode) me).getLat())
                        .append(" Lon: ")
                        .append(((MapNode) me).getLon())
                        .append(" ");
            }
            if (me.getAttributes().length > 0) {
                EntityAttribute[] atts = me.getAttributes();
                content.append("Attributs: ");
                for (EntityAttribute att : atts) {
                    content.append(att.getKey())
                            .append("=")
                            .append(att.getValue())
                            .append(" ");
                }

            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Map Entity Info");
            alert.setHeaderText(header);
            alert.setContentText(content.toString());
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent())
                break;
        }
    }


}
