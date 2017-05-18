package aima.core.search.csp;

/**
 * A variable is a distinguishable object with a name.
 *
 * @author Ruediger Lunde
 */
public class Variable {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    /** Variables with equal names are equal. */
    @Override
    public final boolean equals(Object obj) {
        return obj instanceof Variable && this.name.equals(((Variable) obj).name);
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }
}
