/**
 * This package together with its sub-packages contains the application layer
 * classes of the OSM viewer.
 * The most important interface is <code>MapDataStorage</code>. It defines a
 * container for all map information and serves as model for the viewer. 
 * 
 * <p>For the container as well as for the contained map data objects an example
 * implementation is provided in the <code>impl</code> sub-package. The
 * <code>MapDataFactory</code> singleton is responsible for the selection of
 * the implementation to be used. Selection is controlled by a system property
 * providing the class name of the factory to be used.</p>
 * 
 * @author Ruediger Lunde
 */
package aimax.osm.data;