package aima.core.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.ParseTreeNode;
import aima.core.logic.common.Parser;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.Connective;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */
public class PEParser extends Parser {

	public PEParser() {
		lookAheadBuffer = new Token[lookAhead];
	}

	@Override
	public ParseTreeNode parse(String inputString) {
		lexer = new PELexer(inputString);
		fillLookAheadBuffer();
		return parseSentence(0);
	}
	
	private Sentence parseSentence(int level) {
		List<Object> levelTokens = parseLevel(level);
		
		Sentence result = null;

// TODO - use precedence rules if necessary to construct
// sentence from tokens
		
		return result;
	}

	private List<Object> parseLevel(int level) {
		List<Object> tokens = new ArrayList<Object>();
		while (lookAhead(1).getType() != LogicTokenTypes.EOI && lookAhead(1).getType() != LogicTokenTypes.RPAREN) {
			if (lookAhead(1).getType() == LogicTokenTypes.CONNECTIVE) {
				tokens.add(Connective.get(lookAhead(1).getText()));
			}
			else if () {
				
			}
		}
		
		if (detectAtomicSentence()) {
			
		} else if (detectBracket()) {
			return parseBracketedSentence();
		} else if (detectNOT()) {
			return parseNotSentence();
		} else {

			throw new RuntimeException("Parser Error Token = " + lookAhead(1));
		}
	}
	
	private TrueSentence parseTrue() {
		consume();
		return new TrueSentence();
	}

	private FalseSentence parseFalse() {
		consume();
		return new FalseSentence();
	}

	private Symbol parseSymbol() {
		String sym = lookAhead(1).getText();
		consume();
		return new Symbol(sym);
	}

	private AtomicSentence parseAtomicSentence() {
		Token t = lookAhead(1);
		if (t.getType() == LogicTokenTypes.TRUE) {
			return parseTrue();
		} else if (t.getType() == LogicTokenTypes.FALSE) {
			return parseFalse();
		} else if (t.getType() == LogicTokenTypes.SYMBOL) {
			return parseSymbol();
		} else {
			throw new RuntimeException(
					"Error in parseAtomicSentence with Token " + lookAhead(1));
		}
	}

	private UnarySentence parseNotSentence() {
		match(Connective.NOT.getSymbol());
		Sentence sen = parseSentence();
		return new UnarySentence(Connective.NOT, sen);
	}

	private boolean detectNOT() {
		return (lookAhead(1).getType() == LogicTokenTypes.CONNECTIVE)
				&& (lookAhead(1).getText().equals(Connective.NOT.getSymbol()));
	}
	
	private boolean detectBinaryConnective() {
		return (lookAhead(1).getType() == LogicTokenTypes.CONNECTIVE)
				&& !(lookAhead(1).getText().equals(Connective.NOT.getSymbol()));
	}

	private Sentence parseBracketedSentence() {
		match("(");
		Sentence one = parseSentence();
		if (lookAhead(1).getType() == LogicTokenTypes.RPAREN) {
			match(")");
			return one;
		}
		
		throw new RuntimeException(
				" Runtime Exception at Bracketed Expression with token "
						+ lookAhead(1));
	}

	private boolean detectBracket() {
		return lookAhead(1).getType() == LogicTokenTypes.LPAREN;
	}

	private boolean detectAtomicSentence() {
		int type = lookAhead(1).getType();
		return (type == LogicTokenTypes.TRUE)
				|| (type == LogicTokenTypes.FALSE)
				|| (type == LogicTokenTypes.SYMBOL);
	}
}