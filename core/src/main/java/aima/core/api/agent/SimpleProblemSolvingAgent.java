package aima.core.api.agent;

import aima.core.api.search.Problem;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
 *   persistent: seq, an action sequence, initially empty
 *               state, some description of the current world state
 *               goal, a goal, initially null
 *               problem, a problem formulation
 *
 *   state &lt;- UPDATE-STATE(state, percept)
 *   if seq is empty then
 *       goal    &lt;- FORMULATE-GOAL(state)
 *       problem &lt;- FORMULATE-PROBLEM(state, goal)
 *       seq     &lt;- SEARCH(problem)
 *       if seq = failure then return a null action
 *   action &lt;- FIRST(seq)
 *   seq &lt;- REST(seq)
 *   return action
 * </pre>
 *
 * Figure ?? A simple problem-solving agent. It first formulates a goal and a
 * problem, searches for a sequence of actions that would solve the problem, and
 * then executes the actions one at a time. When this is complete, it formulates
 * another goal and starts over.<br>
 *
 * @param <S> the type of internal state representation used by the agent.
 * @param <G> the type of the goal(s) the agent wishes to reach. In simple cases this will be the same type as S.
 *
 * @author Ciaran O'Reilly
 */
public interface SimpleProblemSolvingAgent<A, P, S, G> extends Agent<A, P> {
    //   persistent: seq, an action sequence, initially empty
    //               state, some description of the current world state
    //               goal, a goal, initially null
    //               problem, a problem formulation
    List<A> getSeq();           void setSeq(List<A> sequence);
    S getState();               void setState(S state);
    G getGoal();                void setGoal(G goal);
    Problem<A, S> getProblem(); void setProblem(Problem<A, S> problem);

    // function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
    @Override
    default A perceive(P percept) {
        // state <- UPDATE-STATE(state, percept)
        setState(updateState(getState(), percept));
        // if seq is empty then
        if (getSeq().isEmpty()) {
            // goal    <- FORMULATE-GOAL(state)
            setGoal(formulateGoal(getState()));
            // problem <- FORMULATE-PROBLEM(state, goal)
            setProblem(formulateProblem(getState(), getGoal()));
            // seq     <- SEARCH(problem)
            setSeq(search(getProblem()));
            // if seq = failure then return a null action
            if (isFailure(getSeq())) { return null; }
        }
        // action <- FIRST(seq)
        A action = getSeq().get(0);
        // seq <- REST(seq)
        setSeq(getSeq().subList(1, 0));
        // return action
        return action;
    }

    // state <- UPDATE-STATE(state, percept)
    S updateState(S currentState, P percept);

    // goal <- FORMULATE-GOAL(state)
    G formulateGoal(S state);

    // problem <- FORMULATE-PROBLEM(state, goal)
    Problem<A, S> formulateProblem(S state, G goal);

    // seq <- SEARCH(problem)
    List<A> search(Problem<A, S> problem);

    default boolean isFailure(List<A> seq) {
        return seq == null || seq.isEmpty();
    }
}
