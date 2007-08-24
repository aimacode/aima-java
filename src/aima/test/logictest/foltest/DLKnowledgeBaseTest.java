package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;
import aima.logic.fol.Clause;
import aima.logic.fol.DLKnowledgeBase;
import aima.logic.fol.FOLDomain;
import aima.logic.fol.Fact;
import aima.logic.fol.Rule;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */

public class DLKnowledgeBaseTest extends TestCase {
	private DLKnowledgeBase wkb, kkb;

	@Override
	public void setUp() {
		FOLDomain weaponsDomain = DomainFactory.weaponsDomain();
		wkb = new DLKnowledgeBase(weaponsDomain);

		FOLDomain kingsDomain = DomainFactory.kingsDomain();
		kkb = new DLKnowledgeBase(kingsDomain);

	}

	public void testAddRule() {
		wkb.add("(Missile(x) => Weapon(x))");
		assertEquals(1, wkb.getRules().size());
		wkb.add("American(West)");
		assertEquals(1, wkb.numRules());
		assertEquals(1, wkb.numFacts());

		Rule rule = wkb.rule(0);
		assertNotNull(rule);
		assertEquals(1, rule.numClauses());
	}

	public void testAddComplexRule() {
		wkb
				.add("( (((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))");
		assertEquals(1, wkb.getRules().size());
		wkb.add("American(West)");
		assertEquals(1, wkb.numRules());

		Rule rule = wkb.rule(0);
		assertNotNull(rule);
		assertEquals(4, rule.numClauses());
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		assertEquals(rule.conclusion(), new Predicate("Criminal", terms));
	}

	public void testClausesFormDomainsCorrectlyWithNoMatchingFacts() {
		kkb.add("((King(x) AND Greedy(x)) => Evil(x))");
		kkb.add("Greedy(John)");
		Rule rule = kkb.rule(0);
		Clause one = rule.clause(0);
		// System.out.println(kkb.fact(0));
		one.populateDomainsFrom(kkb.fact(0));
	}

	public void testClausesFormDomainsCorrectlyWithMatchingFacts() {
		kkb = createKingsKnowledgeBase();
		Constant john = new Constant("John");
		Constant richard = new Constant("Richard");
		Rule rule = kkb.rule(0);
		Clause one = rule.clause(0);
		one.populateDomainsFrom(kkb.fact(0));
		assertTrue(one.domain().getDomainOf("x").contains(john));
	}

	public void testRulesFormDomainsCorrectlyForAllClauses() {
		kkb = createKingsKnowledgeBase();
		Constant john = new Constant("John");
		Constant richard = new Constant("Richard");
		Rule rule = kkb.rule(0);
		rule.initializeAllClauseDomainsFrom(kkb.facts());
		Clause one = rule.clause(0);
		assertTrue(one.domain().getDomainOf("x").contains(john));
		assertTrue(one.domain().getDomainOf("x").contains(richard));
		Clause two = rule.clause(1);
		assertTrue(two.domain().getDomainOf("x").contains(john));
		assertFalse(two.domain().getDomainOf("x").contains(richard));

		List clauses = rule.clausesContaining("x");
		assertTrue(clauses.contains(one));
		assertTrue(clauses.contains(two));

	}

	public void testBasicForwardChainingFails() {
		kkb = createKingsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Criminal", terms);
		Hashtable h = kkb.forwardChain(query);
		assertTrue(h.isEmpty());
	}

	public void testBasicForwardChainingSucceeds() {
		kkb = createKingsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Evil", terms);
		Properties h = kkb.forwardChain(query);
		assertEquals(1, h.size());

		assertEquals("John", h.getProperty("x"));
	}

	public void testRuleTriggerable() {
		kkb = createKingsKnowledgeBase();
		Constant john = new Constant("John");
		Constant richard = new Constant("Richard");
		Rule rule = kkb.rule(0);
		rule.initializeAllClauseDomainsFrom(kkb.facts());
		assertTrue(rule.triggerable());
	}

	public void testComplexForwardChainingSucceeds() {
		kkb = createWeaponsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Criminal", terms);
		Properties h = kkb.forwardChain(query);
		// assertEquals(1,h.size());
		//		
		// assertEquals("John",h.getProperty("x"));
	}

	public void testFactNotAddedWhenAlreadyPresent() {
		kkb = createKingsKnowledgeBase();
		Constant john = new Constant("John");
		Constant richard = new Constant("Richard");
		Rule rule = kkb.rule(0);
		rule.initializeAllClauseDomainsFrom(kkb.facts());
		assertTrue(rule.triggerable());
		Predicate p = kkb.trigger(rule);
		assertTrue(kkb.facts().size() == 3);
		kkb.add(p.toString());
		assertTrue(kkb.facts().size() == 4);
		assertTrue(kkb.facts().contains(new Fact(p)));
		Predicate p2 = kkb.trigger(rule);
		assertTrue(kkb.facts().contains(new Fact(p2)));
	}

	public DLKnowledgeBase createKingsKnowledgeBase() {
		DLKnowledgeBase kb = new DLKnowledgeBase(DomainFactory.kingsDomain());
		kb.add("((King(x) AND Greedy(x)) => Evil(x))");
		kb.add("King(John)");
		kb.add("King(Richard)");
		kb.add("Greedy(John)");
		return kb;
	}

	private DLKnowledgeBase createWeaponsKnowledgeBase() {
		DLKnowledgeBase kb = new DLKnowledgeBase(DomainFactory.weaponsDomain());
		kb
				.add("( (((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))");
		kb.add(" Owns(NoNo, Mone)");
		kb.add("((Missile(m) AND Owns(NoNo,m)) => Sells(West,m,NoNo))");
		kb.add("(Missile(missile) => Weapon(missile))");
		kb.add("(Enemy(enemy,America) => Hostile(America))");
		kb.add("American(West)");
		kb.add("Enemy(NoNo,America)");

		return kb;
	}

}