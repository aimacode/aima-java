package aima.core.probability;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Percept;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 16.9, page 632.<br>
 * </br>
 * <pre>
 *
 * function INFORMATION-GATHERING-AGENT(percept) returns an action
 *  persistent: D, a decision network
 *
 * integrate percept into D
 *  j ← the value that maximizes VPI(Ej) / Cost(Ej)
 *  if VPI(Ej) > Cost(Ej)
 *    return REQUEST(Ej)
 *  else return the best action from D
 *
 *     </pre>
 * <p>
 * Figure ?? Design of a simple information-gathering agent.
 * The agent works by repeatedly selecting the observation with
 * the highest information value, until the cost of the next
 * observation is greater than its expected benefit.
 *
 * @author samagra
 */
public abstract class InformationGatheringAgent implements Agent<Percept, Action> {

    // To carry out conditional probability calculations
    private BayesInference inferenceMethod;
    // persistent: D, a decision network
    private DecisionNetwork decisionNetwork;
    // To store the information collected till now
    private List<AssignmentProposition> observedEvidence;
    // To store the scope of information that can be collected
    private List<RandomVariable> randomVars;

    /**
     * Constructor for the agent.
     *
     * @param decisionNetwork The decision network which represents the problem
     *                        for which the information is to be collected
     * @param inferenceMethod To carry out various conditional probability calculations
     * @param initialEvidence The information which is available beforehand to the agent.
     */
    public InformationGatheringAgent(DecisionNetwork decisionNetwork,
                                     BayesInference inferenceMethod,
                                     List<AssignmentProposition> initialEvidence) {
        this.decisionNetwork = decisionNetwork;
        this.inferenceMethod = inferenceMethod;
        this.observedEvidence = initialEvidence;
        this.randomVars = this.decisionNetwork.getNetwork().getVariablesInTopologicalOrder();
    }

    public InformationGatheringAgent(DecisionNetwork decisionNetwork,
                                     BayesInference inferenceMethod) {
        this(decisionNetwork, inferenceMethod, new ArrayList<>());
    }

    /**
     * function INFORMATION-GATHERING-AGENT(percept) returns an action
     *
     * @param percept The current percept of a sequence perceived by the Agent.
     * @return action to be executed by the agent
     */
    @Override
    public Optional<Action> act(Percept percept) {
        // integrate percept into D
        observedEvidence = integratePercept(observedEvidence, percept);

        // j ← the value that maximizes VPI(Ej) / Cost(Ej)
        List<Double> vpiPerUnitCosts = this.vpiPerUnitCost(this.randomVars);
        int j = vpiPerUnitCosts.indexOf(Collections.max(vpiPerUnitCosts));
        RandomVariable randomVar = this.randomVars.get(j);

        // if VPI(Ej) > Cost(Ej)
        if (getVpi(randomVar) > getCost(randomVar)) {
            // return REQUEST(Ej)
            return Optional.ofNullable(request(randomVar));
        }
        // else return the best action from D
        return Optional.ofNullable((Action) decisionNetwork.getBestAction());
    }

    /**
     * We assume that the result of
     * the action Request (Ej ) is that the next percept provides the value of Ej .
     *
     * @param randomVar The random variable for which the information is needed.
     * @return The action which leads to the agent to the value of Ej.
     */
    protected abstract Action request(RandomVariable randomVar);

    /**
     * Calculates the vpi (value of perfect information) per unit cost
     * for all the random variables.
     *
     * @param variablesInTopologicalOrder The variables for which information is required.
     * @return A list of vpi values.
     */
    private List<Double> vpiPerUnitCost(List<RandomVariable> variablesInTopologicalOrder) {
        List<Double> vpiPerUnitCost = new ArrayList<>();
        for (RandomVariable var :
                variablesInTopologicalOrder) {
            vpiPerUnitCost.add(getVpi(var) / getCost(var));
        }
        return vpiPerUnitCost;
    }

    /**
     * Calculates the cost of obtaining information for
     * a particular variable.
     *
     * @param var
     * @return
     */
     abstract double getCost(RandomVariable var);

    /**
     * Calculates VPI for a particular random variable.
     *
     * @param var
     * @return
     */
     double getVpi(RandomVariable var) {
        double vpi = 0;
        CategoricalDistribution distribution = inferenceMethod.ask((new RandomVariable[]{var}),
                ((AssignmentProposition[]) observedEvidence.toArray()), decisionNetwork.getNetwork());
        for (Object value :
                ((FiniteDomain) var.getDomain()).getPossibleValues()) {
            double posterierProb = distribution.getValue(value);
            List<AssignmentProposition> modifiedEvidence = new ArrayList<>(observedEvidence);
            modifiedEvidence.add(new AssignmentProposition(var, value));
            double expectedUtilityForParticularValue = decisionNetwork.getExpectedUtility(var,
                    modifiedEvidence);
            vpi += posterierProb * expectedUtilityForParticularValue;
        }
        vpi -= decisionNetwork.getExpectedUtility(var, observedEvidence);
        return vpi;
    }

    /**
     * Extracts the information from the percepts and adds ot to our observed evidence.
     *
     * @param observedEvidence
     * @param percept
     * @return
     */
    abstract List<AssignmentProposition> integratePercept(List<AssignmentProposition> observedEvidence, Percept percept);


}
