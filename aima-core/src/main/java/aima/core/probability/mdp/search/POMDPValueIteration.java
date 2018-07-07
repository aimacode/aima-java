package aima.core.probability.mdp.search;

import aima.core.agent.Action;
import aima.core.probability.mdp.POMDP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 663.<br>
 * <br>
 *
 * <pre>
 * function POMDP-VALUE-ITERATION(pomdp, ε) returns a utility function
 *  inputs: pomdp, a POMDP with states S, actions A(s), transition model P(s′ | s, a),
 *       sensor model P(e | s), rewards R(s), discount γ
 *      ε, the maximum error allowed in the utility of any state
 *  local variables: U, U′, sets of plans p with associated utility vectors αp
 *
 *  U′ ← a set containing just the empty plan [], with α[](s) = R(s)
 *  repeat
 *    U ← U′
 *    U′ ← the set of all plans consisting of an action and, for each possible next percept,
 *      a plan in U with utility vectors computed according to Equation(17.13)
 *    U′ ← REMOVE-DOMINATED-PLANS(U′)
 *  until MAX-DIFFERENCE(U, U′) < ε(1 − γ) ⁄ γ
 *  return U
 * </pre>
 * <p>
 * Figure 17.9 A high-level sketch of the value iteration algorithm for POMDPs.
 * The REMOVE-DOMINATED-PLANS step and MAX-DIFFERENCE test are
 * typically implemented as linear programs.
 *
 * @param <S> the state type.
 * @param <A> the action type.
 * @author samagra
 */

public class POMDPValueIteration<S, A extends Action> {
    public POMDP<S, A> pomdp;
    public double maxError;
    public int depth;

    // function POMDP-VALUE-ITERATION(pomdp, ε) returns a utility function
    // inputs: pomdp, a POMDP with states S, actions A(s), transition model P(s′ | s, a),
    //                  sensor model P(e | s), rewards R(s), discount γ
    //      ε, the maximum error allowed in the utility of any state
    public POMDPValueIteration(POMDP<S, A> pomdp, double maxError, int maxDepth) {
        this.pomdp = pomdp;
        this.maxError = maxError;
        this.depth = maxDepth;
    }

    /**
     * The pomdp-Value-Iteration algorithm
     *
     * @return returns a utility function
     */
    public HashMap<List<A>, List<Double>> pomdpValueIteration() {

        // local variables: U, U′, sets of plans p with associated utility vectors αp
        HashMap<List<A>, List<Double>> uDash;
        HashMap<List<A>, List<Double>> u = new HashMap<>();
        List<Double> utilities = new ArrayList<>();

        // U′ ← a set containing just the empty plan [], with α[](s) = R(s)
        for (S state :
                pomdp.states()) {
            utilities.add(pomdp.reward(state));
        }
        uDash = new HashMap<>();
        uDash.put(new ArrayList<>(), utilities);
        int i = 0; // For maintaining tree depth

        // repeat until MAX-DIFFERENCE(U, U′) < ε(1 − γ) ⁄ γ
        while (maxDifference(u, uDash) < maxError * (1 - pomdp.getDiscount()) / pomdp.getDiscount() || (i <= this.depth)) {
            // U ← U′
            u = new HashMap<>(uDash);
            // the set of all plans consisting of an action and, for each possible next percept,
            // a plan in U with utility vectors computed according to Equation(??)
            uDash = increasePlanDepths(uDash);
            uDash = removeDominatedPlans(uDash);
            i++;
        }
        return u;
    }

    /**
     * the set of all plans consisting of an action and, for each possible next percept,
     * a plan in U with utility vectors computed according to Equation(17.13)
     *
     * @param uDash The utility function of depth d-1
     * @return The utility function of depth D.
     */
    private HashMap<List<A>, List<Double>> increasePlanDepths(HashMap<List<A>,
            List<Double>> uDash) {
        HashMap<List<A>, List<Double>> result = new HashMap<>();
        for (A action :
                this.pomdp.getAllActions()) {
            for (List<A> plan :
                    uDash.keySet()) {
                List<A> newPlan = new ArrayList<>(plan);
                List<Double> newUtilities = new ArrayList<>();
                newPlan.add(action);
                for (S currentState :
                        this.pomdp.states()) {
                    double planUtility = 0.0;
                    for (S actualState :
                            this.pomdp.states()) {
                        double tempUtility = 0.0;
                        for (S observation :
                                this.pomdp.states()) {
                            tempUtility += this.pomdp.sensorModel(observation,
                                    actualState) * uDash.get(plan).
                                    get((new ArrayList<>(this.pomdp.states())).indexOf(actualState));
                        }
                        planUtility = tempUtility * this.pomdp.transitionProbability(actualState,
                                currentState, action);
                    }
                    planUtility *= this.pomdp.getDiscount();
                    planUtility += this.pomdp.reward(currentState);
                    newUtilities.add(planUtility);
                }
                result.put(newPlan, newUtilities);
            }
        }
        return result;
    }

    /**
     * This method can be overridden to eliminate dominated plans
     *
     * @param uDash
     * @return Non dominated plans.
     */
    private HashMap<List<A>, List<Double>> removeDominatedPlans(HashMap<List<A>, List<Double>> uDash) {
        return uDash;
    }

    /**
     * This method can be overridden to apply a different difference calculation function.
     * It currently calculates the difference of the average of the maximum utilities of the plans
     *
     * @param u     First utility functtion
     * @param uDash Second Utility Function.
     * @return The maximum difference.
     */
    private double maxDifference(HashMap<List<A>, List<Double>> u, HashMap<List<A>, List<Double>> uDash) {
        double maxUtilOne = 0.0, maxUtilTwo = 0.0;
        for (List<A> plan :
                u.keySet()) {
            maxUtilOne += Collections.max(u.get(plan));
        }
        maxUtilOne /= u.keySet().size();
        for (List<A> plan :
                uDash.keySet()) {
            maxUtilTwo += Collections.max(uDash.get(plan));
        }
        maxUtilTwo /= u.keySet().size();
        return Math.abs(maxUtilOne - maxUtilTwo);
    }
}
