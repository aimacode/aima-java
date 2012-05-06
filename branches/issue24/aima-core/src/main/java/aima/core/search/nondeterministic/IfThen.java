package aima.core.search.nondeterministic;

/**
 * Represents an if-then statement for use with AND-OR search; explanation given
 * on page 135.
 *
 * @author Andrew Brown
 */
public class IfThen {

    Object antecedent;
    Object consequent;

    /**
     * Empty constructor
     */
    public IfThen() {
    }

    /**
     * Constructor
     *
     * @param antecedent
     * @param thenConsequent
     */
    public IfThen(Object antecedent, Object thenConsequent) {
        this.antecedent = antecedent;
        this.consequent = thenConsequent;
    }

    /**
     * Uses this if-then-else to return a result based on the given query
     *
     * @param query
     * @return
     */
    public Object queryWith(Object query) {
        if (this.antecedent.equals(query)) {
            return this.consequent;
        } else {
            return null;
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
        s.append(this.consequent);
        return s.toString();
    }
}