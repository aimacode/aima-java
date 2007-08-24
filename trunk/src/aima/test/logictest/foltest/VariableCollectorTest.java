/*
 * Created on Sep 20, 2004
 *
 */
package aima.test.logictest.foltest;

import java.util.Set;

import junit.framework.TestCase;
import aima.logic.fol.VariableCollector;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */

public class VariableCollectorTest extends TestCase {
	private Sentence sentence;

	FOLParser parser;

	VariableCollector vc;

	@Override
	public void setUp() {
		parser = new FOLParser(DomainFactory.crusadesDomain());
		vc = new VariableCollector(parser);
	}

	public void testSimplepredicate() {
		Set variables = vc.collectAllVariables(parser.parse("King(x)"));
		assertEquals(1, variables.size());
		assertTrue(variables.contains(new Variable("x")));
	}

	public void testMultipleVariables() {
		Set variables = vc.collectAllVariables(parser
				.parse("BrotherOf(x) = EnemyOf(y)"));
		assertEquals(2, variables.size());
		assertTrue(variables.contains(new Variable("x")));
		assertTrue(variables.contains(new Variable("y")));
	}

}
