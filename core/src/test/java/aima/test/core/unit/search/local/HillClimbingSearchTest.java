package aima.test.core.unit.search.local;

import aima.core.search.api.Node;
import aima.core.search.basic.local.HillClimbingSearch;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicProblem;
import aima.test.core.unit.search.support.TestGoAction;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * @author Paul Anton
 */
public class HillClimbingSearchTest {
    Map<String, List<String>> simpleBinaryTreeStateSpace = new HashMap<String, List<String>>() {
		private static final long serialVersionUID = 1L; {
        put("C", Arrays.asList("A", "B"));
        put("E", Arrays.asList("C", "D"));
        put("F", Arrays.asList("X", "D"));
        put("G", Arrays.asList("E", "F"));
        put("I", Arrays.asList("G", "F"));

    }};

    Function<String, Set<TestGoAction>> simpleBinaryTreeActionsFn = state -> {
        if (simpleBinaryTreeStateSpace.containsKey(state)) {
            return new LinkedHashSet<>(simpleBinaryTreeStateSpace.get(state).stream().map(TestGoAction::new).collect(Collectors.toList()));
        }
        return Collections.emptySet();
    };

    BiFunction<String, TestGoAction, String> goActionResultFn = (state, action) -> action.getGoTo();

    //the heuristic function will be represented by the ascii value of the first character in the state name
    ToDoubleFunction<Node<TestGoAction, String>> asciiHeuristicFn = node -> {
        String state = node.state();
        int asciiCode = (int) state.charAt(0);
        return (double) asciiCode;
    };

    @Test
    public void testAsciiHeuristicFunction() {
        HillClimbingSearch<TestGoAction, String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);
        BasicNodeFactory<TestGoAction, String> nodeFactory = new BasicNodeFactory<>();
        
        Node<TestGoAction, String> nodeA = nodeFactory.newRootNode("A");
        Node<TestGoAction, String> nodeB = nodeFactory.newRootNode("B");

        Assert.assertEquals(
                hillClimbingSearch.getHeuristicFunctionH().applyAsDouble(nodeA),
                hillClimbingSearch.getHeuristicFunctionH().applyAsDouble(nodeA),
                0
        );

        Assert.assertEquals(
                hillClimbingSearch.getHeuristicFunctionH().applyAsDouble(nodeB),
                hillClimbingSearch.getHeuristicFunctionH().applyAsDouble(nodeB),
                0
        );

        Assert.assertNotEquals(
                hillClimbingSearch.getHeuristicFunctionH().applyAsDouble(nodeA),
                hillClimbingSearch.getHeuristicFunctionH().applyAsDouble(nodeB),
                0
        );
    }

    @Test
    public void testAlreadyInGoalState() {
        HillClimbingSearch<TestGoAction, String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);

        Assert.assertEquals(
        		Arrays.asList((TestGoAction) null),
                hillClimbingSearch.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
        		Arrays.asList((TestGoAction) null),
                hillClimbingSearch.apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "B"::equals
                )));
    }

    @Test
    public void testSuccessfulGlobalMaximum() {
        HillClimbingSearch<TestGoAction, String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);

        Assert.assertEquals(
                Arrays.asList(new TestGoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("C",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new TestGoAction("C"), new TestGoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("E",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new TestGoAction("E"), new TestGoAction("C"), new TestGoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("G",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));
    }

    @Test
    public void testSuccessfulLocalMaximum() {
        HillClimbingSearch<TestGoAction, String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);

        // ends up in state D, with no hope of ever reaching the Global Maximum
        Assert.assertEquals(
                Arrays.asList(new TestGoAction("D")),
                hillClimbingSearch.apply(new BasicProblem<>("F",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new TestGoAction("F"), new TestGoAction("D")),
                hillClimbingSearch.apply(new BasicProblem<>("I",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));
    }

    @Test
    public void testUnsuccessfulHillClimbing() {
        HillClimbingSearch<TestGoAction, String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);

        // when starting from state C an action towards A will always be selected
        // goalTestPredicate parameter seems redundant, since Hill Climbing will always evolve towards the local maximum, no matter what the destination is
        Assert.assertEquals(
                Arrays.asList(new TestGoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("C",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "B"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new TestGoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("C",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "F"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new TestGoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("C",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "G"::equals
                )));
    }
}
