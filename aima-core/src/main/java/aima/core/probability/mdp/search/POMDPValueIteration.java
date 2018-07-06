package aima.core.probability.mdp.search;

import aima.core.agent.Action;
import aima.core.probability.mdp.POMDP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class POMDPValueIteration<S,A extends Action,E> {
    public POMDP<S,A,E> pomdp;
    public double maxError;

    public POMDPValueIteration(POMDP<S, A, E> pomdp, double maxError) {
        this.pomdp = pomdp;
        this.maxError = maxError;
    }

    public HashMap<List<A>, List<Double>> pomdpValueIteration(POMDP pomdp, double maxError){
        HashMap<List<A>,List<Double>>  uDash;
        HashMap<List<A>,List<Double>> u = new HashMap<>();
        List<Double> utilities = new ArrayList<>();
        for (Object state :
                pomdp.states()) {
            utilities.add(0.0);
        }
        uDash = new HashMap<>();
        uDash.put(new ArrayList<>(),utilities);
        while(maxDifference(u,uDash) < maxError*(1-pomdp.getDiscount())/pomdp.getDiscount()){
            u = new HashMap<>(uDash);
            uDash = removeDominatedPlans(uDash);
        }
        return u;
    }

    private HashMap<List<A>, List<Double>> removeDominatedPlans(HashMap<List<A>, List<Double>> uDash) {
        return null;
    }

    private double maxDifference(HashMap<List<A>, List<Double>> u, HashMap<List<A>, List<Double>> uDash) {
        return 0.0;
    }
}
