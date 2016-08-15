package aima.core.logic.basic.firstorder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aima.core.logic.basic.firstorder.parsing.ast.FOLNode;
import aima.core.logic.basic.firstorder.parsing.ast.Function;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;


/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page
 * ???.<br>
 * <br>
 * 
 * <pre>
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
 *       return UNIFY(x.ARGS, y.ARGS, UNIFY(x.OP, y.OP, theta))
 *   else if LIST?(x) and LIST?(y) then
 *       return UNIFY(x.REST, y.REST, UNIFY(x.FIRST, y.FIRST, theta))
 *   else return failure
 *   
 * ---------------------------------------------------------------------------------------------------
 * 
 * function UNIFY-VAR(var, x, theta) returns a substitution
 *            
 *   if {var/val} E theta then return UNIFY(val, x, theta)
 *   else if {x/val} E theta then return UNIFY(var, val, theta)
 *   else if OCCUR-CHECK?(var, x) then return failure
 *   else return add {var/x} to theta
 * </pre>
 * 
 * Figure ?? The unification algorithm. The algorithm works by comparing the
 * structures of the inputs, elements by element. The substitution theta that is
 * the argument to UNIFY is built up along the way and is used to make sure that
 * later comparisons are consistent with bindings that were established earlier.
 * In a compound expression, such as F(A, B), the OP field picks out the
 * function symbol F and the ARGS field picks out the argument list (A, B).
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
public class Unifier {
	
	private static SubstVisitor _substVisitor = new SubstVisitor();

	public Unifier() {

	}

	/**
	 * Returns a Map<Variable, Term> representing the substitution (i.e. a set
	 * of variable/term pairs) or null which is used to indicate a failure to
	 * unify.
	 * 
	 * @param x
	 *            a variable, constant, list, or compound
	 * @param y
	 *            a variable, constant, list, or compound
	 * 
	 * @return a Map<Variable, Term> representing the substitution (i.e. a set
	 *         of variable/term pairs) or null which is used to indicate a
	 *         failure to unify.
	 */
	public Map<Variable, Term> unify(FOLNode x, FOLNode y) {
		return unify(x, y, new LinkedHashMap<Variable, Term>());
	}

	/**
	 * Returns a Map<Variable, Term> representing the substitution (i.e. a set
	 * of variable/term pairs) or null which is used to indicate a failure to
	 * unify.
	 * 
	 * @param x
	 *            a variable, constant, list, or compound
	 * @param y
	 *            a variable, constant, list, or compound
	 * @param theta
	 *            the substitution built up so far
	 * 
	 * @return a Map<Variable, Term> representing the substitution (i.e. a set
	 *         of variable/term pairs) or null which is used to indicate a
	 *         failure to unify.
	 */
	public Map<Variable, Term> unify(FOLNode x, FOLNode y,
			Map<Variable, Term> theta) {
		// if theta = failure then return failure
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			// else if x = y then return theta
			return theta;
		} else if (x instanceof Variable) {
			// else if VARIABLE?(x) then return UNIVY-VAR(x, y, theta)
			return unifyVar((Variable) x, y, theta);
		} else if (y instanceof Variable) {
			// else if VARIABLE?(y) then return UNIFY-VAR(y, x, theta)
			return unifyVar((Variable) y, x, theta);
		} else if (isCompound(x) && isCompound(y)) {
			// else if COMPOUND?(x) and COMPOUND?(y) then
			// return UNIFY(x.ARGS, y.ARGS, UNIFY(x.OP, y.OP, theta))
			return unify(args(x), args(y), unifyOps(op(x), op(y), theta));
		} else {
			// else return failure
			return null;
		}
	}

	/**
	 * Returns a Map<Variable, Term> representing the substitution (i.e. a set
	 * of variable/term pairs) or null which is used to indicate a failure to
	 * unify.
	 * 
	 * @param x
	 *            a variable, constant, list, or compound
	 * @param y
	 *            a variable, constant, list, or compound
	 * @param theta
	 *            the substitution built up so far
	 * 
	 * @return a Map<Variable, Term> representing the substitution (i.e. a set
	 *         of variable/term pairs) or null which is used to indicate a
	 *         failure to unify.
	 */
	// else if LIST?(x) and LIST?(y) then
	// return UNIFY(x.REST, y.REST, UNIFY(x.FIRST, y.FIRST, theta))
	public Map<Variable, Term> unify(List<? extends FOLNode> x,
			List<? extends FOLNode> y, Map<Variable, Term> theta) {
		if (theta == null) {
			return null;
		} else if (x.size() != y.size()) {
			return null;
		} else if (x.size() == 0 && y.size() == 0) {
			return theta;
		} else if (x.size() == 1 && y.size() == 1) {
			return unify(x.get(0), y.get(0), theta);
		} else {
			return unify(x.subList(1, x.size()), y.subList(1, y.size()),
					unify(x.get(0), y.get(0), theta));
		}
	}

	//
	// PROTECTED METHODS
	//

	// Note: You can subclass and override this method in order
	// to re-implement the OCCUR-CHECK?() to always
	// return false if you want that to be the default
	// behavior, as is the case with Prolog.
	// Note: Implementation is based on unify-bug.pdf document by Peter Norvig:
	// http://norvig.com/unify-bug.pdf
	protected boolean occurCheck(Map<Variable, Term> theta, Variable var,
			FOLNode x) {
		// ((equal var x) t)
		if (var.equals(x)) {
			return true;
			// ((bound? x subst)
		} else if (theta.containsKey(x)) {
			// (occurs-in? var (lookup x subst) subst))
			return occurCheck(theta, var, theta.get(x));
			// ((consp x) (or (occurs-in? var (first x) subst) (occurs-in? var
			// (rest x) subst)))
		} else if (x instanceof Function) {
			// (or (occurs-in? var (first x) subst) (occurs-in? var (rest x)
			// subst)))
			Function fx = (Function) x;
			for (Term fxt : fx.getArgs()) {
				if (occurCheck(theta, var, fxt)) {
					return true;
				}
			}
		}
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
			return unify(var, theta.get(x), theta);
		} else if (occurCheck(theta, var, x)) {
			// else if OCCUR-CHECK?(var, x) then return failure
			return null;
		} else {
			// else return add {var/x} to theta
			cascadeSubstitution(theta, var, (Term) x);
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

	private List<? extends FOLNode> args(FOLNode x) {
		return x.getArgs();
	}

	private String op(FOLNode x) {
		return x.getSymbolicName();
	}

	private boolean isCompound(FOLNode x) {
		return x.isCompound();
	}

	// See:
	// http://logic.stanford.edu/classes/cs157/2008/miscellaneous/faq.html#jump165
	// for need for this.
	private Map<Variable, Term> cascadeSubstitution(Map<Variable, Term> theta,
			Variable var, Term x) {
		theta.put(var, x);
		for (Variable v : theta.keySet()) {
			theta.put(v, _substVisitor.subst(theta, theta.get(v)));
		}
		// Ensure Function Terms are correctly updates by passing over them
		// again. Fix for testBadCascadeSubstitution_LCL418_1()
		for (Variable v : theta.keySet()) {
			Term t = theta.get(v);
			if (t instanceof Function) {
				theta.put(v, _substVisitor.subst(theta, t));
			}
		}
		return theta;
	}
}
