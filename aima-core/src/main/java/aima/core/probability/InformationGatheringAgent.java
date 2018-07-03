package aima.core.probability;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Percept;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class InformationGatheringAgent implements Agent {
    private BayesInference inferenceMethod;
    private BayesianNetwork decisionNetwork;
    private List<AssignmentProposition> observedEvidence;
    private List<RandomVariable> randomVars;
    
    public InformationGatheringAgent(BayesianNetwork decisionNetwork,
                                     BayesInference inferenceMethod,
                                     List<AssignmentProposition> initialEvidence){
        this.decisionNetwork = decisionNetwork;
        this.inferenceMethod = inferenceMethod;
        this.observedEvidence = initialEvidence;
        this.randomVars = this.decisionNetwork.getVariablesInTopologicalOrder();
    }

    public InformationGatheringAgent(BayesianNetwork decisionNetwork,
                                     BayesInference inferenceMethod){
        this(decisionNetwork,inferenceMethod,new ArrayList<>());
    }
    
    @Override
    public Action execute(Percept percept) {
        observedEvidence = integratePercept(observedEvidence,percept);
        List<Double> vpiPerUnitCosts = this.vpiPerUnitCost(this.randomVars);
        int j = vpiPerUnitCosts.indexOf(Collections.max(vpiPerUnitCosts));
        RandomVariable randomVar = this.randomVars.get(j);
        if (getVpi(randomVar)>getCost(randomVar)){
            return this.request(randomVar);
        }
        // TODO :: Best action in network.
        return null;
    }

    protected abstract Action request(RandomVariable randomVar);

    private List<Double> vpiPerUnitCost(List<RandomVariable> variablesInTopologicalOrder){
     List<Double> vpiPerUnitCost = new ArrayList<>();
        for (RandomVariable var :
                variablesInTopologicalOrder) {
            vpiPerUnitCost.add(((double) getVpi(var))/((double) getCost(var)));
        }
        return vpiPerUnitCost;
    }

    protected abstract double getCost(RandomVariable var);

    protected abstract double getVpi(RandomVariable var);

    abstract List<AssignmentProposition> integratePercept(List<AssignmentProposition> observedEvidence, Percept percept);


}
