package aima.core.logic.fol.parsing;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */
public class FOLParser {
	private FOLLexer lexer;

	protected Token[] lookAheadBuffer;

	protected int lookAhead = 1;

	public FOLParser(FOLLexer lexer) {
		this.lexer = lexer;
		lookAheadBuffer = new Token[lookAhead];
	}

	public FOLParser(FOLDomain domain) {
		this(new FOLLexer(domain));
	}

	public FOLDomain getFOLDomain() {
		return lexer.getFOLDomain();
	}

	public Sentence parse(String s) {
		setUpToParse(s);
		return parseSentence();
	}

	public void setUpToParse(String s) {
		lexer.clear();
		lookAheadBuffer = new Token[1];
		lexer.setInput(s);
		fillLookAheadBuffer();

	}

	private Term parseTerm() {
		Token t = lookAhead(1);
		int tokenType = t.getType();
		if (tokenType == LogicTokenTypes.CONSTANT) {
			return parseConstant();
		} else if (tokenType == LogicTokenTypes.VARIABLE) {
			return parseVariable();
		} else if (tokenType == LogicTokenTypes.FUNCTION) {
			return parseFunction();
		}

		else {
			return null;
		}
	}

	public Term parseVariable() {
		Token t = lookAhead(1);
		String value = t.getText();
		consume();
		return new Variable(value);
	}

	public Term parseConstant() {
		Token t = lookAhead(1);
		String value = t.getText();
		consume();
		return new Constant(value);
	}

	public Term parseFunction() {
		Token t = lookAhead(1);
		String functionName = t.getText();
		List<Term> terms = processTerms();
		return new Function(functionName, terms);
	}

	public Sentence parsePredicate() {
		Token t = lookAhead(1);
		String predicateName = t.getText();
		List<Term> terms = processTerms();
		return new Predicate(predicateName, terms);
	}

	private List<Term> processTerms() {
		consume();
		List<Term> terms = new ArrayList<Term>();
		match("(");
		Term term = parseTerm();
		terms.add(term);

		while (lookAhead(1).getType() == LogicTokenTypes.COMMA) {
			match(",");
			term = parseTerm();
			terms.add(term);
		}
		match(")");
		return terms;
	}

	public Sentence parseTermEquality() {
		Term term1 = parseTerm();
		match("=");
		// System.out.println("=");
		Term term2 = parseTerm();
		return new TermEquality(term1, term2);
	}

	public Sentence parseNotSentence() {
		match("NOT");
		return new NotSentence(parseSentence());
	}

	//
	// PROTECTED METHODS
	//
	protected Token lookAhead(int i) {
		return lookAheadBuffer[i - 1];
	}

	protected void consume() {
		// System.out.println("consuming" +lookAheadBuffer[0].getText());
		loadNextTokenFromInput();
		// System.out.println("next token " +lookAheadBuffer[0].getText());
	}

	protected void loadNextTokenFromInput() {

		boolean eoiEncountered = false;
		for (int i = 0; i < lookAhead - 1; i++) {

			lookAheadBuffer[i] = lookAheadBuffer[i + 1];
			if (isEndOfInput(lookAheadBuffer[i])) {
				eoiEncountered = true;
				break;
			}
		}
		if (!eoiEncountered) {
			try {
				lookAheadBuffer[lookAhead - 1] = lexer.nextToken();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	protected boolean isEndOfInput(Token t) {
		return (t.getType() == LogicTokenTypes.EOI);
	}

	protected void fillLookAheadBuffer() {
		for (int i = 0; i < lookAhead; i++) {
			lookAheadBuffer[i] = lexer.nextToken();
		}
	}

	protected void match(String terminalSymbol) {
		if (lookAhead(1).getText().equals(terminalSymbol)) {
			consume();
		} else {
			throw new RuntimeException(
					"Syntax error detected at match. Expected "
							+ terminalSymbol + " but got "
							+ lookAhead(1).getText());
		}

	}

	//
	// PRIVATE METHODS
	//

	private Sentence parseSentence() {
		Token t = lookAhead(1);
		if (lParen(t)) {
			return parseParanthizedSentence();
		} else if ((lookAhead(1).getType() == LogicTokenTypes.QUANTIFIER)) {

			return parseQuantifiedSentence();
		} else if (notToken(t)) {
			return parseNotSentence();
		} else if (predicate(t)) {
			return parsePredicate();
		} else if (term(t)) {
			return parseTermEquality();
		}

		throw new RuntimeException("parse failed with Token " + t.getText());
	}

	private Sentence parseQuantifiedSentence() {
		String quantifier = lookAhead(1).getText();
		consume();
		List<Variable> variables = new ArrayList<Variable>();
		Variable var = (Variable) parseVariable();
		variables.add(var);
		while (lookAhead(1).getType() == LogicTokenTypes.COMMA) {
			consume();
			var = (Variable) parseVariable();
			variables.add(var);
		}
		Sentence sentence = parseSentence();
		return new QuantifiedSentence(quantifier, variables, sentence);
	}

	private Sentence parseParanthizedSentence() {
		match("(");
		Sentence sen = parseSentence();
		while (binaryConnector(lookAhead(1))) {
			String connector = lookAhead(1).getText();
			consume();
			Sentence other = parseSentence();
			sen = new ConnectedSentence(connector, sen, other);
		}
		match(")");
		return sen; /* new ParanthizedSentence */

	}

	private boolean binaryConnector(Token t) {
		if ((t.getType() == LogicTokenTypes.CONNECTOR)
				&& (!(t.getText().equals("NOT")))) {
			return true;
		} else {
			return false;
		}
	}

	private boolean lParen(Token t) {
		if (t.getType() == LogicTokenTypes.LPAREN) {
			return true;
		} else {
			return false;
		}
	}

	private boolean term(Token t) {
		if ((t.getType() == LogicTokenTypes.FUNCTION)
				|| (t.getType() == LogicTokenTypes.CONSTANT)
				|| (t.getType() == LogicTokenTypes.VARIABLE)) {
			return true;
		} else {
			return false;
		}

	}

	private boolean predicate(Token t) {
		if ((t.getType() == LogicTokenTypes.PREDICATE)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean notToken(Token t) {
		if ((t.getType() == LogicTokenTypes.CONNECTOR)
				&& (t.getText().equals("NOT"))) {
			return true;
		} else {
			return false;
		}
	}
}