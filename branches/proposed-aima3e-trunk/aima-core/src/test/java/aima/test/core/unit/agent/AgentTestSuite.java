package aima.test.core.unit.agent;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.agent.impl.DynamicPerceptTest;
import aima.test.core.unit.agent.impl.PerceptSequenceTest;
import aima.test.core.unit.agent.impl.aprog.TableDrivenAgentProgramTest;
import aima.test.core.unit.agent.impl.aprog.simplerule.RuleTest;
import aima.test.core.unit.agent.impl.vacuum.ModelBasedReflexVaccumAgentTest;
import aima.test.core.unit.agent.impl.vacuum.ReflexVaccumAgentTest;
import aima.test.core.unit.agent.impl.vacuum.SimpleReflexVaccumAgentTest;
import aima.test.core.unit.agent.impl.vacuum.TableDrivenVaccumAgentTest;
import aima.test.core.unit.agent.impl.vacuum.VaccumEnvironmentTest;
import aima.test.core.unit.agent.impl.xyenv.XYEnvironmentTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { RuleTest.class, TableDrivenAgentProgramTest.class,
		ModelBasedReflexVaccumAgentTest.class, ReflexVaccumAgentTest.class,
		SimpleReflexVaccumAgentTest.class, TableDrivenVaccumAgentTest.class,
		VaccumEnvironmentTest.class, XYEnvironmentTest.class,
		DynamicPerceptTest.class, PerceptSequenceTest.class })
public class AgentTestSuite {

}
