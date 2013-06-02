package aima.core.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.common.Lexer;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Parser;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 7.7, page
 * 244.<br>
 * 
 * Implementation of a propositional logic parser based on:
 * 
 * <pre>
 * Sentence        -> AtomicSentence : ComplexStence
 * AtomicSentence  -> True : False : P : Q : R : ...
 * ComplexSentence -> (Sentence) | [Sentence]
 *                 :  ~Sentence
 *                 :  Sentence & Sentence
 *                 :  Sentence | Sentence
 *                 :  Sentence => Sentence
 *                 :  Sentence <=> Sentence
 * 
 * OPERATOR PRECEDENCE: ~, &, |, =>, <=>
 * </pre>
 * 
 * Figure 7.7 A BNF (Backus-Naur Form) grammar of sentences in propositional
 * logic, along with operator precedences, from highest to lowest.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class PLParser extends Parser<Sentence> {

	private PLLexer lexer = new PLLexer();

	/**
	 * Default Constructor.
	 */
	public PLParser() {
	}

	@Override
	public Lexer getLexer() {
		return lexer;
	}

	//
	// PROTECTED
	//
	@Override
	protected Sentence parse() {
		return parseSentence(0);
	}

	//
	// PRIVATE
	//
	private Sentence parseSentence(int level) {
		List<Object> levelParseNodes = parseLevel(level);

		Sentence result = null;

		// Now group up the tokens based on precedence order from highest to
		// lowest.
		levelParseNodes = constructSentencesForConnective(Connective.NOT,
				levelParseNodes);
		levelParseNodes = constructSentencesForConnective(Connective.AND,
				levelParseNodes);
		levelParseNodes = constructSentencesForConnective(Connective.OR,
				levelParseNodes);
		levelParseNodes = constructSentencesForConnective(
				Connective.IMPLICATION, levelParseNodes);
		levelParseNodes = constructSentencesForConnective(
				Connective.BICONDITIONAL, levelParseNodes);

		// At this point there should just be the root formula
		// for this level, if not 'null' will be returned
		// and the calling code will deal with.
		if (levelParseNodes.size() == 1
				&& levelParseNodes.get(0) instanceof Sentence) {
			result = (Sentence) levelParseNodes.get(0);
		}

		return result;
	}

	private List<Object> constructSentencesForConnective(
			Connective connectiveToConstruct, List<Object> parseNodes) {
		List<Object> newParseNodes = new ArrayList<Object>();
		int numSentencesMade = 0;
		// Go right to left in order to make right associative,
		// which is a natural default for propositional logic
		for (int i = parseNodes.size() - 1; i >= 0; i--) {
			Object parseNode = parseNodes.get(i);
			if (parseNode instanceof Connective) {
				Connective tokenConnective = (Connective) parseNode;
				if (tokenConnective == Connective.NOT) {
					// A unary connective
					if (i + 1 < parseNodes.size()
							&& parseNodes.get(i + 1) instanceof Sentence) {
						if (tokenConnective == connectiveToConstruct) {
							ComplexSentence newSentence = new ComplexSentence(
									connectiveToConstruct,
									(Sentence) parseNodes.get(i + 1));
							parseNodes.set(i, newSentence);
							parseNodes.set(i + 1, null);
							numSentencesMade++;
						}
					} else {
						throw new RuntimeException(
								"Unary connective argurment is not a sentence");
					}
				} else {
					// A Binary connective
					if ((i - 1 >= 0 && parseNodes.get(i - 1) instanceof Sentence)
							&& (i + 1 < parseNodes.size() && parseNodes
									.get(i + 1) instanceof Sentence)) {
						// A binary connective
						if (tokenConnective == connectiveToConstruct) {
							ComplexSentence newSentence = new ComplexSentence(
									connectiveToConstruct,
									(Sentence) parseNodes.get(i - 1),
									(Sentence) parseNodes.get(i + 1));
							parseNodes.set(i - 1, newSentence);
							parseNodes.set(i, null);
							parseNodes.set(i + 1, null);
							numSentencesMade++;
						}
					} else {
						throw new RuntimeException(
								"Binary connective argurments are not sentences");
					}
				}
			}
		}

		for (int i = 0; i < parseNodes.size(); i++) {
			Object parseNode = parseNodes.get(i);
			if (parseNode != null) {
				newParseNodes.add(parseNode);
			}
		}

		// Ensure no tokens left unaccounted for in this pass.
		int toSubtract = 0;
		if (connectiveToConstruct == Connective.NOT) {
			toSubtract = (numSentencesMade * 2) - numSentencesMade;
		} else {
			toSubtract = (numSentencesMade * 3) - numSentencesMade;
		}

		if (parseNodes.size() - toSubtract != newParseNodes.size()) {
			throw new RuntimeException(
					"Unable to construct sentence for connective: "
							+ connectiveToConstruct + " from: " + parseNodes);
		}

		return newParseNodes;
	}

	private List<Object> parseLevel(int level) {
		List<Object> tokens = new ArrayList<Object>();
		while (lookAhead(1).getType() != LogicTokenTypes.EOI
				&& lookAhead(1).getType() != LogicTokenTypes.RPAREN
				&& lookAhead(1).getType() != LogicTokenTypes.RSQRBRACKET) {
			if (detectConnective()) {
				tokens.add(parseConnective());
			} else if (detectAtomicSentence()) {
				tokens.add(parseAtomicSentence());
			} else if (detectBracket()) {
				tokens.add(parseBracketedSentence(level));
			}
		}

		if (level > 0 && lookAhead(1).getType() == LogicTokenTypes.EOI) {
			throw new RuntimeException(
					"Parser Error end of input not expected at level " + level);
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
		return type == LogicTokenTypes.TRUE || type == LogicTokenTypes.FALSE
				|| type == LogicTokenTypes.SYMBOL;
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
		return lookAhead(1).getType() == LogicTokenTypes.LPAREN
				|| lookAhead(1).getType() == LogicTokenTypes.LSQRBRACKET;
	}

	private Sentence parseBracketedSentence(int level) {
		String start = "(";
		String end = ")";
		if (lookAhead(1).getType() == LogicTokenTypes.LSQRBRACKET) {
			start = "[";
			end = "]";
		}
		match(start);
		Sentence bracketedSentence = parseSentence(level + 1);
		if (bracketedSentence == null) {
			throw new RuntimeException("Empty bracket contents detected.");
		}
		match(end);
		return bracketedSentence;
	}
}