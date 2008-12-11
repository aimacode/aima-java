/*
 * Created on Aug 30, 2003 by Ravi Mohan
 *  
 */
package aima.test.logictest;

import junit.framework.Test;
import junit.framework.TestSuite;
import aima.test.logictest.foltest.CNFConverterTest;
import aima.test.logictest.foltest.ChainTest;
import aima.test.logictest.foltest.ClauseTest;
import aima.test.logictest.foltest.DemodulationTest;
import aima.test.logictest.foltest.DomainTest;
import aima.test.logictest.foltest.FOLBCAskTest;
import aima.test.logictest.foltest.FOLFCAskTest;
import aima.test.logictest.foltest.FOLKnowledgeBaseTest;
import aima.test.logictest.foltest.FOLLexerTest;
import aima.test.logictest.foltest.FOLModelEliminationTest;
import aima.test.logictest.foltest.FOLOTTERLikeTheoremProverTest;
import aima.test.logictest.foltest.FOLParserTest;
import aima.test.logictest.foltest.FOLSubstTest;
import aima.test.logictest.foltest.FOLTFMResolutionTest;
import aima.test.logictest.foltest.ParamodulationTest;
import aima.test.logictest.foltest.PredicateCollectorTest;
import aima.test.logictest.foltest.UnifierTest;
import aima.test.logictest.foltest.VariableCollectorTest;
import aima.test.logictest.prop.PropTests;
import aima.test.utiltest.SetTest;

/**
 * @author Ravi Mohan
 * 
 */
public class LogicTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		// propositional tests
		suite.addTest(PropTests.suite());

		// first order tests
		suite.addTest(new TestSuite(ChainTest.class));
		suite.addTest(new TestSuite(ClauseTest.class));
		suite.addTest(new TestSuite(CNFConverterTest.class));
		suite.addTest(new TestSuite(DemodulationTest.class));
		suite.addTest(new TestSuite(DomainTest.class));
		suite.addTest(new TestSuite(FOLBCAskTest.class));
		suite.addTest(new TestSuite(FOLFCAskTest.class));
		suite.addTest(new TestSuite(FOLKnowledgeBaseTest.class));
		suite.addTest(new TestSuite(FOLLexerTest.class));
		suite.addTest(new TestSuite(FOLModelEliminationTest.class));
		suite.addTest(new TestSuite(FOLOTTERLikeTheoremProverTest.class));
		suite.addTest(new TestSuite(FOLParserTest.class));
		suite.addTest(new TestSuite(FOLSubstTest.class));
		suite.addTest(new TestSuite(FOLTFMResolutionTest.class));
		suite.addTest(new TestSuite(ParamodulationTest.class));
		suite.addTest(new TestSuite(PredicateCollectorTest.class));
		suite.addTest(new TestSuite(UnifierTest.class));
		suite.addTest(new TestSuite(VariableCollectorTest.class));

		// utils
		suite.addTest(new TestSuite(SetTest.class));
		return suite;
	}

	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

}