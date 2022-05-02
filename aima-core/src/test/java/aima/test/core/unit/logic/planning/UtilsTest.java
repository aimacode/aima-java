package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.planning.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author samagra
 */
public class UtilsTest {
    @Test
    public void parserTest() {
        String precondition = "At(C1,JFK) ^ At(C2,SFO)";
        ArrayList<Literal> literals = (ArrayList<Literal>) Utils.parse(precondition);
    }

    @Test
    public void angelicParserTest(){
        String first = "~A";
        String second = "A^~-B";
        String third = "~+A^~+-C";
        Literal a = new Literal(new Predicate("A",new ArrayList<>()));
        Literal b = new Literal(new Predicate("B",new ArrayList<>()));
        Literal c = new Literal(new Predicate("C",new ArrayList<>()));

        // ~A
        List<HashSet<Literal>> parsed = Utils.angelicParse(first);
        Assert.assertFalse(parsed.get(0).contains(a));
        Assert.assertTrue(parsed.get(1).contains(a));
        Assert.assertFalse(parsed.get(2).contains(a));
        Assert.assertFalse(parsed.get(3).contains(a));

        // A^~-B
        parsed = Utils.angelicParse(second);
        Assert.assertTrue(parsed.get(0).contains(a));
        Assert.assertFalse(parsed.get(1).contains(a));
        Assert.assertFalse(parsed.get(2).contains(a));
        Assert.assertFalse(parsed.get(3).contains(a));
        Assert.assertFalse(parsed.get(0).contains(b));
        Assert.assertFalse(parsed.get(1).contains(b));
        Assert.assertFalse(parsed.get(2).contains(b));
        Assert.assertTrue(parsed.get(3).contains(b));

        //~+A^~+-C
        parsed = Utils.angelicParse(third);
        Assert.assertFalse(parsed.get(0).contains(a));
        Assert.assertFalse(parsed.get(1).contains(a));
        Assert.assertTrue(parsed.get(2).contains(a));
        Assert.assertFalse(parsed.get(3).contains(a));
        Assert.assertFalse(parsed.get(0).contains(c));
        Assert.assertFalse(parsed.get(1).contains(c));
        Assert.assertTrue(parsed.get(2).contains(c));
        Assert.assertTrue(parsed.get(3).contains(c));
    }
}
