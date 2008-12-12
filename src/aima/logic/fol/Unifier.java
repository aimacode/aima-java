package aima.logic.fol;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.parsing.ast.FOLNode;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 9.1, page 278.
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
 * </pre>
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
	//
	private static SubstVisitor _substVisitor = new SubstVisitor();
	private static VariableCollector _variableCollector = new VariableCollector();

	public Unifier() {

	}

	public Map<Variable, Term> unify(FOLNode x, FOLNode y) {
		return unify(x, y, new LinkedHashMap<Variable, Term>());
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
	public Map<Variable, Term> unify(FOLNode x, FOLNode y,
			Map<Variable, Term> theta) {

		// if theta = failure then return failure
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			// else if x = y the return theta
			return theta;
		} else if (x instanceof Variable) {
			// else if VARIABLE?(x) then return UNIVY-VAR(x, y, theta)
			return unifyVar((Variable) x, y, theta);
		} else if (y instanceof Variable) {
			// else if VARIABLE?(y) then return UNIFY-VAR(y, x, theta)
			return unifyVar((Variable) y, x, theta);
		} else if (isCompound(x) && isCompound(y)) {
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
	public Map<Variable, Term> unify(List<? extends FOLNode> x,
			List<? extends FOLNode> y,
			Map<Variable, Term> theta) {
		if (theta == null) {
			return null;
		} else if (x.size() != y.size()) {
			return null;
		} else if (x.size() == 0 && y.size() == 0) {
			return theta;
		} else if (x.size() == 1 && y.size() == 1) {
			return unify(x.get(0), y.get(0), theta);
		} else {
			return unify(x.subList(1, x.size()), y.subList(1, y.size()), unify(
					x.get(0), y.get(0), theta));
		}
	}

	//
	// PROTECTED METHODS
	//

	// Note: You can subclass and override this method in order
	// to re-implement the OCCUR-CHECK?() to always
	// return false if you want that to be the default
	// behavior, as is the case with Prolog.
	protected boolean occurCheck(Map<Variable, Term> theta, Variable var,
			FOLNode x) {
		if (x instanceof Function) {
			Set<Variable> vars = _variableCollector
					.collectAllVariables((Function) x);
			if (vars.contains(var)) {
				return true;
			}
			
			// Now need to check if cascading will cause occurs to happen
			// e.g. Loves(SF1(v2),v2) and Loves(v3,SF0(v3))
			for (Variable v : theta.keySet()) {
				Term t = theta.get(v);
				if (t instanceof Function) {
					// If a possible occurs problem
					// i.e. the term x contains this variable
					if (vars.contains(v)) {
						// then need to ensure the function this variable
						// is to be replaced by does not contain var.
						Set<Variable> indirectvars = _variableCollector
								.collectAllVariables((Function) t);
						if (indirectvars.contains(var)) {
							return true;
						}
					}
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
	private void cascadeSubstitution(Map<Variable, Term> theta, Variable var,
			Term x) {
		theta.put(var, x);
		for (Variable v : theta.keySet()) {
			Term t = theta.get(v);
			theta.put(v, _substVisitor.subst(theta, t));		
		}
	}
}