package aima.test.unit.antlr.propositional;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.extra.antlr.logic.prop.PropositionalVisitor;
import aima.extra.logic.propositional.parser.PropositionalLogicLexer;
import aima.extra.logic.propositional.parser.PropositionalLogicParser;

/*
 * @author Anurag Rai
 */
public class VisitorTest {

	@Before
	public void setUp() {
		
	}

	@Test
	public void testSuccessors() {
		String testString ="filter1 & filter2 & filter3 & filter4";
		ANTLRInputStream input = new ANTLRInputStream(testString);
		PropositionalLogicLexer lexer = new PropositionalLogicLexer(input);
		TokenStream tokens = new CommonTokenStream(lexer);
		PropositionalLogicParser parser = new PropositionalLogicParser(tokens);
		
		ParseTree tree = parser.sentence();
		
		Sentence producedSentence = new PropositionalVisitor().visit(tree);
		
		Assert.assertEquals(testString, producedSentence.toString());
	}
}
