package aima.test.core.unit.search.online;

/*
 * @author Anurag Rai
 * 
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import aima.core.api.agent.Action;
import aima.core.api.search.online.StepCostFunction;
import aima.core.search.online.OnlineSearchProblem;

public class OnlineSearchProblemTest {
	
	//The Action that is used
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
	
	/**The graph structure that is implemented
	 * 
	 * A ---- B ---- X
	 *        |
	 *        C
	 */
	Map<String, List<String>> map = new HashMap<String, List<String>>() {{
		put("A", Arrays.asList("B"));
		put("B", Arrays.asList("A", "C", "X"));
		put("C", Arrays.asList("B"));
		put("X", Arrays.asList("B"));

    }};
	
    //The Action Function
	Function<String, Set<Action>> actionsFn = state -> {
        if (map.containsKey(state)) {
            return new LinkedHashSet<>(map.get(state).stream().map(GoAction::new).collect(Collectors.toList()));
        }
        return Collections.emptySet();
    };
    
    //Arbitrary Goal-State Function
    Predicate<String> goalTestFn = state -> { 
    	if (state.equals("X")) { 
    		return true;
    	}
    	return false;
    };
    
    //Arbitrary Step Cost function
    StepCostFunction<String> stepCostFn = (state1, action, state2) -> {
    	if ( state1.equals("A") && state2.equals("X") && action.equals(new GoAction("B"))) {
    		return 5.0;
    	}
    	return 1.0;
    };
    
    
    @Test
    public void testClass() {
    	OnlineSearchProblem<String> onlineSearchPorblem = new OnlineSearchProblem<String>(actionsFn, goalTestFn, stepCostFn);

        Assert.assertEquals(onlineSearchPorblem.getActionsFunction(),actionsFn);

        Assert.assertEquals(onlineSearchPorblem.getStepCostFunction(),stepCostFn);
    }
    
    @Test
    public void testGoalState() {
    	OnlineSearchProblem<String> onlineSearchPorblem = new OnlineSearchProblem<String>(actionsFn, goalTestFn, stepCostFn);

        Assert.assertNotEquals(onlineSearchPorblem.isGoalState("A"),true);
        Assert.assertNotEquals(onlineSearchPorblem.isGoalState("B"),true);
        Assert.assertNotEquals(onlineSearchPorblem.isGoalState("C"),true);
        Assert.assertEquals(onlineSearchPorblem.isGoalState("X"),true);
    }
    
    @Test
    public void testActonsFunction() {
    	OnlineSearchProblem<String> onlineSearchPorblem = new OnlineSearchProblem<String>(actionsFn, goalTestFn, stepCostFn);
    	LinkedHashSet<GoAction> temp1 = new LinkedHashSet<>();
    	temp1.add(new GoAction("B"));
        Assert.assertEquals(onlineSearchPorblem.getActionsFunction().apply("A"), temp1);
        
        LinkedHashSet<GoAction> temp2 = new LinkedHashSet<>();
    	temp2.add(new GoAction("C"));
    	temp2.add(new GoAction("A"));
    	temp2.add(new GoAction("X"));
        Assert.assertEquals(onlineSearchPorblem.getActionsFunction().apply("B"), temp2);
        
        LinkedHashSet<GoAction> temp3 = new LinkedHashSet<>();
    	temp3.add(new GoAction("B"));
        Assert.assertEquals(onlineSearchPorblem.getActionsFunction().apply("C"), temp3);
        
        LinkedHashSet<GoAction> temp4 = new LinkedHashSet<>();
    	temp4.add(new GoAction("B"));
        Assert.assertEquals(onlineSearchPorblem.getActionsFunction().apply("X"), temp4);
    }
    
    @Test
    public void testStepCostFunction() {
    	OnlineSearchProblem<String> onlineSearchPorblem = new OnlineSearchProblem<String>(actionsFn, goalTestFn, stepCostFn);
    	
        Assert.assertEquals(onlineSearchPorblem.getStepCostFunction().apply("A",new GoAction("B"),"X"), 5.0, 0);
        
        Assert.assertNotEquals(onlineSearchPorblem.getStepCostFunction().apply("A",new GoAction("C"),"X"), 5.0, 0);
    }
}
