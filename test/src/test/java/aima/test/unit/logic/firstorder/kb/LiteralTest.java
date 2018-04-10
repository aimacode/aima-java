package aima.test.unit.logic.firstorder.kb;

import aima.core.logic.basic.firstorder.kb.data.Literal;
import aima.core.logic.basic.firstorder.parsing.ast.Constant;
import aima.core.logic.basic.firstorder.parsing.ast.Predicate;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author samagra
 */

public class LiteralTest {
    private Literal positiveLiteral;
    private Literal negativeLiteral;
    private Literal multipleTermLiteral;

    @Before
    public void setup() {
        Term[] terms = {new Constant("John")};
        positiveLiteral = new Literal(new Predicate("King", Arrays.asList(terms)));
        negativeLiteral = new Literal(new Predicate("Greedy", Arrays.asList(terms)), true);
        Term[] multipleTerms = {new Constant("John"), new Constant("Richard")};
        multipleTermLiteral = new Literal(new Predicate("LeftLeg", Arrays.asList(multipleTerms)), true);
    }

    @Test
    public void testLiteralSign() {
        Assert.assertTrue(positiveLiteral.isPositiveLiteral());
        Assert.assertTrue(negativeLiteral.isNegativeLiteral());
        Assert.assertFalse(positiveLiteral.isNegativeLiteral());
        Assert.assertFalse(negativeLiteral.isPositiveLiteral());
        Assert.assertTrue(multipleTermLiteral.isNegativeLiteral());
    }

    @Test
    public void testGetAtomicSentence() {
        Term[] terms = {new Constant("John")};
        Assert.assertEquals("King", positiveLiteral.getAtomicSentence().getSymbolicName());
        Assert.assertEquals(Arrays.asList(terms), positiveLiteral.getAtomicSentence().getArgs());
        Assert.assertEquals("Greedy", negativeLiteral.getAtomicSentence().getSymbolicName());
        Assert.assertEquals(Arrays.asList(terms), positiveLiteral.getAtomicSentence().getArgs());
        Term[] multipleTerms = {new Constant("John"), new Constant("Richard")};
        Assert.assertEquals("LeftLeg", multipleTermLiteral.getAtomicSentence().getSymbolicName());
        Assert.assertEquals(Arrays.asList(multipleTerms), multipleTermLiteral.getAtomicSentence().getArgs());
    }
}
