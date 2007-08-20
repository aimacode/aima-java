/*
 * Created on Sep 20, 2004
 *
 */
package aima.logic.fol.parsing;

import aima.logic.fol.FOLDomain;

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
		domain.addConstant("Mone");
		domain.addConstant("NoNo");
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

}
