package aima.test.core.unit.search.local;

import java.util.Arrays;
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

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.search.BasicProblem;
import aima.core.search.local.BasicSimulatedAnnealingSearch;


/*
 * @author Anurag Rai
 */
public class BasicSimulatedAnnealingTest {

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
        BasicSimulatedAnnealingSearch<String> simulatedAnnealing = new BasicSimulatedAnnealingSearch<>(asciiHeuristicFn);
        Node<String> nodeA = simulatedAnnealing.newNode("A");
        Node<String> nodeB = simulatedAnnealing.newNode("B");

        Assert.assertEquals(
                simulatedAnnealing.getHeuristicFunction().apply(nodeA),
                simulatedAnnealing.getHeuristicFunction().apply(nodeA),
                0
        );

        Assert.assertEquals(
        		simulatedAnnealing.getHeuristicFunction().apply(nodeB),
        		simulatedAnnealing.getHeuristicFunction().apply(nodeB),
                0
        );

        Assert.assertNotEquals(
        		simulatedAnnealing.getHeuristicFunction().apply(nodeA),
        		simulatedAnnealing.getHeuristicFunction().apply(nodeB),
                0
        );
    }

    @Test
    public void testAlreadyInGoalState() {
        BasicSimulatedAnnealingSearch<String> simulatedAnnealing = new BasicSimulatedAnnealingSearch<>(asciiHeuristicFn);

        Assert.assertEquals(
                Arrays.asList(Action.NoOp),
                simulatedAnnealing.apply(new BasicProblem<>("A",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "A"::equals
                )));

        Assert.assertEquals(
                Arrays.asList(Action.NoOp),
                simulatedAnnealing.apply(new BasicProblem<>("B",
                        simpleBinaryTreeActionsFn,
                        goActionResultFn,
                        "B"::equals
                )));
    }

    @Test
    public void testDelE() {
    	BasicSimulatedAnnealingSearch<String> simulatedAnnealing = new BasicSimulatedAnnealingSearch<>(asciiHeuristicFn);
    	int deltaE = -1;
		double higherTemperature = 30.0;
		double lowerTemperature = 29.5;

		Assert.assertTrue(simulatedAnnealing.probabilityOfAcceptance(lowerTemperature,
				deltaE) < simulatedAnnealing.probabilityOfAcceptance(higherTemperature,
				deltaE));
    }
}
