package aima.core.api.search.adversarial;


import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page ??.<br>
 * <br>
 * An extension of the Problem interface with addition of the utility & terminate method
 * to use in chapter 5
 */

public interface Problem<S> extends aima.core.api.search.Problem<S>{

    boolean terminalTest(S state);

    double utility(S state);
}
