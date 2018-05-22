package aima.test.unit.logic.firstorder.kb.data;

import aima.core.logic.basic.firstorder.kb.data.Clause;
import aima.core.logic.basic.firstorder.kb.data.Literal;
import aima.core.logic.basic.firstorder.parsing.ast.Predicate;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author samagra
 */

public class ClauseTest {
    //A predefined list of atomic sentences
    private Clause clause;
    private Literal negatedLiteralOne;
    private Literal negatedLiteralTwo;
    private Literal positiveLiteral;

    @Before
    public void setup(){
        Term[] terms = {new Variable("x")};
        negatedLiteralOne = new Literal(new Predicate("King", Arrays.asList(terms)),true);
        negatedLiteralTwo = new Literal(new Predicate("Greedy",Arrays.asList(terms)),true);
        positiveLiteral = new Literal(new Predicate("Evil",Arrays.asList(terms)));
    }

    @Test
    public void testConstructor(){
        clause = new Clause();
        Assert.assertEquals(0,clause.size());
        clause = new Clause(negatedLiteralOne,negatedLiteralTwo);
        Assert.assertEquals(2,clause.size());
        Literal[] literals = {positiveLiteral,negatedLiteralTwo,negatedLiteralOne};
        clause = new Clause(Arrays.asList(literals));
        Assert.assertEquals(3,clause.size());
    }

    @Test
    public void testDifferentTypesOfClauses(){
        clause = new Clause(negatedLiteralOne);
        Assert.assertTrue(clause.isHornClause());
        Assert.assertTrue(clause.isUnitClause());
        Assert.assertFalse(clause.isDefiniteClause());
        Assert.assertFalse(clause.isImplicationDefiniteClause());
        clause = new Clause(positiveLiteral);
        Assert.assertTrue(clause.isHornClause());
        Assert.assertTrue(clause.isUnitClause());
        Assert.assertFalse(clause.isImplicationDefiniteClause());
        Assert.assertTrue(clause.isDefiniteClause());
        clause = new Clause(negatedLiteralOne,negatedLiteralTwo,positiveLiteral);
        Assert.assertTrue(clause.isHornClause());
        Assert.assertFalse(clause.isUnitClause());
        Assert.assertTrue(clause.isDefiniteClause());
        Assert.assertTrue(clause.isDefiniteClause());
    }

    @Test
    public void testGetConsequence(){
        clause = new Clause(negatedLiteralTwo,negatedLiteralOne);
        Literal literal = clause.getConsequence();
        Assert.assertNull(literal);
        clause = new Clause(negatedLiteralOne,negatedLiteralTwo,positiveLiteral);
        Assert.assertEquals(positiveLiteral,clause.getConsequence());
    }
}
