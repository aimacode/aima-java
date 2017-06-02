package aima.test.core.unit.environment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.environment.cellworld.CellWorldTest;
import aima.test.core.unit.environment.eightpuzzle.EightPuzzleBoardMoveTest;
import aima.test.core.unit.environment.eightpuzzle.EightPuzzleBoardTest;
import aima.test.core.unit.environment.eightpuzzle.EightPuzzleFunctionsTest;
import aima.test.core.unit.environment.eightpuzzle.MisplacedTileHeuristicFunctionTest;
import aima.test.core.unit.environment.map.MapAgentTest;
import aima.test.core.unit.environment.map.MapEnvironmentTest;
import aima.test.core.unit.environment.map.MapFunctionsTest;
import aima.test.core.unit.environment.map.MapTest;
import aima.test.core.unit.environment.nqueens.NQueensBoardTest;
import aima.test.core.unit.environment.nqueens.NQueensGenAlgoUtilTest;
import aima.test.core.unit.environment.nqueens.NQueensFunctionsTest;
import aima.test.core.unit.environment.tictactoe.TicTacToeTest;
import aima.test.core.unit.environment.vacuum.ModelBasedReflexVacuumAgentTest;
import aima.test.core.unit.environment.vacuum.ReflexVacuumAgentTest;
import aima.test.core.unit.environment.vacuum.SimpleReflexVacuumAgentTest;
import aima.test.core.unit.environment.vacuum.TableDrivenVacuumAgentTest;
import aima.test.core.unit.environment.vacuum.VacuumEnvironmentTest;
import aima.test.core.unit.environment.wumpusworld.HybridWumpusAgentTest;
import aima.test.core.unit.environment.wumpusworld.WumpusFunctionsTest;
import aima.test.core.unit.environment.wumpusworld.WumpusKnowledgeBaseTest;
import aima.test.core.unit.environment.xyenv.XYEnvironmentTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CellWorldTest.class, EightPuzzleBoardMoveTest.class,
		EightPuzzleBoardTest.class, EightPuzzleFunctionsTest.class,
		MisplacedTileHeuristicFunctionTest.class, TicTacToeTest.class,
		MapAgentTest.class, MapEnvironmentTest.class,
		MapFunctionsTest.class, MapTest.class,
		NQueensBoardTest.class, NQueensGenAlgoUtilTest.class,
		NQueensFunctionsTest.class,
		ModelBasedReflexVacuumAgentTest.class, ReflexVacuumAgentTest.class,
		SimpleReflexVacuumAgentTest.class, TableDrivenVacuumAgentTest.class,
		VacuumEnvironmentTest.class, HybridWumpusAgentTest.class, 
		WumpusFunctionsTest.class,
		WumpusKnowledgeBaseTest.class, XYEnvironmentTest.class })
public class EnvironmentTestSuite {

}
