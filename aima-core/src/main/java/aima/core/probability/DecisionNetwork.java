package aima.core.probability;

import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;
import org.junit.Test;

import java.util.List;

public abstract class DecisionNetwork {

    private BayesianNetwork network;
    private RandomVariable action;
    private BayesInference inferenceProcedure;

    public DecisionNetwork(BayesianNetwork network,
                           RandomVariable action, BayesInference inferenceProcedure) {
        this.network = network;
        this.action = action;
        this.inferenceProcedure = inferenceProcedure;
    }

    public abstract double getUtilityForAction(RandomVariable action, Object value );

    public double getExpectedUtility(RandomVariable action,
                                     List<AssignmentProposition> evidence){
        double utility = 0;
        CategoricalDistribution distribution = inferenceProcedure.ask((new RandomVariable[]{action}),
                ((AssignmentProposition[])evidence.toArray()),this.getNetwork());
        for (Object value :
                ((FiniteDomain) action.getDomain()).getPossibleValues()) {
            utility += distribution.getValue(value)*this.getUtilityForAction(action,value);
        }
        return utility;
    }

    public Object getBestAction(){
        return action;
    }

    public BayesianNetwork getNetwork() {
        return network;
    }
}
