= AIMAX-OSM =

by Ruediger Lunde (Ruediger.Lunde@gmail.com)

This project provides a framework for building intelligent Open Street Map
(OSM) data applications. It was designed to validate and test agent
and search concepts from the AIMA library in a non-trivial
application area and provide an interesting coding environment for student
projects. Typical programming challenges include:
# Extend the `OsmRoutePlannerApp` and provide additional options, e.g. to optimize
time for a driver, to optimize fun for a cyclist, ...
# Extend the `OsmLRTAStarAgentApp` and add variants of the original LRTAStar-based
agent which try to perform better than the original in this special environment, e.g.
by increasing greediness.
# Extend the `OsmAgentBaseApp` and add a new agent which is able to react on unforeseen
events, e.g. on a road blocking defined by additional markers.
# Develop an agent which plans the Saturday morning shopping tour for you. Try to
create an optimal tour (the typical greedy TSP implementation is not that challenging...).

Two GUIs are provided: One is based on JavaFX (package `aimax.osm.gui.fx`) and the other based on
Swing (package `aimax.osm.gui.swing`). The FX GUI ist newer, more elegant, and
more focused on AIMA concepts evaluation. The Swing GUI provides more functionality including
a toolbox for building small navigation systems.

The framework provides interfaces for central parts of a map visualization system and
additionally example implementations. Fundamental data structures
for nodes, ways, and the map itself can be replaced. The framework supports experiments
with different implementations to optimize routing and also to integrate a database
version of the map representation. The application `aimax.osm.gui.swing.applications.MiniNaviApp`
demonstrates how to plug the components together and provides means to integrate and test
own versions of the needed components.

Central part of the project is an OSM viewer implementation. It is designed
as an efficient general purpose viewer which is highly configurable and extendable.

The internal default map representation is chosen as close as possible to the
original OSM XML file format. A kd-tree is used to improve rendering efficiency.
Classification and abstraction of map entities as well as their
visual appearance within the drawn map are controlled by declarative
rendering rules. They can be replaced or configured at runtime.
New personal map styles can be created quite easily. See classes
`aimax.osm.viewer.MapStyleFactory` and
`aimax.osm.gui.swing.applications.OsmViewerPlusApp` for ideas how that
can be achieved.

Routing functionality is based on the AIMA-CORE library.
All dependencies from the AIMA libraries are encapsulated in the routing
sub-package (search algorithms) and the gui sub-package (JavaFX/Swing-based frameworks).
So all other packages can also be used as stand-alone library for building general OSM
applications.

In the current version, relation entities are still ignored and the size
of the map when using the default representation should be limited to about 
a million nodes to avoid long loading times. For example, detailed maps of
cities like Berlin can be loaded and displayed without any problem if enough
heap space is provided (VM argument -Xmx500M).

Getting started: Run one of the applications in the
`aimax.osm.gui.fx.applications` or `aimax.osm.gui.swing.applications` package,
or use the corresponding integrated applications one package level up. If no map is
displayed by default, make sure that the main/resource folder is included
in the build path of your project, recompile and start again.
Then, place the mouse inside the map viewer pane. Try mouse-left, mouse-middle,
mouse-right, mouse-drag, ctrl-mouse-left, plus button, minus button, shift-plus, shift-minus,
alt-plus, alt-minus, space, ctrl-space, arrow buttons, and also the mouse-wheel
for navigation, mark setting, and track definition. For routing, at least two
markers must be set.


== Current Release: 2.1.1-AIMAX-OSM JavaFX GUIs Added == 
2.1.1-AIMAX-OSM JavaFX GUIs Added : 18 Dec 2016 :<br>
  * Fixed Jar to launch JavaFX Integrated App by default.

== Keywords ==

Open Street Map, OSM, Routing, OSM Viewer, Java


== Requirements ==

# Depends on the aima-core and the aima-gui project. 

# The osm.bz2 support is based on the Apache Commons Compress library
(see http://commons.apache.org/compress/). The corresponding jar-file
together with licence information is provided in the lib directory.
It should be part of the project class path. Otherwise the software will
still run and compile correctly, but the map file choosers will only offer
an OSM file filter.

# To establish a connection to a GPS, the RXTX serial port library
(http://www.rxtx.org/) must be installed. See gps package documentation
for details.


== OSM Maps ==

The provided example map has been created based on data published at:
http://download.geofabrik.de/osm/

Smaller maps from servers like geofabrik or cloudmade can be loaded into the viewer
directly, especially, if enough heap space is provided (e.g. VM argument -Xmx1500M).
Additionally (Swing GUI only!), parts of larger maps can be loaded into the viewer applications
by holding <ctrl> while pressing the load map button and specifying a bounding box.
If two markers are set in the currently visible map, their values are used to define
the bounding box for the next map to load. The coordinates can also be specified in
text form. The easiest way to obtain the needed latitude and longitude bounds is to
select the export tab from page http://www.openstreetmap.org/ and then navigate to the
area which you want to extract. Unfortunately, the XML export typically doesn't
work, but you find the needed coordinates on the left side. An overview map can
be generated for large OSM files by pressing load map while holding the <shift>
button.

Alternatively, data extraction for specific areas such as german cities can be 
performed using the free Osmosis tool. See e.g.
http://wiki.openstreetmap.org/index.php/Osmosis.

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
 * java -jar aimax-osm.jar
   + this will run the default OsmAimaDemoApp, this allows you to run applications and demos from the aima-gui project as well as some of the applications provided in this project.
 * java -classpath aimax-osm.jar aimax.osm.gui.swing.applications.OsmViewerApp
   + just the plain viewer (not dependent on AIMA)
 * java -classpath aimax-osm.jar aimax.osm.gui.swing.applications.OsmViewerPlusApp
   + demonstrates, how to configure and extend the viewer
 * java -classpath aimax-osm.jar aimax.osm.gui.swing.applications.RoutePlannerApp
   + uses aima-core search functionality for routing in OSM maps
 * java -classpath aimax-osm.jar aimax.osm.gui.swing.applications.OsmAgentApp
   + lets map agents from aima-core act in map environments which are defined by OSM data 
 * java -classpath aimax-osm.jar aimax.osm.gui.swing.applications.SearchDemoOsmAgentApp
   + visualizes simulated search space exploration of different search strategies
 * java -classpath aimax-osm.jar aimax.osm.gui.swing.applications.MiniNaviApp
   + provides a base for car navigation system development

It is recommended to start the applications with VM argument -Xmx500m (or higher value) and
program argument -screensize=xx (with xx screen size in inch)

  
= Change History (Update in reverse chronological order) =
2.1.0-AIMAX-OSM JavaFX GUIs Added : 18 Dec 2016 :<br>
   * JavaFX GUIs Added.
   * Reverse map problem generation improved.
   * Minor documentation improvements.
   
2.0.3-AIMAX-OSM Small Improvements : 10 Aug 2014 :<br>
   * One way arrows added.
   * Map renderer is now platform independent. Can also be used for Android. Beta Version!
   * Extendability of RouteCalculator improved.
   * Minor problem with closed ways fixed.
   * Double buffering problem in map view fixed.
   * Minor documentation improvements.
   
2.0.2-AIMAX-OSM Minor Documentation Cleanup : 08 Jan 2012 :<br>
   * Minor documentation improvements.
   
2.0.1-AIMAX-OSM Minor Fixes : 20 Mar 2011 :<br>
   * Defect fix, markers can be loaded and saved again without error.
   * NmeaReader improved. Now supports more GPS devices.
   * Correctly including commons-compress-1.1.jar in release.
   
2.0.0-AIMAX-OSM Redesign to support DBs : 19 Dec 2010 :<br>
   * Package aima.osm.data is now based on interfaces (implementations selected by factory)
   * Data access operations now designed to support representations using databases.
   * Map readers and writers changed so that that parts of the map can be loaded incrementally.
   * Find functionality improved.
   * Library commmons-compress-1.0.jar replaced by version 1.1 - update your build path!
   
1.0.2-AIMAX-OSM Minor Improvements : 05 Nov 2010 :<br>
   * General application starter added.
   * Default map updated.

1.0.1-AIMAX-OSM Map Selection Improvement : 02 Oct 2010 :<br>
   * Improved selection of OSM Maps.
   * Package mapagent renamed to agent.
   * Documentation improvements.

1.0.0-AIMAX-OSM Consolidated : 22 Aug 2010 :<br>
   * Documentation updated.
   * Minor clean-ups included.
   
0.9.2-AIMAX-OSM Many Improvements : 19 June 2010 :<br>
   * Storage efficiency of map representation improved (references to ways).
   * Rendering speed and quality improved
     (text placement, area sorting, one-way streets, icons, default style).
   * osm.gz2 file format support added (additional file filter in file chooser).
   * reader extended to read parts of a map, specified by bounding box or
     attribute filter (try <ctrl> and <shift> when using a load map button).
   * writer for writing maps to file added (formats osm and osm.bz2).
   * slow zoom (activated with <shift>) added.
   * scale factor replaced by true scale.
   * Mark creation at specified gps positions added, info dialog extended by node position.

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