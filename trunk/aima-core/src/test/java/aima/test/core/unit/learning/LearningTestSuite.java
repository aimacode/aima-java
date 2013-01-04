package aima.test.core.unit.learning;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.learning.framework.DataSetTest;
import aima.test.core.unit.learning.framework.InformationAndGainTest;
import aima.test.core.unit.learning.inductive.DLTestTest;
import aima.test.core.unit.learning.inductive.DecisionListTest;
import aima.test.core.unit.learning.learners.DecisionTreeTest;
import aima.test.core.unit.learning.learners.EnsembleLearningTest;
import aima.test.core.unit.learning.learners.LearnerTest;
import aima.test.core.unit.learning.neural.BackPropagationTest;
import aima.test.core.unit.learning.neural.LayerTest;
import aima.test.core.unit.learning.reinforcement.agent.PassiveADPAgentTest;
import aima.test.core.unit.learning.reinforcement.agent.PassiveTDAgentTest;
import aima.test.core.unit.learning.reinforcement.agent.QLearningAgentTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ DataSetTest.class, InformationAndGainTest.class,
		DecisionListTest.class, DLTestTest.class, DecisionTreeTest.class,
		EnsembleLearningTest.class, LearnerTest.class,
		BackPropagationTest.class, LayerTest.class,
		PassiveADPAgentTest.class, PassiveTDAgentTest.class,
		QLearningAgentTest.class })
public class LearningTestSuite {

}
