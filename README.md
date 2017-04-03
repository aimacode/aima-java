# AIMA4e-Java (JDK 8+) [![Build Status](https://travis-ci.org/aimacode/aima-java.svg?branch=AIMA4e)](https://travis-ci.org/aimacode/aima-java)
Java implementation of algorithms from Russell and Norvig's "Artificial Intelligence - A Modern Approach 4th Edition." You can use this in conjunction with a course on AI, or for study on your own. We're loooking for [solid contributors](https://github.com/aimacode/aima-java/wiki/AIMAJava-Contributing) to help.

---
### NOTE: This is an in progress complete rewrite of the algorithms, leveraging JDK 8's new language features, from the AIMA3e branch (currently master branch). This will also become the new master branch once the 4th edition of "Artificial Intelligence - A Modern Approach" is published.

---
## Index of Implemented Algorithms
<table style="width:100%">
   <tbody>
   <tr>
       <td><b>Fig</b></td>
       <td><b>Page</b></td>
       <td><b>Name/Pseudocode (in book)</b></td>
       <td><b>Implementation(s)</b></td>
   </tr>
   <tr>
       <td>2.?</td>
       <td>??</td>
       <td>Agent</td>
       <td><a href="core/src/main/java/aima/core/agent/api/Agent.java">Agent</a></td>
   </tr>
   <tr>
       <td>2.?</td>
       <td>??</td>
       <td>Table-Driven-Vacuum-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/environment/vacuum/TableDrivenVacuumAgent.java">TableDrivenVacuumAgent</a></td>
   </tr>
   <tr>
       <td>2.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Table-Driven-Agent.md">Table-Driven-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/agent/basic/TableDrivenAgent.java">TableDrivenAgent</a></td>
   </tr>
   <tr>
       <td>2.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Reflex-Vacuum-Agent.md">Reflex-Vacuum-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/environment/vacuum/ReflexVacuumAgent.java">ReflexVacuumAgent</a></td>
   </tr>
   <tr>
       <td>2.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Simple-Reflex-Agent.md">Simple-Reflex-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/agent/basic/SimpleReflexAgent.java">SimpleReflexAgent</a></td>
   </tr>
   <tr>
       <td>2.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Model-Based-Reflex-Agent.md">Model-Based-Reflex-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/agent/basic/ModelBasedReflexAgent.java">ModelBasedReflexAgent</a></td>
   </tr>
   <tr>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
   </tr>
   <tr>
       <td>3?</td>
       <td>??</td>
       <td>Problem</td>
       <td><a href="core/src/main/java/aima/core/search/api/Problem.java">Problem</a></td>
   </tr>
   <tr>
       <td>3?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Simple-Problem-Solving-Agent.md">Simple-Problem-Solving-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/agent/basic/SimpleProblemSolvingAgent.java">SimpleProblemSolvingAgent</a></td>
   </tr>
   <tr>
       <td>3.?</td>
       <td>??</td>
       <td>Romania</td>
       <td><a href="core/src/main/java/aima/core/environment/map2d/SimplifiedRoadMapOfPartOfRomania.java">SimplifiedRoadMapOfPartOfRomania</a></td>
   </tr>
   <tr>
       <td rowspan="5">3.?</td>
       <td rowspan="5">??</td>
       <td rowspan="5"><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Tree-Search-and-Graph-Search.md">Tree-Search</a></td>
       <td>
       <a href="core/src/main/java/aima/core/search/basic/uninformed/TreeSearch.java">TreeSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       Alternative(s)
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/TreeQueueSearch.java">TreeQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/TreeGoalTestedFirstQueueSearch.java">TreeGoalTestedFirstQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/TreePriorityQueueSearch.java">TreePriorityQueueSearch</a>
       </td>
   </tr>   
   <tr>
       <td rowspan="6">3.?</td>
       <td rowspan="6">??</td>
       <td rowspan="6"><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Tree-Search-and-Graph-Search.md">Graph-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/uninformed/GraphSearch.java">GraphSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       Alternative(s)
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/GraphQueueSearch.java">GraphQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/GraphGoalTestedFirstQueueSearch.java">GraphGoalTestedFirstQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/GraphPriorityQueueSearch.java">GraphPriorityQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/GraphRLPriorityQueueSearch.java">GraphRLPriorityQueueSearch</a>
       </td>
   </tr>  
   <tr>
       <td>3.?</td>
       <td>??</td>
       <td>Node</td>
       <td><a href="core/src/main/java/aima/core/search/api/Node.java">Node</a></td>
   </tr>
   <tr>
       <td rowspan="3">3.?</td>
       <td rowspan="3">??</td>
       <td rowspan="3"><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Breadth-First-Search.md">Breadth-First-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/uninformed/BreadthFirstSearch.java">BreadthFirstSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       Alternative(s)
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/BreadthFirstQueueSearch.java">BreadthFirstQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td rowspan="3">3.?</td>
       <td rowspan="3">??</td>
       <td rowspan="3"><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Uniform-Cost-Search.md">Uniform-Cost-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/uninformed/UniformCostSearch.java">UniformCostSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       Alternative(s)
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/UniformCostQueueSearch.java">UniformCostQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td>3?</td>
       <td>??</td>
       <td>Depth-first Search</td>
       <td>
       <a href="extra/src/main/java/aima/extra/search/pqueue/uninformed/DepthFirstQueueSearch.java">DepthFirstQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td>3.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Depth-Limited-Search.md">Depth-Limited-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/uninformed/DepthLimitedTreeSearch.java">DepthLimitedTreeSearch</a></td>
   </tr>
   <tr>
       <td>3.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Iterative-Deepening-Search.md">Iterative-Deepening-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/uninformed/IterativeDeepeningSearch.java">IterativeDeepeningSearch</a></td>
   </tr>
   <tr>
       <td rowspan="4">3?</td>
       <td rowspan="4">??</td>
       <td rowspan="4">Bidirectional search</td>
       <td>
           <!-- TODO -->
       </td>
   </tr>  
   <tr>
       <td>
       Alternative(s)
       </td>
   </tr>
   <tr>
       <td>
        <a href="extra/src/main/java/aima/extra/search/uninformed/BidirectionalSearchMRS.java">BidirectionalSearchMRS</a>
       </td>
   </tr>   
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/uninformed/BidirectionalSearchGW.java">BidirectionalSearchGW</a>
       </td>
   </tr>
   <tr>
       <td>3?</td>
       <td>??</td>
       <td>Best-First search</td>
       <td>
           <a href="extra/src/main/java/aima/extra/search/pqueue/informed/BestFirstQueueSearch.java">BestFirstQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td>3?</td>
       <td>??</td>
       <td>Greedy best-First search</td>
       <td>
           <a href="extra/src/main/java/aima/extra/search/pqueue/informed/GreedyBestFirstQueueSearch.java">GreedyBestFirstQueueSearch</a>
       </td>
   </tr>
   <tr>
       <td rowspan="3">3?</td>
       <td rowspan="3">??</td>
       <td rowspan="3">A* Search</td>
       <td>
           <a href="core/src/main/java/aima/core/search/basic/informed/AStarSearch.java">AStarSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       Alternative(s)
       </td>
   </tr>
   <tr>
       <td>
       	<a href="extra/src/main/java/aima/extra/search/pqueue/informed/AStarQueueSearch.java">AStarQueueSearch</a>
       </td>
   </tr>    
   <tr>
       <td>3.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Recursive-Best-First-Search.md">Recursive-Best-First-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/informed/RecursiveBestFirstSearch.java">RecursiveBestFirstSearch</a></td>
   </tr>
   <tr>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
   </tr>
   <tr>
       <td rowspan="3">4.?</td>
       <td rowspan="3">??</td>
       <td rowspan="3"><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Hill-Climbing.md">Hill-Climbing</a></td>
		<td>
       	<a href="core/src/main/java/aima/core/search/basic/local/HillClimbingSearch.java">HillClimbingSearch</a>
       </td>
   </tr>
   <tr>
       <td>
       Alternative(s)
       </td>
   </tr>
   <tr>
       <td>
       	<a href="extra/src/main/java/aima/extra/search/local/HillClimbingSearchWithSidewaysMoves.java">HillClimbingSearchWithSidewaysMoves</a>
       </td>
   </tr>   
   <tr>
       <td>4.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Simulated-Annealing.md">Simulated-Annealing</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/local/SimulatedAnnealingSearch.java">SimulatedAnnealingSearch</a></td>
   </tr>
   <tr>
       <td>4.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Genetic-Algorithm.md">Genetic-Algorithm</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/local/GeneticAlgorithm.java">GeneticAlgorithm</a></td>
   </tr>
   <tr>
       <td>4.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/And-Or-Graph-Search.md">And-Or-Graph-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/contingency/AndOrGraphSearch.java">AndOrGraphSearch</a></td>
   </tr>
   <tr>
       <td>4.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Online-DFS-Agent.md">Online-DFS-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/agent/basic/OnlineDFSAgent.java">OnlineDFSAgent</a></td>
   </tr>
   <tr>
       <td>4.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/LRTAStar-Agent.md">LRTA*-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/agent/basic/LRTAStarAgent.java">LRTAStarAgent</a></td>
   </tr>
   <tr>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
   </tr>
   <tr>
       <td>5.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Minimax-Decision.md">Minimax-Decision</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/adversarial/MinimaxDecision.java">MinimaxDecision</a></td>
   </tr>
   <tr>
       <td rowspan="3">5.?</td>
       <td rowspan="3">??</td>
       <td rowspan="3"><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Alpha-Beta-Search.md">Alpha-Beta-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/adversarial/AlphaBetaSearch.java">AlphaBetaSearch</a></td>
   </tr>
   <tr>
       <td>
       Alternative(s)
       </td>
   </tr>
   <tr>
       <td>
       <a href="extra/src/main/java/aima/extra/search/adversarial/IterativeDeepeningAlphaBetaSearch.java">IterativeDeepeningAlphaBetaSearch</a>
       </td>
   </tr>   
   <tr>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
   </tr>
   <tr>
       <td>6.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/AC-3.md">AC-3</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/csp/AC3.java">AC3</a></td>
   </tr>
   <tr>
       <td>6.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Backtracking-Search.md">Backtracking-Search</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/csp/BacktrackingSearch.java">BacktrackingSearch</a></td>
   </tr>
   <tr>
       <td>6.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Min-Conflicts.md">Min-Conflicts</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/csp/MinConflicts.java">MinConflicts</a></td>
   </tr>
   <tr>
       <td>6.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Tree-CSP-Solver.md">Tree-CSP-Solver</a></td>
       <td><a href="core/src/main/java/aima/core/search/basic/csp/TreeCSPSolver.java">TreeCSPSolver</a></td>
   </tr>
   <tr>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
   </tr>
   <tr>
       <td>7.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/KB-Agent.md">KB-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/agent/basic/KBAgent.java">KBAgent</a></td>
   </tr>
   <tr>
       <td>7.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/TT-Entails.md">TT-Entails</a></td>
       <td><a href="core/src/main/java/aima/core/logic/basic/propositional/inference/TTEntails.java">TTEntails</a></td>
   </tr>
   <tr>
       <td>7.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/PL-Resolution.md">PL-Resolution</a></td>
       <td><a href="core/src/main/java/aima/core/logic/basic/propositional/inference/PLResolution.java">PLResolution</a></td>
   </tr>
   <tr>
       <td>7.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/PL-FC-Entails.md">PL-FC-Entails?</a></td>
       <td><a href="core/src/main/java/aima/core/logic/basic/propositional/inference/PLFCEntails.java">PLFCEntails</a></td>
   </tr>
   <tr>
       <td>7.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/DPLL-Satisfiable.md">DPLL-Satisfiable?</a></td>
       <td><a href="core/src/main/java/aima/core/logic/basic/propositional/inference/DPLLSatisfiable.java">DPLLSatisfiable</a></td>
   </tr>
   <tr>
       <td>7.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/WalkSAT.md">WalkSAT</a></td>
       <td><a href="core/src/main/java/aima/core/logic/basic/propositional/inference/WalkSAT.java">WalkSAT</a></td>
   </tr>
   <tr>
       <td>7.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/Hybrid-Wumpus-Agent.md">Hybrid-Wumpus-Agent</a></td>
       <td><a href="core/src/main/java/aima/core/environment/wumpusworld/HybridWumpusAgent.java">HybridWumpusAgent</a></td>
   </tr>
   <tr>
       <td>7.?</td>
       <td>??</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/md/SATPlan.md">SATPlan</a></td>
       <td><a href="core/src/main/java/aima/core/logic/basic/propositional/inference/SATPlan.java">SATPlan</a></td>
   </tr>
   <tr>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
   </tr>
   </tbody>
</table>

---
## TODO (REMEMBER - KEEP IT SIMPLE SIMPLE SIMPLE!!!! :)
### CURRENT (Rewrite of chapters 3 to 6) 
#### Chp 3 
* Uniform-Cost-Search NoOp case and need for small constant.
* GraphPriorityQueueSearch and TreePriorityQueueSearch potentially 
  need a better mechanism for determining state containment and removal of a node 
  with a lower priority (i.e. AbstractQueueSearchForActions.removedNodeFromFrontierWithSameStateAndLowerPriority()).
* Recursive-Best-First-Search - look to improve/tidy up implementation.
* Add a mechanism for gathering search metrics. Want to make more flexible and introduce less clutter into the core algorithms in order to support.

#### Chp 4
* Follow up on Genetic Algorithm experiments on N-Queens problem (based on aima3e implementation) to determine if performs no better than random selection.
* Online-DFS-Agent - Clarify the need for the additional check `if (!sPrime.equals(result.get(s, a))) {`, which is not present in the pseudocode but required to stop it looping endlessly on certain test problems.

#### Chp 5

#### Chp 6

#### Chp 8
* Restructure the AST for FOL to have clean parser calls.

### LATER

#### Chapter 4 'core' module.
* Add tests for all implemented algorithms.

#### Chapter 3 'core' module.
* Add tests for all implemented algorithms.

#### Chapter 2 'core' module.
* Add tests for all implemented algorithms.

#### Chapter 2 'extra' module.
* Environment definition: Consider specifying Dimensions in API, see pg. 42.
* Environment Simulator referenced on pg. 45 (this will be a re-factor of a lot of the environment stuff in aima3e-core).

---

## USEFUL RESOURCES
### GUI
* http://docs.oracle.com/javase/8/javase-clienttechnologies.htm
* https://bitbucket.org/controlsfx/controlsfx/overview
* http://fxexperience.com/
* https://bitbucket.org/Jerady/fontawesomefx
* http://fortawesome.github.io/Font-Awesome/get-started/
* http://www.pythontutor.com/
* https://svgsalamander.java.net/docs/use.html
* http://blog.netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
* http://www.mrlonee.com/?p=319
* http://harmoniccode.blogspot.com/
* https://rterp.wordpress.com/2014/07/28/adding-custom-javafx-component-to-scene-builder-2-0-part-2/


