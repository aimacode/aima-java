package aima.test.core.unit.logic.propositional;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.logic.propositional.inference.PLResolutionTest;
import aima.test.core.unit.logic.propositional.inference.TTEntailsTest;
import aima.test.core.unit.logic.propositional.kb.BasicKnowledgeBaseTest;
import aima.test.core.unit.logic.propositional.kb.data.ClauseTest;
import aima.test.core.unit.logic.propositional.kb.data.ConvertToConjunctionOfClausesTest;
import aima.test.core.unit.logic.propositional.kb.data.LiteralTest;
import aima.test.core.unit.logic.propositional.kb.data.ModelTest;
import aima.test.core.unit.logic.propositional.parsing.ComplexSentenceTest;
import aima.test.core.unit.logic.propositional.parsing.PLLexerTest;
import aima.test.core.unit.logic.propositional.parsing.PLParserTest;
import aima.test.core.unit.logic.propositional.parsing.PropositionSymbolTest;
import aima.test.core.unit.logic.propositional.visitors.ConvertToCNFTest;
import aima.test.core.unit.logic.propositional.visitors.SymbolCollectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BasicKnowledgeBaseTest.class, ModelTest.class,
		ComplexSentenceTest.class, PLLexerTest.class,
		PLParserTest.class, PropositionSymbolTest.class,
		ConvertToCNFTest.class, ClauseTest.class,
		ConvertToConjunctionOfClausesTest.class, LiteralTest.class,
		SymbolCollectorTest.class, PLResolutionTest.class, TTEntailsTest.class })

public class PropositionalTestSuite {
}
