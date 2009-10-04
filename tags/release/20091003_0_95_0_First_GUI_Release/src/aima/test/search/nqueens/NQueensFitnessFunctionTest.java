package aima.test.search.nqueens;

import java.util.Set;

import junit.framework.TestCase;

import aima.search.nqueens.NQueensBoard;
import aima.search.nqueens.NQueensFitnessFunction;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class NQueensFitnessFunctionTest extends TestCase {

	NQueensFitnessFunction fitnessFunction;

	@Override
	public void setUp() {
		fitnessFunction = new NQueensFitnessFunction();
	}

	public void test_getValue() {
		assertTrue(0.0 == fitnessFunction.getValue("00000000"));
		assertTrue(0.0 == fitnessFunction.getValue("01234567"));
		assertTrue(0.0 == fitnessFunction.getValue("76543210"));

		assertTrue(23.0 == fitnessFunction.getValue("56136477"));
		assertTrue(28.0 == fitnessFunction.getValue("04752613"));
	}

	public void test_isGoalState() {
		assertTrue(fitnessFunction.isGoalState("04752613"));
		assertFalse(fitnessFunction.isGoalState("00000000"));
		assertFalse(fitnessFunction.isGoalState("56136477"));
	}

	public void test_getBoardForIndividual() {
		NQueensBoard board = fitnessFunction.getBoardForIndividual("56136477");
		assertEquals(" -  -  -  -  -  -  -  - \n"
				+ " -  -  Q  -  -  -  -  - \n" + " -  -  -  -  -  -  -  - \n"
				+ " -  -  -  Q  -  -  -  - \n" + " -  -  -  -  -  Q  -  - \n"
				+ " Q  -  -  -  -  -  -  - \n" + " -  Q  -  -  Q  -  -  - \n"
				+ " -  -  -  -  -  -  Q  Q \n", board.getBoardPic());

		assertEquals("--------\n" + "--Q-----\n" + "--------\n" + "---Q----\n"
				+ "-----Q--\n" + "Q-------\n" + "-Q--Q---\n" + "------QQ\n",
				board.toString());
	}

	public void test_generateRandomIndividual() {
		for (int i = Character.MIN_RADIX; i <= Character.MAX_RADIX; i++) {
			String individual = fitnessFunction.generateRandomIndividual(i);
			assertEquals(i, individual.length());
		}
	}

	public void test_getFiniteAlphabet() {
		for (int i = Character.MIN_RADIX; i <= Character.MAX_RADIX; i++) {
			Set<Character> fab = fitnessFunction
					.getFiniteAlphabetForBoardOfSize(i);
			assertEquals(i, fab.size());
		}
	}
}
