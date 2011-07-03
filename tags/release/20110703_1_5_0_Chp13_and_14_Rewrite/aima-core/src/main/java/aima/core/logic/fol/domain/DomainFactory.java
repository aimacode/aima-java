package aima.core.logic.fol.domain;

/**
 * @author Ravi Mohan
 * 
 */
public class DomainFactory {
	public static FOLDomain crusadesDomain() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("John");
		domain.addConstant("Richard");
		domain.addConstant("England");
		domain.addConstant("Saladin");
		domain.addConstant("Crown");

		domain.addFunction("LeftLegOf");
		domain.addFunction("BrotherOf");
		domain.addFunction("EnemyOf");
		domain.addFunction("LegsOf");

		domain.addPredicate("King");
		return domain;
	}

	public static FOLDomain knowsDomain() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("John");
		domain.addConstant("Jane");
		domain.addConstant("Bill");
		domain.addConstant("Elizabeth");
		domain.addFunction("Mother");
		domain.addPredicate("Knows");
		return domain;
	}

	public static FOLDomain weaponsDomain() {

		FOLDomain domain = new FOLDomain();
		domain.addConstant("West");
		domain.addConstant("America");
		domain.addConstant("M1");
		domain.addConstant("Nono");
		domain.addPredicate("American");
		domain.addPredicate("Weapon");
		domain.addPredicate("Sells");
		domain.addPredicate("Hostile");
		domain.addPredicate("Criminal");
		domain.addPredicate("Missile");
		domain.addPredicate("Owns");
		domain.addPredicate("Enemy");

		return domain;
	}

	public static FOLDomain kingsDomain() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("John");
		domain.addConstant("Richard");
		domain.addPredicate("King");
		domain.addPredicate("Greedy");
		domain.addPredicate("Evil");
		return domain;
	}

	public static FOLDomain lovesAnimalDomain() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("Animal");
		domain.addPredicate("Loves");
		domain.addPredicate("Kills");
		domain.addPredicate("Cat");
		domain.addConstant("Jack");
		domain.addConstant("Tuna");
		domain.addConstant("Curiosity");
		return domain;
	}

	public static FOLDomain ringOfThievesDomain() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("Parent");
		domain.addPredicate("Caught");
		domain.addPredicate("Friend");
		domain.addPredicate("Skis");
		domain.addConstant("Mike");
		domain.addConstant("Joe");
		domain.addConstant("Janet");
		domain.addConstant("Nancy");
		domain.addConstant("Ernie");
		domain.addConstant("Bert");
		domain.addConstant("Red");
		domain.addConstant("Drew");
		return domain;
	}
}
