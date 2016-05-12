package aima.test.core.unit.search.uninformed.graph;

import aima.core.search.basic.support.BasicProblem;
import aima.core.search.uninformed.graph.BasicBreadthFirstGraphSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ciaran O'Reilly
 */
public class BasicBreadthFirstGraphSearchTest {

    class GoAction {
        String goTo;
        GoAction(String goTo) {
            this.goTo = goTo;
        }

        public String name() { return "Go(" + goTo + ")"; }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof GoAction) {
                return this.name().equals(((GoAction)obj).name());
            }
            return super.equals(obj);
        }
        @Override
        public int hashCode() {
            return name().hashCode();
        }
    }

    Map<String, List<String>> simpleBinaryTreeStateSpace = new HashMap<String, List<String>>() {{
        put("A", Arrays.asList("B", "C"));
        put("B", Arrays.asList("D", "E"));
        put("C", Arrays.asList("F", "G"));
    }};

    Function<String, Set<GoAction>> simpleBinaryTreeActionsFn = state -> {
        if (simpleBinaryTreeStateSpace.containsKey(state)) {
            return new LinkedHashSet<>(simpleBinaryTreeStateSpace.get(state).stream().map(GoAction::new).collect(Collectors.toList()));
        }
        return Collections.emptySet();
    };

    BiFunction<String, GoAction, String> goActionResultFn = (state, action) -> ((GoAction) action).goTo;

    @Test
    public void testAllSimpleBinaryTreeGoals() {
        Assert.assertEquals(
                Arrays.asList((GoAction) null),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("B")),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "B"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("C")),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "C"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("B"), new GoAction("D")),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "D"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("B"), new GoAction("E")),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "E"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("C"), new GoAction("F")),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "F"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("C"), new GoAction("G")),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "G"::equals
                )));
    }

    @Test
    public void testUnreachableSimpleBinaryTreeGoals() {
        Assert.assertEquals(Collections.<GoAction>emptyList(),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(Collections.<GoAction>emptyList(),
                new BasicBreadthFirstGraphSearch<GoAction, String>().apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "X"::equals
                )));
    }
}
