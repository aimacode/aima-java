package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.planning.Utils;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author samagra
 */
public class UtilsTest {
    @Test
    public void parserTest() {
        String precondition = "At(C1,JFK) ^ At(C2,SFO)";
        ArrayList<Literal> literals = (ArrayList<Literal>) Utils.parse(precondition);
    }
}
