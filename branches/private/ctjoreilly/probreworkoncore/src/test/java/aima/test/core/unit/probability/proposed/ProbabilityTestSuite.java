package aima.test.core.unit.probability.proposed;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.probability.proposed.model.DistributionTest;
import aima.test.core.unit.probability.proposed.model.bayes.FiniteBayesModelTest;
import aima.test.core.unit.probability.proposed.model.bayes.exact.EliminationAskTest;
import aima.test.core.unit.probability.proposed.model.bayes.exact.EnumerationAskTest;
import aima.test.core.unit.probability.proposed.model.full.FullJointProbabilityModelTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { DistributionTest.class, FiniteBayesModelTest.class,
		FullJointProbabilityModelTest.class, EnumerationAskTest.class, EliminationAskTest.class })
public class ProbabilityTestSuite {

}
