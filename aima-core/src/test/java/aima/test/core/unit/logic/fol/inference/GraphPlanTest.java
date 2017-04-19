package aima.test.core.unit.logic.fol.inference;

import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.graphplan.GraphPlan;
import aima.core.logic.fol.inference.graphplan.GraphPlanSolutionExtractor;
import aima.core.logic.fol.inference.graphplan.PDDL;
import aima.core.logic.fol.inference.graphplan.PDDLAction;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Predicate;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test for GraphPlan, figure 10.9 in AIMA3e.
 * @author Matt Grenander
 */
public class GraphPlanTest {
    /**
     * Spare tire example from page 370 and 383 - 385 in AIMA3e.
     */
    @Test
    public void spareTireTest() {
        FOLDomain domain = new FOLDomain(DomainFactory.spareTireDomain());
        FOLParser parser = new FOLParser(domain);

        //At clauses
        ArrayList<Clause> at = new ArrayList<>(12);
        String[] tires = {"Spare","Flat"};
        String[] locations = {"Trunk","Ground","Axle"};

        //Positive literals
        for (String tire: tires) {
            for (String location: locations) {
                parser.setUpToParse("At(" + tire + "," + location + ")");
                at.add(new Clause(Collections.singletonList(new Literal((Predicate)parser.parsePredicate(), false))));
            }
        }

        //Negative literals
        for (String tire: tires) {
            for (String location: locations) {
                parser.setUpToParse("At(" + tire + "," + location + ")");
                at.add(new Clause(Collections.singletonList(new Literal((Predicate)parser.parsePredicate(), true))));
            }
        }

        //Create the PDDL
        //Initial states are literals 5,0,8,10,7,9
        CNF init = new CNF(Arrays.asList(at.get(5),at.get(0),at.get(8),at.get(10),at.get(7),at.get(9)));

        //Goal state is literal 2
        CNF goal = new CNF(Collections.singletonList(at.get(2)));

        //Actions
        PDDLAction leave = new PDDLAction("LeaveOvernight", new CNF(Collections.singletonList(new Clause())), new CNF(Arrays.asList(at.get(6),at.get(7),at.get(8),at.get(9),at.get(10),at.get(11))));
        PDDLAction rm1 = new PDDLAction("Remove(Spare,Axle)", new CNF(Collections.singletonList(at.get(2))), new CNF(Arrays.asList(at.get(8),at.get(1))));
        PDDLAction rm2 = new PDDLAction("Remove(Spare,Trunk)", new CNF(Collections.singletonList(at.get(0))), new CNF(Arrays.asList(at.get(6),at.get(1))));
        PDDLAction rm3 = new PDDLAction("Remove(Flat,Axle)", new CNF(Collections.singletonList(at.get(5))), new CNF(Arrays.asList(at.get(11),at.get(4))));
        PDDLAction rm4 = new PDDLAction("Remove(Flat,Trunk)", new CNF(Collections.singletonList(at.get(3))), new CNF(Arrays.asList(at.get(9),at.get(10))));
        PDDLAction put1 = new PDDLAction("PutOn(Spare,Axle)", new CNF(Arrays.asList(at.get(1),at.get(11))), new CNF(Arrays.asList(at.get(7),at.get(2))));
        PDDLAction put2 = new PDDLAction("PutOn(Flat,Axle)", new CNF(Arrays.asList(at.get(4),at.get(11))), new CNF(Arrays.asList(at.get(10),at.get(5))));

        PDDL pddl = new PDDL(Arrays.asList(leave,rm1,rm2,rm3,rm4,put1,put2),init,goal);

        //Find solution
        GraphPlanSolutionExtractor solEx = new GraphPlanSolutionExtractor();
        GraphPlan graphPlanner = new GraphPlan(solEx);
        List<PDDLAction> graphPlanSolution = graphPlanner.graphPlan(pddl);

        //Expected Solution: [Remove(Spare,Trunk), Remove(Flat,Axle), PutOn(Spare,Axle)]
        List<PDDLAction> expectedSolution = Arrays.asList(rm2,rm3,put1);

        assertEquals(expectedSolution,graphPlanSolution);
    }
}
