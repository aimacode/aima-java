package aima.test.core.unit.agent;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.agent.impl.DynamicPerceptTest;
import aima.test.core.unit.agent.impl.PerceptSequenceTest;
import aima.test.core.unit.agent.impl.aprog.TableDrivenAgentProgramTest;
import aima.test.core.unit.agent.impl.aprog.simplerule.RuleTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { RuleTest.class, TableDrivenAgentProgramTest.class,
		DynamicPerceptTest.class, PerceptSequenceTest.class })
public class AgentTestSuite {

}
