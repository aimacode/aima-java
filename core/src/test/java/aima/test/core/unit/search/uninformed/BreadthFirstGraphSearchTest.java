package aima.test.core.unit.search.uninformed;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.queue.GraphGoalTestedFirstQueueSearch;
import aima.core.search.basic.support.BasicProblem;
import aima.core.search.basic.uninformed.BreadthFirstQueueSearch;
import aima.test.core.unit.search.support.GoAction;

@RunWith(Parameterized.class)
public class BreadthFirstGraphSearchTest {

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> implementations() {
		return Arrays.asList(new Object[][] {
			{new BreadthFirstQueueSearch<GoAction, String>(new GraphGoalTestedFirstQueueSearch<>())}
		});
	}
	 
    @Parameter
	public SearchForActionsFunction<GoAction, String> breadthFirstGraphSearch;
	 
    Map<String, List<String>> simpleBinaryTreeStateSpace = new HashMap<String, List<String>>() {
    	private static final long serialVersionUID = 1L; {
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
    
    BiFunction<String, GoAction, String> goActionResultFn = (state, action) -> ((GoAction) action).getGoTo();
    
    @Test
    public void testAllSimpleBinaryTreeGoals() {
//        Assert.assertEquals(
//                Arrays.asList((GoAction) null),
//                breadthFirstGraphSearch.apply(new BasicProblem<>("A",
//                        simpleBinaryTreeActionsFn,
//                        goActionResultFn,
//                        "A"::equals
//                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("B")),
                breadthFirstGraphSearch.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "B"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("C")),
                breadthFirstGraphSearch.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "C"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("B"), new GoAction("D")),
                breadthFirstGraphSearch.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "D"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("B"), new GoAction("E")),
                breadthFirstGraphSearch.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "E"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("C"), new GoAction("F")),
                breadthFirstGraphSearch.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "F"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(new GoAction("C"), new GoAction("G")),
                breadthFirstGraphSearch.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "G"::equals
                )));
    }

    //@Test
    public void testUnreachableSimpleBinaryTreeGoals() {
        Assert.assertEquals(Collections.<GoAction>emptyList(),
        		breadthFirstGraphSearch.apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(Collections.<GoAction>emptyList(),
        		breadthFirstGraphSearch.apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "X"::equals
                )));
    }
}
