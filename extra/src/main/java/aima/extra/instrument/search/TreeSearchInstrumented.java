package aima.extra.instrument.search;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNode;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.extra.search.pqueue.uninformed.TreeQueueSearch;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchInstrumented<A, S> extends TreeQueueSearch<A, S> implements SearchController<A, S> {
    public interface Cmd<A, S> {
        String             commandId();
        int                currentFrontierSize();
        int                maxFrontierSize();
        int                numberAddedToFrontier();
        int                numberRemovedFromFrontier();
        Node<A, S>         node();
        Map<S, Integer>    statesVisitiedCounts();
        Map<S, Node<A, S>> statesInFrontierNotVisited();
        Node<A, S>         lastNodeVisited();
        List<Integer>      searchSpaceLevelCounts();
        List<Integer>      searchSpaceLevelRemainingCounts();
    }

    public interface Listener<A, S> {
        void cmd(Cmd<A, S> command);
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

    private Listener<A, S> listener;
    private InstrLinkedList frontier;
    private int maxFrontierSize = 0;
    private int numberAddedToFrontier = 0;
    private int numberRemovedFromFrontier = 0;
    private Map<S, Integer> statesVisitiedCounts = new HashMap<>();
    private Map<S, Node<A, S>> statesInFrontierNotVisited = new HashMap<>();
    private Node<A, S> lastNodeVisited;
    private List<Integer> searchSpaceLevelCounts = new ArrayList<>();
    private List<Integer> searchSpaceLevelRemainingCounts = new ArrayList<>();

    public TreeSearchInstrumented(Listener<A, S> listener) {
        this.listener = listener;
        setSearchController(this);
        setNodeFactory(new InstrumentedNodeFactory());
        setFrontierSupplier(new InstrumentedFrontierSupplier());
    }

    @Override
    public boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
        notify(CMD_CHECK_GOAL, frontier.size(), node);
        return SearchController.super.isGoalState(node, problem);
    }

    @Override
    public List<A> failure() {
        notify(CMD_FAILURE, frontier.size(), null);
        return SearchController.super.failure();
    }

    @Override
    public List<A> solution(Node<A, S> node) {
        notify(CMD_SOLUTION, frontier.size(), node);
        return SearchController.super.solution(node);
    }

    private void notify(final String commandId, final int frontierSize, final Node<A, S> node) {
        if (frontierSize > maxFrontierSize) {
            maxFrontierSize = frontierSize;
        }
        int max = maxFrontierSize;
        int numberAdded = numberAddedToFrontier;
        int numberRemoved = numberRemovedFromFrontier;
        final Map<S, Integer> visited =  statesVisitiedCounts;
        final Map<S, Node<A, S>> notVisited = statesInFrontierNotVisited;
        final Node<A, S> last = lastNodeVisited;
        final List<Integer> levelCounts = searchSpaceLevelCounts;
        final List<Integer> levelRemaining = searchSpaceLevelRemainingCounts;
        listener.cmd(new Cmd<A, S>() {
            public String commandId() { return commandId; }
            public int currentFrontierSize() {
                return frontierSize;
            }
            public int numberAddedToFrontier() { return numberAdded; };
            public int  numberRemovedFromFrontier() { return numberRemoved; };
            public int maxFrontierSize() { return max; }
            public Node<A, S> node() { return node; }
            public Map<S, Integer> statesVisitiedCounts() { return visited; }
            public Map<S, Node<A, S>> statesInFrontierNotVisited() { return notVisited; }
            public Node<A, S> lastNodeVisited() { return last; }
            public List<Integer> searchSpaceLevelCounts() { return levelCounts; }
            public List<Integer> searchSpaceLevelRemainingCounts() { return levelRemaining; }
        });
    }

    private class InstrLinkedList extends LinkedList<Node<A, S>> {
		private static final long serialVersionUID = 1L;
		//
		boolean firstAdd = true;

        @Override
        public boolean isEmpty() {
            boolean result = super.isEmpty();
            TreeSearchInstrumented.this.notify(CMD_FRONTIER_EMPTY_CHECK, size(), null);
            return result;
        }

        @Override
        public Node<A, S> remove() {
            Node<A, S> removed =  super.remove();

            numberRemovedFromFrontier++;

            lastNodeVisited = removed;

            int level = BasicNode.depth(removed);
            searchSpaceLevelRemainingCounts = new ArrayList<>(searchSpaceLevelRemainingCounts);
            searchSpaceLevelRemainingCounts.set(level, searchSpaceLevelRemainingCounts.get(level) - 1);

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
        public boolean add(Node<A, S> e) {
            boolean result = super.add(e);

            numberAddedToFrontier++;

            int level = BasicNode.depth(e);
            searchSpaceLevelCounts = new ArrayList<>(searchSpaceLevelCounts);
            searchSpaceLevelRemainingCounts = new ArrayList<>(searchSpaceLevelRemainingCounts);
            if (level >= searchSpaceLevelCounts.size()) {
                searchSpaceLevelCounts.add(1);
                searchSpaceLevelRemainingCounts.add(1);
            } else {
                searchSpaceLevelCounts.set(level, searchSpaceLevelCounts.get(level) + 1);
                searchSpaceLevelRemainingCounts.set(level, searchSpaceLevelRemainingCounts.get(level) + 1);
            }

            if (!statesVisitiedCounts.containsKey(e.state())) {
                if (!statesInFrontierNotVisited.containsKey(e.state())) {
                    statesInFrontierNotVisited = new HashMap<>(statesInFrontierNotVisited);
                    statesInFrontierNotVisited.put(e.state(), e);
                }
            }
            if (firstAdd) {
                firstAdd = false;
                TreeSearchInstrumented.this.notify(CMD_INITIALIZE, size(), e);
            } else {
                TreeSearchInstrumented.this.notify(CMD_ADD_FRONTIER, size(), e);
            }
            return result;
        }
    }
    
    class InstrumentedNodeFactory extends BasicNodeFactory<A, S> {
        @Override
        public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> parent, A action) {
            Node<A, S> child = super.newChildNode(problem, parent, action);
            TreeSearchInstrumented.this.notify(CMD_EXPAND_NODE, frontier.size(), parent);
            return child;
        }
    }
    
    class InstrumentedFrontierSupplier implements Supplier<Queue<Node<A, S>>> {
    	@Override
    	public Queue<Node<A, S>> get() {
    		frontier = new InstrLinkedList();
        	maxFrontierSize = 0;
	        numberAddedToFrontier = 0;
	        numberRemovedFromFrontier = 0;
	        statesVisitiedCounts.clear();
	        statesInFrontierNotVisited.clear();
	        lastNodeVisited = null;
	        searchSpaceLevelCounts.clear();
	        searchSpaceLevelRemainingCounts.clear();
	        TreeSearchInstrumented.this.notify(CMD_START, 0, null);
        	return frontier;
	    }
    }
}
