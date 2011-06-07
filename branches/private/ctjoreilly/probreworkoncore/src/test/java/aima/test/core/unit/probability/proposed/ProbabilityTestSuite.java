package aima.test.core.unit.probability.proposed;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.probability.proposed.bayes.exact.EliminationAskTest;
import aima.test.core.unit.probability.proposed.bayes.exact.EnumerationAskTest;
import aima.test.core.unit.probability.proposed.bayes.model.FiniteBayesModelTest;
import aima.test.core.unit.probability.proposed.full.FullJointProbabilityModelTest;
import aima.test.core.unit.probability.proposed.util.ProbabilityTableTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { ProbabilityTableTest.class, FiniteBayesModelTest.class,
		FullJointProbabilityModelTest.class, EnumerationAskTest.class, EliminationAskTest.class })
public class ProbabilityTestSuite {

}
