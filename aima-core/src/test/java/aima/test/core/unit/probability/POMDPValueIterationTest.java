package aima.test.core.unit.probability;

import aima.core.probability.mdp.search.POMDPValueIteration;
import org.junit.Test;

/**
 * @author samagra
 * @author Ruediger Lunde
 */
public class POMDPValueIterationTest {
    @Test
    public void test() {
        POMDPValueIteration<POMDP.State, POMDP.Action> algo = new POMDPValueIteration<>(new POMDP(), 0.01, 2);
        System.out.println(algo.pomdpValueIteration().toString());
        /**
         * Result comes out to be:
         * {[STAY, GO]=[1.71, 1.19],
         * [GO, STAY]=[0.11000000000000001, 1.9900000000000002],
         * [STAY, STAY]=[0.19, 2.71],
         * [GO, GO]=[0.9900000000000001, 1.11]}
         */
    }
}
