package aima.core.search.nondeterministic;

import java.util.LinkedList;

/**
 * Represents the path the agent travels through the AND-OR tree (see figure
 * 4.10, page 135, AIMAv3).
 *
 * @author Andrew Brown
 */
public class Path extends LinkedList<Object> {
	
	private static final long serialVersionUID = 1L;

	/**
     * Appends multiple steps
     *
     * @param states
     */
    public Path append(Object... states) {
        for (int i = 0; i < states.length; i++) {
            this.add(states[i]);
        }
        return this;
    }

    /**
     * Appends a step to the plan and returns itself
     *
     * @param step
     * @return
     */
    public Path append(Object state) {
        this.offerLast(state);
        return this;
    }

    /**
     * Prepends a step to the plan and returns itself
     *
     * @param step
     * @return
     */
    public Path prepend(Object state) {
        this.offerFirst(state);
        return this;
    }
}
