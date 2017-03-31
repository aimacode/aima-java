package aima.test.unit.search.support;

import aima.core.search.basic.support.BasicBidirectionalSearchResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author manthan.
 */
public class BasicBidirectionalSearchResultTest {
    @Test
    public void testBasicBidirectionalSearchresult() {
        List<String> fromInitialStatePartList = Arrays.asList("start", "second", "third");
        List<String> fromGoalStatePartList = Arrays.asList("goal", "fifth", "fourth");
        BasicBidirectionalSearchResult<String> basicBidirectionalActions = new BasicBidirectionalSearchResult<>();
        basicBidirectionalActions.setFromGoalStateToMeeting(fromGoalStatePartList);
        basicBidirectionalActions.setFromInitialStateToMeeting(fromInitialStatePartList);
        Assert.assertEquals(
                Arrays.asList("start", "second", "third"),
                basicBidirectionalActions.fromInitialStateToMeeting());
        Assert.assertEquals(
                Arrays.asList("goal", "fifth", "fourth"),
                basicBidirectionalActions.fromGoalStateToMeeting());
    }
}
