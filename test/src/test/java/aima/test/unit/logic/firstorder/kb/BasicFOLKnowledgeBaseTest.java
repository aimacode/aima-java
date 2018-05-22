package aima.test.unit.logic.firstorder.kb;

import aima.core.logic.basic.firstorder.BasicFOLKnowledgeBase;
import aima.core.logic.basic.firstorder.domain.DomainFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author samagra
 */
public class BasicFOLKnowledgeBaseTest {
    private BasicFOLKnowledgeBase knowledgeBase;

    @Before
    public void setup(){
        knowledgeBase = new BasicFOLKnowledgeBase(DomainFactory.kingsDomain());
    }

    @Test
    public void tellTest(){
        Assert.assertEquals(0,knowledgeBase.size());
        knowledgeBase.tell("King(John)");
        Assert.assertEquals(1,knowledgeBase.size());
        knowledgeBase.tell("King(Richard)");
        Assert.assertEquals(2,knowledgeBase.size());
    }

}
