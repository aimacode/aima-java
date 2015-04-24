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
public class TreeSearchInstrumented<S> extends BasicSearchFunction<S> implements TreeSearch<S> {
    public interface Cmd<S> {
        String          commandId();
        int             currentFrontierSize();
        int             maxFrontierSize();
        int             numberAddedToFrontier();
        int             numberRemovedFromFrontier();
        Node<S>         node();
        Map<S, Integer> statesVisitiedCounts();
        Map<S, Node<S>> statesInFrontierNotVisited();
        Node<S>         lastNodeVisited();
    }

    public interface Listener<S> {
        void cmd(Cmd<S> command);
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
    private InstrLinkedList frontier;
    private int maxFrontierSize = 0;
    private int numberAddedToFrontier = 0;
    private int numberRemovedFromFrontier = 0;
    private Map<S, Integer> statesVisitiedCounts = new HashMap<>();
    private Map<S, Node<S>> statesInFrontierNotVisited = new HashMap<>();
    private Node<S> lastNodeVisited;

    public TreeSearchInstrumented(Listener<S> listener) {
        this.listener = listener;
    }

    @Override
    public Queue<Node<S>> newFrontier() {
        frontier        = new InstrLinkedList();
        maxFrontierSize = 0;
        numberAddedToFrontier = 0;
        numberRemovedFromFrontier = 0;
        statesVisitiedCounts.clear();
        statesInFrontierNotVisited.clear();
        lastNodeVisited = null;
        notify(CMD_START, 0, null);
        return frontier;
    }

    @Override
    public Node<S> childNode(Problem<S> problem, Node<S> parent, Action action) {
        Node<S> child = super.childNode(problem, parent, action);
        notify(CMD_EXPAND_NODE, frontier.size(), parent);
        return child;
    }

    @Override
    public boolean isGoalState(Node<S> node, Problem<S> problem) {
        notify(CMD_CHECK_GOAL, frontier.size(), node);
        return super.isGoalState(node, problem);
    }

    @Override
    public List<Action> failure() {
        notify(CMD_FAILURE, frontier.size(), null);
        return super.failure();
    }

    @Override
    public List<Action> solution(Node<S> node) {
        notify(CMD_SOLUTION, frontier.size(), node);
        return super.solution(node);
    }

    private void notify(final String commandId, final int frontierSize, final Node<S> node) {
        if (frontierSize > maxFrontierSize) {
            maxFrontierSize = frontierSize;
        }
        int max = maxFrontierSize;
        int numberAdded = numberAddedToFrontier;
        int numberRemoved = numberRemovedFromFrontier;
        final Map<S, Integer> visited =  statesVisitiedCounts;
        final Map<S, Node<S>> notVisited = statesInFrontierNotVisited;
        final Node<S> last = lastNodeVisited;
        listener.cmd(new Cmd<S>() {
            public String commandId() {
                return commandId;
            }
            public int currentFrontierSize() {
                return frontierSize;
            }
            public int numberAddedToFrontier() { return numberAdded; };
            public int  numberRemovedFromFrontier() { return numberRemoved; };
            public int maxFrontierSize() { return max; }
            public Node<S> node() {
                return node;
            }
            public Map<S, Integer> statesVisitiedCounts() { return visited; }
            public Map<S, Node<S>> statesInFrontierNotVisited() { return notVisited; }
            public Node<S> lastNodeVisited() { return last; }
        });
    }

    private class InstrLinkedList extends LinkedList<Node<S>> {
        boolean firstAdd = true;

        @Override
        public boolean isEmpty() {
            boolean result = super.isEmpty();
            TreeSearchInstrumented.this.notify(CMD_FRONTIER_EMPTY_CHECK, size(), null);
            return result;
        }

        @Override
        public Node<S> remove() {
            Node<S> removed =  super.remove();

            numberRemovedFromFrontier++;

            lastNodeVisited = removed;

            statesVisitiedCounts = new HashMap<>(statesVisitiedCounts);
            Integer visitedCount = statesVisitiedCounts.get(removed.state());
            if (visitedCount == null)  {
                statesVisitiedCounts.put(removed.state(), 1);
            }
            else {
                statesVisitiedCounts.put(removed.state(), visitedCount+1);
            }


            if (statesInFrontierNotVisited.containsKey(removed.state())) {
                statesInFrontierNotVisited = new HashMap<>(statesInFrontierNotVisited);
                statesInFrontierNotVisited.remove(removed.state());
            }

            TreeSearchInstrumented.this.notify(CMD_FRONTIER_REMOVE, size(), removed);
            return removed;
        }

        @Override
        public boolean add(Node<S> e) {
            boolean result = super.add(e);

            numberAddedToFrontier++;

            if (!statesVisitiedCounts.containsKey(e.state())) {
                if (!statesInFrontierNotVisited.containsKey(e.state())) {
                    statesInFrontierNotVisited = new HashMap<>(statesInFrontierNotVisited);
                    statesInFrontierNotVisited.put(e.state(), e);
                }
            }
            if (firstAdd) {
                firstAdd = false;
                TreeSearchInstrumented.this.notify(CMD_INITIALIZE, size(), e);
            }
            else {
                TreeSearchInstrumented.this.notify(CMD_ADD_FRONTIER, size(), e);
            }
            return result;
        }
    }
}
