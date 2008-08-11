package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;
import aima.logic.fol.Unifier;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */

public class UnifierTest extends TestCase {
	FOLParser parser;

	Unifier unifier;

	Hashtable theta;

	@Override
	public void setUp() {
		parser = new FOLParser(DomainFactory.knowsDomain());
		unifier = new Unifier(parser);
		theta = new Hashtable();
	}

	public void testFailureIfThetaisNull() {
		Variable var = new Variable("x");
		Sentence sentence = parser.parse("Knows(x)");
		theta = null;
		Hashtable result = unifier.unify(var, sentence, theta);
		assertNull(result);
	}

	public void testUnificationFailure() {
		Variable var = new Variable("x");
		Sentence sentence = parser.parse("Knows(y)");
		theta = null;
		Hashtable result = unifier.unify(var, sentence, theta);
		assertNull(result);
	}

	public void testThetaPassedBackIfXEqualsYBothVariables() {
		Variable var1 = new Variable("x");
		Variable var2 = new Variable("x");

		theta.put("dummy", "dummy");
		Hashtable result = unifier.unify(var1, var2, theta);
		assertEquals(1, theta.keySet().size());
		assertTrue(theta.contains("dummy"));
	}

	public void testVariableEqualsConstant() {
		Variable var1 = new Variable("x");
		Constant constant = new Constant("John");

		Hashtable result = unifier.unify(var1, constant, theta);
		assertEquals(1, theta.keySet().size());
		assertTrue(theta.keySet().contains(var1));
		assertEquals(constant, theta.get(var1));
	}

	public void testSimpleVariableUnification() {
		Variable var1 = new Variable("x");
		List<Term> terms1 = new ArrayList<Term>();
		terms1.add(var1);
		Predicate p1 = new Predicate("King", terms1); // King(x)

		List<Term> terms2 = new ArrayList<Term>();
		terms2.add(new Constant("John"));
		Predicate p2 = new Predicate("King", terms2); // King(John)

		Hashtable result = unifier.unify(p1, p2, theta);
		assertEquals(1, theta.keySet().size());
		assertTrue(theta.keySet().contains(new Variable("x"))); // x =
		assertEquals(new Constant("John"), theta.get(var1)); // John
	}

	public void testKnows1() {
		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(John,Jane)");
		Hashtable result = unifier.unify(query, johnKnowsJane, theta);
		assertTrue(theta.keySet().contains(new Variable("x"))); // x =
		assertEquals(new Constant("Jane"), theta.get(new Variable("x"))); // Jane

	}

	public void testKnows2() {
		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(y,Bill)");
		Hashtable result = unifier.unify(query, johnKnowsJane, theta);

		assertEquals(2, result.size());

		assertEquals(new Constant("Bill"), theta.get(new Variable("x"))); // x =
		// Bill
		assertEquals(new Constant("John"), theta.get(new Variable("y"))); // y =
		// John

	}

	public void testKnows3() {
		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(y,Mother(y))");
		Hashtable result = unifier.unify(query, johnKnowsJane, theta);

		assertEquals(2, result.size());

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("y"));
		Function mother = new Function("Mother", terms);
		assertEquals(mother, theta.get(new Variable("x")));
		assertEquals(new Constant("John"), theta.get(new Variable("y"))); // y =
		// John

	}

	public void testKnows5() {
		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(y,z)");
		Hashtable result = unifier.unify(query, johnKnowsJane, theta);

		assertEquals(2, result.size());

		assertEquals(new Variable("z"), theta.get(new Variable("x"))); // x = z
		assertEquals(new Constant("John"), theta.get(new Variable("y"))); // y =
		// John

	}

}
