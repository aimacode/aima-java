package aima.core.learning.reinforcement.example;

import java.util.List;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorldAction;

/**
 * Interface for determining the possible outcomes from performing an action
 * within a cell.
 * 
 * @param <C>
 *            the content type of the cell.
 * 
 * @author Ciaran O'Reilly
 */
public interface CellWorldPossibleOutcomesFunction<C> {

	/**
	 * A list of possible outcomes from performing a specified action within a
	 * cell.
	 * 
	 * @param c
	 *            the current cell the action is performed.
	 * @param a
	 *            the action performed within the cell.
	 * @return a collection of cells that may be reached by applying the action.
	 *         This would be > 1 if the world is non-deterministic (duplicates
	 *         may exist).
	 */
	List<Cell<C>> possibleOutcomes(Cell<C> c, CellWorldAction a);
}