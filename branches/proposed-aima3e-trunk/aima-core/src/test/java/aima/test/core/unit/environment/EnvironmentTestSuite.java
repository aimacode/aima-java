package aima.test.core.unit.environment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.environment.cellworld.CellWorldTest;
import aima.test.core.unit.environment.eightpuzzle.EightPuzzleBoardMoveTest;
import aima.test.core.unit.environment.eightpuzzle.EightPuzzleBoardTest;
import aima.test.core.unit.environment.eightpuzzle.EightPuzzleFunctionFactoryTest;
import aima.test.core.unit.environment.eightpuzzle.MisplacedTileHeuristicFunctionTest;
import aima.test.core.unit.environment.map.MapAgentTest;
import aima.test.core.unit.environment.map.MapEnvironmentTest;
import aima.test.core.unit.environment.map.MapFunctionFactoryTest;
import aima.test.core.unit.environment.map.MapStepCostFunctionTest;
import aima.test.core.unit.environment.map.MapTest;
import aima.test.core.unit.environment.nqueens.NQueensBoardTest;
import aima.test.core.unit.environment.nqueens.NQueensFitnessFunctionTest;
import aima.test.core.unit.environment.nqueens.NQueensFunctionFactoryTest;
import aima.test.core.unit.environment.nqueens.NQueensGoalTestTest;
import aima.test.core.unit.environment.tictactoe.TicTacToeTest;
import aima.test.core.unit.environment.vacuum.ModelBasedReflexVacuumAgentTest;
import aima.test.core.unit.environment.vacuum.ReflexVacuumAgentTest;
import aima.test.core.unit.environment.vacuum.SimpleReflexVacuumAgentTest;
import aima.test.core.unit.environment.vacuum.TableDrivenVacuumAgentTest;
import aima.test.core.unit.environment.vacuum.VacuumEnvironmentTest;
import aima.test.core.unit.environment.xyenv.XYEnvironmentTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { CellWorldTest.class, EightPuzzleBoardMoveTest.class,
		EightPuzzleBoardTest.class, EightPuzzleFunctionFactoryTest.class,
		MisplacedTileHeuristicFunctionTest.class, TicTacToeTest.class,
		MapAgentTest.class, MapEnvironmentTest.class,
		MapStepCostFunctionTest.class, MapFunctionFactoryTest.class,
		MapTest.class, NQueensBoardTest.class,
		NQueensFitnessFunctionTest.class, NQueensGoalTestTest.class,
		NQueensFunctionFactoryTest.class,
		ModelBasedReflexVacuumAgentTest.class, ReflexVacuumAgentTest.class,
		SimpleReflexVacuumAgentTest.class, TableDrivenVacuumAgentTest.class,
		VacuumEnvironmentTest.class, XYEnvironmentTest.class })
public class EnvironmentTestSuite {

}
