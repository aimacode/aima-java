package aima.logic.fol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.logic.fol.parsing.ast.FOLNode;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 9.1, page 278.
 * 
 * <code>
 * function UNIFY(x, y, theta) returns a substitution to make x and y identical
 *   inputs: x, a variable, constant, list, or compound
 *           y, a variable, constant, list, or compound
 *           theta, the substitution built up so far (optional, defaults to empty)
 *           
 *   if theta = failure then return failure
 *   else if x = y the return theta
 *   else if VARIABLE?(x) then return UNIVY-VAR(x, y, theta)
 *   else if VARIABLE?(y) then return UNIFY-VAR(y, x, theta)
 *   else if COMPOUND?(x) and COMPOUND?(y) then
 *       return UNIFY(ARGS[x], ARGS[y], UNIFY(OP[x], OP[y], theta))
 *   else if LIST?(x) and LIST?(y) then
 *       return UNIFY(REST[x], REST[y], UNIFY(FIRST[x], FIRST[y], theta))
 *   else return failure
 *   
 * -------------------------------------------------------------------------------------------------
 * 
 * function UNIFY-VAR(var, x, theta) returns a substitution
 *   inputs: var, a variable
 *           x, any expression
 *           theta, the substitution built up so far
 *           
 *   if {var/val} E theta then return UNIFY(val, x, theta)
 *   else if {x/val} E theta then return UNIFY(var, val, theta)
 *   else if OCCUR-CHECK?(var, x) then return failure
 *   else return add {var/x} to theta
 * </code>
 * 
 * Figure 9.1 The unification algorithm. The algorithm works by comparing the structures
 * of the inputs, elements by element. The substitution theta that is the argument to UNIFY is built
 * up along the way and is used to make sure that later comparisons are consistent with bindings
 * that were established earlier. In a compound expression, such as F(A, B), the function OP
 * picks out the function symbol F and the function ARGS picks out the argument list (A, B).
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public class Unifier {

	public Unifier() {

	}

	/**
	 * <code>
	 * function UNIFY(x, y, theta) returns a substitution to make x and y identical
	 *   inputs: x, a variable, constant, list, or compound
	 *           y, a variable, constant, list, or compound
	 *           theta, the substitution built up so far (optional, defaults to empty)
	 * </code>
	 * 
	 * @return a Map<Variable, Term> representing the substitution (i.e. a set
	 *         of variable/term pairs, see pg. 254 for a description) or null
	 *         which is used to indicate a failure to unify.
	 */
	public Map<Variable, Term> unify(FOLNode x, FOLNode y) {
		return unify(x, y, new HashMap<Variable, Term>());
	}

	public Map<Variable, Term> unify(FOLNode x, FOLNode y,
			Map<Variable, Term> theta) {

		// if theta = failure then return failure
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			// else if x = y the return theta
			return theta;
		} else if (isVariable(x)) {
			// else if VARIABLE?(x) then return UNIVY-VAR(x, y, theta)
			return unifyVar((Variable) x, y, theta);
		} else if (isVariable(y)) {
			// else if VARIABLE?(y) then return UNIFY-VAR(y, x, theta)
			return unifyVar((Variable) y, x, theta);
		} else if ((isCompound(x)) && (isCompound(y))) {
			// else if COMPOUND?(x) and COMPOUND?(y) then
			// return UNIFY(ARGS[x], ARGS[y], UNIFY(OP[x], OP[y], theta))
			return unify(args(x), args(y), unifyOps(op(x), op(y), theta));
		} else {
			// else return failure
			return null;
		}
	}
	
	// else if LIST?(x) and LIST?(y) then
	// return UNIFY(REST[x], REST[y], UNIFY(FIRST[x], FIRST[y], theta))
	public Map<Variable, Term> unify(List<Term> x, List<Term> y,
			Map<Variable, Term> theta) {
		if (theta == null) {
			return null;
		} else if (x.size() == 0 && y.size() == 0) {
			return theta;
		} else {
			return unify(rest(x), rest(y), unify(first(x), first(y), theta));
		}
	}

	//
	// PROTECTED METHODS
	//

	// Note: You can subclass and override this method in order
	// to implement the OCCUR-CHECK?() if needed.
	protected boolean occurCheck(Variable var, FOLNode x) {
		return false;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * <code>
	 * function UNIFY-VAR(var, x, theta) returns a substitution
	 *   inputs: var, a variable
	 *       x, any expression
	 *       theta, the substitution built up so far
	 * </code>
	 */
	private Map<Variable, Term> unifyVar(Variable var, FOLNode x,
			Map<Variable, Term> theta) {
		
		if (!Term.class.isInstance(x)) {
			return null;
		} else if (theta.keySet().contains(var)) {
			// if {var/val} E theta then return UNIFY(val, x, theta)
			return unify(theta.get(var), x, theta);
		} else if (theta.keySet().contains(x)) {
			// else if {x/val} E theta then return UNIFY(var, val, theta)
			return unify(var, theta.get(var), theta);
		} else if (occurCheck(var, x)) {
			// else if OCCUR-CHECK?(var, x) then return failure
			return null;
		} else {
			// else return add {var/x} to theta
			theta.put(var, (Term) x);
			return theta;
		}
	}

	private Map<Variable, Term> unifyOps(String x, String y,
			Map<Variable, Term> theta) {
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			return theta;
		} else {
			return null;
		}
	}

	private List<Term> args(FOLNode x) {
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

	private boolean isVariable(FOLNode x) {
		return Variable.class.isInstance(x);
	}

	private boolean isPredicate(FOLNode x) {
		return Predicate.class.isInstance(x);
	}

	private boolean isFunction(FOLNode x) {
		return Function.class.isInstance(x);
	}

	private FOLNode first(List<Term> x) {
		List<Term> other = duplicate(x);
		FOLNode first = (FOLNode) other.get(0);
		return first;
	}

	private List<Term> rest(List<Term> x) {
		if (x.size() == 1) {
			return new ArrayList<Term>();
		} else {
			List<Term> other = duplicate(x);
			other.remove(0);
			return other;
		}
	}

	private List<Term> duplicate(List<Term> x) {
		return new ArrayList<Term>(x);
	}

	private boolean isCompound(FOLNode x) {
		if (isPredicate(x) || isFunction(x)) {
			return true;
		} else {
			return false;
		}
	}
}