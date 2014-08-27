package aima.test.core.unit.logic.propositional;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.logic.propositional.inference.DPLLTest;
import aima.test.core.unit.logic.propositional.inference.PLFCEntailsTest;
import aima.test.core.unit.logic.propositional.inference.PLResolutionTest;
import aima.test.core.unit.logic.propositional.inference.TTEntailsTest;
import aima.test.core.unit.logic.propositional.kb.KnowledgeBaseTest;
import aima.test.core.unit.logic.propositional.kb.data.ClauseTest;
import aima.test.core.unit.logic.propositional.kb.data.ConvertToConjunctionOfClausesTest;
import aima.test.core.unit.logic.propositional.kb.data.LiteralTest;
import aima.test.core.unit.logic.propositional.kb.data.ModelTest;
import aima.test.core.unit.logic.propositional.parsing.ComplexSentenceTest;
import aima.test.core.unit.logic.propositional.parsing.ListTest;
import aima.test.core.unit.logic.propositional.parsing.PLLexerTest;
import aima.test.core.unit.logic.propositional.parsing.PLParserTest;
import aima.test.core.unit.logic.propositional.parsing.PropositionSymbolTest;
import aima.test.core.unit.logic.propositional.visitors.ConvertToCNFTest;
import aima.test.core.unit.logic.propositional.visitors.SymbolCollectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ DPLLTest.class, KnowledgeBaseTest.class, ModelTest.class,
		PLFCEntailsTest.class, PLResolutionTest.class, TTEntailsTest.class,
		ComplexSentenceTest.class, ListTest.class, PLLexerTest.class,
		PLParserTest.class, PropositionSymbolTest.class,
		ConvertToCNFTest.class, ClauseTest.class,
		ConvertToConjunctionOfClausesTest.class, LiteralTest.class,
		SymbolCollectorTest.class })
public class PropositionalTestSuite {

}
