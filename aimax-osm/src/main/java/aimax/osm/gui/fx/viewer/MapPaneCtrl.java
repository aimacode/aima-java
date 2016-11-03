package aimax.osm.gui.fx.viewer;

import aimax.osm.data.OsmMap;
import aimax.osm.data.impl.DefaultMap;
import aimax.osm.viewer.AbstractEntityRenderer;
import aimax.osm.viewer.CoordTransformer;
import aimax.osm.viewer.UnifiedMapDrawer;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

import java.io.InputStream;

/**
 * Created by rlunde on 28.10.2016.
 */
public class MapPaneCtrl {

    private StackPane pane;
    private Canvas currCanvas;
    private Canvas osCanvas;

    private UnifiedMapDrawer<Canvas> mapDrawer;

    public MapPaneCtrl(StackPane pane) {
        this.pane = pane;
        mapDrawer = new UnifiedMapDrawer<Canvas>(new FXImageBuilder(), createMap());
        pane.widthProperty().addListener((obs, o, n) -> update());
        pane.heightProperty().addListener((obs, o, n) -> update());
        pane.setMinSize(0, 0);
    }

    protected OsmMap createMap() { return new DefaultMap(); }

    public OsmMap getMap() {
        return mapDrawer.getMap();
    }

    public void loadMap(InputStream stream) {
        mapDrawer.loadMap(stream);
    }

    public AbstractEntityRenderer getRenderer() {
        return mapDrawer.getRenderer();
    }

    public void setRenderer(AbstractEntityRenderer renderer) { mapDrawer.setRenderer(renderer); }

    public CoordTransformer getTransformer() { return mapDrawer.getTransformer(); }

    public void update() {
        Canvas canvas = osCanvas;
        if (canvas == null
                || Math.round(canvas.getWidth()) != pane.getWidth()
                || Math.round(canvas.getHeight()) != pane.getWidth()) {
            canvas = new Canvas(pane.getWidth(), pane.getHeight());
        }
        mapDrawer.drawMap(canvas, true);
        pane.getChildren().add(canvas);
        if (pane.getChildren().size() > 1)
            pane.getChildren().remove(0);
        osCanvas = currCanvas;
        currCanvas = canvas;
    }




}
