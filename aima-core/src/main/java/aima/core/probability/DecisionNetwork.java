package aima.core.probability;

import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.domain.FiniteDomain;
import org.junit.Test;

public abstract class DecisionNetwork {

    private BayesianNetwork network;
    private RandomVariable action;

    public abstract double getUtilityForAction(RandomVariable action, Object value );

    public Object getBestAction(){
        double maxUtilValue = Double.MIN_VALUE;
        Object actionValue = null;
        for (Object value :
                ((FiniteDomain) action.getDomain()).getPossibleValues()) {
            if (maxUtilValue < getUtilityForAction(action,value)){
                maxUtilValue = getUtilityForAction(action,value);
                actionValue = value;
            }
        }
        return actionValue;
    }

    public BayesianNetwork getNetwork() {
        return network;
    }
}
