/**
 * This package together with its sub-packages contains the application layer
 * classes of the OSM viewer.
 * The most important interface is <code>OsmMap</code>. It defines a
 * container for all map information and serves as model for the viewer. The
 * provided operations focus on read access. To create and modify
 * OSM maps, a builder interface called <code>MapBuilder</code> is provided. 
 * 
 * <p>For the container as well as for the contained map data objects an example
 * implementation is provided in the <code>impl</code> sub-package.</p>
 * 
 * @author Ruediger Lunde
 */
package aimax.osm.data;