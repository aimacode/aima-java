package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Percept;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.nondeterministic.IfThenElse;
import aima.core.search.nondeterministic.Plan;

/**
 * This agent traverses the NondeterministicVacuumEnvironment using a
 * contingency plan. See page 135, AIMAv3.
 *
 * @author Andrew Brown
 */
public class NondeterministicVacuumAgent implements Agent {

    boolean alive = true;
    Plan contingencyPlan;

    /**
     * Execute an action from the contingency plan
     *
     * @param percept
     * @return
     */
    @Override
    public Action execute(Percept percept) {
        if (this.getContingencyPlan().size() < 1) {
            return NoOpAction.NO_OP;
        }
        Object currentStep = this.getContingencyPlan().removeFirst();
        // case: next step is an action
        if (currentStep instanceof Action) {
            return (Action) currentStep;
        } // case: next step is another plan
        else if (currentStep instanceof Plan) {
            this.setContingencyPlan((Plan) currentStep);
            return this.execute(percept);
        } // case: next step is an if-then-else
        else if (currentStep instanceof IfThenElse) {
            VacuumEnvironmentPercept p = (VacuumEnvironmentPercept) percept;
            IfThenElse s = (IfThenElse) currentStep;
            Object result;
            do {
                result = s.queryWith(p);
            } while (result instanceof IfThenElse);
            if (result instanceof Action) {
                return (Action) result;
            } else if (result instanceof Plan) {
                this.setContingencyPlan((Plan) result);
                return this.execute(percept);
            }
        } // case: unknown step
        else {
            throw new RuntimeException("Unrecognized contingency plan step.");
        }
        return NoOpAction.NO_OP;
    }

    /**
     * Test whether the agent is still alive
     *
     * @return
     */
    @Override
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Set whether the agent is alive
     *
     * @param alive
     */
    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Set the agent contingency plan
     *
     * @param contingencyPlan the plan the agent uses to clean the vacuum world
     */
    public void setContingencyPlan(Plan contingencyPlan) {
        this.contingencyPlan = contingencyPlan;
    }

    /**
     * Return the agent contingency plan
     *
     * @return the plan the agent uses to clean the vacuum world
     */
    public Plan getContingencyPlan() {
        if (this.contingencyPlan == null) {
            throw new RuntimeException("Contingency plan not set.");
        }
        return this.contingencyPlan;
    }
}
