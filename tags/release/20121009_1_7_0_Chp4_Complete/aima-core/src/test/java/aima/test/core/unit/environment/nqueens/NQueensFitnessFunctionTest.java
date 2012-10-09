package aima.test.core.unit.environment.nqueens;

import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFitnessFunction;
import aima.core.search.local.Individual;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class NQueensFitnessFunctionTest {

	NQueensFitnessFunction fitnessFunction;

	@Before
	public void setUp() {
		fitnessFunction = new NQueensFitnessFunction();
	}

	@Test
	public void test_getValue() {
		Assert.assertTrue(0.0 == fitnessFunction
				.getValue(new Individual<Integer>(Arrays.asList(new Integer[] {
						0, 0, 0, 0, 0, 0, 0, 0 }))));
		Assert.assertTrue(0.0 == fitnessFunction
				.getValue(new Individual<Integer>(Arrays.asList(new Integer[] {
						0, 1, 2, 3, 4, 5, 6, 7 }))));
		Assert.assertTrue(0.0 == fitnessFunction
				.getValue(new Individual<Integer>(Arrays.asList(new Integer[] {
						7, 6, 5, 4, 3, 2, 1, 0 }))));

		Assert.assertTrue(23.0 == fitnessFunction
				.getValue(new Individual<Integer>(Arrays.asList(new Integer[] {
						5, 6, 1, 3, 6, 4, 7, 7 }))));
		Assert.assertTrue(28.0 == fitnessFunction
				.getValue(new Individual<Integer>(Arrays.asList(new Integer[] {
						0, 4, 7, 5, 2, 6, 1, 3 }))));
	}

	@Test
	public void test_isGoalState() {
		Assert.assertTrue(fitnessFunction.isGoalState(new Individual<Integer>(
				Arrays.asList(new Integer[] { 0, 4, 7, 5, 2, 6, 1, 3 }))));
		Assert.assertFalse(fitnessFunction.isGoalState(new Individual<Integer>(
				Arrays.asList(new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0 }))));
		Assert.assertFalse(fitnessFunction.isGoalState(new Individual<Integer>(
				Arrays.asList(new Integer[] { 5, 6, 1, 3, 6, 4, 7, 7 }))));
	}

	@Test
	public void test_getBoardForIndividual() {
		NQueensBoard board = fitnessFunction
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
			Individual<Integer> individual = fitnessFunction
					.generateRandomIndividual(i);
			Assert.assertEquals(i, individual.length());
		}
	}

	@Test
	public void test_getFiniteAlphabet() {
		for (int i = 2; i <= 40; i++) {
			Set<Integer> fab = fitnessFunction
					.getFiniteAlphabetForBoardOfSize(i);
			Assert.assertEquals(i, fab.size());
		}
	}
}
