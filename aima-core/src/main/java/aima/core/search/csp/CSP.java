package aima.core.search.csp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Section 6.1, Page 202.<br>
 * <br>
 * A constraint satisfaction problem or CSP consists of three components, X, D,
 * and C:
 * <ul>
 * <li>X is a set of variables, {X1, ... ,Xn}.</li>
 * <li>D is a set of domains, {D1, ... ,Dn}, one for each variable.</li>
 * <li>C is a set of constraints that specify allowable combinations of values.</li>
 * </ul>
 *
 * @param <VAR> Type which is used to represent variables
 * @param <VAL> Type which is used to represent the values in the domains
 *
 * @author Ruediger Lunde
 */
public class CSP<VAR extends Variable, VAL> implements Cloneable {

    private final List<VAR> variables;
    private List<Domain<VAL>> domains;
    private final List<Constraint<VAR, VAL>> constraints;

    /**
     * Lookup, which maps a variable to its index in the list of variables.
     */
    private final Hashtable<Variable, Integer> varIndexHash;
    /**
     * Constraint network. Maps variables to those constraints in which they
     * participate.
     */
    private final Hashtable<Variable, List<Constraint<VAR, VAL>>> cnet;

    /**
     * Creates a new CSP.
     */
    public CSP() {
        variables = new ArrayList<>();
        domains = new ArrayList<>();
        constraints = new ArrayList<>();

        varIndexHash = new Hashtable<>();
        cnet = new Hashtable<>();
    }

    /**
     * Creates a new CSP.
     */
    public CSP(List<VAR> vars) {
        this();
        vars.forEach(this::addVariable);
    }

    /**
     * Adds a new variable only if its name is new.
     */
    protected void addVariable(VAR var) {
        if (!varIndexHash.containsKey(var)) {
            Domain<VAL> emptyDomain = new Domain<>(Collections.emptyList());
            variables.add(var);
            domains.add(emptyDomain);
            varIndexHash.put(var, variables.size() - 1);
            cnet.put(var, new ArrayList<>());
        } else {
            throw new IllegalArgumentException("Variable with same name already exists.");
        }
    }

    public List<VAR> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    public int indexOf(Variable var) {
        return varIndexHash.get(var);
    }

    public void setDomain(VAR var, Domain<VAL> domain) {
        domains.set(indexOf(var), domain);
    }

    public Domain<VAL> getDomain(Variable var) {
        return domains.get(varIndexHash.get(var));
    }

    /**
     * Replaces the domain of the specified variable by new domain, which
     * contains all values of the old domain except the specified value.
     */
    public boolean removeValueFromDomain(VAR var, VAL value) {
        Domain<VAL> currDomain = getDomain(var);
        List<VAL> values = new ArrayList<>(currDomain.size());
        for (VAL v : currDomain)
            if (!v.equals(value))
                values.add(v);
        if (values.size() < currDomain.size()) {
            setDomain(var, new Domain<>(values));
            return true;
        }
        return false;
    }

    public void addConstraint(Constraint<VAR, VAL> constraint) {
        constraints.add(constraint);
        constraint.getScope().forEach(var -> cnet.get(var).add(constraint));
    }

    public boolean removeConstraint(Constraint<VAR, VAL> constraint) {
        if (constraints.remove(constraint)) {
            constraint.getScope().forEach(var -> cnet.get(var).remove(constraint));
            return true;
        }
        return false;
    }

    public List<Constraint<VAR, VAL>> getConstraints() {
        return constraints;
    }

    /**
     * Returns all constraints in which the specified variable participates.
     */
    public List<Constraint<VAR, VAL>> getConstraints(Variable var) {
        return cnet.get(var);
    }

    /**
     * Returns for binary constraints the other variable from the scope.
     *
     * @return a variable or null for non-binary constraints.
     */
    public VAR getNeighbor(VAR var, Constraint<VAR, VAL> constraint) {
        List<VAR> scope = constraint.getScope();
        if (scope.size() == 2) {
            if (var.equals(scope.get(0)))
                return scope.get(1);
            else if (var.equals(scope.get(1)))
                return scope.get(0);
        }
        return null;
    }

    /**
     * Returns a copy which contains a copy of the domains list and is in all
     * other aspects a flat copy of this.
     */
    @SuppressWarnings("unchecked")
    public CSP<VAR, VAL> copyDomains() {
        CSP<VAR, VAL> result;
        try {
            result = (CSP<VAR, VAL>) clone();
            result.domains = new ArrayList<>(domains);
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("Could not copy domains.");
        }
        return result;
    }
}