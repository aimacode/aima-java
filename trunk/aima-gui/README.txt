= AIMA-GUI =

by Ruediger Lunde (Ruediger.Lunde@gmail.com)

This project contains graphical applications and command line demos, which
demonstrate the use of some of the aima-core project features. Currently, it
focuses on search algorithms and agent concepts. Application class names end
with "App" and command line demo class names with "Demo". An all-in-one
application called aima.gui.applications.AimaDemoApp is provided which is
a starter for all the other applications and demos.

== Requirements ==
# Depends on the aima-core project. 

== Current Release: 1.0.5-AIMA-GUI Updated ==
1.0.5-AIMA-GUI Updated : 31 Jul 2011 :<br>
  * Updated to work with the latest version of aima-core (0.10.1).
  ** Added demo of Fixed-Lag-Smoothing to command line probability demo.

== Running the GUIs and Command Line Demos ==
Under the release/ directory you should find two jar files, aima-core.jar and aima-gui.jar, ensure these are on your CLASSPATH, the different GUI programs that can be run using these are:
 * java -jar aima-gui.jar
   + this will run the default AimaDemoApp, this allows you to run all other applications from a central location as well as all of the current command line demos.
 
 * java -classpath aima-gui.jar aima.gui.applications.vacuum.VacuumApp
   + provides a demo of the different agents described in Chapter 2 for tackling the Vacuum World.
  * java -classpath aima-gui.jar aima.gui.applications.search.games.EightPuzzleApp
   + provides a demo of the different search algorithms described in Chapter 3 and 4.
 * java -classpath aima-gui.jar aima.gui.applications.search.games.NQueensApp
   + provides a demo of the different search algorithms described in Chapter 3 and 4.
 * java -classpath aima-gui.jar aima.gui.applications.search.games.TicTacToeApp
   + provides a demo of the different search algorithms described in Chapter 5.
 * java -classpath aima-gui.jar aima.gui.applications.search.map.RouteFindingAgentApp
   + provides a demo of the different agents/search algorithms described in Chapters 3 and 4, for tackling route planning tasks within simplified Map environments.
 * java -classpath aima-gui.jar aima.gui.applications.search.csp.MapColoringApp
   + provides a demo of the different csp algorithms described in Chapters 6.
 
The following command line demos can be run as well:
 * java -classpath aima-gui.jar aima.gui.demo.agent.TrivialVacuumDemo
 * java -classpath aima-gui.jar aima.gui.demo.learning.LearningDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.DPLLDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.FolDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.PLFCEntailsDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.PLResolutionDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.TTEntailsDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.WalkSatDemo
 * java -classpath aima-gui.jar aima.gui.demo.probability.ProbabilityDemo
 * java -classpath aima-gui.jar aima.gui.demo.search.CSPDemo
 * java -classpath aima-gui.jar aima.gui.demo.search.EightPuzzleDemo
 * java -classpath aima-gui.jar aima.gui.demo.search.NQueensDemo
 * java -classpath aima-gui.jar aima.gui.demo.search.TicTacToeDemo
 

= Change History (Update in reverse chronological order) =
1.0.4-AIMA-GUI Updated : 03 Jul 2011 :<br>
  * Updated to work with the latest version of aima-core (0.10.0).
  
1.0.3-AIMA-GUI Updated : 19 Dec 2010 :<br>
  * Updated to work with the latest version of aima-core.
  * Menu entry name fixed in MapColoringApp.
  
1.0.2-AIMA-GUI Clean Up : 05 Nov 2010 :<br>
  * AimaDemoFrame now shows complete package path names and orders them lexicographically.
  * CSPDemo renamed to MapColoringCSPDemo.
  
1.0.1-AIMA-GUI Old Demo Re-Added : 02 Oct 2010 :<br>
  * Re-added the command line Trivial Vacuum Demo so that its easier 
    for someone to get up and running with the code.
    
1.0.0-AIMA-GUI New Games : 22 Aug 2010 :<br>
  * New graphical game applications added (8-Puzzle, N-Queens, TicTacToe).
  * New graphical CSP application added (map coloring).
  * Code consolidated and documentation updated.
  
0.2.0-AIMA-GUI Enhancements : 15 Mar 2010 :<br>
New features added (inspired by course TDDC17)
  * Added all command line demos and GUI demos to a
    unified interface for running all of them from one
    place.
  * Agent simulator now has step and pause button.
  * Logic for simulation control buttons improved.
  * Design of 2d vacuum view updated.
  * Vacuum symbol now animated.
  * AgentThread renamed to SimulationThread.
  * Map agent controller cleaned up.
  * Documentation updated.

0.1.2-AIMAX-OSM Minor Fixes : 09 Feb 2010 :<br>
  * Java Doc now uses newer package-info.java mechanism.
  * Documentation improvements.
  
0.1.1-AIMAX-OSM Added : 06 Feb 2010 :<br>
 * Major redesign, structures simplified, agent environment now serves as mvc-model.
 * Minor updates to support addition of aimax-osm project to AIMA3e-Java.
 
0.1.0-AIMA3e Published : 10 Dec 2009 :<br>
First full release based on the 3rd edition of AIMA. This projects contains all the GUI and command line demo 
code separated out from the original AIMA2e source tree and has been updated to work with the 
latest version of the aima-core library.