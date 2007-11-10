package aima.search.framework;

import java.util.ArrayList;
import java.util.List;
import aima.basic.Agent;
import aima.basic.Percept;
import aima.util.Util;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 3.1, page 61.
 * <code>
 * function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
 *   inputs: percept, a percept
 *   static: seq, an action sequence, initially empty
 *           state, some description of the current world state
 *           goal, a goal, initially null
 *           problem, a problem formulation
 *           
 *   state <- UPDATE-STATE(state, percept)
 *   if seq is empty then do
 *     goal    <- FORMULATE-GOAL(state)
 *     problem <- FORMULATE-PROBLEM(state, goal)
 *     seq     <- SEARCH(problem)
 *   action <- FIRST(seq)
 *   seq <- REST(seq)
 * </code>
 * Figure 3.1 A simple problem-solving agent. It first formulates a goal and a problem,
 * searches for a sequence of actions that would solve the problem, and then executes the actions
 * one at a time. When this is complete, it formulates another goal and starts over. Note that
 * when it is executing the sequence it ignores its percepts: it assumes that the solution it has
 * found will always work.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class SimpleProblemSolvingAgent extends Agent {

	// seq, an action sequence, initially empty
	private List<String> seq = new ArrayList<String>();

	//
	private boolean formulateGoalsIndefinitely = true;

	private int maxGoalsToFormulate = 1;

	private int goalsFormulated = 0;

	public SimpleProblemSolvingAgent() {
		formulateGoalsIndefinitely = true;
	}

	public SimpleProblemSolvingAgent(int maxGoalsToFormulate) {
		formulateGoalsIndefinitely = false;
		this.maxGoalsToFormulate = maxGoalsToFormulate;
	}

	// function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
	@Override
	public String execute(Percept p) {
		String action = NO_OP;

		// state <- UPDATE-STATE(state, percept)
		Object state = updateState(p);
		// if seq is empty then do
		if (0 == seq.size()) {
			if (formulateGoalsIndefinitely
					|| goalsFormulated < maxGoalsToFormulate) {
				if (goalsFormulated > 0) {
					notifyViewOfMetrics();
				}
				// goal <- FORMULATE-GOAL(state)
				Object goal = formulateGoal();
				goalsFormulated++;
				// problem <- FORMULATE-PROBLEM(state, goal)
				Problem problem = formulateProblem(goal);
				// seq <- SEARCH(problem)
				seq.addAll(search(problem));
				if (0 == seq.size()) {
					// Unable to identify a path
					seq.add(NO_OP);
				}
			} else {
				// Agent no longer wishes to
				// achieve any more goals
				die();
				notifyViewOfMetrics();
			}
		}

		if (seq.size() > 0) {
			// action <- FIRST(seq)
			action = Util.first(seq);
			// seq <- REST(seq)
			seq = Util.rest(seq);
		}

		return action;
	}

	//
	// PROTECTED METHODS
	//
	protected abstract Object updateState(Percept p);

	protected abstract Object formulateGoal();

	protected abstract Problem formulateProblem(Object goal);

	protected abstract List<String> search(Problem problem);

	protected abstract void notifyViewOfMetrics();
}