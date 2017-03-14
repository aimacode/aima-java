package aima.test.unit.search.support;

import aima.core.search.basic.support.BasicBidirectionalActions;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author manthan.
 */
public class BasicBidirectionalActionsTest {
    @Test
    public void testBasicBidirectionalActions() {
        List<String> fromInitialStatePartList = Arrays.asList("start", "second", "third");
        List<String> fromGoalStatePartList = Arrays.asList("goal", "fifth", "fourth");
        BasicBidirectionalActions<String> basicBidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePartList, fromGoalStatePartList);
        Assert.assertEquals(
                Arrays.asList("start", "second", "third", "fourth", "fifth", "goal"),
                basicBidirectionalActions.fromInitialStateToGoalState());
        Assert.assertEquals(
                Arrays.asList("goal", "fifth", "fourth", "third", "second", "start"),
                basicBidirectionalActions.fromGoalStateToInitialState());
        Assert.assertEquals(
                Arrays.asList("start", "second", "third", "fourth", "fifth", "goal"),
                basicBidirectionalActions.fromInitialStateToGoalState());
        Assert.assertEquals(
                Arrays.asList("goal", "fifth", "fourth", "third", "second", "start"),
                basicBidirectionalActions.fromGoalStateToInitialState());
    }
}
