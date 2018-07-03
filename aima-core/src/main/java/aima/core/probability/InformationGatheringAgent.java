package aima.core.probability;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Percept;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class InformationGatheringAgent implements Agent {
    private BayesInference inferenceMethod;
    private DecisionNetwork decisionNetwork;
    private List<AssignmentProposition> observedEvidence;
    private List<RandomVariable> randomVars;
    
    public InformationGatheringAgent(DecisionNetwork decisionNetwork,
                                     BayesInference inferenceMethod,
                                     List<AssignmentProposition> initialEvidence){
        this.decisionNetwork = decisionNetwork;
        this.inferenceMethod = inferenceMethod;
        this.observedEvidence = initialEvidence;
        this.randomVars = this.decisionNetwork.getNetwork().getVariablesInTopologicalOrder();
    }

    public InformationGatheringAgent(DecisionNetwork decisionNetwork,
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
        return ((Action) decisionNetwork.getBestAction());
    }

    protected abstract Action request(RandomVariable randomVar);

    private List<Double> vpiPerUnitCost(List<RandomVariable> variablesInTopologicalOrder){
     List<Double> vpiPerUnitCost = new ArrayList<>();
        for (RandomVariable var :
                variablesInTopologicalOrder) {
            vpiPerUnitCost.add(getVpi(var) / getCost(var));
        }
        return vpiPerUnitCost;
    }

    protected abstract double getCost(RandomVariable var);

    protected double getVpi(RandomVariable var){
        for (Object value :
                ((FiniteDomain) var.getDomain()).getPossibleValues()) {
            inferenceMethod.ask()
        }
    }

    abstract List<AssignmentProposition> integratePercept(List<AssignmentProposition> observedEvidence, Percept percept);


}
