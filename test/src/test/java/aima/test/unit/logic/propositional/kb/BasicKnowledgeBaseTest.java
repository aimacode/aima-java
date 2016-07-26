package aima.test.unit.logic.propositional.kb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.propositional.kb.BasicKnowledgeBase;
import aima.extra.logic.propositional.parser.PLParserWrapper;

/**
 * @author Ravi Mohan
 * @author Anurag Rai
 */
public class BasicKnowledgeBaseTest {
	private BasicKnowledgeBase kb;

	@Before
	public void setUp() {
		kb = new BasicKnowledgeBase(new PLParserWrapper());
	}

	@Test
	public void testTellInsertsSentence() {
		kb.tell("(A & B)");
		Assert.assertEquals(1, kb.size());
	}

	@Test
	public void testTellDoesNotInsertSameSentenceTwice() {
		kb.tell("(A & B)");
		Assert.assertEquals(1, kb.size());
		kb.tell("(A & B)");
		Assert.assertEquals(1, kb.size());
	}

	@Test
	public void testEmptyKnowledgeBaseIsAnEmptyString() {
		Assert.assertEquals("", kb.toString());
	}

	@Test
	public void testKnowledgeBaseWithOneSentenceToString() {
		kb.tell("(A & B)");
		Assert.assertEquals("A & B", kb.toString());
	}

	@Test
	public void testKnowledgeBaseWithTwoSentencesToString() {
		kb.tell("(A & B)");
		kb.tell("(C & D)");
		Assert.assertEquals("A & B & C & D", kb.toString());
	}

	@Test
	public void testKnowledgeBaseWithThreeSentencesToString() {
		kb.tell("(A & B)");
		kb.tell("(C & D)");
		kb.tell("(E & F)");
		Assert.assertEquals(
				"A & B & C & D & E & F",
				kb.toString());
	}
	
	@Test
	public void testKnowledgeBaseWithNestedSentencesToString() {
		kb.tell("(A & B) & (C & D)");
		kb.tell("(C & D)");
		Assert.assertEquals("A & B & C & D & C & D", kb.toString());
	}
}
