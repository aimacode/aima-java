= AIMAX-OSM =

by Ruediger Lunde

This project provides a framework for building intelligent Open Street Map
(OSM) data applications. It was originally designed to validate and test agent
and search concepts from the AIMA library in an interesting, non-trivial
application area.

Central part of the project is an OSM viewer implementation. It is designed
as an efficient general purpose viewer which is highly configurable and extendible.
The internal map representation is chosen as close as possible to the
original OSM XML file format. A kd-tree is used to improve rendering efficiency.
Classification and abstraction of map entities as well as their
visual appearance within the drawn map are controlled by declarative
rendering rules. They can be replaced or configured at runtime.
New personal map styles can be created quite easily. See classes
<code>aimax.osm.viewer.MapStyleFactory</code> and
<code>aimax.osm.application.OsmViewerPlusApp</code> for ideas how that can be
achieved.

Routing functionality is based on the AIMA-CORE library.
All dependencies to the AIMA libraries are encapsulated in the routing
sub-package and the applications using functionality from that package.
So the viewer classes can also be used as stand-alone library
for building general OSM applications.

In the current version, relation entities are ignored and
the size of the map should be limited to about a million nodes
to avoid long loading times. For example, detailed maps of cities like Berlin 
can be loaded and displayed without any problem if enough heap space
is provided (VM argument -Xmx500M).

Getting started: Run one of the applications in the
<code>aimax.osm.applications</code> package. If no map is displayed
by default, make sure that the main/resource folder is included 
in the build path of your project, recompile and start again.
Then place the mouse inside the map viewer pane. Try mouse-left, mouse-right,
mouse-drag, ctrl-mouse-left, plus button, minus button, shift-plus, shift-minus,
ctrl-plus, ctrl-minus, space, ctrl-space, arrow buttons, and also the mouse-wheel
for navigation, mark setting, and track definition. For routing, at least two
marks must be set.


== Current Release: 0.9.2-AIMAX-OSM Many Improvements ==
0.9.2-AIMAX-OSM Many Improvements : 07 May 2010 :<br>
   * Storage efficiency of map representation improved (references to ways).
   * Rendering speed and quality improved
     (text placement, area sorting, one-way streets, icons, default style).
   * osm.gz2 file format support added.
   * slow zoom (activated with <shift>) added.

== Keywords ==

Open Street Map, OSM, Routing, OSM Viewer, Java


== Requirements ==

- Depends on the aima-core and the aima-gui project. 

- To establish a connection to a GPS, the RXTX serial port library
(http://www.rxtx.org/) must be installed. See gps package documentation
for details.

- To enable the osm.bz2 reader, the Apache Commons Compress library
(see http://commons.apache.org/compress/) is needed. If you want to read
compressed osm files, download the corresponding jar file and add it to
your class path.


== Example OSM Maps ==

Can be downloaded from the AIMA project website, example-osm-maps.zip :

http://code.google.com/p/aima-java/downloads/list

All provided example maps have been created based on the data published at:
http://download.geofabrik.de/osm/

Data extraction for specific areas such as german cities has been performed using
the free Osmosis tool. See e.g. http://wiki.openstreetmap.org/index.php/Osmosis.

If you want to create a map of your home town, the easiest way to get the
coordinates for the bounding box is to select the export tab from page
http://www.openstreetmap.org/ and then navigate to the area which
you want to extract. Unfortunately, the XML export typically doesn't work, but you
find the needed coordinates on the left side.

The following lines give you an example how Osmosis could be called within a Windows
cmd file to extract the region of Ulm from the Geofabrik germany.osm.bz2 file:

set JAVACMD_OPTIONS=-Xmx1024M
osmosis-0.31.1\bin\osmosis --read-xml ..\germany.osm.bz2 --bounding-box left="9.43" top="48.69" right="10.56" bottom="48.19" --write-xml ulm.osm

Open Street Map data is published under the
Creative Commons Attribution-ShareAlike 2.0 license.

For details, see:

http://www.openstreetmap.org/
http://creativecommons.org/licenses/by-sa/2.0/


== Applications ==
Under the release/ directory you should find three jar files, aima-core.jar, aima-gui.jar, and aimax-osm.jar. 
Ensure these are on your CLASSPATH, the different GUI programs that can be run using these are:
 * java -classpath aimax-osm.jar aimax.osm.applications.OsmViewerApp
  * just the plain viewer (not dependent on AIMA)
 * java -classpath aimax-osm.jar aimax.osm.applications.OsmViewerPlusApp
  * demonstrates, how to configure and extend the viewer
 * java -classpath aimax-osm.jar aimax.osm.applications.RoutePlannerApp
  * uses aima-core search functionality for routing in OSM maps
 * java -classpath aimax-osm.jar aimax.osm.applications.OsmAgentApp
  * lets map agents from aima-core act in map environments which are defined by OSM data 
 * java -classpath aimax-osm.jar aimax.osm.applications.OsmSearchDemoAgentApp
  * visualizes simulated search space exploration of different search strategies
 * java -classpath aimax-osm.jar aimax.osm.applications.MiniNaviApp
  * provides a base for car navigation system development
 
  
= Change History (Update in reverse chronological order) =

0.9.1-AIMAX-OSM GUI Updates : 15 Mar 2010 :<br>
   * Code updated to correspond with changes in AIMA-GUI 0.2.0 release.
   * Offline search now available in OSM search demo application.

0.9.0-AIMAX-OSM Map Style Redesign : 02 Mar 2010 :<br>
Major refactorings
 * Responsibility for map entity classification moved from presentation to application layer.
 * Classification mechanism for mapping map entities on style parameters extended
   (now more than one attribute can be checked).
 * kd-tree is now aware of minimal scales of entities.
Additionally, several code cleanups and documentation updates are included.
 
New features 
 * Performance of the viewer improved for large maps.
 * Default map style improved, especially for alpine maps.
 * Find function for map entity search improved (checks also attribute names and values).
 * MiniNavi now performs routing in background thread, which can be canceled.

0.1.2-AIMAX-OSM Minor Fixes : 09 Feb 2010 :<br>
Minor updates/defect fixes/clean up to project release:
 * Java Doc now uses newer package-info.java mechanism.
 * Default settings improved.
 * Example OSM maps made available in a separate download.
 * Defect fix: OSM applications now start without a default map, if the
   map cannot be found.
 * Documentation improvements.
 * Marks now represented as pins on the map displays.

0.1.1-AIMAX-OSM Added : 06 Feb 2010 :<br>
First release based on the 3rd edition of AIMA, which contains the Open Street Map (OSM) library.