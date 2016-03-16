package aima.test.core.unit.search.local;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.search.BasicProblem;
import aima.core.search.local.HillClimbingSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Paul Anton
 */
public class HillClimbingSearchTest {

    class GoAction implements Action {
        String goTo;

        GoAction(String goTo) {
            this.goTo = goTo;
        }

        @Override
        public String name() {
            return "Go(" + goTo + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof GoAction) {
                return this.name().equals(((Action) obj).name());
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return name().hashCode();
        }
    }

    Map<String, List<String>> simpleBinaryTreeStateSpace = new HashMap<String, List<String>>() {{
        put("C", Arrays.asList("A", "B"));
        put("E", Arrays.asList("C", "D"));
        put("F", Arrays.asList("X", "D"));
        put("G", Arrays.asList("E", "F"));
        put("I", Arrays.asList("G", "F"));

    }};

    Function<String, Set<Action>> simpleBinaryTreeActionsFn = state -> {
        if (simpleBinaryTreeStateSpace.containsKey(state)) {
            return new LinkedHashSet<>(simpleBinaryTreeStateSpace.get(state).stream().map(GoAction::new).collect(Collectors.toList()));
        }
        return Collections.emptySet();
    };

    BiFunction<String, Action, String> goActionResultFn = (state, action) -> ((GoAction) action).goTo;

    //the heuristic function will be represented by the ascii value of the first character in the state name
    Function<Node<String>, Double> asciiHeuristicFn = node -> {
        String state = node.state();
        int asciiCode = (int) state.charAt(0);
        return (double) asciiCode;
    };

    @Test
    public void testAsciiHeuristicFunction() {
        HillClimbingSearch<String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);
        Node<String> nodeA = hillClimbingSearch.newNode("A");
        Node<String> nodeB = hillClimbingSearch.newNode("B");

        Assert.assertEquals(
                hillClimbingSearch.h(nodeA),
                hillClimbingSearch.h(nodeA),
                0
        );

        Assert.assertEquals(
                hillClimbingSearch.h(nodeB),
                hillClimbingSearch.h(nodeB),
                0
        );

        Assert.assertNotEquals(
                hillClimbingSearch.h(nodeA),
                hillClimbingSearch.h(nodeB),
                0
        );
    }

    @Test
    public void testAlreadyInGoalState() {
        HillClimbingSearch<String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);

        Assert.assertEquals(
                Arrays.asList(Action.NoOp),
                hillClimbingSearch.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(Action.NoOp),
                hillClimbingSearch.apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "B"::equals
                )));
    }

    @Test
    public void testSuccessfulGlobalMaximum() {
        HillClimbingSearch<String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);

        Assert.assertEquals(
                Arrays.asList(new GoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("C",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("C"), new GoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("E",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("E"), new GoAction("C"), new GoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("G",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));
    }

    @Test
    public void testSuccessfulLocalMaximum() {
        HillClimbingSearch<String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);

        // ends up in state D, with no hope of ever reaching the Global Maximum
        Assert.assertEquals(
                Arrays.asList(new GoAction("D")),
                hillClimbingSearch.apply(new BasicProblem<>("F",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("F"), new GoAction("D")),
                hillClimbingSearch.apply(new BasicProblem<>("I",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));
    }

    @Test
    public void testUnsuccessfulHillClimbing() {
        HillClimbingSearch<String> hillClimbingSearch = new HillClimbingSearch<>(asciiHeuristicFn);

        // when starting from state C an action towards A will always be selected
        // goalTestPredicate parameter seems redundant, since Hill Climbing will always evolve towards the local maximum, no matter what the destination is
        Assert.assertEquals(
                Arrays.asList(new GoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("C",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "B"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("C",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "F"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("A")),
                hillClimbingSearch.apply(new BasicProblem<>("C",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "G"::equals
                )));
    }

}
