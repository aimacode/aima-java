package aima.extra.instrument.search;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;
import aima.core.api.search.TreeSearch;
import aima.core.search.BasicSearchFunction;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchCmdInstr<S> extends BasicSearchFunction<S> implements TreeSearch<S> {
    interface Listener<S> {
        void cmd(String commandId, int frontierSize, Node<S> node);
    }

    public static final String CMD_START                = "start";
    public static final String CMD_INITIALIZE           = "initialize";
    public static final String CMD_FRONTIER_EMPTY_CHECK = "frontier-empty-check";
    public static final String CMD_FAILURE              = "failure";
    public static final String CMD_FRONTIER_REMOVE      = "frontier-remove";
    public static final String CMD_CHECK_GOAL           = "check-goal";
    public static final String CMD_SOLUTION             = "solution";
    public static final String CMD_EXPAND_NODE          = "expand-node";
    public static final String CMD_ADD_FRONTIER         = "add-frontier";

    public static final Set<String> CMDS = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(
            CMD_START,
            CMD_INITIALIZE,
            CMD_FRONTIER_EMPTY_CHECK,
            CMD_FAILURE,
            CMD_FRONTIER_REMOVE,
            CMD_CHECK_GOAL,
            CMD_SOLUTION,
            CMD_EXPAND_NODE,
            CMD_ADD_FRONTIER
    )));

    private Listener<S> listener;
    private Queue<Node<S>> frontier;

    public TreeSearchCmdInstr(Listener<S> listener) {
        this.listener = listener;
    }

    @Override
    public Queue<Node<S>> newFrontier() {
        frontier = new InstrLinkedList();
        listener.cmd(CMD_START, 0, null);
        return frontier;
    }

    @Override
    public Node<S> childNode(Problem<S> problem, Node<S> parent, Action action) {
        Node<S> child = super.childNode(problem, parent, action);
        listener.cmd(CMD_EXPAND_NODE, frontier.size(), child);
        return child;
    }

    @Override
    public boolean isGoalState(Node<S> node, Problem<S> problem) {
        listener.cmd(CMD_CHECK_GOAL, frontier.size(), node);
        return super.isGoalState(node, problem);
    }

    @Override
    public List<Action> failure() {
        listener.cmd(CMD_FAILURE, frontier.size(), null);
        return super.failure();
    }

    @Override
    public List<Action> solution(Node<S> node) {
        listener.cmd(CMD_SOLUTION, frontier.size(), node);
        return super.solution(node);
    }

    private class InstrLinkedList extends LinkedList<Node<S>> {
        boolean firstAdd = true;

        @Override
        public boolean isEmpty() {
            boolean result = super.isEmpty();
            listener.cmd(CMD_FRONTIER_EMPTY_CHECK, size(), null);
            return result;
        }

        @Override
        public Node<S> remove() {
            Node<S> removed =  super.remove();
            listener.cmd(CMD_FRONTIER_REMOVE, size(), removed);
            return removed;
        }

        @Override
        public boolean add(Node<S> e) {
            boolean result = super.add(e);
            if (firstAdd) {
                firstAdd = false;
                listener.cmd(CMD_INITIALIZE, size(), e);
            }
            else {
                listener.cmd(CMD_ADD_FRONTIER, size(), e);
            }
            return result;
        }
    }
}
