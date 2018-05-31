package aima.test.unit.environment.nqueens;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.util.datastructure.GridLocation2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author samagra
 */
public class NQueensBoardTest {

    private NQueensBoard board;

    @Before
    public void setup(){
        board = new NQueensBoard(8);
    }

    @Test
    public void testAddQueen(){
        Assert.assertNotNull(board);
        Assert.assertEquals(0,board.getNumberOfQueensOnBoard());
        board.addQueenAt(new GridLocation2D(0, 0));
        Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
        board.addQueenAt(new GridLocation2D(0, 0));
        Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
        board.addQueenAt(new GridLocation2D(1, 1));
        Assert.assertEquals(2, board.getNumberOfQueensOnBoard());
        Assert.assertTrue(board.queenExistsAt(new GridLocation2D(1, 1)));
        Assert.assertTrue(board.queenExistsAt(new GridLocation2D(0, 0)));
        board.removeQueenFrom(new GridLocation2D(0,0));
        Assert.assertFalse(board.queenExistsAt(new GridLocation2D(0,0)));
        Assert.assertEquals(1,board.getNumberOfQueensOnBoard());
    }

    @Test
    public void testRemoveQueen(){
        board.addQueenAt(new GridLocation2D(0, 0));
        Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
        board.removeQueenFrom(new GridLocation2D(0, 0));
        Assert.assertEquals(0, board.getNumberOfQueensOnBoard());
    }
    @Test
    public void testRemoveNonExistentQueen() {
        board.removeQueenFrom(new GridLocation2D(0, 0));
        Assert.assertEquals(0, board.getNumberOfQueensOnBoard());
    }

    @Test
    public void testMoveQueen(){
        GridLocation2D from = new GridLocation2D(0, 0);
        GridLocation2D to = new GridLocation2D(1, 1);

        board.addQueenAt(from);
        Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
        Assert.assertTrue(board.queenExistsAt(from));
        Assert.assertFalse(board.queenExistsAt(to));

        board.moveQueen(from, to);
        Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
        Assert.assertFalse(board.queenExistsAt(from));
        Assert.assertTrue(board.queenExistsAt(to));

        board.moveQueen(to,to);
        Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
        Assert.assertFalse(board.queenExistsAt(from));
        Assert.assertTrue(board.queenExistsAt(to));
    }

    @Test
    public void testPrint() {
        NQueensBoard board2 = new NQueensBoard(2);
        board2.addQueenAt(new GridLocation2D(0, 0));
        String expected = " Q  - \n -  - \n";
        Assert.assertEquals(expected, board2.getBoardPic());
    }

    @Test
    public void testQueensOnSameLocation(){
        board.addQueenAt(new GridLocation2D(0, 0));
        board.addQueenAt(new GridLocation2D(0, 0));
        Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
    }

    @Test
    public void testBoardPic(){
        board.addQueenAt(new GridLocation2D(0, 5));
        board.addQueenAt(new GridLocation2D(1, 6));
        board.addQueenAt(new GridLocation2D(2, 1));
        board.addQueenAt(new GridLocation2D(3, 3));
        board.addQueenAt(new GridLocation2D(4, 6));
        board.addQueenAt(new GridLocation2D(5, 4));
        board.addQueenAt(new GridLocation2D(6, 7));
        board.addQueenAt(new GridLocation2D(7, 7));
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
    public void testAttacks(){
        GridLocation2D loc1 = new GridLocation2D(3, 3);
        board.addQueenAt(loc1);
        Assert.assertEquals(1, board.getNumberOfAttacksOn(loc1.right()));

        board.addQueenAt(loc1.right().right());
        Assert.assertEquals(1, board.getNumberOfAttacksOn(loc1));
        Assert.assertEquals(2, board.getNumberOfAttacksOn(loc1.right()));

        board.addQueenAt(loc1.right().down());
        Assert.assertEquals(2, board.getNumberOfAttacksOn(loc1));
        Assert.assertEquals(3, board.getNumberOfAttacksOn(loc1.right()));
        Assert.assertEquals(2, board.getNumberOfAttacksOn(loc1.right().right()));
    }

    @Test
    public void testAttackingPairs(){
        GridLocation2D location = new GridLocation2D(3,3);
        board.addQueenAt(location);
        Assert.assertEquals(0,board.getNumberOfAttackingPairs());
        board.addQueenAt(location.left().left());
        Assert.assertEquals(1,board.getNumberOfAttackingPairs());
        board.addQueenAt(location.up().up());
        Assert.assertEquals(3,board.getNumberOfAttackingPairs());
        board.addQueenAt(location.up().right());
        Assert.assertEquals(5,board.getNumberOfAttackingPairs());
    }

}
