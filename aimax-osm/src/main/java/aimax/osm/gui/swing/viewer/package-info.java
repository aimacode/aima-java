/**
 * This package contains Swing implementations of graphical components
 * used in the presentation layer of the OSM viewer. 
 * The class <code>MapViewPane</code> gets its data from an
 * <code>OsmMap</code> object and visualizes it on a canvas. It also
 * reacts on keyboard and mouse events and provides a popup menu.
 * By default, it uses a platform independent
 * <code>DefaultEntityRenderer</code> to draw entities
 * but other renderers can be used as well. The default renderer does not
 * decide itself, which icon to use for a certain entity, or which color.
 * Instead, it assumes, that all entities have been classified before and
 * provide an attached <code>DefaultEntityViewInfo</code> object. For this
 * purpose, the OSM map must be equipped with an entity classifier.
 * The <code>MapViewFrame</code> demonstrates, how the viewer can be
 * embedded within a graphical application frame.</p>
 */
package aimax.osm.gui.swing.viewer;