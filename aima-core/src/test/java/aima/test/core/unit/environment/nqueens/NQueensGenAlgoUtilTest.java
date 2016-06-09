package aima.test.core.unit.environment.nqueens;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensGenAlgoUtil;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class NQueensGenAlgoUtilTest {

	FitnessFunction<Integer> fitnessFunction;
	GoalTest goalTest;

	@Before
	public void setUp() {
		fitnessFunction = NQueensGenAlgoUtil.getFitnessFunction();
		goalTest = NQueensGenAlgoUtil.getGoalTest();
	}

	@Test
	public void test_getValue() {
		Assert.assertTrue(0.0 == fitnessFunction
				.apply(new Individual<Integer>(Arrays.asList(new Integer[] {
						0, 0, 0, 0, 0, 0, 0, 0 }))));
		Assert.assertTrue(0.0 == fitnessFunction
				.apply(new Individual<Integer>(Arrays.asList(new Integer[] {
						0, 1, 2, 3, 4, 5, 6, 7 }))));
		Assert.assertTrue(0.0 == fitnessFunction
				.apply(new Individual<Integer>(Arrays.asList(new Integer[] {
						7, 6, 5, 4, 3, 2, 1, 0 }))));

		Assert.assertTrue(23.0 == fitnessFunction
				.apply(new Individual<Integer>(Arrays.asList(new Integer[] {
						5, 6, 1, 3, 6, 4, 7, 7 }))));
		Assert.assertTrue(28.0 == fitnessFunction
				.apply(new Individual<Integer>(Arrays.asList(new Integer[] {
						0, 4, 7, 5, 2, 6, 1, 3 }))));
	}

	@Test
	public void test_isGoalState() {
		Assert.assertTrue(goalTest.isGoalState(new Individual<Integer>(
				Arrays.asList(new Integer[] { 0, 4, 7, 5, 2, 6, 1, 3 }))));
		Assert.assertFalse(goalTest.isGoalState(new Individual<Integer>(
				Arrays.asList(new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0 }))));
		Assert.assertFalse(goalTest.isGoalState(new Individual<Integer>(
				Arrays.asList(new Integer[] { 5, 6, 1, 3, 6, 4, 7, 7 }))));
	}

	@Test
	public void test_getBoardForIndividual() {
		NQueensBoard board = NQueensGenAlgoUtil
				.getBoardForIndividual(new Individual<Integer>(Arrays
						.asList(new Integer[] { 5, 6, 1, 3, 6, 4, 7, 7 })));
		Assert.assertEquals(" -  -  -  -  -  -  -  - \n"
				+ " -  -  Q  -  -  -  -  - \n" + " -  -  -  -  -  -  -  - \n"
				+ " -  -  -  Q  -  -  -  - \n" + " -  -  -  -  -  Q  -  - \n"
				+ " Q  -  -  -  -  -  -  - \n" + " -  Q  -  -  Q  -  -  - \n"
				+ " -  -  -  -  -  -  Q  Q \n", board.getBoardPic());

		Assert.assertEquals("--------\n" + "--Q-----\n" + "--------\n"
				+ "---Q----\n" + "-----Q--\n" + "Q-------\n" + "-Q--Q---\n"
				+ "------QQ\n", board.toString());
	}

	@Test
	public void test_generateRandomIndividual() {
		for (int i = 2; i <= 40; i++) {
			Individual<Integer> individual = NQueensGenAlgoUtil
					.generateRandomIndividual(i);
			Assert.assertEquals(i, individual.length());
		}
	}

	@Test
	public void test_getFiniteAlphabet() {
		for (int i = 2; i <= 40; i++) {
			Collection<Integer> fab = NQueensGenAlgoUtil
					.getFiniteAlphabetForBoardOfSize(i);
			Assert.assertEquals(i, fab.size());
		}
	}
}
