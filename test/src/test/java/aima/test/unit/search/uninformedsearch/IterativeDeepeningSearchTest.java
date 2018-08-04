package aima.test.unit.search.uninformedsearch;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.environment.vacuum.VELocalState;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.basic.uninformedsearch.IterativeDeepeningSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class IterativeDeepeningSearchTest {
    IterativeDeepeningSearch iterativeDeepeningSearch = new IterativeDeepeningSearch();

    @Test
    public void testSimplifiedRomania() {
        Problem problem = ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
        Node result = iterativeDeepeningSearch.search(problem);
        Assert.assertEquals("In(Bucharest)", result.state().toString());
        List actions = Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS),
                new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST));
        Assert.assertEquals(actions, iterativeDeepeningSearch.apply(problem));
    }

    @Test
    public void testVacuumEnvironment() {
        Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_RIGHT, VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("A",
                        new VELocalState("A", VacuumEnvironment.Status.Clean),
                        new VELocalState("B", VacuumEnvironment.Status.Dirty))));

        Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("A",
                        new VELocalState("A", VacuumEnvironment.Status.Dirty),
                        new VELocalState("B", VacuumEnvironment.Status.Clean))));

        Assert.assertEquals(
                Arrays.asList(VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_RIGHT,
                        VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("A",
                        new VELocalState("A", VacuumEnvironment.Status.Dirty),
                        new VELocalState("B", VacuumEnvironment.Status.Dirty))));

        Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("B",
                        new VELocalState("A", VacuumEnvironment.Status.Clean),
                        new VELocalState("B", VacuumEnvironment.Status.Dirty))));

        Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_LEFT, VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("B",
                        new VELocalState("A", VacuumEnvironment.Status.Dirty),
                        new VELocalState("B", VacuumEnvironment.Status.Clean))));

        Assert.assertEquals(
                Arrays.asList(VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_LEFT,
                        VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("B",
                        new VELocalState("A", VacuumEnvironment.Status.Dirty),
                        new VELocalState("B", VacuumEnvironment.Status.Dirty))));

        // A slightly larger world
        Assert.assertEquals(
                Arrays.asList(VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_RIGHT,
                        VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_RIGHT, VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("A",
                        new VELocalState("A", VacuumEnvironment.Status.Dirty),
                        new VELocalState("B", VacuumEnvironment.Status.Dirty),
                        new VELocalState("C", VacuumEnvironment.Status.Dirty))));

        Assert.assertEquals(
                Arrays.asList(VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_LEFT,
                        VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_LEFT, VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("C",
                        new VELocalState("A", VacuumEnvironment.Status.Dirty),
                        new VELocalState("B", VacuumEnvironment.Status.Dirty),
                        new VELocalState("C", VacuumEnvironment.Status.Dirty))));

        Assert.assertEquals(
                // NOTE: This sequence of actions is only correct because we
                // always order our possible actions [Left, Suck, Right] in
                // ProblemFactory.getSimpleVacuumWorldProblem(), if the order
                // was different we would likely get a different answer.
                Arrays.asList(VacuumEnvironment.ACTION_RIGHT, VacuumEnvironment.ACTION_SUCK,
                        VacuumEnvironment.ACTION_LEFT, VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_LEFT,
                        VacuumEnvironment.ACTION_SUCK),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleVacuumWorldProblem("B",
                        new VELocalState("A", VacuumEnvironment.Status.Dirty),
                        new VELocalState("B", VacuumEnvironment.Status.Dirty),
                        new VELocalState("C", VacuumEnvironment.Status.Dirty))));

    }

    @Test
    public void simpleBinaryTreeTests() {
        Assert.assertEquals(Arrays.asList("B"), iterativeDeepeningSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "B")));

        Assert.assertEquals(Arrays.asList("C"), iterativeDeepeningSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "C")));

        Assert.assertEquals(Arrays.asList("B", "E"),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "E")));

        Assert.assertEquals(Arrays.asList("C", "F", "L"),
                iterativeDeepeningSearch.apply(ProblemFactory.getSimpleBinaryTreeProblem("A", "L")));
    }
}
