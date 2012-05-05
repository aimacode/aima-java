package aima.core.search.nondeterministic;

import java.util.LinkedList;

/**
 * Represents a solution plan for an AND-OR search; according to page 135, the
 * plan must be "a subtree that (1) has a goal node at every leaf, (2) specifies
 * one Object at each of its OR nodes, and (3) includes every outcome branch at
 * each of its AND nodes." As demonstrated on page 136, this subtree is
 * implemented as a linked list where every OR node is an Object-- satisfying
 * (2)--and every AND node is an if-then-else chain--satisfying (3).
 *
 * @author Andrew Brown
 */
public class Plan extends LinkedList<Object> {

    /**
     * Each step is an IfThenElse construct or another plan
     */
    LinkedList<Object> steps = new LinkedList<>();

    /**
     * Empty constructor
     */
    public Plan() {
    }

    /**
     * Constructor
     *
     * @param steps
     */
    public Plan(Object... steps) {
        for (int i = 0; i < steps.length; i++) {
            this.add(steps[i]);
        }
    }

    /**
     * Constructor
     *
     * @param step
     */
    public Plan(Object step) {
        this.offerLast(step);
    }

    /**
     * Appends a step to the plan and returns itself
     *
     * @param step
     * @return
     */
    public Plan append(Object step) {
        this.offerLast(step);
        return this;
    }

    /**
     * Prepends a step to the plan and returns itself
     *
     * @param step
     * @return
     */
    public Plan prepend(Object step) {
        this.offerFirst(step);
        return this;
    }

    /**
     * Returns the string representation of this plan
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        int count = 0;
        int size = this.size();
        for (Object step : this) {
            s.append(step);
            if (count < size - 1) {
                s.append(", ");
            }
            count++;
        }
        s.append("]");
        return s.toString();
    }
}
