# ![](https://github.com/aimacode/aima-java/blob/gh-pages/aima3e/images/aima3e.jpg)AIMA3e-Java (JDK 8+) [![Build Status](https://travis-ci.org/aimacode/aima-java.svg?branch=AIMA3e)](https://travis-ci.org/aimacode/aima-java)
Java implementation of algorithms from [Russell](http://www.cs.berkeley.edu/~russell/) and [Norvig's](http://www.norvig.com/) [Artificial Intelligence - A Modern Approach 3rd Edition](http://aima.cs.berkeley.edu/). You can use this in conjunction with a course on AI, or for study on your own. We're looking for [solid contributors](https://github.com/aimacode/aima-java/wiki/AIMAJava-Contributing) to help.

### Getting Started Links 
  * [Overview of Project](https://github.com/aimacode/aima-java/wiki/AIMA3e-Overview).
  * [Interested in Contributing](https://github.com/aimacode/aima-java/wiki/AIMAJava-Contributing)
  * [Setting up your own workspace](https://github.com/aimacode/aima-java/wiki/AIMA3e-Workspace-Setup).
  * [Demo Applications that can be run from your browser](http://aimacode.github.io/aima-java/aima3e/aima3ejavademos.html).
  * [Comments on architecture and design](https://github.com/aimacode/aima-java/wiki/AIMA3e-Architecture-and-Design).
  * [Javadoc for the aima-core project](http://aimacode.github.io/aima-java/aima3e/javadoc/aima-core/index.html).
  * [Download the latest official version  = 1.9.1 (Dec 18 2016)](https://github.com/aimacode/aima-java/releases/tag/aima3e-v1.9.1).
  * Latest Maven Information (for integration as a third party library)<br>
  
      ```
      <dependency>
          <groupId>com.googlecode.aima-java</groupId>
          <artifactId>aima-core</artifactId>
          <version>3.0.0</version>
      </dependency>
      ```

### Index of Implemented Algorithms

|Fig|Page|Name (in book)|Code|
| -------- |:--------:| :-----| :----- |
|2|34|Environment|[Environment](/aima-core/src/main/java/aima/core/agent/Environment.java)|
|2.1|35|Agent|[Agent](/aima-core/src/main/java/aima/core/agent/Agent.java)|
|2.3|36|Table-Driven-Vacuum-Agent|[TableDrivenVacuumAgent](/aima-core/src/main/java/aima/core/environment/vacuum/TableDrivenVacuumAgent.java)|
|2.7|47|Table-Driven-Agent|[TableDrivenAgentProgram](/aima-core/src/main/java/aima/core/agent/impl/aprog/TableDrivenAgentProgram.java)|
|2.8|48|Reflex-Vacuum-Agent|[ReflexVacuumAgent](/aima-core/src/main/java/aima/core/environment/vacuum/ReflexVacuumAgent.java)|
|2.10|49|Simple-Reflex-Agent|[SimpleReflexAgentProgram](/aima-core/src/main/java/aima/core/agent/impl/aprog/SimpleReflexAgentProgram.java)|
|2.12|51|Model-Based-Reflex-Agent|[ModelBasedReflexAgentProgram](/aima-core/src/main/java/aima/core/agent/impl/aprog/ModelBasedReflexAgentProgram.java)|
|3|66|Problem|[Problem](/aima-core/src/main/java/aima/core/search/framework/problem/Problem.java)|
|3.1|67|Simple-Problem-Solving-Agent|[SimpleProblemSolvingAgent](/aima-core/src/main/java/aima/core/search/framework/SimpleProblemSolvingAgent.java)|
|3.2|68|Romania|[SimplifiedRoadMapOfPartOfRomania](/aima-core/src/main/java/aima/core/environment/map/SimplifiedRoadMapOfPartOfRomania.java)|
|3.7|77|Tree-Search|[TreeSearch](/aima-core/src/main/java/aima/core/search/framework/qsearch/TreeSearch.java)|
|3.7|77|Graph-Search|[GraphSearch](/aima-core/src/main/java/aima/core/search/framework/qsearch/GraphSearch.java)|
|3.10|79|Node|[Node](/aima-core/src/main/java/aima/core/search/framework/Node.java)|
|3.11|82|Breadth-First-Search|[BreadthFirstSearch](/aima-core/src/main/java/aima/core/search/uninformed/BreadthFirstSearch.java)|
|3.14|84|Uniform-Cost-Search|[UniformCostSearch](/aima-core/src/main/java/aima/core/search/uninformed/UniformCostSearch.java)|
|3|85|Depth-first Search|[DepthFirstSearch](/aima-core/src/main/java/aima/core/search/uninformed/DepthFirstSearch.java)|
|3.17|88|Depth-Limited-Search|[DepthLimitedSearch](/aima-core/src/main/java/aima/core/search/uninformed/DepthLimitedSearch.java)|
|3.18|89|Iterative-Deepening-Search|[IterativeDeepeningSearch](/aima-core/src/main/java/aima/core/search/uninformed/IterativeDeepeningSearch.java)|
|3|90|Bidirectional search|[BidirectionalSearch](/aima-core/src/main/java/aima/core/search/framework/qsearch/BidirectionalSearch.java)|
|3|92|Best-First search|[BestFirstSearch](/aima-core/src/main/java/aima/core/search/informed/BestFirstSearch.java)|
|3|92|Greedy best-First search|[GreedyBestFirstSearch](/aima-core/src/main/java/aima/core/search/informed/GreedyBestFirstSearch.java)|
|3|93|A\* Search|[AStarSearch](/aima-core/src/main/java/aima/core/search/informed/AStarSearch.java)|
|3.26|99|Recursive-Best-First-Search |[RecursiveBestFirstSearch](/aima-core/src/main/java/aima/core/search/informed/RecursiveBestFirstSearch.java)|
|4.2|122|Hill-Climbing|[HillClimbingSearch](/aima-core/src/main/java/aima/core/search/local/HillClimbingSearch.java)|
|4.5|126|Simulated-Annealing|[SimulatedAnnealingSearch](/aima-core/src/main/java/aima/core/search/local/SimulatedAnnealingSearch.java)|
|4.8|129|Genetic-Algorithm|[GeneticAlgorithm](/aima-core/src/main/java/aima/core/search/local/GeneticAlgorithm.java)|
|4.11|136|And-Or-Graph-Search|[AndOrSearch](/aima-core/src/main/java/aima/core/search/nondeterministic/AndOrSearch.java)|
|4|147|Online search problem|[OnlineSearchProblem](/aima-core/src/main/java/aima/core/search/online/OnlineSearchProblem.java)|
|4.21|150|Online-DFS-Agent|[OnlineDFSAgent](/aima-core/src/main/java/aima/core/search/online/OnlineDFSAgent.java)|
|4.24|152|LRTA\*-Agent|[LRTAStarAgent](/aima-core/src/main/java/aima/core/search/online/LRTAStarAgent.java)|
|5.3|166|Minimax-Decision|[MinimaxSearch](/aima-core/src/main/java/aima/core/search/adversarial/MinimaxSearch.java)|
|5.7|170|Alpha-Beta-Search|[AlphaBetaSearch](/aima-core/src/main/java/aima/core/search/adversarial/AlphaBetaSearch.java)|
|6|202|CSP|[CSP](/aima-core/src/main/java/aima/core/search/csp/CSP.java)|
|6.1|204|Map CSP|[MapCSP](/aima-core/src/main/java/aima/core/search/csp/examples/MapCSP.java)|
|6.3|209|AC-3|[AC3Strategy](/aima-core/src/main/java/aima/core/search/csp/inference/AC3Strategy.java)|
|6.5|215|Backtracking-Search|[AbstractBacktrackingSolver](/aima-core/src/main/java/aima/core/search/csp/AbstractBacktrackingSolver.java)|
|6.8|221|Min-Conflicts|[MinConflictsSolver](/aima-core/src/main/java/aima/core/search/csp/MinConflictsSolver.java)|
|6.11|224|Tree-CSP-Solver|[TreeCspSolver](/aima-core/src/main/java/aima/core/search/csp/TreeCspSolver.java)|
|7|235|Knowledge Base|[KnowledgeBase](/aima-core/src/main/java/aima/core/logic/propositional/kb/KnowledgeBase.java)|
|7.1|236|KB-Agent|[KBAgent](/aima-core/src/main/java/aima/core/logic/propositional/agent/KBAgent.java)|
|7.7|244|Propositional-Logic-Sentence|[Sentence](/aima-core/src/main/java/aima/core/logic/propositional/parsing/ast/Sentence.java)|
|7.10|248|TT-Entails|[TTEntails](/aima-core/src/main/java/aima/core/logic/propositional/inference/TTEntails.java)|
|7|253|Convert-to-CNF|[ConvertToCNF](/aima-core/src/main/java/aima/core/logic/propositional/visitors/ConvertToCNF.java)|
|7.12|255|PL-Resolution|[PLResolution](/aima-core/src/main/java/aima/core/logic/propositional/inference/PLResolution.java)|
|7.15|258|PL-FC-Entails?|[PLFCEntails](/aima-core/src/main/java/aima/core/logic/propositional/inference/PLFCEntails.java)|
|7.17|261|DPLL-Satisfiable?|[DPLLSatisfiable](/aima-core/src/main/java/aima/core/logic/propositional/inference/DPLLSatisfiable.java)|
|7.18|263|WalkSAT|[WalkSAT](/aima-core/src/main/java/aima/core/logic/propositional/inference/WalkSAT.java)|
|7.20|270|Hybrid-Wumpus-Agent|[HybridWumpusAgent](/aima-core/src/main/java/aima/core/environment/wumpusworld/HybridWumpusAgent.java)|
|7.22|272|SATPlan|[SATPlan](/aima-core/src/main/java/aima/core/logic/propositional/inference/SATPlan.java)|
|9|323|Subst|[SubstVisitor](/aima-core/src/main/java/aima/core/logic/fol/SubstVisitor.java)|
|9.1|328|Unify|[Unifier](/aima-core/src/main/java/aima/core/logic/fol/Unifier.java)|
|9.3|332|FOL-FC-Ask|[FOLFCAsk](/aima-core/src/main/java/aima/core/logic/fol/inference/FOLFCAsk.java)|
|9.3|332|FOL-BC-Ask|[FOLBCAsk](/aima-core/src/main/java/aima/core/logic/fol/inference/FOLBCAsk.java)|
|9|345|CNF|[CNFConverter](/aima-core/src/main/java/aima/core/logic/fol/CNFConverter.java)|
|9|347|Resolution|[FOLTFMResolution](/aima-core/src/main/java/aima/core/logic/fol/inference/FOLTFMResolution.java)|
|9|354|Demodulation|[Demodulation](/aima-core/src/main/java/aima/core/logic/fol/inference/Demodulation.java)|
|9|354|Paramodulation|[Paramodulation](/aima-core/src/main/java/aima/core/logic/fol/inference/Paramodulation.java)|
|9|345|Subsumption|[SubsumptionElimination](/aima-core/src/main/java/aima/core/logic/fol/SubsumptionElimination.java)|
|10.9|383|Graphplan|---|
|11.5|409|Hierarchical-Search|---|
|11.8|414|Angelic-Search|---|
|13.1|484|DT-Agent|---|
|13|484|Probability-Model|[ProbabilityModel](/aima-core/src/main/java/aima/core/probability/ProbabilityModel.java)|
|13|487|Probability-Distribution|[ProbabilityDistribution](/aima-core/src/main/java/aima/core/probability/ProbabilityDistribution.java)|
|13|490|Full-Joint-Distribution|[FullJointDistributionModel](/aima-core/src/main/java/aima/core/probability/full/FullJointDistributionModel.java)|
|14|510|Bayesian Network|[BayesianNetwork](/aima-core/src/main/java/aima/core/probability/bayes/BayesianNetwork.java)|
|14.9|525|Enumeration-Ask|[EnumerationAsk](/aima-core/src/main/java/aima/core/probability/bayes/exact/EnumerationAsk.java)|
|14.11|528|Elimination-Ask|[EliminationAsk](/aima-core/src/main/java/aima/core/probability/bayes/exact/EliminationAsk.java)|
|14.13|531|Prior-Sample|[PriorSample](/aima-core/src/main/java/aima/core/probability/bayes/approx/PriorSample.java)|
|14.14|533|Rejection-Sampling|[RejectionSampling](/aima-core/src/main/java/aima/core/probability/bayes/approx/RejectionSampling.java)|
|14.15|534|Likelihood-Weighting|[LikelihoodWeighting](/aima-core/src/main/java/aima/core/probability/bayes/approx/LikelihoodWeighting.java)|
|14.16|537|GIBBS-Ask|[GibbsAsk](/aima-core/src/main/java/aima/core/probability/bayes/approx/GibbsAsk.java)|
|15.4|576|Forward-Backward|[ForwardBackward](/aima-core/src/main/java/aima/core/probability/temporal/generic/ForwardBackward.java)|
|15|578|Hidden Markov Model|[HiddenMarkovModel](/aima-core/src/main/java/aima/core/probability/hmm/HiddenMarkovModel.java)|
|15.6|580|Fixed-Lag-Smoothing|[FixedLagSmoothing](/aima-core/src/main/java/aima/core/probability/hmm/exact/FixedLagSmoothing.java)|
|15|590|Dynamic Bayesian Network|[DynamicBayesianNetwork](/aima-core/src/main/java/aima/core/probability/bayes/DynamicBayesianNetwork.java)|
|15.17|598|Particle-Filtering|[ParticleFiltering](/aima-core/src/main/java/aima/core/probability/bayes/approx/ParticleFiltering.java)|
|16.9|632|Information-Gathering-Agent|---|
|17|647|Markov Decision Process|[MarkovDecisionProcess](/aima-core/src/main/java/aima/core/probability/mdp/MarkovDecisionProcess.java)|
|17.4|653|Value-Iteration|[ValueIteration](/aima-core/src/main/java/aima/core/probability/mdp/search/ValueIteration.java)|
|17.7|657|Policy-Iteration|[PolicyIteration](/aima-core/src/main/java/aima/core/probability/mdp/search/PolicyIteration.java)|
|17.9|663|POMDP-Value-Iteration|---|
|18.5|702|Decision-Tree-Learning|[DecisionTreeLearner](/aima-core/src/main/java/aima/core/learning/learners/DecisionTreeLearner.java)|
|18.8|710|Cross-Validation-Wrapper|---|
|18.11|717|Decision-List-Learning|[DecisionListLearner](/aima-core/src/main/java/aima/core/learning/learners/DecisionListLearner.java)|
|18.24|734|Back-Prop-Learning|[BackPropLearning](/aima-core/src/main/java/aima/core/learning/neural/BackPropLearning.java)|
|18.34|751|AdaBoost|[AdaBoostLearner](/aima-core/src/main/java/aima/core/learning/learners/AdaBoostLearner.java)|
|19.2|771|Current-Best-Learning|---|
|19.3|773|Version-Space-Learning|---|
|19.8|786|Minimal-Consistent-Det|---|
|19.12|793|FOIL|---|
|21.2|834|Passive-ADP-Agent|[PassiveADPAgent](/aima-core/src/main/java/aima/core/learning/reinforcement/agent/PassiveADPAgent.java)|
|21.4|837|Passive-TD-Agent|[PassiveTDAgent](/aima-core/src/main/java/aima/core/learning/reinforcement/agent/PassiveTDAgent.java)|
|21.8|844|Q-Learning-Agent|[QLearningAgent](/aima-core/src/main/java/aima/core/learning/reinforcement/agent/QLearningAgent.java)|
|22.1|871|HITS|[HITS](/aima-core/src/main/java/aima/core/nlp/ranking/HITS.java)|
|23.5|894|CYK-Parse|[CYK](/aima-core/src/main/java/aima/core/nlp/parsing/CYK.java)|
|25.9|982|Monte-Carlo-Localization|[MonteCarloLocalization](/aima-core/src/main/java/aima/core/robotics/MonteCarloLocalization.java)|
