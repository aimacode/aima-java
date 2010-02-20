package aima.test.core.unit.logic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.logic.fol.FOLTestSuite;
import aima.test.core.unit.logic.propositional.PropositionalTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { FOLTestSuite.class, PropositionalTestSuite.class })
public class LogicTestSuite {

}
