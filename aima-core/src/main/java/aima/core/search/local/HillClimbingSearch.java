package aima.core.search.local;

import aima.core.search.framework.*;
import aima.core.search.framework.problem.Problem;
import aima.core.search.informed.Informed;
import aima.core.util.Tasks;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.2, page
 * 122.<br>
 * <br>
 * <p>
 * <pre>
 * function HILL-CLIMBING(problem) returns a state that is a local maximum
 *
 *   current &lt;- MAKE-NODE(problem.INITIAL-STATE)
 *   loop do
 *     neighbor &lt;- a highest-valued successor of current
 *     if neighbor.VALUE &lt;= current.VALUE then return current.STATE
 *     current &lt;- neighbor
 * </pre>
 * <p>
 * Figure 4.2 The hill-climbing search algorithm, which is the most basic local
 * search technique. At each step the current node is replaced by the best
 * neighbor; in this version, that means the neighbor with the highest VALUE,
 * but if a heuristic cost estimate h is used, we would find the neighbor with
 * the lowest h.
 *
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class HillClimbingSearch<S, A> implements SearchForActions<S, A>, SearchForStates<S, A> {

    public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
    public static final String METRIC_NODE_VALUE = "nodeValue";

    private ToDoubleFunction<Node<S, A>> evalFn = null;
    private final NodeFactory<S, A> nodeFactory;
    private S lastState = null;
    private Metrics metrics = new Metrics();

    /**
     * Constructs a hill-climbing search for a specified evaluation function.
     *
     * @param evalFn a function mapping nodes to the height of their state (the higher, the better).
     */
    public HillClimbingSearch(ToDoubleFunction<Node<S, A>> evalFn) {
        this(evalFn, new NodeFactory<>());
    }

    public HillClimbingSearch(ToDoubleFunction<Node<S, A>> evalFn, NodeFactory<S, A> nodeFactory) {
        this.evalFn = evalFn;
        this.nodeFactory = nodeFactory;
        nodeFactory.addNodeListener((node) -> metrics.incrementInt(METRIC_NODES_EXPANDED));
    }

    @Override
    public Optional<List<A>> findActions(Problem<S, A> p) {
        nodeFactory.useParentLinks(true);
        return SearchUtils.toActions(findNode(p));
    }

    @Override
    public Optional<S> findState(Problem<S, A> p) {
        nodeFactory.useParentLinks(false);
        return SearchUtils.toState(findNode(p));
    }

    /**
     * Returns a node corresponding to a goal state or empty. Method {@link #getLastState()}
     * provides the local maximum if result is empty.
     *
     * @param p the search problem
     * @return a node or empty
     */
    /// function HILL-CLIMBING(problem) returns a state that is a local maximum
    public Optional<Node<S, A>> findNode(Problem<S, A> p) {
        clearMetrics();
        /// current <- MAKE-NODE(problem.INITIAL-STATE)
        Node<S, A> current = nodeFactory.createNode(p.getInitialState());
        Node<S, A> neighbor;
        /// loop do
        while (!Tasks.currIsCancelled()) {
            metrics.set(METRIC_NODE_VALUE, getValue(current));
            List<Node<S, A>> children = nodeFactory.getSuccessors(current, p);
            /// neighbor <- a highest-valued successor of current
            neighbor = getHighestValuedNodeFrom(children);

            /// if neighbor.VALUE <= current.VALUE then return current.STATE
            if (neighbor == null || getValue(neighbor) <= getValue(current)) {
                lastState = current.getState();
                return Optional.ofNullable(p.testSolution(current) ? current : null);
            }
            /// current <- neighbor
            current = neighbor;
        }
        lastState = current.getState();
        return Optional.empty();
    }

    private Node<S, A> getHighestValuedNodeFrom(List<Node<S, A>> children) {
        double highestValue = Double.NEGATIVE_INFINITY;
        Node<S, A> nodeWithHighestValue = null;
        for (Node<S, A> child : children) {
            double value = getValue(child);
            if (value > highestValue) {
                highestValue = value;
                nodeWithHighestValue = child;
            }
        }
        return nodeWithHighestValue;
    }

    private double getValue(Node<S, A> n) {
        return evalFn.applyAsDouble(n);
    }

    /**
     * Returns the last explored state which is at least a local maximum if search was not cancelled by the user.
     */
    public S getLastState() {
        return lastState;
    }

    /**
     * Returns all the search metrics.
     */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Sets all metrics to zero.
     */
    private void clearMetrics() {
        metrics.set(METRIC_NODES_EXPANDED, 0);
        metrics.set(METRIC_NODE_VALUE, 0);
    }

    @Override
    public void addNodeListener(Consumer<Node<S, A>> listener) {
        nodeFactory.addNodeListener(listener);
    }

    @Override
    public boolean removeNodeListener(Consumer<Node<S, A>> listener) {
        return nodeFactory.removeNodeListener(listener);
    }
}