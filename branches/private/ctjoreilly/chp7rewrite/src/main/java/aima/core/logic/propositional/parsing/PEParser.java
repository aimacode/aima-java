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

		levelTokens = constructSentencesForConnective(Connective.NOT,           levelTokens);
		levelTokens = constructSentencesForConnective(Connective.AND,           levelTokens);
		levelTokens = constructSentencesForConnective(Connective.OR,            levelTokens);
		levelTokens = constructSentencesForConnective(Connective.IMPLICATION,   levelTokens);
		levelTokens = constructSentencesForConnective(Connective.BICONDITIONAL, levelTokens);
		
		// At the end there should just be the root formula
		// for this level.
		if (levelTokens.size() == 1 && levelTokens.get(0) instanceof Sentence) {
			result = (Sentence) levelTokens.get(0);
		}
		
		return result;
	}
	
	private List<Object> constructSentencesForConnective(Connective connectiveToConstruct, List<Object> tokens) {
		List<Object> newTokens = new ArrayList<Object>();
		int numSentencesMade = 0;
		for (int i = 0; i < tokens.size(); i++) {
			Object token = tokens.get(i);
			if (token instanceof Connective) {
				Connective tokenConnective = (Connective) token;
				if (tokenConnective == Connective.NOT) {
					// A unary connective
					if (i+1 < tokens.size() && tokens.get(i+1) instanceof Sentence) {
						if (tokenConnective == connectiveToConstruct) {
							UnarySentence newSentence = new UnarySentence(connectiveToConstruct, (Sentence) tokens.get(i+1));
							tokens.set(i,   null);
							tokens.set(i+1, newSentence);
							numSentencesMade++;
						}
					}
					else {
						throw new RuntimeException("Unary connective argurment is not a sentence");
					}
				}
				else {
					// A Binary connective
					if ((i-1 >= 0 && tokens.get(i-1) instanceof Sentence) && (i+1 < tokens.size() && tokens.get(i+1) instanceof Sentence)) {
						// A binary connective
						if (tokenConnective == connectiveToConstruct) {
							BinarySentence newSentence = new BinarySentence(connectiveToConstruct,
																(Sentence)tokens.get(i-1), (Sentence)tokens.get(i+1));
							tokens.set(i-1, null);
							tokens.set(i,   null);
							tokens.set(i+1, newSentence);
							numSentencesMade++;
						}
					}
					else {
						throw new RuntimeException("Binary connective argurments are not sentences");
					}
				}
				// Can skip forward one
				i++;
			}
		}
		
		for (int i = 0; i < tokens.size(); i++) {
			Object token = tokens.get(i);
			if (token != null) {
				newTokens.add(token);
			}
		}
		
		// Ensure no tokens left unaccounted for in this pass.
		int toSubtract = 0;
		if (connectiveToConstruct == Connective.NOT) {
			toSubtract = (numSentencesMade*2)-numSentencesMade;
		}
		else {
			toSubtract = (numSentencesMade*3)-numSentencesMade;
		}
		
		if (tokens.size()-toSubtract != newTokens.size()) {
			throw new RuntimeException("Unable to construct sentence for connective: "+connectiveToConstruct+" from: "+tokens);
		}
		
		return newTokens;
	}

	private List<Object> parseLevel(int level) {
		List<Object> tokens = new ArrayList<Object>();
		while (lookAhead(1).getType() != LogicTokenTypes.EOI && lookAhead(1).getType() != LogicTokenTypes.RPAREN) {
			if (detectConnective()) {
				tokens.add(parseConnective());
			} else if (detectAtomicSentence()) {
				tokens.add(parseAtomicSentence());
			} else if (detectBracket()) {
				tokens.add(parseBracketedSentence(level));
			}
		}
		
		if (level > 0 && lookAhead(1).getType() == LogicTokenTypes.EOI) {
			throw new RuntimeException("Parser Error end of input not expected at level " + level);
		}
		
		return tokens;
	}
	
	private boolean detectConnective() {
		return lookAhead(1).getType() == LogicTokenTypes.CONNECTIVE;
	}
	
	private Connective parseConnective() {
		Connective connective = Connective.get(lookAhead(1).getText());
		consume();
		return connective;
	}
	
	private boolean detectAtomicSentence() {
		int type = lookAhead(1).getType();
		return type == LogicTokenTypes.TRUE || type == LogicTokenTypes.FALSE || type == LogicTokenTypes.SYMBOL;
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

	private boolean detectBracket() {
		return lookAhead(1).getType() == LogicTokenTypes.LPAREN;
	}
	
	private Sentence parseBracketedSentence(int level) {
		match("(");
		Sentence bracketedSentence = parseSentence(level+1);
		if (bracketedSentence == null) {
			throw new RuntimeException("Empty bracket contents detected.");
		}
		match(")");
		return bracketedSentence;
	}
}