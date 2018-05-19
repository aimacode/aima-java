package aima.core.logic.planning;

import java.util.ArrayList;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 379.<br>
 * <p>
 * A planning graph is a directed graph organized into levels: first a level S 0 for the initial
 * state, consisting of nodes representing each fluent that holds in S 0 ; then a level A 0 consisting
 * of nodes for each ground action that might be applicable in S 0 ; then alternating levels S i
 * followed by A i ; until we reach a termination condition.
 *
 * @author samagra
 */
public class Graph {
    ArrayList<Level> levels;// Levels
    Problem problem;// The planning problem
    List<ActionSchema> propositionalisedActions;

    public Graph(Problem problem, Level initialLevel) {
        this.problem = problem;
        levels = new ArrayList<>();
        levels.add(initialLevel);
        propositionalisedActions = problem.getPropositionalisedActions();
    }

    public int numLevels() {
        return levels.size();
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public Problem getProblem() {
        return problem;
    }

    public List<ActionSchema> getPropositionalisedActions() {
        return propositionalisedActions;
    }

    public Graph addLevel() {
        Level lastLevel = levels.get(levels.size() - 1);
        Level level = new Level(lastLevel, this.problem);
        this.levels.add(level);
        return this;
    }
}
