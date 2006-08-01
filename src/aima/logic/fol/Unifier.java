package aima.logic.fol;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.FOLNode;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 *  
 */
public class Unifier {

	private FOLParser parser;

	public Unifier(FOLParser parser) {

		this.parser = parser;
	}

	//returns a Hashtable of rsults if succesful , null if fail
	public Hashtable unify(FOLNode x, FOLNode y, Hashtable theta) {

		//System.out.println("UNIFY1 unify ( " + x + "," + y + "," + theta
		//+ ") + xclass = " + x.getClass().getName());
		if (theta == null) {
			//System.out.println("theta is null\n");
			return null;
		} else if (x.equals(y)) {
			//System.out.println(x + " = " + y);
			return theta;
		} else if (Variable.class.isInstance(x)) {
			//System.out.println("x is a variable");
			return unifyVar((Variable) x, y, theta);
		} else if (Variable.class.isInstance(y)) {
			return unifyVar((Variable) y, x, theta);
		} else if ((isCompound(x)) && (isCompound(y))) {
			return unify(args(x), args(y), unify(op(x), op(y), theta));
		} else if (List.class.isInstance(x) && List.class.isInstance(y)) {
			return unify((List) x, (List) y, theta);
		}

		else {
			return null;
		}
	}

	public Hashtable unify(List x, List y, Hashtable theta) {
		//System.out.println("unifyList ( " + x + "," + y + "," + theta + ")");
		if (theta == null) {
			//System.out.println("unifyList returning null "+ theta + ")");
			return null;
		} else if (x.equals(y)) {
			//System.out.println("unifyList returning "+ theta + ")");
			return theta;
		} else {
			List restX = rest(x);
			return unify(restX, rest(y), unify(first(x), first(y), theta));
		}
	}

	public Hashtable unify(String x, String y, Hashtable theta) {
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			return theta;
		} else {

			return null;
		}
	}

	private FOLNode first(List x) {
		List other = duplicate(x);
		FOLNode first = (FOLNode) other.get(0);
		//System.out.println("First(" + x + ") returns " + first);
		return first;
	}

	private List rest(List x) {
		if (x.size() == 1) {
			//System.out.println("rest(" + x + ") returns [] ");
			return new ArrayList();
		} else {
			List other = duplicate(x);
			//System.out.println("REST - other before remove "+other);
			other.remove(0);
			//System.out.println("REST - other after remove "+other);
			//System.out.println("rest(" + x + ") returns " + other);
			return other;
		}

	}

	private List duplicate(List x) {
		List<Object> other = new ArrayList<Object>();
		Iterator iter = x.iterator();
		while (iter.hasNext()) {
			other.add(iter.next());
		}
		return other;
	}

	private boolean isCompound(FOLNode x) {
		if (Predicate.class.isInstance(x) || Function.class.isInstance(x)) {
			return true;
		} else {
			return false;
		}
	}

	private Hashtable unifyVar(Variable var, FOLNode x, Hashtable theta) {
		//System.out.println("unifyVar ( " + var + "," + x + "," + theta +
		// ")");
		if (theta.keySet().contains(var)) {
			//System.out.println("unifyVar #1 Theta" + theta + " contains " +
			// var
			//+ "( " + var + ") so returning -->");
			return unify((FOLNode) theta.get(var), x, theta);
		} else if (theta.keySet().contains(x)) {
			//System.out.println("unifyVar #2 Theta" + theta + " contains " + x
			//+ "( " + ") so returning -->");
			return unify(var, (FOLNode) theta.get(var), theta);
		} else if (occurCheck(var, x)) {
			return null;//failure
		} else {
			//System.out.print("unify Var#4 added " + var + ":" + x + " to "
			//+ theta + " giving ");
			theta.put(var, x);
			//System.out.println(theta);
			//.print("returning "+theta);
			return theta;
		}
	}

	private boolean occurCheck(Variable var, FOLNode x) {
		return false;
	}

	private List args(FOLNode x) {
		if (Function.class.isInstance(x)) {
			return ((Function) x).getTerms();
		} else if (Predicate.class.isInstance(x)) {
			return ((Predicate) x).getTerms();
		} else {
			return null;
		}

	}

	private String op(FOLNode x) {
		if (Function.class.isInstance(x)) {
			return ((Function) x).getFunctionName();
		} else if (Predicate.class.isInstance(x)) {
			return ((Predicate) x).getPredicateName();
		} else {
			return null;
		}

	}

}