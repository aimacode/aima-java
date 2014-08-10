/**
 * This package contains classes for drawing images from map data. Drawing
 * commands are send to a <code>UnifiedImageBuilder</code> which abstracts
 * from the concrete graphics framework being used.
 * <p>Renderers are responsible for controlling the drawing process. Here,
 * a renderer is implemented as a visitor. It can be provided as parameter
 * to {@link aimax.osm.data.OsmMap#visitEntities(aimax.osm.data.EntityVisitor, aimax.osm.data.BoundingBox, float)}.   
 * <code>AbstractEntityRenderer</code> defines the general renderer interface
 * and contains some common attributes. The <code>DefaultEntityRenderer</code>
 * gives an example for a concrete implementation. The mapping between
 * types of map entities and symbols for visualization is done by
 * entity classifiers. They contain rules which assign <code>EntityViewInfo</code>
 * objects to map entities depending on its attributes. See the
 * <code>MapStyleFactory</code> for examples. Entity classifies are
 * maintained by the map and not by the renderer for two reasons:
 * First, attribute checks for classification can be reduced to one check
 * per entity when loading the map. Second, scale visibility information
 * can be used to organize map for fast scale-dependent filtering.
 * 
 * @author Ruediger Lunde
 */
package aimax.osm.viewer;