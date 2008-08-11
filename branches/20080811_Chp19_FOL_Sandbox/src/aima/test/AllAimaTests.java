package aima.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import aima.test.coretest.PerceptSequenceTest;
import aima.test.coretest.PerceptTest;
import aima.test.coretest.RuleTest;
import aima.test.coretest.XYEnvironmentTest;
import aima.test.coretest.XYLocationTest;
import aima.test.gametest.TicTacToeTest;
import aima.test.learningtest.LearningTests;
import aima.test.logictest.LogicTests;
import aima.test.probabilitytest.ProbabilityTests;
import aima.test.probdecisiontest.ProbabilityDecisionTests;
import aima.test.probreasoningtest.ProbabilisticReasoningTests;
import aima.test.search.SearchTests;
import aima.test.tvenvironmenttest.ModelBasedTVEVaccumAgentTest;
import aima.test.tvenvironmenttest.ReflexVaccumAgentTest;
import aima.test.tvenvironmenttest.ReflexVaccumAgentWithStateTest;
import aima.test.tvenvironmenttest.SimpleReflexVaccumAgentTest;
import aima.test.tvenvironmenttest.TableDrivenAgentProgramTest;
import aima.test.tvenvironmenttest.TableDrivenVaccumAgentTest;
import aima.test.tvenvironmenttest.TrivialVaccumEnvironmentTest;
import aima.test.utiltest.MeanStDevTests;
import aima.test.utiltest.TableTest;

/**
 * @author Ravi Mohan
 * 
 */

public class AllAimaTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(MeanStDevTests.class));
		suite.addTest(new TestSuite(ModelBasedTVEVaccumAgentTest.class));
		suite.addTest(new TestSuite(PerceptTest.class));
		suite.addTest(new TestSuite(PerceptSequenceTest.class));
		suite.addTest(new TestSuite(ReflexVaccumAgentTest.class));
		suite.addTest(new TestSuite(ReflexVaccumAgentWithStateTest.class));
		suite.addTest(new TestSuite(RuleTest.class));
		suite.addTest(new TestSuite(SimpleReflexVaccumAgentTest.class));
		suite.addTest(new TestSuite(TableTest.class));
		suite.addTest(new TestSuite(TableDrivenAgentProgramTest.class));
		suite.addTest(new TestSuite(TableDrivenVaccumAgentTest.class));
		suite.addTest(new TestSuite(TicTacToeTest.class));
		suite.addTest(new TestSuite(TrivialVaccumEnvironmentTest.class));
		suite.addTest(new TestSuite(XYEnvironmentTest.class));
		suite.addTest(new TestSuite(XYLocationTest.class));

		suite.addTest(LearningTests.suite());
		suite.addTest(LogicTests.suite());
		suite.addTest(SearchTests.suite());
		suite.addTest(ProbabilityTests.suite());
		suite.addTest(ProbabilisticReasoningTests.suite());
		suite.addTest(ProbabilityDecisionTests.suite());

		return suite;
	}

}
