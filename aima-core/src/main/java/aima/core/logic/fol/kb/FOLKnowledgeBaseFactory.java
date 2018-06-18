package aima.core.logic.fol.kb;

import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.InferenceProcedure;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLKnowledgeBaseFactory {

	public static FOLKnowledgeBase createKingsKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(DomainFactory.kingsDomain(),
				infp);
		kb.tell("((King(x) AND Greedy(x)) => Evil(x))");
		kb.tell("King(John)");
		kb.tell("King(Richard)");
		kb.tell("Greedy(John)");

		return kb;
	}

	public static FOLKnowledgeBase createWeaponsKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(
				DomainFactory.weaponsDomain(), infp);
		kb.tell("( (((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))");
		kb.tell(" Owns(Nono, M1)");
		kb.tell(" Missile(M1)");
		kb.tell("((Missile(x) AND Owns(Nono,x)) => Sells(West,x,Nono))");
		kb.tell("(Missile(x) => Weapon(x))");
		kb.tell("(Enemy(x,America) => Hostile(x))");
		kb.tell("American(West)");
		kb.tell("Enemy(Nono,America)");

		return kb;
	}

	public static FOLKnowledgeBase createLovesAnimalKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(
				DomainFactory.lovesAnimalDomain(), infp);

		kb.tell("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");
		kb.tell("FORALL x (EXISTS y (Animal(y) AND Kills(x, y)) => FORALL z NOT(Loves(z, x)))");
		kb.tell("FORALL x (Animal(x) => Loves(Jack, x))");
		kb.tell("(Kills(Jack, Tuna) OR Kills(Curiosity, Tuna))");
		kb.tell("Cat(Tuna)");
		kb.tell("FORALL x (Cat(x) => Animal(x))");

		return kb;
	}

	public static FOLKnowledgeBase createRingOfThievesKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(
				DomainFactory.ringOfThievesDomain(), infp);

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
	public static FOLKnowledgeBase createABCEqualityKnowledgeBase(
			InferenceProcedure infp, boolean includeEqualityAxioms) {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");

		FOLKnowledgeBase kb = new FOLKnowledgeBase(domain, infp);

		kb.tell("B = A");
		kb.tell("B = C");

		if (includeEqualityAxioms) {
			// Reflexivity Axiom
			kb.tell("x = x");
			// Symmetry Axiom
			kb.tell("(x = y => y = x)");
			// Transitivity Axiom
			kb.tell("((x = y AND y = z) => x = z)");
		}

		return kb;
	}

	// Note: see -
	// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
	// slide 16,17, and 18 for where this test example was taken from.
	public static FOLKnowledgeBase createABCDEqualityAndSubstitutionKnowledgeBase(
			InferenceProcedure infp, boolean includeEqualityAxioms) {
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

		if (includeEqualityAxioms) {
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
		}

		return kb;
	}

	public static FOLKnowledgeBase familyKnowledgeBase(){
		FOLDomain domain = new FOLDomain();
		domain.addConstant("George");
		domain.addConstant("Mum");
		domain.addConstant("Spencer");
		domain.addConstant("Kydd");
		domain.addConstant("Elizabeth");
		domain.addConstant("Philip");
		domain.addConstant("Margaret");
		domain.addConstant("Diana");
		domain.addConstant("Charles");
		domain.addConstant("Anne");
		domain.addConstant("Mark");
		domain.addConstant("Andrew");
		domain.addConstant("Sarah");
		domain.addConstant("Edward");
		domain.addConstant("Sophie");
		domain.addConstant("William");
		domain.addConstant("Harry");
		domain.addConstant("Peter");
		domain.addConstant("Zara");
		domain.addConstant("Beatrice");
		domain.addConstant("Eugenie");
		domain.addConstant("Louise");
		domain.addConstant("James");
		// Now predicates
		domain.addPredicate("Mother");
		domain.addPredicate("Father");
		domain.addPredicate("Married");
		domain.addPredicate("Male");
		domain.addPredicate("Female");
		//male
		FOLKnowledgeBase kb = new FOLKnowledgeBase(domain);
		kb.tell("Male(George)");
		kb.tell("Male(Spencer)");
		kb.tell("Male(Philip)");
		kb.tell("Male(Charles)");
		kb.tell("Male(Mark)");
		kb.tell("Male(Andrew)");
		kb.tell("Male(Edward)");
		kb.tell("Male(William)");
		kb.tell("Male(Harry)");
		kb.tell("Male(Peter)");
		kb.tell("Male(James)");
		kb.tell("Male(Louise)");
		// female
		kb.tell("Female(Mum)");
		kb.tell("Female(Kydd)");
		kb.tell("Female(Elizabeth)");
		kb.tell("Female(Margaret)");
		kb.tell("Female(Diana)");
		kb.tell("Female(Anne)");
		kb.tell("Female(Sarah)");
		kb.tell("Female(Sophie)");
		kb.tell("Female(Zara)");
		kb.tell("Female(Beatrice)");
		kb.tell("Female(Eugenie)");
		//married
		kb.tell("Married(George,Mum)");
		kb.tell("Married(Spencer,Kydd)");
		kb.tell("Married(Elizabeth,Philip)");
		kb.tell("Married(Diana,Charles)");
		kb.tell("Married(Anne,Mark)");
		kb.tell("Married(Andrew,Sarah)");
		kb.tell("Married(Edward,Sophie)");
		kb.tell("(Married(x,y) => Married(y,x))");
		//Mother
		kb.tell("Mother(Mum,Elizabeth)");
		kb.tell("Mother(Mum,Margaret)");
		kb.tell("Mother(Kydd,Diana)");
		kb.tell("Mother(Elizabeth,Charles)");
		kb.tell("Mother(Elizabeth,Anne)");
		kb.tell("Mother(Elizabeth,Andrew)");
		kb.tell("Mother(Elizabeth,Edward)");
		kb.tell("Mother(Diana,William)");
		kb.tell("Mother(Diana,Harry)");
		kb.tell("Mother(Anne,Peter)");
		kb.tell("Mother(Anne,Zara)");
		kb.tell("Mother(Sarah,Beatrice)");
		kb.tell("Mother(Sarah,Eugenie)");
		kb.tell("Mother(Sophie,Louise)");
		kb.tell("Mother(Sophie,James)");
		//father
		kb.tell("Father(George,Elizabeth)");
		kb.tell("Father(George,Margaret)");
		kb.tell("Father(Spencer,Diana)");
		kb.tell("Father(Philip,Charles)");
		kb.tell("Father(Philip,Anne)");
		kb.tell("Father(Philip,Andrew)");
		kb.tell("Father(Philip,Edward)");
		kb.tell("Father(William,Charles)");
		kb.tell("Father(William,Harry)");
		kb.tell("Father(Mark,Peter)");
		kb.tell("Father(Mark,Zara)");
		kb.tell("Father(Andrew,Beatrice)");
		kb.tell("Father(Andrew,Eugenie)");
		kb.tell("Father(Edward,Louise)");
		kb.tell("Father(Edward,James)");
		return kb;
	}
}
