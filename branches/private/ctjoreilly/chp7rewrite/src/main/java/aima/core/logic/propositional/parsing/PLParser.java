package aima.core.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.SourceVersion;

import aima.core.logic.common.Lexer;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Parser;
import aima.core.logic.common.ParserException;
import aima.core.logic.common.Token;
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
 * AtomicSentence  -> True : False : P : Q : R : ... // (1)
 * ComplexSentence -> (Sentence) | [Sentence]
 *                 :  ~Sentence
 *                 :  Sentence & Sentence
 *                 :  Sentence | Sentence
 *                 :  Sentence => Sentence
 *                 :  Sentence <=> Sentence
 * 
 * OPERATOR PRECEDENCE: ~, &, |, =>, <=> // (2)
 * </pre>
 * 
 * Figure 7.7 A BNF (Backus-Naur Form) grammar of sentences in propositional
 * logic, along with operator precedences, from highest to lowest.<br>
 * <br>
 * Note (1): While the book states 'We use symbols that start with an upper case
 * letter and may contain other letters or subscripts' in this implementation we
 * allow any legal java identifier to stand in for a proposition symbol.<br>
 * <br>
 * Note (2): This implementation is right associative (tends to be more
 * intuitive for this language), for example:<br>
 * 
 * <pre>
 * A & B & C & D 
 * 
 * will be parsed as:
 * 
 * (A & (B & (C & D)))
 * 
 * </pre>
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 * @see SourceVersion#isIdentifier(CharSequence)
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
		Sentence result = null;

		ParseNode root = parseSentence(0);
		if (root != null && root.node instanceof Sentence) {
			result = (Sentence) root.node;
		}

		return result;
	}

	//
	// PRIVATE
	//
	private ParseNode parseSentence(int level) {
		List<ParseNode> levelParseNodes = parseLevel(level);

		ParseNode result = null;

		// Now group up the tokens based on precedence order from highest to
		// lowest.
		levelParseNodes = groupSimplerSentencesByConnective(Connective.NOT,
				levelParseNodes);
		levelParseNodes = groupSimplerSentencesByConnective(Connective.AND,
				levelParseNodes);
		levelParseNodes = groupSimplerSentencesByConnective(Connective.OR,
				levelParseNodes);
		levelParseNodes = groupSimplerSentencesByConnective(
				Connective.IMPLICATION, levelParseNodes);
		levelParseNodes = groupSimplerSentencesByConnective(
				Connective.BICONDITIONAL, levelParseNodes);

		// At this point there should just be the root formula
		// for this level.
		if (levelParseNodes.size() == 1
				&& levelParseNodes.get(0).node instanceof Sentence) {
			result = levelParseNodes.get(0);
		} else {
			// Did not identify a root sentence for this level,
			// therefore throw an exception indicating the problem.
			throw new ParserException("Unable to correctly parse sentence: "
					+ levelParseNodes, getTokens(levelParseNodes));
		}

		return result;
	}

	private List<ParseNode> groupSimplerSentencesByConnective(
			Connective connectiveToConstruct, List<ParseNode> parseNodes) {
		List<ParseNode> newParseNodes = new ArrayList<ParseNode>();
		int numSentencesMade = 0;
		// Go right to left in order to make right associative,
		// which is a natural default for propositional logic
		for (int i = parseNodes.size() - 1; i >= 0; i--) {
			ParseNode parseNode = parseNodes.get(i);
			if (parseNode.node instanceof Connective) {
				Connective tokenConnective = (Connective) parseNode.node;
				if (tokenConnective == Connective.NOT) {
					// A unary connective
					if (i + 1 < parseNodes.size()
							&& parseNodes.get(i + 1).node instanceof Sentence) {
						if (tokenConnective == connectiveToConstruct) {
							ComplexSentence newSentence = new ComplexSentence(
									connectiveToConstruct,
									(Sentence) parseNodes.get(i + 1).node);
							parseNodes.set(i, new ParseNode(newSentence,
									parseNode.token));
							parseNodes.set(i + 1, null);
							numSentencesMade++;
						}
					} else {
						throw new ParserException(
								"Unary connective argurment is not a sentence at input position "
										+ parseNode.token
												.getStartCharPositionInInput(),
								parseNode.token);
					}
				} else {
					// A Binary connective
					if ((i - 1 >= 0 && parseNodes.get(i - 1).node instanceof Sentence)
							&& (i + 1 < parseNodes.size() && parseNodes
									.get(i + 1).node instanceof Sentence)) {
						// A binary connective
						if (tokenConnective == connectiveToConstruct) {
							ComplexSentence newSentence = new ComplexSentence(
									connectiveToConstruct,
									(Sentence) parseNodes.get(i - 1).node,
									(Sentence) parseNodes.get(i + 1).node);
							parseNodes.set(i - 1, new ParseNode(newSentence,
									parseNode.token));
							parseNodes.set(i, null);
							parseNodes.set(i + 1, null);
							numSentencesMade++;
						}
					} else {
						throw new ParserException(
								"Binary connective argurments are not sentences at input position "
										+ parseNode.token
												.getStartCharPositionInInput(),
								parseNode.token);
					}
				}
			}
		}

		for (int i = 0; i < parseNodes.size(); i++) {
			ParseNode parseNode = parseNodes.get(i);
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
			throw new ParserException(
					"Unable to construct sentence for connective: "
							+ connectiveToConstruct + " from: " + parseNodes,
					getTokens(parseNodes));
		}

		return newParseNodes;
	}

	private List<ParseNode> parseLevel(int level) {
		List<ParseNode> tokens = new ArrayList<ParseNode>();
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
			throw new ParserException(
					"Parser Error: end of input not expected at level " + level,
					lookAhead(1));
		}

		return tokens;
	}

	private boolean detectConnective() {
		return lookAhead(1).getType() == LogicTokenTypes.CONNECTIVE;
	}

	private ParseNode parseConnective() {
		Token token = lookAhead(1);
		Connective connective = Connective.get(token.getText());
		consume();
		return new ParseNode(connective, token);
	}

	private boolean detectAtomicSentence() {
		int type = lookAhead(1).getType();
		return type == LogicTokenTypes.TRUE || type == LogicTokenTypes.FALSE
				|| type == LogicTokenTypes.SYMBOL;
	}

	private ParseNode parseAtomicSentence() {
		Token t = lookAhead(1);
		if (t.getType() == LogicTokenTypes.TRUE) {
			return parseTrue();
		} else if (t.getType() == LogicTokenTypes.FALSE) {
			return parseFalse();
		} else if (t.getType() == LogicTokenTypes.SYMBOL) {
			return parseSymbol();
		} else {
			throw new ParserException(
					"Error parsing atomic sentence at position "
							+ t.getStartCharPositionInInput(), t);
		}
	}

	private ParseNode parseTrue() {
		Token token = lookAhead(1);
		consume();
		return new ParseNode(new PropositionSymbol(PropositionSymbol.TRUE_SYMBOL),
				token);
	}

	private ParseNode parseFalse() {
		Token token = lookAhead(1);
		consume();
		return new ParseNode(new PropositionSymbol(PropositionSymbol.FALSE_SYMBOL),
				token);
	}

	private ParseNode parseSymbol() {
		Token token = lookAhead(1);
		String sym = token.getText();
		consume();
		return new ParseNode(new PropositionSymbol(sym), token);
	}

	private boolean detectBracket() {
		return lookAhead(1).getType() == LogicTokenTypes.LPAREN
				|| lookAhead(1).getType() == LogicTokenTypes.LSQRBRACKET;
	}

	private ParseNode parseBracketedSentence(int level) {
		Token startToken = lookAhead(1);

		String start = "(";
		String end = ")";
		if (startToken.getType() == LogicTokenTypes.LSQRBRACKET) {
			start = "[";
			end = "]";
		}

		match(start);
		ParseNode bracketedSentence = parseSentence(level + 1);
		match(end);

		return bracketedSentence;
	}

	private Token[] getTokens(List<ParseNode> parseNodes) {
		Token[] result = new Token[parseNodes.size()];

		for (int i = 0; i < parseNodes.size(); i++) {
			result[i] = parseNodes.get(i).token;
		}

		return result;
	}

	private class ParseNode {
		public Object node = null;
		public Token token = null;

		public ParseNode(Object node, Token token) {
			this.node = node;
			this.token = token;
		}

		public String toString() {
			return node.toString() + " at "
					+ token.getStartCharPositionInInput();
		}
	}
}