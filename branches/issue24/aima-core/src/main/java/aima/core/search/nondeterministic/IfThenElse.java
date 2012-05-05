package aima.core.search.nondeterministic;

/**
 * Represents an if-then-else statement for use with AND-OR search; explanation
 * given on page 135.
 *
 * @author Andrew Brown
 */
public class IfThenElse {

    Object antecedent;
    Object thenConsequent;
    Object elseConsequent;

    /**
     * Empty constructor
     */
    public IfThenElse() {
    }

    /**
     * Constructor
     *
     * @param antecedent
     * @param thenConsequent
     */
    public IfThenElse(Object antecedent, Object thenConsequent) {
        this.antecedent = antecedent;
        this.thenConsequent = thenConsequent;
    }

    /**
     * Constructor
     *
     * @param antecedent
     * @param thenConsequent
     * @param elseConsequent
     */
    public IfThenElse(Object antecedent, Object thenConsequent, Object elseConsequent) {
        this.antecedent = antecedent;
        this.thenConsequent = thenConsequent;
        this.elseConsequent = elseConsequent;
    }

    /**
     * Uses this if-then-else to return a result based on the given query
     *
     * @param query
     * @return
     */
    public Object queryWith(Object query) {
        if (this.antecedent.equals(query)) {
            return this.thenConsequent;
        } else {
            return this.elseConsequent;
        }
    }

    /**
     * Return string representation of this if-then-else
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("if ");
        s.append(this.antecedent);
        s.append(" then ");
        s.append(this.thenConsequent);
        s.append(" else ");
        s.append(this.elseConsequent);
        return s.toString();
    }
}