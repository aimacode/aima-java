package aima.core.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.ParseTreeNode;
import aima.core.logic.common.Parser;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
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
		return parseSentence();
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
		match("NOT");
		Sentence sen = parseSentence();
		return new UnarySentence(sen);
	}

	private MultiSentence parseMultiSentence() {
		consume();
		String connector = lookAhead(1).getText();
		consume();
		List<Sentence> sentences = new ArrayList<Sentence>();
		while (lookAhead(1).getType() != LogicTokenTypes.RPAREN) {
			Sentence sen = parseSentence();
			// consume();
			sentences.add(sen);
		}
		match(")");
		return new MultiSentence(connector, sentences);
	}

	private Sentence parseSentence() {
		if (detectAtomicSentence()) {
			return parseAtomicSentence();
		} else if (detectBracket()) {
			return parseBracketedSentence();
		} else if (detectNOT()) {
			return parseNotSentence();
		} else {

			throw new RuntimeException("Parser Error Token = " + lookAhead(1));
		}
	}

	private boolean detectNOT() {
		return (lookAhead(1).getType() == LogicTokenTypes.CONNECTOR)
				&& (lookAhead(1).getText().equals("NOT"));
	}

	private Sentence parseBracketedSentence() {

		if (detectMultiOperator()) {
			return parseMultiSentence();
		} else {
			match("(");
			Sentence one = parseSentence();
			if (lookAhead(1).getType() == LogicTokenTypes.RPAREN) {
				match(")");
				return one;
			} else if ((lookAhead(1).getType() == LogicTokenTypes.CONNECTOR)
					&& (!(lookAhead(1).getText().equals("Not")))) {
				String connector = lookAhead(1).getText();
				consume(); // connector
				Sentence two = parseSentence();
				match(")");
				return new BinarySentence(connector, one, two);
			}

		}
		throw new RuntimeException(
				" Runtime Exception at Bracketed Expression with token "
						+ lookAhead(1));
	}

	private boolean detectMultiOperator() {
		return (lookAhead(1).getType() == LogicTokenTypes.LPAREN)
				&& ((lookAhead(2).getText().equals("AND")) || (lookAhead(2)
						.getText().equals("OR")));
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