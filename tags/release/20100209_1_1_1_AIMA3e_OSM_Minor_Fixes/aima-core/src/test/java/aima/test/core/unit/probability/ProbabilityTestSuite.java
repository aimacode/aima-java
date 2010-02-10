package aima.test.core.unit.probability;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.probability.decision.PolicyIterationTest;
import aima.test.core.unit.probability.decision.ValueIterationTest;
import aima.test.core.unit.probability.reasoning.HMMAgentTest;
import aima.test.core.unit.probability.reasoning.HMMTest;
import aima.test.core.unit.probability.reasoning.ParticleFilterTest;
import aima.test.core.unit.probability.reasoning.RandomVariableTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { PolicyIterationTest.class, ValueIterationTest.class,
		HMMAgentTest.class, HMMTest.class, ParticleFilterTest.class,
		RandomVariableTest.class, BayesNetNodeTest.class, BayesNetTest.class,
		EnumerationAskTest.class, EnumerationJointAskTest.class,
		ProbabilitySamplingTest.class })
public class ProbabilityTestSuite {

}
