= AIMA-OSM =

This project provides a framework for building intelligent Open Street Map
data applications. It was originally designed to validate and test agent
and search concepts from the AIMA library in an interesting, non-trivial
application area.

Central part of the project is an OSM viewer. It is designed as a
general purpose viewer which is highly configurable and extendible.
The internal map representation is chosen as close as possible to the
original OSM file. Classification and abstraction lies in the responsibility
of the renderer. A rather general renderer implementation is included,
which is based on declarative rendering rules.

Routing functionality is based on the AIMA-CORE library.
All dependencies to the AIMA libraries are encapsulated in the routing
sub-package and the applications using functionality from that package.
So the viewer classes can also be used as stand-alone library
for building general OSM applications.

In the current version, relation entities are ignored and
the size of the map should be limited to about a million nodes
to avoid long loading times. The tool Osmosis can be used to generate
maps complying to this requirement.

Getting started: Run one of the classes in the applications sub-package.
If no map is displayed, edit the map file name in the main method of the
application's source file and try again.
Then place the mouse inside the map viewer pane. Try Mouse-Left, Mouse-Right,
Mouse-Drag, Ctrl-Mouse-Left, Plus button, Minus button, Ctrl-Plus, Ctrl-Minus,
arrow buttons, and also the Mouse-Wheel for navigation,
mark setting, and track definition. Before routing, at least two marks
must be set.</p>

== Requirements ==
# Depends on the aima-core and the aima-gui project. 

== Current Release: 0.1.1-AIMA3e Published ==
First release based on the 3rd edition of AIMA which contains the
OSM library.

== Applications ==

aimax.osm.applications.OsmViewerApp
- just the plain viewer (not dependent on AIMA)
aimax.osm.applications.OsmViewerPlusApp
- demonstrates, how to configure and extend a viewer
aimax.osm.applications.RoutePlannerApp
- uses AIMA-CORE search functionality for routing in OSM maps
aimax.osm.applications.OsmAgentApp
- lets map agents from AIMA-CORE act in map environments which are defined by OSM data 
aimax.osm.applications.OsmSearchDemoAgentApp
- visualizes simulated search space exploration of different search strategies
aimax.osm.applications.MiniNaviApp
- provides a base for GPS navigation system development