package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import aima.logic.fol.domain.FOLDomain;
import aima.logic.fol.inference.InferenceProcedure;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.NotSentence;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class CommonFOLInferenceProcedureTests extends TestCase {

	//
	// Protected Methods
	//
	protected void testDefiniteClauseKBKingsQueryCriminalXFalse(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Criminal", terms);
		Set<Map<Variable, Term>> answer = kkb.ask(query);
		assertTrue(null != answer);
		assertTrue(0 == answer.size());
	}
	
	protected void testDefiniteClauseKBKingsQueryRichardEvilFalse(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Richard"));
		Predicate query = new Predicate("Evil", terms);
		Set<Map<Variable, Term>> answer = kkb.ask(query);
		assertTrue(null != answer);
		assertEquals(0, answer.size());
	}

	protected void testDefiniteClauseKBKingsQueryJohnEvilSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("John"));
		Predicate query = new Predicate("Evil", terms);
		Set<Map<Variable, Term>> answer = kkb.ask(query);
		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(0, answer.iterator().next().size());
	}

	protected void testDefiniteClauseKBKingsQueryEvilXReturnsJohnSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase(infp);
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
	
	protected void testDefiniteClauseKBKingsQueryKingXReturnsJohnAndRichardSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase kkb = createKingsKnowledgeBase(infp);
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

	protected void testDefiniteClauseKBWeaponsQueryCriminalXReturnsWestSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase wkb = createWeaponsKnowledgeBase(infp);
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
	
	protected void testHornClauseKBRingOfThievesQuerySkisXReturnsNancyRedBertDrew(
			InferenceProcedure infp, boolean expectedToTimeOut) {
		FOLKnowledgeBase rotkb = createRingOfThievesKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Skis", terms);

		Set<Map<Variable, Term>> answer = rotkb.ask(query);
		
		if (expectedToTimeOut) {
			assertNull(answer);
		} else {
			assertTrue(null != answer);
			assertEquals(4, answer.size());
			List<Constant> expected = new ArrayList<Constant>();
			expected.add(new Constant("Nancy"));
			expected.add(new Constant("Red"));
			expected.add(new Constant("Bert"));
			expected.add(new Constant("Drew"));
			for (Map<Variable, Term> subst : answer) {
				expected.remove(subst.get(new Variable("x")));
			}
			assertEquals(0, expected.size());
		}
	}
	
	protected void testFullFOLKBLovesAnimalQueryKillsCuriosityTunaSucceeds(
			InferenceProcedure infp, boolean expectedToTimeOut) {
		FOLKnowledgeBase akb = createLovesAnimalKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Curiosity"));
		terms.add(new Constant("Tuna")); 
		Predicate query = new Predicate("Kills", terms);
		
		Set<Map<Variable, Term>> answer = akb.ask(query);
		if (expectedToTimeOut) {
			assertNull(answer);
		} else {
			assertTrue(null != answer);
			assertEquals(1, answer.size());
			assertEquals(0, answer.iterator().next().size());
		}
	}
	
	protected void testFullFOLKBLovesAnimalQueryNotKillsJackTunaSucceeds(
			InferenceProcedure infp, boolean expectedToTimeOut) {
		FOLKnowledgeBase akb = createLovesAnimalKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Jack"));
		terms.add(new Constant("Tuna"));
		NotSentence query = new NotSentence(new Predicate("Kills", terms));

		Set<Map<Variable, Term>> answer = akb.ask(query);
		
		if (expectedToTimeOut) {
			assertNull(answer);
		} else {
			assertTrue(null != answer);
			assertEquals(1, answer.size());
			assertEquals(0, answer.iterator().next().size());
		}
	}
	
	protected void testFullFOLKBLovesAnimalQueryKillsJackTunaFalse(
			InferenceProcedure infp, boolean expectedToTimeOut) {
		FOLKnowledgeBase akb = createLovesAnimalKnowledgeBase(infp);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Jack"));
		terms.add(new Constant("Tuna"));
		Predicate query = new Predicate("Kills", terms);

		Set<Map<Variable, Term>> answer = akb.ask(query);
		
		if (expectedToTimeOut) {
			assertNull(answer);
		} else {
			assertTrue(null != answer);
			assertEquals(0, answer.size());
		}
	}
	
	protected void testEqualityAxiomsKBabcAEqualsCSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase akb = createABCEqualityAxiomsKnowledgeBase(infp);

		TermEquality query = new TermEquality(new Constant("A"), new Constant(
				"C"));
		
		Set<Map<Variable, Term>> answer = akb.ask(query);

		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(0, answer.iterator().next().size());
	}
	
	protected void testEqualityAndSubstitutionAxiomsKBabcdFFASucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase akb = createABCDEqualityAndSubstitutionAxiomsKnowledgeBase(infp);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("A"));
		Function fa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(fa);
		TermEquality query = new TermEquality(new Function("F", terms),
				new Constant("A"));
		
		Set<Map<Variable, Term>> answer = akb.ask(query);

		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(0, answer.iterator().next().size());
	}
	
	protected void testEqualityAndSubstitutionAxiomsKBabcdPDSucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase akb = createABCDEqualityAndSubstitutionAxiomsKnowledgeBase(infp);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("D"));
		Predicate query = new Predicate("P", terms);

		Set<Map<Variable, Term>> answer = akb.ask(query);

		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(0, answer.iterator().next().size());
	}
	
	protected void testEqualityAndSubstitutionAxiomsKBabcdPFFASucceeds(
			InferenceProcedure infp) {
		FOLKnowledgeBase akb = createABCDEqualityAndSubstitutionAxiomsKnowledgeBase(infp);

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("A"));
		Function fa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(fa);
		Function ffa = new Function("F", terms);
		terms = new ArrayList<Term>();
		terms.add(ffa);
		Predicate query = new Predicate("P", terms);

		Set<Map<Variable, Term>> answer = akb.ask(query);

		assertTrue(null != answer);
		assertEquals(1, answer.size());
		assertEquals(0, answer.iterator().next().size());
	}
	
	//
	// PRIVATE
	//
	
	private FOLKnowledgeBase createKingsKnowledgeBase(InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory.kingsDomain(),
				infp);
		kb.tell("((King(x) AND Greedy(x)) => Evil(x))");
		kb.tell("King(John)");
		kb.tell("King(Richard)");
		kb.tell("Greedy(John)");

		return kb;
	}

	private FOLKnowledgeBase createWeaponsKnowledgeBase(InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory
				.weaponsDomain(), infp);
		kb
				.tell("( (((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))");
		kb.tell(" Owns(Nono, M1)");
		kb.tell(" Missile(M1)");
		kb.tell("((Missile(x) AND Owns(Nono,x)) => Sells(West,x,Nono))");
		kb.tell("(Missile(x) => Weapon(x))");
		kb.tell("(Enemy(x,America) => Hostile(x))");
		kb.tell("American(West)");
		kb.tell("Enemy(Nono,America)");

		return kb;
	}

	private FOLKnowledgeBase createLovesAnimalKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory
				.lovesAnimalDomain(), infp);

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
	
	private FOLKnowledgeBase createRingOfThievesKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory
				.ringOfThievesDomain(), infp);
		
		// s(x) => ~c(x) One who skis never gets caught
		kb.tell("(Skis(x) => NOT(Caught(x)))");
		// c(x) => ~s(x) Those who are caught don't ever ski
		kb.tell("(Caught(x) => NOT(Skis(x)))");
		// p(x,y) & c(y) => s(x) Jailbird parents have skiing kids
		kb.tell("((Parent(x,y) AND Caught(y)) => Skis(x))");
		// s(x) & f(x,y) => s(y) All friends ski together
		kb.tell("(Skis(x) AND Friend(x,y) => Skis(y))");
		// f(x,y) => f(y,x) Friendship is symmetric
		kb.tell("(Friend(x,y) => Friend(y,x))");
		// FACTS
		// 1. { p(Mike,Joe) } Premise
		kb.tell("Parent(Mike, Joe)");
		// 2. { p(Janet,Joe) } Premise
		kb.tell("Parent(Janet,Joe)");
		// 3. { p(Nancy,Mike) } Premise
		kb.tell("Parent(Nancy,Mike)");
		// 4. { p(Ernie,Janet) } Premise
		kb.tell("Parent(Ernie,Janet)");
		// 5. { p(Bert,Nancy) } Premise
		kb.tell("Parent(Bert,Nancy)");
		// 6. { p(Red,Ernie) } Premise
		kb.tell("Parent(Red,Ernie)");
		// 7. { f(Red,Bert) } Premise
		kb.tell("Friend(Red,Bert)");
		// 8. { f(Drew,Nancy) } Premise
		kb.tell("Friend(Drew,Nancy)");
		// 9. { c(Mike) } Premise
		kb.tell("Caught(Mike)");
		// 10. { c(Ernie) } Premise
		kb.tell("Caught(Ernie)");

		return kb;
	}
	
	// Note: see -
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// slide 12 for where this test example was taken from.
	private FOLKnowledgeBase createABCEqualityAxiomsKnowledgeBase(
			InferenceProcedure infp) {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");

		FOLKnowledgeBase kb = new FOLKnowledgeBase(domain, infp);

		kb.tell("B = A");
		kb.tell("B = C");
		// Reflexivity Axiom
		kb.tell("x = x");
		// Symmetry Axiom
		kb.tell("(x = y => y = x)");
		// Transitivity Axiom
		kb.tell("((x = y AND y = z) => x = z)");

		return kb;
	}
	
	// Note: see -
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// slide 16,17, and 18 for where this test example was taken from.
	private FOLKnowledgeBase createABCDEqualityAndSubstitutionAxiomsKnowledgeBase(
			InferenceProcedure infp) {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addPredicate("P");
		domain.addFunction("F");

		FOLKnowledgeBase kb = new FOLKnowledgeBase(domain, infp);

		kb.tell("F(A) = B");
		kb.tell("F(B) = A");
		kb.tell("C = D");
		kb.tell("P(A)");
		kb.tell("P(C)");
		// Reflexivity Axiom
		kb.tell("x = x");
		// Symmetry Axiom
		kb.tell("(x = y => y = x)");
		// Transitivity Axiom
		kb.tell("((x = y AND y = z) => x = z)");
		// Function F Substitution Axiom
		kb.tell("((x = y AND F(y) = z) => F(x) = z)");
		// Predicate P Substitution Axiom
		kb.tell("((x = y AND P(y)) => P(x))");
		
		return kb;
	}
}