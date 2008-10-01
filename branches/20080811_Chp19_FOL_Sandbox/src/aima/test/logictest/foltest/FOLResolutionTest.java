package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import aima.logic.fol.inference.FOLResolution;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLResolutionTest extends TestCase {

	public void testBasicFOLResolutionFails() {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Criminal", terms);
		Set<Map<Variable, Term>> answer = kkb.ask(query);
		assertTrue(null != answer);
		assertTrue(0 == answer.size());
	}
	
	public void testBasicDefiniteClauseFOLResolutionTruthResponseFails() {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Richard"));
		Predicate query = new Predicate("Evil", terms);
		Set<Map<Variable, Term>> answer = kkb.ask(query);
		assertTrue(null != answer);
		assertEquals(0, answer.size());
	}

	public void testBasicDefiniteClauseFOLResolutionTruthResponseSucceeds() {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("John"));
		Predicate query = new Predicate("Evil", terms);
		Set<Map<Variable, Term>> answer = kkb.ask(query);
		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(0, answer.iterator().next().size());
	}

	public void testBasicDefiniteClauseFOLResolutionSingleQueryResponseSucceeds() {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Evil", terms);
		Set<Map<Variable, Term>> answer = kkb.ask(query);
		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(1, answer.iterator().next().size());
		assertEquals(new Constant("John"), answer.iterator().next().get(
				new Variable("x")));
	}
	
	public void testBasicDefiniteClauseFOLResolutionDoubleQueryResponseSucceeds() {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("King", terms);
		Set<Map<Variable, Term>> answer = kkb.ask(query);
		assertTrue(null != answer);
		assertEquals(2, answer.size());
		boolean gotJohn, gotRichard;
		gotJohn = gotRichard = false;
		Constant cJohn = new Constant("John");
		Constant cRichard = new Constant("Richard");
		for (Map<Variable, Term> ans : answer) {
			assertEquals(1, ans.size());
			if (cJohn.equals(ans.get(new Variable("x")))) {
				gotJohn = true;
			}
			if (cRichard.equals(ans.get(new Variable("x")))) {
				gotRichard = true;
			}
		}
		assertTrue(gotJohn);
		assertTrue(gotRichard);
	}

	public void testComplexDefiniteClauseFOLResolutionSingleQueryResponseSucceeds() {
		FOLKnowledgeBase wkb = createWeaponsKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Criminal", terms);

		Set<Map<Variable, Term>> answer = wkb.ask(query);
		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(1, answer.iterator().next().size());
		assertEquals(new Constant("West"), answer.iterator().next().get(
				new Variable("x")));
	}
	
	public void testComplexFullFOLResolutionTruthResponseSucceeds() {
		FOLKnowledgeBase akb = createLovesAnimalKnowledgeBase();
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Curiosity"));
		terms.add(new Constant("Tuna")); 
		Predicate query = new Predicate("Kills", terms);
		
		Set<Map<Variable, Term>> answer = akb.ask(query);
		assertTrue(null != answer);
		assertEquals(1, answer.size());
	}

	public FOLKnowledgeBase createKingsKnowledgeBase() {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory.kingsDomain(),
				new FOLResolution());
		kb.tell("((King(x) AND Greedy(x)) => Evil(x))");
		kb.tell("King(John)");
		kb.tell("King(Richard)");
		kb.tell("Greedy(John)");

		return kb;
	}

	private FOLKnowledgeBase createWeaponsKnowledgeBase() {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory
				.weaponsDomain(), new FOLResolution());
		kb
				.tell("( (((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))");
		kb.tell(" Owns(NoNo, Mone)");
		kb.tell(" Missile(Mone)");
		kb.tell("((Missile(x) AND Owns(NoNo,x)) => Sells(West,x,NoNo))");
		kb.tell("(Missile(x) => Weapon(x))");
		kb.tell("(Enemy(x,America) => Hostile(x))");
		kb.tell("American(West)");
		kb.tell("Enemy(NoNo,America)");

		return kb;
	}

	private FOLKnowledgeBase createLovesAnimalKnowledgeBase() {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory
				.lovesAnimalDomain(), new FOLResolution());

		kb
				.tell("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");
		kb
				.tell("FORALL x (EXISTS y (Animal(y) AND Kills(x, y)) => FORALL z NOT(Loves(z, x)))");
		kb.tell("FORALL x (Animal(x) => Loves(Jack, x))");
		kb.tell("(Kills(Jack, Tuna) OR Kills(Curiosity, Tuna))");
		kb.tell("Cat(Tuna)");
		kb.tell("FORALL x (Cat(x) => Animal(x))");

		return kb;
	}
}