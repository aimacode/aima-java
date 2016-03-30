package aima.core.api.search.adversarial;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page ??.<br>
 * <br>
 * An extension of the Problem interface with addition of the utility & terminate method
 * to use in chapter 5
 */

public interface Problem<A, S> extends aima.core.api.search.Problem<A, S>{

    boolean terminalTest(S s);

    double utility(S s);
}
