/*
 * Created on Sep 12, 2004
 *
 */
package aima.search.demos;

import java.util.Random;

import aima.search.eightpuzzle.EightPuzzleBoard;

/**
 * @author Ravi Mohan
 *  
 */
public class GenerateRandomEightPuzzleBoard {
	public static void main(String[] args) {
		Random r = new Random();
		EightPuzzleBoard board = new EightPuzzleBoard(new int[] { 0, 1, 2, 3,
				4, 5, 6, 7, 8 });
		for (int i = 0; i < 50; i++) {
			int th = r.nextInt(4);
			if (th == 0) {
				board.moveGapUp();
			}
			if (th == 1) {
				board.moveGapDown();
			}
			if (th == 2) {
				board.moveGapLeft();
			}
			if (th == 3) {
				board.moveGapRight();
			}
		}
		System.out.println(board);
	}

}