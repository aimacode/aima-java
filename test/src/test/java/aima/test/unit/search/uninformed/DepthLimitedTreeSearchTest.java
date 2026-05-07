package aima.test.unit.search.uninformed;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.environment.vacuum.VELocalState;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.search.api.Problem;
import aima.core.search.basic.uninformed.DepthLimitedTreeSearch;

public class DepthLimitedTreeSearchTest {

	private <A, S> List<A> searchForActions(Problem<A, S> problem, int limit) {
		return new DepthLimitedTreeSearch<A, S>(limit).apply(problem);
	}

	@Test
	public void testStartEqualsGoalReturnsTrivialSolution() {
		Assert.assertEquals(Arrays.asList((String) null),
				searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
						SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD), 0));
	}

	@Test
	public void testRomaniaAtSufficientDepth() {
		Assert.assertEquals(
				Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
						new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS),
						new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST)),
				searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
						SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST), 3));
	}

	@Test
	public void testRomaniaAtInsufficientDepthReturnsCutoff() {
		// Shortest Arad -> Bucharest is 3 actions, so a limit of 2 must hit cutoff.
		Assert.assertNull(searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
				SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST), 2));
	}

	@Test
	public void testRomaniaAtZeroLimitNonGoalReturnsCutoff() {
		Assert.assertNull(searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
				SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST), 0));
	}

	@Test
	public void testVacuumGoalAlreadyReachableInOneStep() {
		// Single SUCK at the start state already cleans both squares — no LEFT no-op
		// loop can come first, since SUCK is tried before RIGHT in the action order.
		Assert.assertEquals(Arrays.asList(VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("A",
						new VELocalState("A", VacuumEnvironment.Status.Dirty),
						new VELocalState("B", VacuumEnvironment.Status.Clean)), 1));
	}

	@Test
	public void testTreeSearchYieldsNonMinimalPathInVacuumWorld() {
		// DLS-tree has no cycle check. With LEFT as a no-op at the leftmost position
		// and action order [Left, Suck, Right], the first goal-reaching plan it finds
		// at limit=5 walks the LEFT self-loop before turning right.
		Assert.assertEquals(
				Arrays.asList(VacuumEnvironment.ACTION_LEFT, VacuumEnvironment.ACTION_LEFT,
						VacuumEnvironment.ACTION_LEFT, VacuumEnvironment.ACTION_RIGHT,
						VacuumEnvironment.ACTION_SUCK),
				searchForActions(ProblemFactory.getSimpleVacuumWorldProblem("A",
						new VELocalState("A", VacuumEnvironment.Status.Clean),
						new VELocalState("B", VacuumEnvironment.Status.Dirty)), 5));
	}

	@Test
	public void testReachableBinaryTreeGoals() {
		Assert.assertEquals(Arrays.asList((String) null),
				searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("A", "A"), 0));

		Assert.assertEquals(Arrays.asList("B"),
				searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("A", "B"), 1));

		Assert.assertEquals(Arrays.asList("C", "F", "L"),
				searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("A", "L"), 3));
	}

	@Test
	public void testUnreachableBinaryTreeGoalReturnsFailure() {
		// "B" is a child of "A" — searching from B cannot reach A in this tree.
		// With no path even at large depth, DLS should report failure (empty list), not cutoff.
		Assert.assertEquals(Collections.<String>emptyList(),
				searchForActions(ProblemFactory.getSimpleBinaryTreeProblem("B", "A"), 10));
	}
}
