package aima.test.core.unit.logic.propositional;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.logic.propositional.algorithms.DPLLTest;
import aima.test.core.unit.logic.propositional.algorithms.KnowledgeBaseTest;
import aima.test.core.unit.logic.propositional.algorithms.ModelTest;
import aima.test.core.unit.logic.propositional.algorithms.PLFCEntailsTest;
import aima.test.core.unit.logic.propositional.algorithms.PLResolutionTest;
import aima.test.core.unit.logic.propositional.algorithms.TTEntailsTest;
import aima.test.core.unit.logic.propositional.parsing.ComplexSentenceTest;
import aima.test.core.unit.logic.propositional.parsing.ListTest;
import aima.test.core.unit.logic.propositional.parsing.PELexerTest;
import aima.test.core.unit.logic.propositional.parsing.PEParserTest;
import aima.test.core.unit.logic.propositional.parsing.PropositionSymbolTest;
import aima.test.core.unit.logic.propositional.visitors.CNFClauseGathererTest;
import aima.test.core.unit.logic.propositional.visitors.CNFTransformerTest;
import aima.test.core.unit.logic.propositional.visitors.SymbolClassifierTest;
import aima.test.core.unit.logic.propositional.visitors.SymbolCollectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ DPLLTest.class, KnowledgeBaseTest.class, ModelTest.class,
		PLFCEntailsTest.class, PLResolutionTest.class, TTEntailsTest.class,
		ComplexSentenceTest.class, ListTest.class, PELexerTest.class, PEParserTest.class, PropositionSymbolTest.class,
		CNFClauseGathererTest.class, CNFTransformerTest.class,
		SymbolClassifierTest.class, SymbolCollectorTest.class })
public class PropositionalTestSuite {

}
