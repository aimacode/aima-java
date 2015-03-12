# AIMA3e-Java (JDK 7+)
Java implementation of algorithms from Norvig And Russell's "Artificial Intelligence - A Modern Approach - A Modern Approach 3rd Edition."

Java implementation of algorithms from [Norvig](http://www.norvig.com/) and [Russell's](http://www.cs.berkeley.edu/~russell/)  [Artificial Intelligence - A Modern Approach 3rd Edition](http://aima.cs.berkeley.edu/) 

# Getting Started Links 
  * [Demo Applications that can be run from your browser](http://aima-java.googlecode.com/svn/trunk/aima-all/release/aima3ejavademos.html).
  * [An overview of the project](http://code.google.com/p/aima-java/wiki/AIMA3eReadme).
  * [Instructions on how to set up your workspace](http://code.google.com/p/aima-java/wiki/GettingStarted).
  * [Javadoc for the aima-core project](http://aima-java.googlecode.com/svn/trunk/aima-all/release/javadoc/aima-core/index.html).
  * [Download the latest official version  = 1.8.0 (Aug 10 2014)](https://code.google.com/p/aima-java/wiki//p/aima-java/wiki/AIMADownloads).
  * Latest Maven Information (for integration as a third party library)<br>
       `<dependency>`<br>
       `    <groupId>com.googlecode.aima-java</groupId>`<br>
       `    <artifactId>aima-core</artifactId>`<br>
       `    <version>0.11.0</version>`<br>
       `</dependency>`<br>

# Index of Implemented Algorithms
<table style="width:100%">
   <tbody>
   <tr>
       <td>Fig</td>
       <td>Page</td>
       <td>Name (in book)</td>
       <td>Code</td>
   </tr>
   <tr>
       <td>2</td>
       <td>34</td>
       <td>Environment</td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/aima-core/src/main/java/aima/core/agent/Environment.java">Environment</a></td>
   </tr>
   <tr>
       <td>2.1</td>
       <td>35</td>
       <td>Agent</td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/aima-core/src/main/java/aima/core/agent/Agent.java">Agent</a></td>
   </tr>
   <tr>
       <td>2.3</td>
       <td>36</td>
       <td>Table-Driven-Vacuum-Agent</td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/aima-core/src/main/java/aima/core/environment/vacuum/TableDrivenVacuumAgent.java">TableDrivenVacuumAgent</a></td>
   </tr>
   <tr>
       <td>2.7</td>
       <td>47</td>
       <td>Table-Driven-Agent</td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/aima-core/src/main/java/aima/core/agent/impl/aprog/TableDrivenAgentProgram.java">TableDrivenAgent</a></td>
   </tr>
   <tr>
       <td>2.8</td>
       <td>48</td>
       <td>Reflex-Vacuum-Agent</td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/aima-core/src/main/java/aima/core/environment/vacuum/ReflexVacuumAgent.java">ReflexVacuumAgent</a></td>
   </tr>
   <tr>
       <td>2.10</td>
       <td>49</td>
       <td>Simple-Reflex-Agent</td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/aima-core/src/main/java/aima/core/agent/impl/aprog/SimpleReflexAgentProgram.java">SimpleReflexAgentProgram</a></td>
   </tr>
   <tr>
       <td>2.12</td>
       <td>51</td>
       <td>Model-Based-Reflex-Agent</td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/aima-core/src/main/java/aima/core/agent/impl/aprog/ModelBasedReflexAgentProgram.java">ModelBasedReflexAgentProgram</a></td>
   </tr>
   <tr>
       <td></td>
       <td></td>
       <td></td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/"></a></td>
   </tr>
   <tr>
       <td></td>
       <td></td>
       <td></td>
       <td><a href="https://github.com/ctjoreilly/aima-java/blob/master/"></a></td>
   </tr>
   </tbody>
</table> 



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
||   4.11||     136|| And-Or-Graph-Search          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/nondeterministic/AndOrSearch.java AndOrSearch]||
||   4   ||     147|| Online search problem        ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/online/OnlineSearchProblem.java OnlineSearchProblem] ||
||   4.21||     150|| Online-DFS-Agent             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/online/OnlineDFSAgent.java OnlineDFSAgent] ||
||   4.24||     152|| LRTA`*`-Agent                ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/online/LRTAStarAgent.java LRTAStarAgent] ||
||   5.3 ||     166|| Minimax-Decision             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/adversarial/MinimaxSearch.java MinimaxSearch]||
||   5.7 ||     170|| Alpha-Beta-Search            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/adversarial/AlphaBetaSearch.java AlphaBetaSearch]||
||   6   ||     202|| CSP                          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/CSP.java CSP]||
||   6.1 ||     204|| Map CSP                      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/MapCSP.java MapCSP]||
||   6.3 ||     209|| AC-3                         ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/AC3Strategy.java AC3Strategy]||
||   6.5 ||     215|| Backtracking-Search          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/BacktrackingStrategy.java BacktrackingStrategy]||
||   6.8 ||     221|| Min-Conflicts                ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/search/csp/MinConflictsStrategy.java MinConflictsStrategy]||
||   6.11||     209|| Tree-CSP-Solver              ||---||
||   7   ||     235|| Knowledge Base               ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/kb/KnowledgeBase.java KnowledgeBase]||
||   7.1 ||     236|| KB-Agent                     ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/agent/KBAgent.java KBAgent]||
||   7.7 ||     244|| Propositional-Logic-Sentence ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/parsing/ast/Sentence.java Sentence], [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/parsing/PLParser.java PLParser]||
||   7.10||     248|| TT-Entails                   ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/inference/TTEntails.java TTEntails]||
||   7   ||     253|| Convert-to-CNF               ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/visitors/ConvertToCNF.java ConvertToCNF]||
||   7.12||     255|| PL-Resolution                ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/inference/PLResolution.java PLResolution]||
||   7.15||     258|| PL-FC-Entails?               ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/inference/PLFCEntails.java PLFCEntails]||
||   7.17||     261|| DPLL-Satisfiable?            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/inference/DPLLSatisfiable.java DPLLSatisfiable]||
||   7.18||     263|| WalkSAT                      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/inference/WalkSAT.java WalkSAT]||
||   7.20||     270|| Hybrid-Wumpus-Agent          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/environment/wumpusworld/HybridWumpusAgent.java HybridWumpusAgent]||
||   7.22||     272|| SATPlan                      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/logic/propositional/inference/SATPlan.java SATPlan]||
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
||  15.4 ||     576|| Forward-Backward             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/temporal/generic/ForwardBackward.java ForwardBackward], [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/hmm/exact/HMMForwardBackward.java HMMForwardBackward], [http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/hmm/exact/HMMForwardBackwardConstantSpace.java HMMForwardBackwardConstantSpace]||
||  15   ||     578|| Hidden Markov Model          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/hmm/HiddenMarkovModel.java HiddenMarkovModel]||
||  15.6 ||     580|| Fixed-Lag-Smoothing          ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/hmm/exact/FixedLagSmoothing.java FixedLagSmoothing]||
||  15   ||     590|| Dynamic Bayesian Network     ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/DynamicBayesianNetwork.java DynamicBayesianNetwork]||
||  15.17||     598|| Particle-Filtering           ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/bayes/approx/ParticleFiltering.java ParticleFiltering]||
||  16.9 ||     632|| Information-Gathering-Agent  ||---||
||  17   ||     647|| Markov Decision Process      ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/mdp/MarkovDecisionProcess.java MarkovDecisionProcess]||
||  17.4 ||     653|| Value-Iteration              ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/mdp/search/ValueIteration.java ValueIteration]||
||  17.7 ||     657|| Policy-Iteration             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/probability/mdp/search/PolicyIteration.java PolicyIteration]||
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
||  21.2 ||     834|| Passive-ADP-Agent            ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/reinforcement/agent/PassiveADPAgent.java PassiveADPAgent]||
||  21.4 ||     837|| Passive-TD-Agent             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/reinforcement/agent/PassiveTDAgent.java PassiveTDAgent]||
||  21.8 ||     844|| Q-Learning-Agent             ||[http://aima-java.googlecode.com/svn/trunk/aima-core/src/main/java/aima/core/learning/reinforcement/agent/QLearningAgent.java QLearningAgent]||
||  22.1 ||     871|| HITS                         ||---||
||  23.5 ||     894|| CYK-Parse                    ||---||
||  25.9 ||     982|| Monte-Carlo-Localization     ||---||
