package aima.core.probability;

import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 626.<br>
 * <br>
 * Decision networks combine Bayesian networks
 * with additional node types for actions and utilities.<br>
 * <p>
 * In its most general form, a decision network represents information about the agent’s current
 * state, its possible actions, the state that will result from the agent’s action, and the utility of
 * that state.
 *
 * @author samagra
 */
public abstract class DecisionNetwork {

    // The underlying Bayesian network
    private BayesianNetwork network;
    // The single decision node
    private RandomVariable action;
    // To calculate various conditional probabilities
    private BayesInference inferenceProcedure;

    /**
     * Constructor for the decision network.
     *
     * @param network            The underlying Bayesian Network.
     * @param action             The decision node .
     * @param inferenceProcedure The inference procedure to be utilised for probability calculations.
     */
    public DecisionNetwork(BayesianNetwork network,
                           RandomVariable action, BayesInference inferenceProcedure) {
        this.network = network;
        this.action = action;
        this.inferenceProcedure = inferenceProcedure;
    }

    // Returns the utility for a particular state
    public abstract double getUtilityForAction(RandomVariable action, Object value);

    /**
     * Calculates the expected utility of an action in the presence of a certain random variable.
     *
     * @param action   Action for which the utility is to be calculated.
     * @param evidence The available information.
     * @return
     */
    public double getExpectedUtility(RandomVariable action,
                                     List<AssignmentProposition> evidence) {
        double utility = 0;
        CategoricalDistribution distribution = inferenceProcedure.ask((new RandomVariable[]{action}),
                ((AssignmentProposition[]) evidence.toArray()), this.getNetwork());
        for (Object value :
                ((FiniteDomain) action.getDomain()).getPossibleValues()) {
            utility += distribution.getValue(value) * this.getUtilityForAction(action, value);
        }
        return utility;
    }

    /**
     * Currently the decision network supports only a single decision node and hence returns
     * the same action.
     *
     * @return
     */
    public Object getBestAction() {
        return action;
    }

    /**
     * Returns the underlying Bayesian Network.
     *
     * @return
     */
    public BayesianNetwork getNetwork() {
        return network;
    }
}
