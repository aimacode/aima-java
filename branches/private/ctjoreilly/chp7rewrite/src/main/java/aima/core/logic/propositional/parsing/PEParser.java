package aima.core.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Parser;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class PEParser extends Parser {

	public PEParser() {
		lookAheadBuffer = new Token[lookAhead];
	}

	@Override
	public Sentence parse(String inputString) {
		lexer = new PELexer(inputString);
		fillLookAheadBuffer();
		return parseSentence(0);
	}
	
	//
	// PRIVATE
	//	
	private Sentence parseSentence(int level) {
		List<Object> levelTokens = parseLevel(level);
		
		Sentence result = null;

		// Now group up the tokens based on precedence order from highest to lowest.
		levelTokens = constructSentencesForConnective(Connective.NOT,           levelTokens);
		levelTokens = constructSentencesForConnective(Connective.AND,           levelTokens);
		levelTokens = constructSentencesForConnective(Connective.OR,            levelTokens);
		levelTokens = constructSentencesForConnective(Connective.IMPLICATION,   levelTokens);
		levelTokens = constructSentencesForConnective(Connective.BICONDITIONAL, levelTokens);
		
		// At this point there should just be the root formula
		// for this level, if not 'null' will be returned
		// and the calling code will deal with.
		if (levelTokens.size() == 1 && levelTokens.get(0) instanceof Sentence) {
			result = (Sentence) levelTokens.get(0);
		}
		
		return result;
	}
	
	private List<Object> constructSentencesForConnective(Connective connectiveToConstruct, List<Object> tokens) {
		List<Object> newTokens = new ArrayList<Object>();
		int numSentencesMade = 0;
		// Go right to left in order to make right associative, 
		// which is a natural default for propositional logic
		for (int i = tokens.size()-1; i >= 0; i--) {
			Object token = tokens.get(i);
			if (token instanceof Connective) {
				Connective tokenConnective = (Connective) token;
				if (tokenConnective == Connective.NOT) {
					// A unary connective
					if (i+1 < tokens.size() && tokens.get(i+1) instanceof Sentence) {
						if (tokenConnective == connectiveToConstruct) {
							ComplexSentence newSentence = new ComplexSentence(connectiveToConstruct, (Sentence) tokens.get(i+1));
							tokens.set(i,   newSentence);
							tokens.set(i+1, null);
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
							ComplexSentence newSentence = new ComplexSentence(connectiveToConstruct,
																(Sentence)tokens.get(i-1), (Sentence)tokens.get(i+1));
							tokens.set(i-1, newSentence);
							tokens.set(i,   null);
							tokens.set(i+1, null);
							numSentencesMade++;
						}
					}
					else {
						throw new RuntimeException("Binary connective argurments are not sentences");
					}
				}
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
		while (lookAhead(1).getType() != LogicTokenTypes.EOI && lookAhead(1).getType() != LogicTokenTypes.RPAREN && lookAhead(1).getType() != LogicTokenTypes.RSQRBRACKET) {
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
	
	private PropositionSymbol parseTrue() {
		consume();
		return new PropositionSymbol(PropositionSymbol.TRUE);
	}

	private PropositionSymbol parseFalse() {
		consume();
		return new PropositionSymbol(PropositionSymbol.FALSE);
	}

	private PropositionSymbol parseSymbol() {
		String sym = lookAhead(1).getText();
		consume();
		return new PropositionSymbol(sym);
	}

	private boolean detectBracket() {
		return lookAhead(1).getType() == LogicTokenTypes.LPAREN || lookAhead(1).getType() == LogicTokenTypes.LSQRBRACKET;
	}
	
	private Sentence parseBracketedSentence(int level) {
		String start = "(";
		String end   = ")";
		if (lookAhead(1).getType() == LogicTokenTypes.LSQRBRACKET) {
			start = "[";
			end   = "]";
		}
		match(start);
		Sentence bracketedSentence = parseSentence(level+1);
		if (bracketedSentence == null) {
			throw new RuntimeException("Empty bracket contents detected.");
		}
		match(end);
		return bracketedSentence;
	}
}