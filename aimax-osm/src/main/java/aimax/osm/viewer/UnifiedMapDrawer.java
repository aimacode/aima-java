package aimax.osm.viewer;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.MapBuilder;
import aimax.osm.data.OsmMap;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.impl.DefaultMap;
import aimax.osm.reader.Bz2OsmReader;
import aimax.osm.reader.MapReader;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Ruediger Lunde
 */
public class UnifiedMapDrawer<IMAGE_TYPE> {

    protected OsmMap map;
    protected CoordTransformer transformer;
    private AbstractEntityRenderer renderer;
    private UnifiedImageBuilder<IMAGE_TYPE> imageBdr;

    public UnifiedMapDrawer(UnifiedImageBuilder<IMAGE_TYPE> imgBdr) {
        this(imgBdr, new DefaultMap());
    }

    public UnifiedMapDrawer(UnifiedImageBuilder<IMAGE_TYPE> imgBdr, OsmMap map) {
        transformer = new CoordTransformer();
        this.map = map;
        renderer = new DefaultEntityRenderer();
        imageBdr = imgBdr;
    }

    public OsmMap getMap() {
        return map;
    }

    public void setMap(OsmMap map) {
        this.map = map;
    }

    public void loadMap(InputStream stream) {
        MapBuilder builder = map.getBuilder();
        builder.setEntityClassifier(new MapStyleFactory().createDefaultClassifier());
        MapReader mapReader = new Bz2OsmReader();
        mapReader.readMap(stream, builder);
        builder.buildMap();
    }

    public void loadMap(File file) {
        MapBuilder builder = map.getBuilder();
        builder.setEntityClassifier(new MapStyleFactory().createDefaultClassifier());
        MapReader mapReader = new Bz2OsmReader();
        mapReader.readMap(file, builder);
        builder.buildMap();
    }

    public AbstractEntityRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(AbstractEntityRenderer renderer) {
        this.renderer = renderer;
    }

    public CoordTransformer getTransformer() {
        return transformer;
    }

    public void drawMap(IMAGE_TYPE image, boolean adjustToFit) {
        imageBdr.initialize(image);
        imageBdr.setColor(UColor.WHITE);
        imageBdr.setAreaFilled(true);
        imageBdr.drawRect(0, 0, imageBdr.getWidth(), imageBdr.getHeight());
        if (imageBdr.getWidth() > 0 && map != null) {
            if (adjustToFit)
                getTransformer().adjustTransformation(getMap().getBoundingBox(),
                        imageBdr.getWidth(), imageBdr.getHeight());
            float latMin = transformer.lat(imageBdr.getHeight());
            float lonMin = transformer.lon(0);
            float latMax = transformer.lat(0);
            float lonMax = transformer.lon(imageBdr.getWidth());
            float scale = transformer.computeScale();
            BoundingBox vbox = new BoundingBox(latMin, lonMin, latMax, lonMax);
            float viewScale = scale / renderer.getDisplayFactor();
            renderer.initForRendering(imageBdr, transformer, map);
            map.visitEntities(renderer, vbox, viewScale);
            for (MapEntity entity : map.getVisibleMarkersAndTracks(viewScale))
                entity.accept(renderer);
            renderer.printBufferedObjects();
            if (renderer.isDebugModeEnabled() && map instanceof DefaultMap) {
                List<double[]> splits = ((DefaultMap) map).getEntityTree()
                        .getSplitCoords();
                imageBdr.setColor(UColor.LIGHT_GRAY);
                imageBdr.setLineStyle(false, 1f);
                imageBdr.setAreaFilled(false);
                CoordTransformer trans = renderer.getTransformer();
                for (double[] split : splits)
                    imageBdr.drawLine(renderer.getTransformer().x(split[1]),
                            trans.y(split[0]), trans.x(split[3]),
                            trans.y(split[2]));
            }
        }
        image = imageBdr.getResult();
    }
}
