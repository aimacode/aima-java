package aima.test.core.unit.probability;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.probability.bayes.approx.GibbsAskTest;
import aima.test.core.unit.probability.bayes.approx.LikelihoodWeightingTest;
import aima.test.core.unit.probability.bayes.approx.PriorSampleTest;
import aima.test.core.unit.probability.bayes.approx.RejectionSamplingTest;
import aima.test.core.unit.probability.bayes.exact.EliminationAskTest;
import aima.test.core.unit.probability.bayes.exact.EnumerationAskTest;
import aima.test.core.unit.probability.bayes.impl.CPTTest;
import aima.test.core.unit.probability.bayes.model.FiniteBayesModelTest;
import aima.test.core.unit.probability.full.FullJointProbabilityModelTest;
import aima.test.core.unit.probability.hmm.HMMAgentTest;
import aima.test.core.unit.probability.hmm.HMMForwardBackwardTest;
import aima.test.core.unit.probability.hmm.HMMTest;
import aima.test.core.unit.probability.hmm.ParticleFilterTest;
import aima.test.core.unit.probability.hmm.VarDistributionTest;
import aima.test.core.unit.probability.mdp.PolicyIterationTest;
import aima.test.core.unit.probability.mdp.ValueIterationTest;
import aima.test.core.unit.probability.temporal.generic.ForwardBackwardTest;
import aima.test.core.unit.probability.util.ProbUtilTest;
import aima.test.core.unit.probability.util.ProbabilityTableTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { GibbsAskTest.class, LikelihoodWeightingTest.class,
		PriorSampleTest.class, RejectionSamplingTest.class,
		EliminationAskTest.class, EnumerationAskTest.class, CPTTest.class,
		FiniteBayesModelTest.class, FullJointProbabilityModelTest.class,
		HMMAgentTest.class, HMMForwardBackwardTest.class, HMMTest.class,
		ParticleFilterTest.class, VarDistributionTest.class,
		PolicyIterationTest.class, ValueIterationTest.class,
		ForwardBackwardTest.class, ProbUtilTest.class,
		ProbabilityTableTest.class })
public class ProbabilityTestSuite {

}
