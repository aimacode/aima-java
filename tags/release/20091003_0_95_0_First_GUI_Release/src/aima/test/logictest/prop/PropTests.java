/*
 * Created on Nov 9, 2004
 *
 */
package aima.test.logictest.prop;

import junit.framework.Test;
import junit.framework.TestSuite;
import aima.test.logictest.prop.algorithms.DPLLTest;
import aima.test.logictest.prop.algorithms.KnowledgeBaseTest;
import aima.test.logictest.prop.algorithms.ModelTest;
import aima.test.logictest.prop.algorithms.PLFCEntailsTest;
import aima.test.logictest.prop.algorithms.PLResolutionTest;
import aima.test.logictest.prop.algorithms.TTEntailsTest;
import aima.test.logictest.prop.parser.PELexerTest;
import aima.test.logictest.prop.parser.PEParserTest;
import aima.test.logictest.prop.visitors.CNFClauseGathererTest;
import aima.test.logictest.prop.visitors.CNFTransformerTest;
import aima.test.logictest.prop.visitors.SymbolClassifierTest;
import aima.test.logictest.prop.visitors.SymbolCollectorTest;

/**
 * @author Ravi Mohan
 * 
 */

public class PropTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(CNFClauseGathererTest.class));
		suite.addTest(new TestSuite(CNFTransformerTest.class));
		suite.addTest(new TestSuite(DPLLTest.class));
		suite.addTest(new TestSuite(KnowledgeBaseTest.class));
		suite.addTest(new TestSuite(ModelTest.class));
		suite.addTest(new TestSuite(PELexerTest.class));
		suite.addTest(new TestSuite(PEParserTest.class));
		suite.addTest(new TestSuite(PLFCEntailsTest.class));
		suite.addTest(new TestSuite(PLResolutionTest.class));
		suite.addTest(new TestSuite(SymbolClassifierTest.class));
		suite.addTest(new TestSuite(SymbolCollectorTest.class));
		suite.addTest(new TestSuite(TTEntailsTest.class));

		return suite;
	}
}