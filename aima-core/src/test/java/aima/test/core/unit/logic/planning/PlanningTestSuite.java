package aima.test.core.unit.logic.planning;

import aima.test.core.unit.logic.planning.hierarchicalsearch.HierarchicalSearchTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ActionSchemaTest.class, GraphPlanAlgorithmTest.class, GraphTest.class, LevelTest.class,
PlanningProblemTest.class, StateTest.class, UtilsTest.class, HierarchicalSearchTest.class})

public class PlanningTestSuite {

}
