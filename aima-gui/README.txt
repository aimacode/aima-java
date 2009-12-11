= AIMA-CORE =

== Requirements ==
# Depends on the aima-core project. 

== Current Release: 0.1.0-AIMA3e Published ==
First full release based on the 3rd edition of AIMA. 
This projects contains all the GUI and command line demo 
code from the original separated out from the original 
AIMA2e source tree and has been updated to work with the 
latest version of the aima-core library.

== Running the GUIs and Command Line Demos ==
Under the release/ directory you should find two jar files, aima-core.jar and aima-gui.jar, ensure these are on your CLASSPATH, the different GUI programs that can be run using these are:
 * java -classpath aima-gui.jar aima.gui.applications.search.map.RoutePlanningAgentApp
 ** provides a demo of the different agents/search algorithms described in Chapters 3 and 4, for tackling route planning tasks within simplified Map environments.
 * java -classpath aima-gui.jar aima.gui.applications.vacuum.VacuumApp
 ** provides a demo of the different agents described in Chapter 2 and 3 for tackling the Vacuum World.
 * java -jar aima-gui.jar
 ** this will run the default AIMADemoApp, this is currently just a place holder, which we intend to evolve in future releases.

The following command line demos can be run as well (Note we plan to convert these over to run in the AIMADemoApp):
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
