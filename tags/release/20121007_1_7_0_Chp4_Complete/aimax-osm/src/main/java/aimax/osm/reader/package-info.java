/**
 * This package contains a simple reader for XML files using the
 * OSM syntax. To use it, just take a look at classes <code>OsmReader</code>,
 * <code>FilteringOsmReader</code>, and <code>Bz2OsmReader</code>.
 * The architecture relies on the application of the state
 * design pattern (GoF). All element processors can be viewed as states
 * in this sense.
 * <p>The SAX-parser is a strongly simplified version of the Osmosis OSM
 * file parser, written by Brett Henderson. Many parts of the code remained
 * almost unchanged. Some refactoring was done to reduce the number of
 * classes needed and to improve speed by focusing on the relevant
 * data.
 */
package aimax.osm.reader;