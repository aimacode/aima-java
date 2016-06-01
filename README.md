# AIMA4e-Java (JDK 8+) [![Build Status](https://travis-ci.org/aimacode/aima-java.svg?branch=AIMA4e)](https://travis-ci.org/aimacode/aima-java)
Java implementation of algorithms from Norvig And Russell's "Artificial Intelligence - A Modern Approach 4th Edition." You can use this in conjunction with a course on AI, or for study on your own. We're loooking for [solid contributors](https://github.com/aimacode/aima-java/wiki/AIMAJava-Contributing) to help.

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
       <td>2.1</td>
       <td>35</td>
       <td>Agent</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/agent/api/Agent.java">Agent</a></td>
   </tr>
   <tr>
       <td>2.3</td>
       <td>36</td>
       <td>Table-Driven-Vacuum-Agent</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/environment/vacuum/agent/TableDrivenVacuumAgent.java">TableDrivenVacuumAgent</a></td>
   </tr>
   <tr>
       <td>2.7</td>
       <td>47</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Table-Driven-Agent.md">Table-Driven-Agent</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/agent/basic/TableDrivenAgent.java">TableDrivenAgent</a></td>
   </tr>
   <tr>
       <td>2.8</td>
       <td>48</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Reflex-Vacuum-Agent.md">Reflex-Vacuum-Agent</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/environment/vacuum/agent/ReflexVacuumAgent.java">ReflexVacuumAgent</a></td>
   </tr>
   <tr>
       <td>2.10</td>
       <td>49</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Simple-Reflex-Agent.md">Simple-Reflex-Agent</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/agent/basic/SimpleReflexAgent.java">SimpleReflexAgent</a></td>
   </tr>
   <tr>
       <td>2.12</td>
       <td>51</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Model-Based-Reflex-Agent.md">Model-Based-Reflex-Agent</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/agent/basic/ModelBasedReflexAgent.java">ModelBasedReflexAgent</a></td>
   </tr>
   <tr>
       <td>3</td>
       <td>66</td>
       <td>Problem</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/api/Problem.java">Problem</a></td>
   </tr>
   <tr>
       <td>3.1</td>
       <td>67</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Simple-Problem-Solving-Agent.md">Simple-Problem-Solving-Agent</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/agent/basic/SimpleProblemSolvingAgent.java">SimpleProblemSolvingAgent</a></td>
   </tr>
   <tr>
       <td>3.2</td>
       <td>68</td>
       <td>Romania</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/environment/map2d/SimplifiedRoadMapOfPartOfRomania.java">SimplifiedRoadMapOfPartOfRomania</a></td>
   </tr>
   <tr>
       <td>3.7</td>
       <td>77</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Tree-Search-and-Graph-Search.md">Tree-Search</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/TreeSearch.java">TreeSearch</a></td>
   </tr>
   <tr>
       <td>3.7</td>
       <td>77</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Tree-Search-and-Graph-Search.md">Graph-Search</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/GraphSearch.java">GraphSearch</a></td>
   </tr>
   <tr>
       <td>3.10</td>
       <td>79</td>
       <td>Node</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/api/Node.java">Node</a></td>
   </tr>
   <tr>
       <td rowspan="2">3.11</td>
       <td rowspan="2">82</td>
       <td rowspan="2"><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Breadth-First-Search.md">Breadth-First-Search</a></td>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/uninformed/BreadthFirstGraphSearch.java">BreadthFirstGraphSearch</a>
       </td>
   </tr>
      <tr>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/uninformed/BreadthFirstTreeSearch.java">BreadthFirstTreeSearch</a>
       </td>
   </tr>
   <tr>
       <td rowspan="2">3.14</td>
       <td rowspan="2">84</td>
       <td rowspan="2"><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Uniform-Cost-Search.md">Uniform-Cost-Search</a></td>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/uninformed/UniformCostGraphSearch.java">UniformCostGraphSearch</a>
       </td>
   </tr>
      <tr>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/uninformed/UniformCostTreeSearch.java">UniformCostTreeSearch</a>
       </td>
   </tr>
   <tr>
       <td rowspan="2">3</td>
       <td rowspan="2">85</td>
       <td rowspan="2">Depth-first Search</td>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/uninformed/DepthFirstGraphSearch.java">DepthFirstGraphSearch</a>
       </td>
   </tr>
   <tr>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/uninformed/DepthFirstTreeSearch.java">DepthFirstTreeSearch</a>
       </td>
   </tr>
   <tr>
       <td>3.17</td>
       <td>88</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Depth-Limited-Search.md">Depth-Limited-Search</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/uninformed/DepthLimitedTreeSearch.java">DepthLimitedTreeSearch</a></td>
   </tr>
   <tr>
       <td>3.18</td>
       <td>89</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Iterative-Deepening-Search.md">Iterative-Deepening-Search</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/uninformed/IterativeDeepeningSearch.java">IterativeDeepeningSearch</a></td>
   </tr>
   <tr>
       <td rowspan="2">3</td>
       <td rowspan="2">92</td>
       <td rowspan="2">Best-First search</td>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/informed/BestFirstGraphSearch.java">BestFirstGraphSearch</a>
       </td>
   </tr>
   <tr>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/informed/BestFirstTreeSearch.java">BestFirstTreeSearch</a>
       </td>
   </tr>
   <tr>
       <td rowspan="2">3</td>
       <td rowspan="2">92</td>
       <td rowspan="2">Greedy best-First search</td>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/informed/GreedyBestFirstGraphSearch.java">GreedyBestFirstGraphSearch</a>
       </td>
   </tr>
   <tr>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/informed/GreedyBestFirstTreeSearch.java">GreedyBestFirstTreeSearch</a>
       </td>
   </tr>
   <tr>
       <td rowspan="2">3</td>
       <td rowspan="2">93</td>
       <td rowspan="2">A* Search</td>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/informed/AStarGraphSearch.java">AStarGraphSearch</a>
       </td>
   </tr>
   <tr>
       <td>
           <a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/informed/AStarTreeSearch.java">AStarTreeSearch</a>
       </td>
   </tr>   
   <tr>
       <td>3.26</td>
       <td>99</td>
       <td><a href="https://github.com/aimacode/aima-pseudocode/blob/master/Recursive-Best-First-Search.md">Recursive-Best-First-Search</a></td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/informed/RecursiveBestFirstSearch.java">RecursiveBestFirstSearch</a></td>
   </tr>
   <tr>
       <td>4.2</td>
       <td>122</td>
       <td>Hill-Climbing</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/local/HillClimbingSearch.java">HillClimbingSearch</a></td>
   </tr>
   <tr>
       <td>4.5</td>
       <td>126</td>
       <td>Simulated-Annealing</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/basic/local/SimulatedAnnealingSearch.java">SimulatedAnnealingSearch</a></td>
   </tr>   
   </tbody>
</table>

---
## TODO (REMEMBER - KEEP IT SIMPLE SIMPLE SIMPLE!!!! :)
### CURRENT (Rewrite of chapters 3 to 6) 
#### Chp 3 
* GraphShortestPathPrioritySearch and TreeShortestPathPrioritySearch potentially 
  need a better mechanism for determining state containment and removal of a node 
  with a lower priority.
* Recursive-Best-First-Search - look to improve/tidy up implementation.
* Add a mechanism for gathering search metrics. Want to make more flexible and introduce less clutter into the core algorithms in order to support.

#### Chp 4
* Follow up on Genetic Algorithm experiments on N-Queens problem (based on aima3e implementation) to determine if performs no better than random selection.

#### Chp 5

#### Chp 6

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


