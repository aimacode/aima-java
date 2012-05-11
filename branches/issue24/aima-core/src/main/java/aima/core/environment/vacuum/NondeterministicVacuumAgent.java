package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Percept;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.nondeterministic.IfStateThenPlan;
import aima.core.search.nondeterministic.Plan;
import java.util.LinkedList;

/**
 * This agent traverses the NondeterministicVacuumEnvironment using a
 * contingency plan. See page 135, AIMA3e.
 *
 * @author Andrew Brown
 */
public class NondeterministicVacuumAgent implements Agent {

    boolean alive = true;
    Plan contingencyPlan;
    LinkedList<Object> stack = new LinkedList<Object>();

    /**
     * Execute an action from the contingency plan
     *
     * @param percept
     * @return
     */
    @Override
    public Action execute(Percept percept) {
        // check if goal state
        NondeterministicVacuumEnvironmentPercept p = (NondeterministicVacuumEnvironmentPercept) percept;
        VacuumEnvironment.LocationState clean = VacuumEnvironment.LocationState.Clean;
        if (p.getState().getLocationState(VacuumEnvironment.LOCATION_A) == clean
                && p.getState().getLocationState(VacuumEnvironment.LOCATION_B) == clean) {
            return NoOpAction.NO_OP;
        }
        // check stack size
        if (this.stack.size() < 1) {
            if (this.contingencyPlan.size() < 1) {
                return NoOpAction.NO_OP;
            } else {
                this.stack.push(this.getContingencyPlan().removeFirst());
            }
        }
        // pop...
        Object currentStep = this.stack.peek();
        // push...
        if (currentStep instanceof Action) {
            return (Action) this.stack.pop();
        } // case: next step is a plan
        else if (currentStep instanceof Plan) {
            Plan newPlan = (Plan) currentStep;
            if (newPlan.size() > 0) {
                this.stack.push(newPlan.removeFirst());
            } else {
                this.stack.pop();
            }
            return this.execute(percept);
        } // case: next step is an if-then
        else if (currentStep instanceof IfStateThenPlan) {
            IfStateThenPlan conditional = (IfStateThenPlan) this.stack.pop();
            this.stack.push(conditional.ifStateMatches(percept));
            return this.execute(percept);
        } // case: ignore next step if null 
        else if (currentStep == null) {
            this.stack.pop();
            return this.execute(percept);
        } else {
            throw new RuntimeException("Unrecognized contingency plan step.");
        }
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
