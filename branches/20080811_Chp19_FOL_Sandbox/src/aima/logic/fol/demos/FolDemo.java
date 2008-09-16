/*
 * Created on Sep 22, 2004
 *
 */
package aima.logic.fol.demos;

import aima.logic.fol.inference.FOLFCAsk;
import aima.logic.fol.kb.DefiniteClauseKnowledgeBase;
import aima.logic.fol.parsing.DomainFactory;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
// TODO: Extends the Demos shown here.
public class FolDemo {
	public static void main(String[] args) {
		substDemo();
		unifierDemo();
		fOL_FCDemo();
	}

	private static void substDemo() {
		// TODO write this ?
	}
	
	private static void unifierDemo() {
		// TODO write this
	}

	private static void fOL_FCDemo() {
		kingsDemo();
		weaponsDemo();
	}

	private static void kingsDemo() {
		DefiniteClauseKnowledgeBase kb = new DefiniteClauseKnowledgeBase(
				DomainFactory.kingsDomain(), new FOLFCAsk());
		String rule1 = "((King(x) AND Greedy(x)) => Evil(x))";
		String fact1 = "King(John)";
		String fact2 = "King(Richard)";
		String fact3 = "Greedy(John)";

		System.out.println("\n\nForward Chaining Demo 2 -Kings\n\n");
		System.out.println("Adding " + rule1);
		kb.tell(rule1);
		System.out.println("Adding " + fact1);
		kb.tell(fact1);
		System.out.println("Adding " + fact2);
		kb.tell(fact2);
		System.out.println("Adding " + fact3);
		kb.tell(fact3);
		String query = "Evil(who)";
		System.out.println("\nQuery = " + query);
		System.out.println("Forward Chaining gives " + kb.ask(query));
	}

	private static void weaponsDemo() {
		DefiniteClauseKnowledgeBase kb = new DefiniteClauseKnowledgeBase(
				DomainFactory.weaponsDomain(), new FOLFCAsk());

		// rules
		String rule1 = "((((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))";
		String rule2 = "((Missile(x) AND Owns(NoNo,x)) => Sells(West,x,NoNo))";
		String rule3 = "(Missile(x) => Weapon(x))";
		String rule4 = "(Enemy(x,America) => Hostile(x))";

		// facts
		String fact1 = "Owns(NoNo, Mone)";
		String fact2 = "Missile(Mone)";
		String fact3 = "American(West)";
		String fact4 = "Enemy(NoNo,America)";
		
		System.out.println("\n\nForward Chaining Demo 2 -Missiles\n\n");
		System.out.println("Adding Rules to KB\n");

		System.out.println("Adding " + rule1);
		kb.tell(rule1);
		System.out.println("Adding " + rule2);
		kb.tell(rule2);
		System.out.println("Adding " + rule3);
		kb.tell(rule3);
		System.out.println("Adding " + rule4);
		kb.tell(rule4);

		System.out.println("Adding Facts \n");

		System.out.println("Adding " + fact1);
		kb.tell(fact1);
		System.out.println("Adding " + fact2);
		kb.tell(fact2);
		System.out.println("Adding " + fact3);
		kb.tell(fact3);
		System.out.println("Adding " + fact4);
		kb.tell(fact4);

		String query = "Criminal(who)";
		System.out.println("\nQuery = " + query);
		System.out.println("Forward Chaining gives " + kb.ask(query));
	}
}