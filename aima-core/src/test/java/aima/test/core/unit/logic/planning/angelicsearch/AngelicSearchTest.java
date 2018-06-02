package aima.test.core.unit.logic.planning.angelicsearch;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.angelicsearch.AngelicHLA;
import aima.core.logic.planning.angelicsearch.AngelicSearchAlgorithm;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AngelicSearchTest {
    @Test
    public void angelicSearchTest(){
        AngelicHLA h1 = new AngelicHLA("h1", null, "~A", "A^~-B");
        AngelicHLA h2 = new AngelicHLA("h2", null, "~B", "~+A^~+-C");
        ActionSchema
        AngelicSearchAlgorithm algo = new AngelicSearchAlgorithm();
        System.out.println("Answer");
        List<Object> list =  algo.angelicSearch(PlanningProblemFactory.getAngelicABProblem(),
                PlanningProblemFactory.getAngelicInitialPlan(new ArrayList<>(Arrays.asList(h1,h2)),PlanningProblemFactory.getAngelicABProblem(),"A^C"));
        System.out.println(list.size());
    }
}
