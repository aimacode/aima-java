package aima.test.core.unit.nlp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import aima.test.core.unit.nlp.rank.HITSTest;
import aima.test.core.unit.nlp.rank.PagesDatasetTest;
import aima.test.core.unit.nlp.rank.WikiLinkFinderTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { HITSTest.class, PagesDatasetTest.class, WikiLinkFinderTest.class } )
public class NLPTestSuite {
}
