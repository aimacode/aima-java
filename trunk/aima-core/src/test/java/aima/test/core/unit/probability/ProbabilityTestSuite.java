package aima.test.core.unit.probability;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.probability.bayes.approx.GibbsAskTest;
import aima.test.core.unit.probability.bayes.approx.LikelihoodWeightingTest;
import aima.test.core.unit.probability.bayes.approx.ParticleFilterTest;
import aima.test.core.unit.probability.bayes.approx.PriorSampleTest;
import aima.test.core.unit.probability.bayes.approx.RejectionSamplingTest;
import aima.test.core.unit.probability.bayes.exact.EliminationAskTest;
import aima.test.core.unit.probability.bayes.exact.EnumerationAskTest;
import aima.test.core.unit.probability.bayes.impl.CPTTest;
import aima.test.core.unit.probability.bayes.model.FiniteBayesModelTest;
import aima.test.core.unit.probability.full.FullJointProbabilityModelTest;
import aima.test.core.unit.probability.hmm.exact.FixedLagSmoothingTest;
import aima.test.core.unit.probability.hmm.exact.HMMForwardBackwardConstantSpaceTest;
import aima.test.core.unit.probability.hmm.exact.HMMForwardBackwardTest;
import aima.test.core.unit.probability.mdp.MarkovDecisionProcessTest;
import aima.test.core.unit.probability.mdp.PolicyIterationTest;
import aima.test.core.unit.probability.mdp.ValueIterationTest;
import aima.test.core.unit.probability.temporal.generic.ForwardBackwardTest;
import aima.test.core.unit.probability.util.ProbUtilTest;
import aima.test.core.unit.probability.util.ProbabilityTableTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { GibbsAskTest.class, LikelihoodWeightingTest.class,
		ParticleFilterTest.class, PriorSampleTest.class,
		RejectionSamplingTest.class, EliminationAskTest.class,
		EnumerationAskTest.class, CPTTest.class, FiniteBayesModelTest.class,
		FullJointProbabilityModelTest.class, FixedLagSmoothingTest.class,
		HMMForwardBackwardConstantSpaceTest.class,
		HMMForwardBackwardTest.class, MarkovDecisionProcessTest.class,
		PolicyIterationTest.class, ValueIterationTest.class,
		ForwardBackwardTest.class, ProbUtilTest.class,
		ProbabilityTableTest.class })
public class ProbabilityTestSuite {

}
