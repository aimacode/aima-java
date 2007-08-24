/* Created on Sep 18, 2004
 *
 */
package aima.test.logictest.foltest;

import junit.framework.TestCase;
import aima.logic.common.LogicTokenTypes;
import aima.logic.common.Token;
import aima.logic.fol.FOLDomain;
import aima.logic.fol.parsing.FOLLexer;

/**
 * @author Ravi Mohan
 * 
 */

public class FOLLexerTest extends TestCase {
	FOLLexer lexer;

	@Override
	public void setUp() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("P");
		domain.addConstant("John");
		domain.addConstant("Saladin");
		domain.addFunction("LeftLeg");
		domain.addFunction("BrotherOf");
		domain.addFunction("EnemyOf");
		domain.addPredicate("HasColor");
		domain.addPredicate("King");
		lexer = new FOLLexer(domain);
	}

	public void testLexBasicExpression() {
		lexer.setInput("( P )");
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.CONSTANT, "P"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer.nextToken());
	}

	public void testConnectors() {
		lexer.setInput(" p  AND q");
		assertEquals(new Token(LogicTokenTypes.VARIABLE, "p"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.CONNECTOR, "AND"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.VARIABLE, "q"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer.nextToken());
	}

	public void testFunctions() {
		lexer.setInput(" LeftLeg(q)");
		assertEquals(new Token(LogicTokenTypes.FUNCTION, "LeftLeg"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.VARIABLE, "q"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer.nextToken());
	}

	public void testPredicate() {
		lexer.setInput(" HasColor(r)");
		assertEquals(new Token(LogicTokenTypes.PREDICATE, "HasColor"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.VARIABLE, "r"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer.nextToken());
	}

	public void testMultiArgPredicate() {
		lexer.setInput(" King(x,y)");
		assertEquals(new Token(LogicTokenTypes.PREDICATE, "King"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.VARIABLE, "x"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.COMMA, ","), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.VARIABLE, "y"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer.nextToken());
		// assertEquals(new Token(LogicTokenTypes.EOI, "EOI"),
		// lexer.nextToken());
	}

	public void testQuantifier() {
		lexer.setInput("FORALL x,y");
		assertEquals(new Token(LogicTokenTypes.QUANTIFIER, "FORALL"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.VARIABLE, "x"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.COMMA, ","), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.VARIABLE, "y"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer.nextToken());
	}

	public void testTermEquality() {
		lexer.setInput("BrotherOf(John) = EnemyOf(Saladin)");
		assertEquals(new Token(LogicTokenTypes.FUNCTION, "BrotherOf"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.CONSTANT, "John"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.EQUALS, "="), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.FUNCTION, "EnemyOf"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer.nextToken());
		assertEquals(new Token(LogicTokenTypes.CONSTANT, "Saladin"), lexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer.nextToken());
		// assertEquals(new Token(LogicTokenTypes.COMMA, ","),
		// lexer.nextToken());
		// assertEquals(new Token(LogicTokenTypes.VARIABLE, "y"),
		// lexer.nextToken());
		// assertEquals(new Token(LogicTokenTypes.EOI, "EOI"),
		// lexer.nextToken());
	}
}
