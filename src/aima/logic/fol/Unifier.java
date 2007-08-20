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

	// returns a Hashtable of results if succesful , null if fail
	public Hashtable unify(FOLNode x, FOLNode y, Hashtable theta) {

		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			return theta;
		} else if (isVariable(x)) {
			return unifyVar((Variable) x, y, theta);
		} else if (isVariable(y)) {
			return unifyVar((Variable) y, x, theta);
		} else if ((isCompound(x)) && (isCompound(y))) {
			return unifyLists(args(x), args(y), unifyOps(op(x), op(y), theta));
		} else if (isList(x) && isList(y)) {
			return unifyLists((List) x, (List) y, theta);
		}

		else {
			return null;
		}
	}

	public Hashtable unifyLists(List x, List y, Hashtable theta) {
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			return theta;
		} else {
			return unifyLists(rest(x), rest(y),
					unify(first(x), first(y), theta));
		}
	}

	public Hashtable unifyOps(String x, String y, Hashtable theta) {
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			return theta;
		} else {

			return null;
		}
	}

	private Hashtable unifyVar(Variable var, FOLNode x, Hashtable theta) {
		if (theta.keySet().contains(var)) {

			return unify((FOLNode) theta.get(var), x, theta);
		} else if (theta.keySet().contains(x)) {

			return unify(var, (FOLNode) theta.get(var), theta);
		} else if (occurCheck(var, x)) {
			return null;// failure
		} else {
			theta.put(var, x);
			return theta;
		}
	}

	private boolean occurCheck(Variable var, FOLNode x) {
		return false;
	}

	private List args(FOLNode x) {
		if (isFunction(x)) {
			return ((Function) x).getTerms();
		} else if (isPredicate(x)) {
			return ((Predicate) x).getTerms();
		} else {
			return null;
		}

	}

	private String op(FOLNode x) {
		if (isFunction(x)) {
			return ((Function) x).getFunctionName();
		} else if (isPredicate(x)) {
			return ((Predicate) x).getPredicateName();
		} else {
			return null;
		}

	}

	private boolean isList(FOLNode x) {
		return List.class.isInstance(x);
	}

	private boolean isVariable(FOLNode x) {
		return Variable.class.isInstance(x);
	}

	private boolean isPredicate(FOLNode x) {
		return Predicate.class.isInstance(x);
	}

	private boolean isFunction(FOLNode x) {
		return Function.class.isInstance(x);
	}

	private FOLNode first(List x) {
		List other = duplicate(x);
		FOLNode first = (FOLNode) other.get(0);
		return first;
	}

	private List rest(List x) {
		if (x.size() == 1) {
			return new ArrayList();
		} else {
			List other = duplicate(x);
			other.remove(0);
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
		if (isPredicate(x) || isFunction(x)) {
			return true;
		} else {
			return false;
		}
	}

}