package aima.core.logic.fol;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.parsing.ast.FOLNode;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 9.1, page 328.
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
 * Figure 9.1 The unification algorithm. The algorithm works by comparing the structures
 * of the inputs, elements by element. The substitution theta that is the argument to UNIFY is built
 * up along the way and is used to make sure that later comparisons are consistent with bindings
 * that were established earlier. In a compound expression, such as F(A, B), the OP field picks
 * out the function symbol F and the ARGS field picks out the argument list (A, B).
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
			Set<Variable> varsToCheck = _variableCollector
					.collectAllVariables((Function) x);
			if (varsToCheck.contains(var)) {
				return true;
			}

			// Now need to check if cascading will cause occurs to happen
			// e.g.
			// Loves(SF1(v2),v2)
			// Loves(v3,SF0(v3))
			// or
			// P(v1,SF0(v1),SF0(v1))
			// P(v2,SF0(v2),v2 )
			// or
			// P(v1, F(v2),F(v2),F(v2),v1, F(F(v1)),F(F(F(v1))),v2)
			// P(F(v3),v4, v5, v6, F(F(v5)),v4, F(v3), F(F(v5)))
			return cascadeOccurCheck(theta, var, varsToCheck,
					new HashSet<Variable>(varsToCheck));
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

	private boolean cascadeOccurCheck(Map<Variable, Term> theta, Variable var,
			Set<Variable> varsToCheck, Set<Variable> varsCheckedAlready) {
		// Want to check if any of the variable to check end up
		// looping back around on the new variable.
		Set<Variable> nextLevelToCheck = new HashSet<Variable>();
		for (Variable v : varsToCheck) {
			Term t = theta.get(v);
			if (null == t) {
				// Variable may not be a key so skip
				continue;
			}
			if (t.equals(var)) {
				// e.g.
				// v1=v2
				// v2=SFO(v1)
				return true;
			} else if (t instanceof Function) {
				// Need to ensure the function this variable
				// is to be replaced by does not contain var.
				Set<Variable> indirectvars = _variableCollector
						.collectAllVariables(t);
				if (indirectvars.contains(var)) {
					return true;
				} else {
					// Determine the next cascade/level
					// of variables to check for looping
					for (Variable iv : indirectvars) {
						if (!varsCheckedAlready.contains(iv)) {
							nextLevelToCheck.add(iv);
						}
					}
				}
			}
		}
		if (nextLevelToCheck.size() > 0) {
			varsCheckedAlready.addAll(nextLevelToCheck);
			return cascadeOccurCheck(theta, var, nextLevelToCheck,
					varsCheckedAlready);
		}
		return false;
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