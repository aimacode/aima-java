/*
 * Created on Sep 22, 2004
 *
 */
package aima.logic.fol.demos;

import aima.logic.fol.DLKnowledgeBase;
import aima.logic.fol.parsing.DomainFactory;

/**
 * @author Ravi Mohan
 * 
 */
public class FolDemo {
	public static void main(String[] args) {
		substDemo();
		unifierDemo();
		fOL_FCDemo();
	}

	private static void unifierDemo() {
		// TODO write this

	}

	private static void fOL_FCDemo() {
		kingsDemo();

		weaponsDemo();

	}

	private static void kingsDemo() {

		DLKnowledgeBase kb = new DLKnowledgeBase(DomainFactory.kingsDomain());
		String rule1 = "((King(x) AND Greedy(x)) => Evil(x))";
		String fact1 = "King(John)";
		String fact2 = "King(Richard)";
		String fact3 = "Greedy(John)";

		System.out.println("\n\nForward Chaining Demo 2 -Kings\n\n");
		System.out.println("Adding " + rule1);
		kb.add(rule1);
		System.out.println("Adding " + fact1);
		kb.add(fact1);
		System.out.println("Adding " + fact2);
		kb.add(fact2);
		System.out.println("Adding " + fact3);
		kb.add(fact3);
		String query = "Evil(who)";
		System.out.println("\nQuery = " + query);
		System.out.println("Forward Chaining gives " + kb.forwardChain(query));

	}

	private static void weaponsDemo() {
		/*
		 * 1.The Parser is very Finicky the names used MUST match the ones in
		 * Domain EXACTLY ! 2.Numbers canNOT be part of the names thus Mone
		 * instaed of M1 3.Rules - Parantheses ARE important for connected
		 * sentences including AND and => sentences 4.Standardise Apart is NOT
		 * implemented => every separate variable in the Kb must have a UNIQUE
		 * string.BUT otoh, you can use any string starting with a small letter.
		 */

		String rule1 = "( (((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))";
		String rule2 = "((Missile(m) AND Owns(NoNo,m)) => Sells(West,m,NoNo))";
		String rule3 = "(Missile(missile) => Weapon(missile))";
		String rule4 = "(Enemy(enemy,America) => Hostile(America))";

		// facts

		String fact1 = " Owns(NoNo, Mone)";
		String fact2 = "American(West)";
		String fact3 = "Enemy(NoNo,America)";
		DLKnowledgeBase kb = new DLKnowledgeBase(DomainFactory.weaponsDomain());
		System.out.println("\n\nForward Chaining Demo 2 -Missiles\n\n");
		System.out.println("Adding Rules to KB\n");

		System.out.println("Adding " + rule1);
		kb.add(rule1);

		System.out.println("Adding " + rule2);
		kb.add(rule2);

		System.out.println("Adding " + rule3);
		kb.add(rule3);

		System.out.println("Adding " + rule4);
		kb.add(rule4);

		System.out.println("Adding Facts \n");

		System.out.println("Adding " + fact1);
		kb.add(fact1);
		System.out.println("Adding " + fact2);
		kb.add(fact2);
		System.out.println("Adding " + fact3);
		kb.add(fact3);

		String query = "Criminal(who)";
		System.out.println("\nQuery = " + query);
		System.out.println("Forward Chaining gives " + kb.forwardChain(query));
	}

	private static void substDemo() {
		// TODO write this ?

	}

}