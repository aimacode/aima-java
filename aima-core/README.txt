= AIMA-CORE =

== Requirements ==
# JDK 1.6 - is the baseline JDK against which this project is developed. 


== Current Release: 0.10.1-Chp15-Rewrite ==
0.10.1-Chp15-Rewrite : 31 Jul 2011 :<br>
  * All of the algorithms from Chapter 15 have been rewritten.
  ** 15.4 Forward-Backward (3 implementations provided)
  ** 15.6 Fixed-Lag-Smoothing 
  ** 15.17 Particle-Filtering 
  * Added an Iterator interface and supporting methods to CategoricalDistribution and Factor.
  ** ProbabilityTable.Iterator removed getPostIterateValue() method from API due to not being general.
  * Fixed Issue 63 - all compilation warnings have been resolved or suppressed where appropriate for now.
  * Documentation clean up.
 
= Details =

== Build Instructions ==
If you just want to use the classes, all you need to do is put the release/aima-core.jar on your CLASSPATH.

If you want to rebuild from the source, run the unit tests etc.., follow these instructions:

To build from the command line:
  # Ensure you have [http://ant.apache.org/ ant] installed.
  # Download the release archive.
  # Unzip
  # Go to the aima-core directory
  # Type 'ant'. This will generate a build directory, which will include the following sub-directories:
    # bin/ will contain all the main and test Java classes.
    # doc/ will contain generated JavaDoc for the project.
    # release/ will contain a jar file of all the core algorithms.

Note: Many IDE's have built in ant versions. So you may want to try that first. 
Included in the aima-core directory are .classpath and .project files for the [http://www.eclipse.org Eclipse] IDE.


== Index of Implemented Algorithms ==
|| *Fig* || *Page* || *Name (in book)*             || *Code* ||
||   2   ||      34|| Environment                  ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/agent/Environment.java Environment]||
||   2.1 ||      35|| Agent                        ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/agent/Agent.java Agent]||
||   2.3 ||      36|| Table-Driven-Vacuum-Agent    ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/environment/vacuum/TableDrivenVacuumAgent.java TableDrivenVacuumAgent]||
||   2.7 ||      47|| Table-Driven-Agent           ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/agent/impl/aprog/TableDrivenAgentProgram.java TableDrivenAgent]||
||   2.8 ||      48|| Reflex-Vacuum-Agent          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/environment/vacuum/ReflexVacuumAgent.java ReflexVacuumAgent]||
||   2.10||      49|| Simple-Reflex-Agent          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/agent/impl/aprog/SimpleReflexAgentProgram.java SimpleReflexAgentProgram]||
||   2.12||      51|| Model-Based-Reflex-Agent     ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/agent/impl/aprog/ModelBasedReflexAgentProgram.java ModelBasedReflexAgentProgram]||
||   3   ||      66|| Problem                      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/Problem.java Problem]||
||   3.1 ||      67|| Simple-Problem-Solving-Agent ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/SimpleProblemSolvingAgent.java SimpleProblemSolvingAgent]||
||   3.2 ||      68|| Romania                      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/environment/map/SimplifiedRoadMapOfPartOfRomania.java SimplifiedRoadMapOfPartOfRomania]||
||   3.7 ||      77|| Tree-Search                  ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/TreeSearch.java TreeSearch]||
||   3.7 ||      77|| Graph-Search                 ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/GraphSearch.java GraphSearch]||
||   3.10||      79|| Node                         ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/Node.java Node]||
||   3   ||      79|| Queue                        ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/util/datastructure/Queue.java Queue]||
||   3.11||      82|| Breadth-First-Search         ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/uninformed/BreadthFirstSearch.java BreadthFirstSearch]||
||   3.14||      84|| Uniform-Cost-Search          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/uninformed/UniformCostSearch.java UniformCostSearch]||
||   3   ||      85|| Depth-first Search           ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/uninformed/DepthFirstSearch.java DepthFirstSearch]||
||   3.17||      88|| Depth-Limited-Search         ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/uninformed/DepthLimitedSearch.java DepthLimitedSearch]||
||   3.18||      89|| Iterative-Deepening-Search   ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/uninformed/IterativeDeepeningSearch.java IterativeDeepeningSearch]||
||   3   ||      90|| Bidirectional search         ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/uninformed/BidirectionalSearch.java BidirectionalSearch]||
||   3   ||      92|| Best-First search            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/informed/BestFirstSearch.java BestFirstSearch]||
||   3   ||      92|| Greedy best-First search     ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/informed/GreedyBestFirstSearch.java GreedyBestFirstSearch]||
||   3   ||      93|| A`*` search                  ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/informed/AStarSearch.java AStarSearch]||
||   3.26||      99|| Recursive-Best-First-Search  ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/informed/RecursiveBestFirstSearch.java RecursiveBestFirstSearch]||
||   4.2 ||     122|| Hill-Climbing                ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/local/HillClimbingSearch.java HillClimbingSearch]||
||   4.5 ||     126|| Simulated-Annealing          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/local/SimulatedAnnealingSearch.java SimulatedAnnealingSearch]||
||   4.8 ||     129|| Genetic-Algorithm            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/local/GeneticAlgorithm.java GeneticAlgorithm]||
||   4.11||     136|| And-Or-Graph-Search          ||---||
||   4   ||     147|| Online search problem        ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/online/OnlineSearchProblem.java OnlineSearchProblem] ||
||   4.21||     150|| Online-DFS-Agent             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/online/OnlineDFSAgent.java OnlineDFSAgent] ||
||   4.24||     152|| LRTA`*`-Agent                ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/online/LRTAStarAgent.java LRTAStarAgent] ||
||   5.3 ||     166|| Minimax-Decision             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/adversarial/Game.java (method)-makeMiniMaxMove()]||
||   5.7 ||     170|| Alpha-Beta-Search            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/adversarial/Game.java (method)-makeAlphaBetaMove()]||
||   6   ||     202|| CSP                          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/CSP.java CSP]||
||   6.1 ||     204|| Map CSP                      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/MapCSP.java MapCSP]||
||   6.3 ||     209|| AC-3                         ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/AC3Strategy.java AC3Strategy]||
||   6.5 ||     215|| Backtracking-Search          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/BacktrackingStrategy.java BacktrackingStrategy]||
||   6.8 ||     221|| Min-Conflicts                ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/MinConflictsStrategy.java MinConflictsStrategy]||
||   6.11||     209|| Tree-CSP-Solver              ||---||
||   7   ||     235|| Knowledge Base               ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/algorithms/KnowledgeBase.java KnowledgeBase]||
||   7.1 ||     236|| KB-Agent                     ||---||
||   7.7 ||     244|| Propositional-Logic-Sentence ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/parsing/ast/Sentence.java Sentence]||
||   7.10||     248|| TT-Entails                   ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/algorithms/TTEntails.java TTEntails]||
||   7   ||     253|| Convert-to-CNF               ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/visitors/CNFTransformer.java CNFTransformer]||
||   7.12||     255|| PL-Resolution                ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/algorithms/PLResolution.java PLResolution]||
||   7.15||     258|| PL-FC-Entails?               ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/algorithms/PLFCEntails.java PLFCEntails]||
||   7.17||     261|| DPLL-Satisfiable?            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/algorithms/DPLL.java (method)-dpllSatisfiable(String)]||
||   7.18||     263|| WalkSAT                      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/algorithms/WalkSAT.java WalkSAT]||
||   7.20||     270|| Hybrid-Wumpus-Agent          ||---||
||   7.22||     272|| SATPlan                      ||---||
||   9   ||     323|| Subst                        ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/SubstVisitor.java SubstVisitor]||
||   9.1 ||     328|| Unify                        ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/Unifier.java Unifier]||
||   9.3 ||     332|| FOL-FC-Ask                   ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/inference/FOLFCAsk.java FOLFCAsk]||
||   9.3 ||     332|| FOL-BC-Ask                   ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/inference/FOLBCAsk.java FOLBCAsk]||
||   9   ||     345|| CNF                          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/CNFConverter.java CNFConverter]||
||   9   ||     347|| Resolution                   ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/inference/FOLTFMResolution.java FOLTFMResolution], [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/inference/FOLOTTERLikeTheoremProver.java FOLOTTERLikeTheoremProver]||
||   9   ||     354|| Demodulation                 ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/inference/Demodulation.java Demodulation]||
||   9   ||     354|| Paramodulation               ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/inference/Paramodulation.java Paramodulation]||
||   9   ||     345|| Subsumption                  ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/fol/SubsumptionElimination.java SubsumptionElimination]||
||  10.9 ||     383|| Graphplan                    ||---||
||  11.5 ||     409|| Hierarchical-Search          ||---||
||  11.8 ||     414|| Angelic-Search               ||---||
||  13.1 ||     484|| DT-Agent                     ||---||
||  13   ||     484|| Probability-Model            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/ProbabilityModel.java ProbabilityModel], [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/FiniteProbabilityModel.java FiniteProbabilityModel]||
||  13   ||     487|| Probability-Distribution     ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/ProbabilityDistribution.java ProbabilityDistribution], [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/CategoricalDistribution.java CategoricalDistribution]||
||  13   ||     490|| Full-Joint-Distribution      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/full/FullJointDistributionModel.java FullJointDistributionModel]||
||  14   ||     510|| Bayesian Network             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/BayesianNetwork.java BayesianNetwork]||
||  14.9 ||     525|| Enumeration-Ask              ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/exact/EnumerationAsk.java EnumerationAsk]||
||  14.11||     528|| Elimination-Ask              ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/exact/EliminationAsk.java EliminationAsk]||
||  14.13||     531|| Prior-Sample                 ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/approx/PriorSample.java PriorSample]||
||  14.14||     533|| Rejection-Sampling           ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/approx/RejectionSampling.java RejectionSampling]||
||  14.15||     534|| Likelihood-Weighting         ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/approx/LikelihoodWeighting.java LikelihoodWeighting]||
||  14.16||     537|| GIBBS-Ask                    ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/approx/GibbsAsk.java GibbsAsk]||
||  15.4 ||     576|| Forward-Backward             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/temporal/generic/ForwardBackward.java ForwardBackward], [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/hmm/HMMForwardBackward.java HMMForwardBackward], [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/hmm/HMMForwardBackwardConstantSpace.java HMMForwardBackwardConstantSpace]||
||  15   ||     578|| Hidden Markov Model          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/hmm/HiddenMarkovModel.java HiddenMarkovModel]||
||  15.6 ||     580|| Fixed-Lag-Smoothing          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/hmm/FixedLagSmoothing.java FixedLagSmoothing]||
||  15   ||     590|| Dynamic Bayesian Network     ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/DynamicBayesianNetwork.java DynamicBayesianNetwork]||
||  15.17||     598|| Particle-Filtering           ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/approx/ParticleFiltering.java ParticleFiltering]||
||  16.9 ||     632|| Information-Gathering-Agent  ||---||
||  17.4 ||     653|| Value-Iteration              ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/mdp/MDP.java (method)-valueIteration()]||
||  17.7 ||     657|| Policy-Iteration             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/mdp/MDP.java (method)-policyIteration]||
||  17.9 ||     663|| POMDP-Value-Iteration        ||---||
||  18.5 ||     702|| Decision-Tree-Learning       ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/learners/DecisionTreeLearner.java DecisionTreeLearner]||
||  18.8 ||     710|| Cross-Validation-Wrapper     ||---||
||  18.11||     717|| Decision-List-Learning       ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/learners/DecisionListLearner.java DecisionListLearner]||
||  18.24||     734|| Back-Prop-Learning           ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/neural/BackPropLearning.java BackPropLearning]||
||  18.34||     751|| AdaBoost                     ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/learners/AdaBoostLearner.java AdaBoostLearner]||
||  19.2 ||     771|| Current-Best-Learning        ||---||
||  19.3 ||     773|| Version-Space-Learning       ||---||
||  19.8 ||     786|| Minimal-Consistent-Det       ||---||
||  19.12||     793|| FOIL                         ||---||
||  21.2 ||     834|| Passive-ADP-Agent            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/reinforcement/PassiveADPAgent.java PassiveADPAgent]||
||  21.4 ||     837|| Passive-TD-Agent             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/reinforcement/PassiveTDAgent.java PassiveTDAgent]||
||  21.8 ||     844|| Q-Learning-Agent             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/reinforcement/QLearningAgent.java QLearningAgent]||
||  22.1 ||     871|| HITS                         ||---||
||  23.5 ||     894|| CYK-Parse                    ||---||
||  25.9 ||     982|| Monte-Carlo-Localization     ||---||


= Using the Code =

For examples of how to use the various algorithms and supporting classes, look at the test cases in the parallel directory structure under src/test.


== Notes on Search ==

To solve a problem with (non CSP )Search .
  # you need to write five classes:
	# a class that represents the Problem state. This class is independent of the framework and does NOT need to subclass anything. Let us, for the rest of these instruction, assume you are going to solve the NQueens problem. So in this step you need to write something like aima.core.environment.nqueens.NQueensBoard. 
	# an implementation of the aima.core.search.framework.GoalTest interface. This implements only a single function ---boolean isGoalState(Object state); The parameter state is an instance of the class you created in  step 1-a above. For the NQueensProblem you would need to write something like aima.core.environment.nqueens.NQueensGoalTest.
	# an implementation of the aima.core.search.framework.ActionsFunction interface. This generates the allowable actions from a particular state. An example is aima.core.environment.nqueens.NQueensFunctionFactory.NQActionsFunction.
	# an implementation of the aima.core.search.framework.ResultFunction interface. This generates the state that results from doing action a in a state. An example is aima.core.environment.nqueens.NQueensFunctionFactory.NQResultFunction.	 
	# if you need to do an informed search, you should create a fourth class which implements the aima.core.search.framework.HeuristicFunction. For the NQueens problem, you need to write something like aima.core.environment.nqueens.QueensToBePlacedHeuristic.

that is all you need to do (unless you plan to write a different search than is available in the code base).

To actually search you need to
  # configure a problem instance
  # select a search. Configure this with Tree Search or GraphSearch if applicable.
  # instantiate a SerachAgent and 
  # print any actions and metrics 

A good example (from the NQueens Demo ) is: 
{{{
	private static void nQueensWithBreadthFirstSearch() {
		try {
			System.out.println("\nNQueensDemo BFS -->");
			Problem problem = new Problem(new NQueensBoard(8),
					NQueensFunctionFactory.getActionsFunction(),
					NQueensFunctionFactory.getResultFunction(),
					new NQueensGoalTest());
			Search search = new BreadthFirstSearch(new TreeSearch());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}}}


== Search Inheritance Trees ==

There are two inheritance trees in Search. One deals with the "mechanism" of search.

This inheritance hierarchy looks like this:

 ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/NodeExpander.java NodeExpander] (encapsulates the Node expansion mechanism)||---||---||
 ||---|| [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/QueueSearch.java QueueSearch]||---||
 ||---||---||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/GraphSearch.java GraphSearch]||
 ||---||---||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/TreeSearch.java TreeSearch]||

The second tree deals with the search instances you can use to solve a problem. These implement the aima.core.search.framework.Search interface.

||Search||---||---||---||
||---||BreadthFirstSearch||---||---||
||---||DepthFirstSearch||---||---||
||---||HillClimbingSearch||---||---||
||---||PrioritySearch||---||---||
||---||---||BestFirstSearch||---||

etc...

So if you see a declaration like 
"SimulatedAnnealingSearch extends NodeExpander implements Search" , do not be confused.
	
the  superclass ([http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/framework/NodeExpander.java NodeExpander]) provides the mechanism of the search and the interface (Search) makes it suitable for use in solving actual problems.

Searches like DepthFirstSearch which need to be used as a search (so implementing the Search interface) and can be configured with either GraphSearch or TreeSearch (the mechanism) have a  constructor like
	 public DepthFirstSearch(QueueSearch search).

== Logic Notes ==
The ONE thing you need to watch out for is that the Parsers are VERY unforgiving. If you get a lexing or parsing error, there is a high probability there is an error in your logic string.

To use First Order Logic, first you need to create an instance of aima.core.logic.fol.domain.FOLDomain which collects the FOL Constants, Prredicates, and Function etc... that you use to solve a particular problem.

A parser (that understands the Grammar in figure 8.3 (page 293 of AIMA3e) needs to be instantiated with this domain, e.g:
 
FOLDomain weaponsDomain = DomainFactory.weaponsDomain();
FOLParser parser = new FOLParser(weaponsDomain);

the basic design of all the logic code is that the parser creates a Composite (Design Patterns by Gamma, et al) parse tree over which various Visitors (Design Patterns by Gamma, et al) traverse. The key difference between the Visitor elucidated in the GOF book and the code is that in the former the visit() methods have a void visit(ConcreteNode) signature while the visitors used in the logic code have a Object visit(ConcreteNode,Object arg) signature. This makes testing easier and allows some recursive code that is hard with the former .

== Probability Notes ==

I have tried to make the code stick very closely to Dr.Norvig's' pseudo-code. Looking at the tests will reveal how to use the code. 

==LearningNotes==

=== Main Classes and responsibilities ===
A <DataSet> is a collection of <Example>s. Wherever you see "examples" in plural in the text, the code uses a DataSet. This makes it easy to aggregate operations that work on collections of examples in one place.

An Example is a collection of Attributes. Each example is a data point for Supervised Learning.

DataSetSpecification and AttributeSpecification do some error checking on the attributes when they are read in from a file or string. At present there are two types of Attributes - A sring attribute, used for datasets like "restaurant" and a Numeric Attribute, which represents attributes which are numbers. These are presently modeled as Doubles.

A Numerizer specifies how a particular DataSet's examples may be converted to Lists of Doubles so they can be used in Neural Networks. There is presently one numerizer in the codebase (IrisDataSetNumerizer) but it is trivial to write more by implementing the Numerizer interface.

=== How to Apply Learners ===

The DecisionTreeLearner and DecisionListLearner work only on datasets with ordinal attributes (no numbers). Numbers are treated as distinct strings.

The Perceptron and DecisionTreeLearners work on *numerized datasets*. If you intend to work with these, you need to write a DataSetSpecific Numerizer by implementing the Numerizer interface.

1. To import a dataset into a system so that learners can be applied to it , first add a public static DataSet getXDataSet(where "x" is the name of the DataSet you want to import) to the DataSetFactory

2. Learners all implement the Learner interface with 3 methods, train, predict and test. If you want to add a new type of Learner (a partitioning Decision Tree learner perhaps?) you need to implement this interface.


= Change History (Update in reverse chronological order) =
0.10.0-Chp13-and-14-Rewrite : 03 Jul 2011 :<br>
  * All of the algorithms from Chapters 13 and 14 have been rewritten.
  ** Rewritten:
  *** 14.9 Enumeration-Ask
  *** 14.13 Prior-Sample
  *** 14.14 Rejection-Sampling 
  *** 14.15 Likelihood-Weighting 
  *** 14.16 GIBBS-Ask 
  ** Added:
  *** 14.11 Elimination-Ask
  * Moved Randomizer interface and related implementation underneath
    aima.core.util.
  * Moved TwoKeyHashMashMap to sub-package datastructure.
  * Fix for Issue 66
  * Documentation clean up.
  
0.9.14-Probability-and-Logic-Fixes : 20 Mar 2011 :<br>
  * Resolved Issue 58, related to forward-backward algorithm. 
  * Fixed defect in Unifier that would cause incorrect unifications in particular
    edge cases (which would cause unsound proofs).
  * Fixed defect in resolution proof step output, that would show an incorrect
    unification. In addition, updated proof step information to make easier
    to read.
    
0.9.13-UBUNTU-Fixes : 19 Dec 2010 :<br>
  * Resolved Issue 56, related to compilation and test failures on Ubuntu platform.
  * Propositional ask-tell logic fixed using DPLL.
  * Map of Australia location corrected.
  * Minor code clean up/re-factoring
  
0.9.12-Online+CSP-Improvements : 05 Nov 2010 :<br>
  * StateAction replaced by TwoKeyHashMap (Online Search)
  * NotifyEnvironmentViews renamed to EnvironmentViewNotifier.
  * Method createExogenousChange reintroduced (from AIMA2e implementation).
  * CSP constraint propagation result handling code cleaned up.
  
0.9.11-CSP+PathCost-Fixes : 02 Oct 2010 :<br>
  * Fixed defect in Breath First Search where the Path Cost metric was not being updated correctly (issue #54).
  * Fixed CSP issue with respect to domain reconstruction with backtracking search.
  * Re-introduced SimpleEnvironmentView so its easier for people to setup and play with the code.
  * Minor documentation improvements.
  
0.9.10-CSP+AC-3 : 22 Aug 2010 :<br>
  * CSP package significantly restructured, added AC-3 implementation.
  * Search can now create more than one solution within the same run (see aima.core.search.framework.SolutionChecker).
  * The N-Queens representation now supports incremental as well as complete-state problem formulation.
  * Minor clean-ups included.
  * Now compiles on Android 2.1.
  
0.9.9-AIMAX-OSM Minor Fixes : 09 Feb 2010 :<br>
  * Java Doc now uses newer package-info.java mechanism.
 
0.9.8-AIMAX-OSM Added : 06 Feb 2010 :<br>
 * Minor updates to support addition of aimax-osm project to AIMA3e-Java.
 * Vacuum world locations changed from enum to Strings to better support extensibility.
 * Queue Searches may now be canceled from within a thread (see CancelableThread).
 
0.9.7-AIMA3e Published : 10 Dec 2009 :<br>
First full release based on the 3rd edition of AIMA. The following major 
updates have been included in this release:<br>
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
<br>
0.95-AIMA2eFinal : 03 Oct 2009 :<br>
Last full release based on the 2nd edition of AIMA. This is our first release 
containing GUIs (thanks to Ruediger Lunde):<br>
  * aima.gui.applications.VacuumAppDemo<br>
    Provides a demo of the different agents described in Chapter 2 and 3
    for tackling the Vacuum World.<br>
  * aima.gui.applications.search.map.RoutePlanningAgentAppDemo<br>
    Provides a demo of the different agents/search algorithms described 
    in Chapters 3 and 4, for tackling route planning tasks within 
    simplified Map environments.<br>
  * aima.gui.framework.SimpleAgentAppDemo<br>
    Provides a basic example of how to create your own Agent based 
    demonstrations based on the provided framework.<br>
<br>    
This will also be our last full release based on the 2nd edition of AIMA. 
We are currently in the planning phases to re-organize this project based 
on the 3rd edition of AIMA, which should be available soon.