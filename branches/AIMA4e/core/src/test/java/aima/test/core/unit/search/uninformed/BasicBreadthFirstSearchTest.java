package aima.test.core.unit.search.uninformed;

import aima.core.api.agent.Action;
import aima.core.search.BasicProblem;
import aima.core.search.uninformed.BasicBreadthFirstSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ciaran O'Reilly
 */
public class BasicBreadthFirstSearchTest {

    class GoAction implements Action {
        String goTo;
        GoAction(String goTo) {
            this.goTo = goTo;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof GoAction) {
                return this.goTo.equals(((GoAction) obj).goTo);
            }
            return super.equals(obj);
        }

        @Override
        public String toString() {
            return "Go("+goTo+")";
        }
    }

    Map<String, List<String>> simpleBinaryTreeStateSpace = new HashMap<String, List<String>>() {{
        put("A", Arrays.asList("B", "C"));
        put("B", Arrays.asList("D", "E"));
        put("C", Arrays.asList("F", "G"));
    }};

    Function<String, Set<Action>> simpleBinaryTreeActionsFn = state -> {
        if (simpleBinaryTreeStateSpace.containsKey(state)) {
            return new LinkedHashSet<>(simpleBinaryTreeStateSpace.get(state).stream().map(GoAction::new).collect(Collectors.toList()));
        }
        return Collections.emptySet();
    };

    BiFunction<String, Action, String> goActionResultFn = (state, action) -> ((GoAction) action).goTo;

    @Test
    public void testAllSimpleBinaryTreeGoals() {
        Assert.assertEquals(
                Arrays.asList(Action.NoOp),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList((Action) new GoAction("B")),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "B"::equals
                )));

        Assert.assertEquals(
                Arrays.asList((Action) new GoAction("C")),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "C"::equals
                )));

        Assert.assertEquals(
                Arrays.asList((Action) new GoAction("B"), new GoAction("D")),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "D"::equals
                )));

        Assert.assertEquals(
                Arrays.asList((Action) new GoAction("B"), new GoAction("E")),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "E"::equals
                )));

        Assert.assertEquals(
                Arrays.asList((Action) new GoAction("C"), new GoAction("F")),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "F"::equals
                )));

        Assert.assertEquals(
                Arrays.asList((Action) new GoAction("C"), new GoAction("G")),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "G"::equals
                )));
    }

    @Test
    public void testUnreachableSimpleBinaryTreeGoals() {
        Assert.assertEquals(Collections.<Action>emptyList(),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(Collections.<Action>emptyList(),
                new BasicBreadthFirstSearch<String>().apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "X"::equals
                )));
    }
}
