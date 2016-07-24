package aima.test.core.unit.nlp.parse;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import aima.core.nlp.data.grammars.ProbCNFGrammarExamples;
import aima.core.nlp.parsing.CYK;
import aima.core.nlp.parsing.grammars.ProbCNFGrammar;

public class CYKParseTest {

	CYK parser;
	ArrayList<String> words1; ArrayList<String> words2;
	ProbCNFGrammar trivGrammar = ProbCNFGrammarExamples.buildTrivialGrammar();
	// Get Example Grammar 2
	
	@Before
	public void setUp() {
		parser = new CYK();
		words1 = new ArrayList<String>( Arrays.asList("the","man","liked","a","woman"));
		
	} // end setUp()
	
	@Test
	public void testParseReturn() {
		float[][][] probTable = null;
	    probTable = parser.parse( words1, trivGrammar );
		assertNotNull( probTable );
	}
	
	@Test
	public void testParse() {
		float[][][] probTable;
		probTable = parser.parse(words1, trivGrammar);
		assertTrue( probTable[5][0][4] > 0); // probTable[5,0,4] = [S][Start=0][Length=5] 
	}
}
