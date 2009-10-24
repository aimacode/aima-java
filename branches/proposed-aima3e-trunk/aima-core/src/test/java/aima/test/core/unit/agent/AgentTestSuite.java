package aima.test.core.unit.agent;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.agent.impl.DynamicPerceptTest;
import aima.test.core.unit.agent.impl.PerceptSequenceTest;
import aima.test.core.unit.agent.impl.aprog.TableDrivenAgentProgramTest;
import aima.test.core.unit.agent.impl.aprog.simplerule.RuleTest;
import aima.test.core.unit.agent.impl.vacuum.ModelBasedReflexVacuumAgentTest;
import aima.test.core.unit.agent.impl.vacuum.ReflexVacuumAgentTest;
import aima.test.core.unit.agent.impl.vacuum.SimpleReflexVacuumAgentTest;
import aima.test.core.unit.agent.impl.vacuum.TableDrivenVacuumAgentTest;
import aima.test.core.unit.agent.impl.vacuum.VacuumEnvironmentTest;
import aima.test.core.unit.agent.impl.xyenv.XYEnvironmentTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { RuleTest.class, TableDrivenAgentProgramTest.class,
		ModelBasedReflexVacuumAgentTest.class, ReflexVacuumAgentTest.class,
		SimpleReflexVacuumAgentTest.class, TableDrivenVacuumAgentTest.class,
		VacuumEnvironmentTest.class, XYEnvironmentTest.class,
		DynamicPerceptTest.class, PerceptSequenceTest.class })
public class AgentTestSuite {

}
