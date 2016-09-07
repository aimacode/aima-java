package aima.test.core.unit.nlp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.agent.impl.aprog.simplerule.RuleTest;
import aima.test.core.unit.nlp.parse.CYKParseTest;
import aima.test.core.unit.nlp.parse.GrammarTest;
import aima.test.core.unit.nlp.parse.LexiconTest;
import aima.test.core.unit.nlp.parse.ProbCNFGrammarTest;
import aima.test.core.unit.nlp.parse.ProbContextFreeGrammarTest;
import aima.test.core.unit.nlp.rank.HITSTest;
import aima.test.core.unit.nlp.rank.PagesDatasetTest;
import aima.test.core.unit.nlp.rank.WikiLinkFinderTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ HITSTest.class, PagesDatasetTest.class, WikiLinkFinderTest.class, CYKParseTest.class,
		GrammarTest.class, LexiconTest.class, ProbCNFGrammarTest.class, ProbContextFreeGrammarTest.class,
		RuleTest.class })
public class NLPTestSuite {
}
