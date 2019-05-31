package aima.core.environment.nqueens;

import aima.core.search.framework.Node;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.util.datastructure.XYLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Provides useful functions for two versions of the n-queens problem. The
 * incremental formulation and the complete-state formulation share the same
 * RESULT function but use different ACTIONS functions.
 *
 * @author Ruediger Lunde
 */
public class NQueensFunctions {

    public static Problem<NQueensBoard, QueenAction> createIncrementalFormulationProblem(int boardSize) {
        return new GeneralProblem<>(new NQueensBoard(boardSize), NQueensFunctions::getIFActions,
                NQueensFunctions::getResult, NQueensFunctions::testGoal);
    }

    public static Problem<NQueensBoard, QueenAction> createCompleteStateFormulationProblem
            (int boardSize, NQueensBoard.Config config) {
        return new GeneralProblem<>(new NQueensBoard(boardSize, config), NQueensFunctions::getCSFActions,
                NQueensFunctions::getResult, NQueensFunctions::testGoal);
    }

    /**
     * Implements an ACTIONS function for the incremental formulation of the
     * n-queens problem.
     * <p>
     * Assumes that queens are placed column by column, starting with an empty
     * board, and provides queen placing actions for all non-attacked positions
     * of the first free column.
     */
    public static List<QueenAction> getIFActions(NQueensBoard state) {
        List<QueenAction> actions = new ArrayList<>();

        int numQueens = state.getNumberOfQueensOnBoard();
        int boardSize = state.getSize();
        for (int i = 0; i < boardSize; i++) {
            XYLocation newLocation = new XYLocation(numQueens, i);
            if (!(state.isSquareUnderAttack(newLocation)))
                actions.add(new QueenAction(QueenAction.PLACE_QUEEN, newLocation));
        }
        return actions;
    }

    /**
     * Implements an ACTIONS function for the complete-state formulation of the
     * n-queens problem.
     * <p>
     * Assumes exactly one queen in each column and provides all possible queen
     * movements in vertical direction as actions.
     */
    public static List<QueenAction> getCSFActions(NQueensBoard state) {
        List<QueenAction> actions = new ArrayList<>();
        for (int i = 0; i < state.getSize(); i++)
            for (int j = 0; j < state.getSize(); j++) {
                XYLocation loc = new XYLocation(i, j);
                if (!state.queenExistsAt(loc))
                    actions.add(new QueenAction(QueenAction.MOVE_QUEEN, loc));
            }
        return actions;
    }

    /**
     * Implements a RESULT function for the n-queens problem.
     * Supports queen placing, queen removal, and queen movement actions.
     */
    public static NQueensBoard getResult(NQueensBoard state, QueenAction action) {
        NQueensBoard result = new NQueensBoard(state.getSize());
        result.setQueensAt(state.getQueenPositions());
        if (Objects.equals(action.getName(), QueenAction.PLACE_QUEEN))
            result.addQueenAt(action.getLocation());
        else if (Objects.equals(action.getName(), QueenAction.REMOVE_QUEEN))
            result.removeQueenFrom(action.getLocation());
        else if (Objects.equals(action.getName(), QueenAction.MOVE_QUEEN))
            result.moveQueenTo(action.getLocation());
        // if action is not understood or is a NoOp
        // the result will be the current state.
        return result;
    }

    /**
     * Implements a GOAL-TEST for the n-queens problem.
     */
    public static boolean testGoal(NQueensBoard state) {
        return state.getNumberOfQueensOnBoard() == state.getSize() && state.getNumberOfAttackingPairs() == 0;
    }

    /**
     * Estimates the distance to goal by the number of attacking pairs of queens on
     * the board.
     */
    public static double getNumberOfAttackingPairs(Node<NQueensBoard, QueenAction> node) {
        return node.getState().getNumberOfAttackingPairs();
    }
}
