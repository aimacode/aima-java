package aima.test.core.unit.logic.planning.hierarchicalsearch;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.hierarchicalsearch.HighLevelAction;
import org.junit.Assert;
import org.junit.Test;


public class HighLevelActionTest {
    @Test
    public void inheritanceTest(){
        ActionSchema actionOne = new ActionSchema("Go",null,"At(A)","At(B)^~At(A)");
        ActionSchema hla = new HighLevelAction("Go",null,"","",actionOne);

        Assert.assertTrue(hla instanceof HighLevelAction);
        Assert.assertTrue(hla instanceof ActionSchema);
        Assert.assertFalse(actionOne instanceof HighLevelAction);

    }
}
