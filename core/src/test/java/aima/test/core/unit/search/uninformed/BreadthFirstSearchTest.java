package aima.test.core.unit.search.uninformed;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.environment.vacuum.VELocalState;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.example.ExampleBreadthFirstSearch;
import aima.core.search.basic.queue.GraphGoalTestedFirstQueueSearch;
import aima.core.search.basic.queue.GraphQueueSearch;
import aima.core.search.basic.queue.QueueSearchForActions;
import aima.core.search.basic.queue.TreeGoalTestedFirstQueueSearch;
import aima.core.search.basic.queue.TreeQueueSearch;
import aima.core.search.basic.uninformed.BreadthFirstQueueSearch;

@RunWith(Parameterized.class)
public class BreadthFirstSearchTest {

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> implementations() {
		return Arrays.asList(new Object[][] { { "ExampleBreadthFirstSearch" },
				{ "BreadthFirstQueueSearch-GraphGoalTestFirst" }, { "BreadthFirstQueueSearch-Graph" },
				{ "BreadthFirstQueueSearch-TreeGoalTestedFirst" }, { "BreadthFirstQueueSearch-Tree" } });
	}

	@Parameter
	public String searchFunctionName;

	public <A, S> List<A> searchForActions(Problem<A, S> problem) {
		SearchForActionsFunction<A, S> searchForActionsFunction;
		if ("ExampleBreadthFirstSearch".equals(searchFunctionName)) {
			searchForActionsFunction = new ExampleBreadthFirstSearch<A, S>();
		} else {
			QueueSearchForActions<A, S> qsearchVariant;
			String variant = searchFunctionName.split("-")[1];
			// NOTE: Matches version in book
			if (variant.equals("GraphGoalTestFirst")) {
				qsearchVariant = new GraphGoalTestedFirstQueueSearch<>();
			} else if (variant.equals("Graph")) {
				qsearchVariant = new GraphQueueSearch<>();
			} else if (variant.equals("TreeGoalTestedFirst")) {
				qsearchVariant = new TreeGoalTestedFirstQueueSearch<>();
			} else {
				qsearchVariant = new TreeQueueSearch<>();
			}
			searchForActionsFunction = new BreadthFirstQueueSearch<A, S>(qsearchVariant);
		}

		return searchForActionsFunction.apply(problem);
	}

	@Test
	public void testSimplifiedRoadmapOfPartOfRomania() {
		Assert.assertEquals(Arrays.asList((String) null),
				searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
						SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD)));

		Assert.assertEquals(
				Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
						new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS),
						new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST)),
				searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
						SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST)));
	}

	@Test
	public void testSimpleVacuumEnvironment() {
		Assert.assertEquals(Arrays.asList((String) null),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("A",
						new VELocalState("A", VacuumEnvironment.Status.Clean),
						new VELocalState("B", VacuumEnvironment.Status.Clean))));

		Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_RIGHT, VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("A",
						new VELocalState("A", VacuumEnvironment.Status.Clean),
						new VELocalState("B", VacuumEnvironment.Status.Dirty))));

		Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("A",
						new VELocalState("A", VacuumEnvironment.Status.Dirty),
						new VELocalState("B", VacuumEnvironment.Status.Clean))));

		Assert.assertEquals(
				Arrays.asList(VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_RIGHT,
						VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("A",
						new VELocalState("A", VacuumEnvironment.Status.Dirty),
						new VELocalState("B", VacuumEnvironment.Status.Dirty))));

		Assert.assertEquals(Arrays.asList((String) null),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("B",
						new VELocalState("A", VacuumEnvironment.Status.Clean),
						new VELocalState("B", VacuumEnvironment.Status.Clean))));

		Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("B",
						new VELocalState("A", VacuumEnvironment.Status.Clean),
						new VELocalState("B", VacuumEnvironment.Status.Dirty))));

		Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_LEFT, VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("B",
						new VELocalState("A", VacuumEnvironment.Status.Dirty),
						new VELocalState("B", VacuumEnvironment.Status.Clean))));

		Assert.assertEquals(
				Arrays.asList(VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_LEFT,
						VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("B",
						new VELocalState("A", VacuumEnvironment.Status.Dirty),
						new VELocalState("B", VacuumEnvironment.Status.Dirty))));

		// A slightly larger world
		Assert.assertEquals(
				Arrays.asList(VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_RIGHT,
						VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_RIGHT, VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("A",
						new VELocalState("A", VacuumEnvironment.Status.Dirty),
						new VELocalState("B", VacuumEnvironment.Status.Dirty),
						new VELocalState("C", VacuumEnvironment.Status.Dirty))));

		Assert.assertEquals(
				Arrays.asList(VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_LEFT,
						VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_LEFT, VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("C",
						new VELocalState("A", VacuumEnvironment.Status.Dirty),
						new VELocalState("B", VacuumEnvironment.Status.Dirty),
						new VELocalState("C", VacuumEnvironment.Status.Dirty))));

		Assert.assertEquals(
				// NOTE: This sequence of actions is only correct because we
				// always order our possible actions [Left, Suck, Right] in
				// ProblemFactory.getSimpleVacuumWorldProblem(), if the order
				// was different we would likely get a different answer.
				Arrays.asList(VacuumEnvironment.ACTION_LEFT, VacuumEnvironment.ACTION_SUCK,
						VacuumEnvironment.ACTION_RIGHT, VacuumEnvironment.ACTION_SUCK, VacuumEnvironment.ACTION_RIGHT,
						VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("B",
						new VELocalState("A", VacuumEnvironment.Status.Dirty),
						new VELocalState("B", VacuumEnvironment.Status.Dirty),
						new VELocalState("C", VacuumEnvironment.Status.Dirty))));
	}

	@Test
	public void testReachableSimpleBinaryTreeGoals() {
		Assert.assertEquals(Arrays.asList((String) null),
				searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("A", "A")));

		Assert.assertEquals(Arrays.asList("B"), searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("A", "B")));

		Assert.assertEquals(Arrays.asList("C"), searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("A", "C")));

		Assert.assertEquals(Arrays.asList("B", "E"),
				searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("A", "E")));

		Assert.assertEquals(Arrays.asList("C", "F", "L"),
				searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("A", "L")));
	}

	@Test
	public void testUnreachableSimpleBinaryTreeGoals() {
		Assert.assertEquals(Collections.<String>emptyList(),
				searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("B", "A")));
	}
}
