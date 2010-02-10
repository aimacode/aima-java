package aima.test.core.unit.logic.fol;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.logic.fol.inference.DemodulationTest;
import aima.test.core.unit.logic.fol.inference.FOLBCAskTest;
import aima.test.core.unit.logic.fol.inference.FOLFCAskTest;
import aima.test.core.unit.logic.fol.inference.FOLModelEliminationTest;
import aima.test.core.unit.logic.fol.inference.FOLOTTERLikeTheoremProverTest;
import aima.test.core.unit.logic.fol.inference.FOLTFMResolutionTest;
import aima.test.core.unit.logic.fol.inference.ParamodulationTest;
import aima.test.core.unit.logic.fol.kb.FOLKnowledgeBaseTest;
import aima.test.core.unit.logic.fol.kb.data.ChainTest;
import aima.test.core.unit.logic.fol.kb.data.ClauseTest;
import aima.test.core.unit.logic.fol.parsing.FOLLexerTest;
import aima.test.core.unit.logic.fol.parsing.FOLParserTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { DemodulationTest.class, FOLBCAskTest.class,
		FOLFCAskTest.class, FOLModelEliminationTest.class,
		FOLOTTERLikeTheoremProverTest.class, FOLTFMResolutionTest.class,
		ParamodulationTest.class, ChainTest.class, ClauseTest.class,
		FOLKnowledgeBaseTest.class, FOLLexerTest.class, FOLParserTest.class,
		CNFConverterTest.class, PredicateCollectorTest.class,
		SubstVisitorTest.class, SubsumptionEliminationTest.class,
		UnifierTest.class, VariableCollectorTest.class })
public class FOLTestSuite {

}
