# AIMA-CORE

### Requirements
JDK 1.8 - is the baseline JDK against which this project is developed.

### Current Release: 3.0.0-Search-Improvements
#### 3.0.0-Search-Improvements : Dec 18 2016
* Releasing as a full version number, corresponding to 3rd edition of book 
  (i.e. all 3rd edition releases will start with 3.x.x going forward).
 
## Details

### Build Instructions
If you just want to use the classes, all you need to do is put the aima-core.jar on your CLASSPATH.

If you want to rebuild from the source, run the unit tests etc.., follow these instructions:

To build from the command line:
* Ensure you have JDK 1.8+ installed.
* Ensure you have [ant](http://ant.apache.org/) installed.
* Download the release archive.
* Unzip
* Go to the aima-core directory
* Type 'ant'. This will generate a build directory, which will include the following sub-directories:
  * bin/ will contain all the main and test Java classes.
  * doc/ will contain generated JavaDoc for the project.
  * release/ will contain a jar file of all the core algorithms.

Note: Many IDE's have built in ant versions. So you may want to try that first. 
Included in the aima-core directory are .classpath and .project files for the [Eclipse](http://www.eclipse.org Eclipse) IDE.

## Using the Code

For examples of how to use the various algorithms and supporting classes, look at the test cases in the parallel directory structure under src/test.


### Notes on Search

To define a problem for (non CSP) search you can follow this steps:
* Choose or implement a class to represent states and another to represent
  actions for navigation through the state space. Often, `String`/ `Integer`
  and `DynamicAction` will do. For the N-Queens-Problem,
  `aima.core.environment.nqueens.NQueensBoard` and
  `aima.core.environment.nqueens.QueenAction` are suitable. Most of the framework
  classes are generic. Their type parameters `S` and `A` should always be bound to
  the classes chosen in this step.
* Provide the functions needed to define the problem. Methods can be used as well
  as nested classes or lambda expressions.
  Class `aima.core.environment.nqueens.NQueensFunctions` contains some example
  implementations, e.g.
  ```java
  public static boolean testGoal(NQueensBoard state) {
     return state.getNumberOfQueensOnBoard() == state.getSize() && state.getNumberOfAttackingPairs() == 0;
  }
  ```
  If you need to do an informed search, you should create a heuristic function
  which implements the `ToDoubleFunction<Node<S, A>>` interface.
* Implement `aima.core.search.framework.problem.Problem` directly using the functional material from the previous step
  or use/extend `aima.core.search.framework.problem.GeneralProblem` to create an instance of the problem to be solved.
  `GeneralProblem` accepts an initial state, an actions function, a result function, a goal test, and a
  step-cost function as constructor arguments.
  ```java
  public static Problem<NQueensBoard, QueenAction> createIncrementalFormulationProblem(int boardSize) {
      return new GeneralProblem<>(new NQueensBoard(boardSize), NQueensFunctions::getIFActions,
          NQueensFunctions::getResult, NQueensFunctions::testGoal, (s, a, sPrimed) -> 1.0);
  }
  ```
That is all you need to do (unless you plan to write a different search than is available in the code base).

To actually search you need to:
* Configure a problem instance (see above).
* Select a search strategy. Configure this with some `QueueSearch` (e.g. `TreeSearch` or `GraphSearch`) if applicable.
* Call methods `findActions` or `findState` directly or instantiate a `SearchAgent`.
* Print any actions and metrics.

A good example (from the NQueens Demo) is:
```java
private static void nQueensWithBreadthFirstSearch() {
    try {
	System.out.println("\nNQueensDemo BFS -->");
        Problem<NQueensBoard, QueenAction> problem =
            NQueensFunctions.createIncrementalFormulationProblem(boardSize);
        SearchForActions<NQueensBoard, QueenAction> search = new BreadthFirstSearch<>(new TreeSearch<>());
        SearchAgent<NQueensBoard, QueenAction> agent = new SearchAgent<>(problem, search);
        printActions(agent.getActions());
        printInstrumentation(agent.getInstrumentation());
    } catch (Exception e) {
	e.printStackTrace();
    }
}
```

For further information about code design, see the [Wiki](https://github.com/aimacode/aima-java/wiki).

### Logic Notes
To use First Order Logic, first you need to create an instance of `aima.core.logic.fol.domain.FOLDomain`
which collects the FOL Constants, Prredicates, and Function etc... that you use to solve a particular problem.

A parser (that understands the Grammar in figure 8.3 (page 293 of AIMA3e) needs to be instantiated with this domain, e.g:
 
    FOLDomain weaponsDomain = DomainFactory.weaponsDomain();
    FOLParser parser = new FOLParser(weaponsDomain);

the basic design of all the logic code is that the parser creates a Composite (Design Patterns by Gamma, et al) parse tree over which various Visitors (Design Patterns by Gamma, et al) traverse. The key difference between the Visitor elucidated in the GOF book and the code is that in the former the visit() methods have a void visit(ConcreteNode) signature while the visitors used in the logic code have a Object visit(ConcreteNode,Object arg) signature. This makes testing easier and allows some recursive code that is hard with the former .


### Probability Notes

I have tried to make the code stick very closely to Dr. Norvig's pseudo-code. Looking at the tests will reveal how to use the code. 


### LearningNotes

#### Main Classes and responsibilities
A <DataSet> is a collection of <Example>s. Wherever you see "examples" in plural in the text, the code uses a DataSet. This makes it easy to aggregate operations that work on collections of examples in one place.

An Example is a collection of Attributes. Each example is a data point for Supervised Learning.

DataSetSpecification and AttributeSpecification do some error checking on the attributes when they are read in from a file or string. At present there are two types of Attributes - A sring attribute, used for datasets like "restaurant" and a Numeric Attribute, which represents attributes which are numbers. These are presently modeled as Doubles.

A Numerizer specifies how a particular DataSet's examples may be converted to Lists of Doubles so they can be used in Neural Networks. There is presently one numerizer in the codebase (IrisDataSetNumerizer) but it is trivial to write more by implementing the Numerizer interface.

#### How to Apply Learners

The DecisionTreeLearner and DecisionListLearner work only on datasets with ordinal attributes (no numbers). Numbers are treated as distinct strings.

The Perceptron and DecisionTreeLearners work on *numerized datasets*. If you intend to work with these, you need to write a DataSetSpecific Numerizer by implementing the Numerizer interface.

1. To import a dataset into a system so that learners can be applied to it, first add a public static DataSet getXDataSet(where "x" is the name of the DataSet you want to import) to the DataSetFactory

2. Learners all implement the Learner interface with 3 methods, train, predict and test. If you want to add a new type of Learner (a partitioning Decision Tree learner perhaps?) you need to implement this interface.

## Change History (Update in reverse chronological order)
#### 0.12.0-Search-Improvements : Dec 18 2016
* Improvements/Simplifications across search related APIs.
* Complete set of algorithms for Chapters 22, 23 and 25 added.
* Moved from JDK-7 to 8 as the baseline for further development.
  
#### 0.11.1-Chp7-Complete : Mar 15 2015
* Fixed Issue 33, Add implementation of - Fig 7.22 SATPlan
* Improved performance of DPLL implementation and added an alternative implementation that uses a couple
  of trivial optimizations to improve performance by about 40% over the default DPLL implementation
  that matches the description in the book.
* New DPLL interface added to allow people to experiment with different implementations in order to try
  out performance enhancement ideas from the book and other sources.
* Added tests for and corrected defects found in the HybridWumpusAgent and WumpusKnowledgeBase implementations.
  
#### 0.11.0-Chp7-Rewrite : 10 Aug 2014
* Rewrite of the algorithms in Chapter 7 to more closely map to pseudo-code
  in book and to resolve outstanding issues.
* Baseline JDK supported by this library has been moved up from 1.6 to 1.7.
* Upgraded JUnit from 4.7 to 4.11.
* General Lexer and Parser Improvements:
  * Tokens now track the position in the input that they started at.
  * More informative Lexer and Parser exceptions are now generated.
    Intended to help with identifying in the input where an error occurred.
* Propositional Parser Improvements:
  * Takes operator precedence into account (i.e. does not require concrete syntax to be fully bracketed).
  * Abstract syntax to Concrete syntax (ie. toString) only outputs brackets when necessary (easier to read)
    so concrete syntax can be parsed back in again unchanged.
  * Square brackets can be used in addition to parenthesis to explicitly indicate precedence.
  * Symbols changed for the following logical connectives:
    * not -> ~
    * and -> &
    * or  -> |
  * Abstract syntax tree (i.e. Sentence) simplified to correspond more closely with description in book.
* FOL Clause synchronization and performance enhancements contributed by Tobias Barth.
* Fixed Issue 31, Add implementation of - Fig 7.1 KB-Agent
* Fixed Issue 32, Add implementation of - Fig 7.20 Hybrid-Wumpus-Agent 
* Fixed Issue 72, Propositional CNF parsing issue.
* Fixed Issue 78, Propositional CNFTransformer fails to transform Sentence.
* Fixed Issue 79, Random/False Bug in AIMA WalkSAT.java
* Fixed Issue 80, small bug in XYLocation.hashCode()
* Fixed Issue 83, Wrong time variable used in WumpusWorldKnowledgeBase.java
* Improvements and defect fixes to CSP logic.
  
#### 0.10.5-Chp4-Rewrite : 09 Oct 2012
* Implemented AND-OR-GRAPH-SEARCH from Chapter 4 (completing the set of algorithms from this chapter).
* Fixed Issue 65, Improve genetic algorithm implementation, rewritten to be easier to use/extend.
* Fixed Issue 73, misplaced tile heuristic function no longer counts the gap.
* Fixed Issue 74, defect in implementation of genetic algorithm, fixed indirectly due to Issue 65 re-implementation.
* Fixed Issue 76, QLearning Agent corrected to know which actions are possible in which states.
* Fixed Issue 77, valid hashCode() method missing on TicTacToeState.
* Minor documentation and code cleanup.
  
#### 0.10.4-Chp5-Rewrite : 08 Jan 2012
* Redesigned and re-implemented adversarial search algorithms from Chapter 5.
  * Rewrote Minimax-Decision and Alpha-Beta-Search algorithms.
  * Redesigned Game interface to more closely reflect that as described in AIMA3e.
  * Added Minimax search with alpha-beta pruning and action ordering (IterativeDeepeningAlphaBetaSearch.java).
  * Updated environment definitions/classes for Tic-Tac-Toe game.
  * Added environment definitions/classes for Connect 4 game.
* Minor documentation cleanup.
  
#### 0.10.3-Chp17n21-Rewrite-DF1 : 16 Sept 2011
* Fixed defect in FrequencyCounter when reset.
    
#### 0.10.2-Chp17n21-Rewrite : 16 Sept 2011
* All of the algorithms from Chapters 17 and 21 have been rewritten.
  * 17.4 Value-Iteration
  * 17.7 Policy-Iteration 
  * 21.2 Passive-ADP-Agent
  * 21.4 Passive-TD-Agent
  * 21.8 Q-Learning-Agent 
* Rewrote Cell World Environment (environment.cellworld) to be independent of use.
* Re-organized probability.hmm package.
* Minor optimization to FrequencyCounter implementation.
* Documentation clean up.
  
#### 0.10.1-Chp15-Rewrite : 31 Jul 2011
* All of the algorithms from Chapter 15 have been rewritten.
  * 15.4 Forward-Backward (3 implementations provided)
  * 15.6 Fixed-Lag-Smoothing 
  * 15.17 Particle-Filtering 
* Added an Iterator interface and supporting methods to CategoricalDistribution and Factor.
  * ProbabilityTable.Iterator removed getPostIterateValue() method from API due to not being general.
* Fixed Issue 63 - all compilation warnings have been resolved or suppressed where appropriate for now.
* Documentation clean up.
  
#### 0.10.0-Chp13-and-14-Rewrite : 03 Jul 2011
* All of the algorithms from Chapters 13 and 14 have been rewritten.
* Rewritten:
  * 14.9 Enumeration-Ask
  * 14.13 Prior-Sample
  * 14.14 Rejection-Sampling 
  * 14.15 Likelihood-Weighting 
  * 14.16 GIBBS-Ask 
* Added:
  * 14.11 Elimination-Ask
* Moved Randomizer interface and related implementation underneath
    `aima.core.util.`
* Moved TwoKeyHashMashMap to sub-package datastructure.
* Fix for Issue 66
* Documentation clean up.
  
#### 0.9.14-Probability-and-Logic-Fixes : 20 Mar 2011
* Resolved Issue 58, related to forward-backward algorithm. 
* Fixed defect in Unifier that would cause incorrect unifications in particular
  edge cases (which would cause unsound proofs).
* Fixed defect in resolution proof step output, that would show an incorrect
  unification. In addition, updated proof step information to make easier
  to read.
    
#### 0.9.13-UBUNTU-Fixes : 19 Dec 2010
* Resolved Issue 56, related to compilation and test failures on Ubuntu platform.
* Propositional ask-tell logic fixed using DPLL.
* Map of Australia location corrected.
* Minor code clean up/re-factoring
  
#### 0.9.12-Online+CSP-Improvements : 05 Nov 2010
* StateAction replaced by TwoKeyHashMap (Online Search)
* NotifyEnvironmentViews renamed to EnvironmentViewNotifier.
* Method createExogenousChange reintroduced (from AIMA2e implementation).
* CSP constraint propagation result handling code cleaned up.
  
#### 0.9.11-CSP+PathCost-Fixes : 02 Oct 2010
* Fixed defect in Breath First Search where the Path Cost metric was not being updated correctly (issue #54).
* Fixed CSP issue with respect to domain reconstruction with backtracking search.
* Re-introduced SimpleEnvironmentView so its easier for people to setup and play with the code.
* Minor documentation improvements.
  
#### 0.9.10-CSP+AC-3 : 22 Aug 2010
* CSP package significantly restructured, added AC-3 implementation.
* Search can now create more than one solution within the same run (see `aima.core.search.framework.SolutionChecker`).
* The N-Queens representation now supports incremental as well as complete-state problem formulation.
* Minor clean-ups included.
* Now compiles on Android 2.1.

#### 0.9.9-AIMAX-OSM Minor Fixes : 09 Feb 2010
* Java Doc now uses newer package-info.java mechanism.
 
#### 0.9.8-AIMAX-OSM Added : 06 Feb 2010
* Minor updates to support addition of aimax-osm project to AIMA3e-Java.
* Vacuum world locations changed from enum to Strings to better support extensibility.
* Queue Searches may now be canceled from within a thread (see CancelableThread).
 
#### 0.9.7-AIMA3e Published : 10 Dec 2009
First full release based on the 3rd edition of AIMA. The following major 
updates have been included in this release:
* Re-organized packages to more closely reflect AIMA3e structure:
* Renamed basic to agent
* Moved general purpose data structures underneath util.
* Moved all Environment implementations under environment.    
* Agent package defined now in terms of interfaces as opposed to
  abstract classes.
* Added explicit Action interface.
* General improvements/enhancements across all the APIs.
* All algorithms from chapters 1-4 have been updated to reflect
  changes in their description in AIMA3e. Primarily this involved
  splitting the Successor function concept from AIMA2e into 
  separate Action and Result functions as described in AIMA3e.
* All tests have been updated to JUnit 4.7, which is included
  explicitly as a testing dependency of this project (see /lib).
* Bug fixes to OnlineDFSAgent and GeneticAlgorithm implementations.
* SetOPs, converted to use static methods based on:
  http://java.sun.com/docs/books/tutorial/collections/interfaces/set.html
* Queue implementations now extends Java's corresponding collection classes.
* Dependencies between Map Agents and their Environment have been
  decoupled by introducing appropriate intermediaries.
* All source formatted using Eclipse 3.4 default settings.

#### 0.95-AIMA2eFinal : 03 Oct 2009
Last full release based on the 2nd edition of AIMA. This is our first release 
containing GUIs (thanks to Ruediger Lunde):
* `aima.gui.applications.VacuumAppDemo`
  Provides a demo of the different agents described in Chapter 2 and 3
  for tackling the Vacuum World.
* `aima.gui.applications.search.map.RoutePlanningAgentAppDemo`
  Provides a demo of the different agents/search algorithms described 
  in Chapters 3 and 4, for tackling route planning tasks within 
  simplified Map environments.
* `aima.gui.framework.SimpleAgentAppDemo`
  Provides a basic example of how to create your own Agent based 
  demonstrations based on the provided framework.

This will also be our last full release based on the 2nd edition of AIMA. 
We are currently in the planning phases to re-organize this project based 
on the 3rd edition of AIMA, which should be available soon.
