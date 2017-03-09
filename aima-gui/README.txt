= AIMA-GUI =

by Ruediger Lunde (Ruediger.Lunde@gmail.com)

This project contains graphical applications and command line demos, which
demonstrate the use of some of the aima-core project features. Currently, it
focuses on search algorithms and agent concepts. Application class names end
with "App" and command line demo class names end with "Demo".
Simple demo command line applications can be found in package aima.gui.demo.
Graphical demo applications are available based on JavaFX (package aima.gui.fx.applications) and
Swing (package aima.gui.swing.applications). Each platform-specific root package contains an
integrated application providing access to all integrable demos. The individual demos are placed
in sub-packages. Note that JavaFX and Swing demos are structured quite differently.
They use different frameworks (sub-package framework) and also differ in function.

== Requirements ==
# Depends on the aima-core project. 

== Current Release: 1.1.1-AIMA-GUI JavaFX GUIs Added ==
1.1.1-JavaFX GUIs Added : 18 Dec 2016 :<br>
  * Fixed Jar to launch JavaFX Integrated App by default.
  
== Running the GUIs and Command Line Demos ==
Under the release/ directory you should find two jar files, aima-core.jar and aima-gui.jar,
ensure these are on your CLASSPATH, the different GUI programs that can be run using these are:
 * java -jar aima-gui.jar
   + this will run the default IntegratedAimaFxApp. It allows you to run all other JavaFX applications from a central location as well as all of the current command line demos.
* java -classpath aima-gui.jar aima.gui.swing.applications.IntegratedAimaApp
   + this will run the Swing version of the integrated AIMA application.

JavaFX and Swing demo applications can be started directly. Here, only the Swing versions are listed: 
 * java -classpath aima-gui.jar aima.gui.swing.applications.vacuum.VacuumApp
   + provides a demo of the different agents described in Chapter 2 for tackling the Vacuum World.
  * java -classpath aima-gui.jar aima.gui.swing.applications.search.games.EightPuzzleApp
   + provides a demo of the different search algorithms described in Chapter 3 and 4.
 * java -classpath aima-gui.jar aima.gui.swing.applications.search.games.NQueensApp
   + provides a demo of the different search algorithms described in Chapter 3 and 4.
 * java -classpath aima-gui.jar aima.gui.swing.applications.search.games.TicTacToeApp
   + provides a demo of the different search algorithms described in Chapter 5.
 * java -classpath aima-gui.jar aima.gui.swing.applications.search.map.RouteFindingAgentApp
   + provides a demo of the different agents/search algorithms described in Chapters 3 and 4, for tackling route planning tasks within simplified Map environments.
 * java -classpath aima-gui.jar aima.gui.swing.applications.search.csp.MapColoringApp
   + provides a demo of the different csp algorithms described in Chapters 6.
 
The following command line demos can be run as well:
 * java -classpath aima-gui.jar aima.gui.demo.agent.TrivialVacuumDemo
 * java -classpath aima-gui.jar aima.gui.demo.learning.LearningDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.DpllDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.FolDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.PlFcEntailsDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.PLResolutionDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.TTEntailsDemo
 * java -classpath aima-gui.jar aima.gui.demo.logic.WalkSatDemo
 * java -classpath aima-gui.jar aima.gui.demo.probability.ProbabilityDemo
 * java -classpath aima-gui.jar aima.gui.demo.search.CSPDemo
 * java -classpath aima-gui.jar aima.gui.demo.search.EightPuzzleDemo
 * java -classpath aima-gui.jar aima.gui.demo.search.NQueensDemo
 * java -classpath aima-gui.jar aima.gui.demo.search.TicTacToeDemo
 

= Change History (Update in reverse chronological order) =
1.1.0-JavaFX GUIs Added : 18 Dec 2016 :<br>
  * Java FX based GUIs added.
  * Simulated Annealing, FX based, Application Added.
  * Genetic Algorithm, FX based, Application Added.
  * Updated to work with the latest version of aima-core (0.12.0).
      * Search Framework improvements.
      * Bidirectional Search options added to GUI demo applications.
  * Monte Carlo Localization (MCL), Swing based, Application Added.
  
1.0.9-Chp 7 Rewrite Support : 10 Aug 2014 :<br>
  * Updated to work with the latest version of aima-core (0.11.0).
  
1.0.8-AIMA-GUI And Or Search Supported : 09 Oct 2012 :<br>
  * Updated to work with the latest version of aima-core (0.10.5).
  
1.0.7-AIMA-GUI Connect4 Added : 08 Jan 2012 :<br>
  * Updated to work with the latest version of aima-core (0.10.4).
  * Added Connect 4 game, which demonstrates improved adversarial search capabilities of aima-core.
  * Tic-Tac-Toe command line and gui rewritten to work with improved APIs.
  
1.0.6-AIMA-GUI Updated : 16 Sept 2011 :<br>
  * Updated to work with the latest version of aima-core (0.10.2).
  ** Moved Chapter 21 Demos on Reinforcement Learning to LearningDemo from ProbabilityDemo.
  ** Updated Reinforcement Learning Demos to generate data that can be used to create graphs of learning rates and RMS errors in utility.

1.0.5-AIMA-GUI Updated : 31 Jul 2011 :<br>
  * Updated to work with the latest version of aima-core (0.10.1).
  ** Added demo of Fixed-Lag-Smoothing to command line probability demo.
  
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
