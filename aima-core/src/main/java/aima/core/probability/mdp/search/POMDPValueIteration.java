package aima.core.probability.mdp.search;

import aima.core.agent.Action;
import aima.core.probability.mdp.POMDP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class POMDPValueIteration<S,A extends Action,E> {
    public POMDP<S,A,E> pomdp;
    public double maxError;
    public int depth;

    public POMDPValueIteration(POMDP<S, A, E> pomdp, double maxError, int maxDepth) {
        this.pomdp = pomdp;
        this.maxError = maxError;
        this.depth = maxDepth;
    }

    public HashMap<List<A>, List<Double>> pomdpValueIteration(POMDP pomdp, double maxError){
        HashMap<List<A>,List<Double>>  uDash;
        HashMap<List<A>,List<Double>> u = new HashMap<>();
        List<Double> utilities = new ArrayList<>();
        for (Object state :
                pomdp.states()) {
            utilities.add(pomdp.reward(state));
        }
        uDash = new HashMap<>();
        uDash.put(new ArrayList<>(),utilities);
        int i = 0;
        while(maxDifference(u,uDash) < maxError*(1-pomdp.getDiscount())/pomdp.getDiscount() || (i<=this.depth)){
            u = new HashMap<>(uDash);
            uDash = increasePlanDepths(uDash);
            uDash = removeDominatedPlans(uDash);
            i++;
        }
        return u;
    }

    private HashMap<List<A>, List<Double>> increasePlanDepths(HashMap<List<A>,
            List<Double>> uDash) {
        HashMap<List<A>,List<Double>> result = new HashMap<>();
        for (A action :
                this.pomdp.getAllActions()) {
            for (List<A> plan :
                    uDash.keySet()) {
                List<A> newPlan = new ArrayList<>(plan);
                List<Double> newUtilities = new ArrayList<>();
                newPlan.add(action);
                for (S currentState:
                        this.pomdp.states()) {
                    double planUtility = 0.0;
                    for (S actualState :
                            this.pomdp.states()) {
                        double tempUtility = 0.0;
                        for (S observation :
                             this.pomdp.states()) {
                            tempUtility+=this.pomdp.sensorModel(observation,
                                    actualState)*uDash.get(plan).
                                    get((new ArrayList<>(this.pomdp.states())).indexOf(actualState));
                        }
                        planUtility = tempUtility*this.pomdp.transitionProbability(actualState,
                                currentState,action);
                    }
                    planUtility*= this.pomdp.getDiscount();
                    planUtility+=this.pomdp.reward(currentState);
                    newUtilities.add(planUtility);
                }
                result.put(newPlan,newUtilities);
            }
        }
        return result;
    }

    private HashMap<List<A>, List<Double>> removeDominatedPlans(HashMap<List<A>, List<Double>> uDash) {
        return uDash;
    }

    private double maxDifference(HashMap<List<A>, List<Double>> u, HashMap<List<A>, List<Double>> uDash) {
        return 2;
    }
}
