package aima.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import aima.test.coretest.PerceptSequenceTest;
import aima.test.coretest.RuleTest;
import aima.test.coretest.TableDrivenAgentProgramTest;
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
import aima.test.tvenvironmenttest.TrivialVaccumEnvironmentTest;
import aima.test.utiltest.TableTest;

public class AllAimaTests {


	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(ModelBasedTVEVaccumAgentTest.class));
		suite.addTest(new TestSuite(PerceptSequenceTest.class));
		suite.addTest(new TestSuite(RuleTest.class));
		suite.addTest(new TestSuite(TableTest.class));
		suite.addTest(new TestSuite(TableDrivenAgentProgramTest.class));
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
