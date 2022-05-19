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
    private ArrayList<Level> levels;// Levels
    private List<ActionSchema> propositionalisedActions;

    public Graph(Problem problem) {
        levels = new ArrayList<>();
        levels.add(new Level(null, problem));
        propositionalisedActions = problem.getPropositionalisedActions();
    }

    public Level getLevel(int i) {
        return levels.get(i);
    }

    public int numLevels() {
        return levels.size();
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public List<ActionSchema> getPropositionalisedActions() {
        return propositionalisedActions;
    }

    /**
     * This method adds levels (an action and a state level) for a new state
     * to the planning graph.
     */
    public void expand(Problem problem) {
        Level level0 = levels.get(levels.size() - 1);
        Level level1 = new Level(level0, problem); // new action level
        Level level2 = new Level(level1, problem); // new state level
        levels.add(level1);
        levels.add(level2);
    }
}
