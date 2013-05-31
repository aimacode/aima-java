package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 244.<br>
 * <br>
 * <b>Complex Sentence:</b> are constructed from simpler sentences, using
 * parentheses (and square brackets) and logical connectives.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public class ComplexSentence extends Sentence {

	private Connective connective;
	private Sentence[] simplerSentences;
	// Lazy initialize these values.
	private int    hashCode = -1;
	private String concreteSyntax = null;

	/**
	 * Constructor.
	 * 
	 * @param connective
	 *            the complex sentence's connective.
	 * @param sentences
	 *            the simpler sentences making up the complex sentence.
	 */
	public ComplexSentence(Connective connective, Sentence... sentences) {
		// Assertion checks
		if (connective == null) {
			throw new IllegalArgumentException("Connective must be specified.");
		}
		if (sentences == null) {
			throw new IllegalArgumentException("> 0 simpler sentences must be specified.");
		}
		this.connective  = connective;
		simplerSentences = new Sentence[sentences.length];
		for (int i = 0; i < sentences.length; i++) {
			simplerSentences[i] = sentences[i];
		}
	}

	/**
	 * 
	 * @return the logical connective associated with this sentence.
	 */
	public Connective getConnective() {
		return connective;
	}

	public int getSize() {
		return simplerSentences.length;
	}
	
	public Sentence get(int idx) {
		return simplerSentences[idx];
	}
	
	public boolean isUnary() {
		return simplerSentences.length == 1;
	}
	
	public boolean isBinary() {
		return simplerSentences.length == 2;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		
		boolean result = false;
		ComplexSentence other = (ComplexSentence) o;
		if (other.hashCode() == this.hashCode()) {
			if (other.getConnective().equals(this.getConnective()) && other.getSize() == this.getSize()) {
				// connective and # of simpler sentences match
				// assume match and then test each simpler sentence
				result = true;
				for (int i = 0; i < this.getSize(); i++) {
					if (!other.get(i).equals(this.get(i))) {
						result = false;
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public int hashCode() {
		if (hashCode == -1) {
			hashCode = toString().hashCode();
		}
		
		return hashCode;
	}
	
	@Override
	public String toString() {
		if (concreteSyntax == null) {
			if (isUnary()) {
				concreteSyntax = getConnective() + bracketSentenceIfNecessary(get(0));
			}
			else if (isBinary()) {
				concreteSyntax = bracketSentenceIfNecessary(get(0)) + " " + getConnective() + " " + bracketSentenceIfNecessary(get(1));
			}
		}
		return concreteSyntax;
	}
	
	@Override
	public Object accept(PLVisitor plv, Object arg) {
		Object result = null;
		if (isUnary()) {
			result = plv.visitUnarySentence(this, arg);
		}
		else if (isBinary()) {
			result = plv.visitBinarySentence(this, arg);
		}
		
		return result;
	}
	
	//
	// PROTECTED
	//
	
	/**
	 * Utility routine that will create a string representation of a given
	 * Sentence and place it inside brackets if it is a complex sentence that
	 * has lower precedence than this complex sentence.<br>
	 * <br>
	 * Note: this is a form of pretty printing, whereby we only add brackets in
	 * the concrete syntax representation as needed to ensure it can be parsed
	 * back again into an equivalent abstract syntax representation used here.
	 * 
	 * @param aSentence
	 * @return a String representation of the Sentence, bracketed if this
	 *         Sentence has higher precedence.
	 */
	protected String bracketSentenceIfNecessary(Sentence aSentence) {
		String result = null;
		if (aSentence instanceof ComplexSentence) {
			ComplexSentence cs = (ComplexSentence) aSentence;
			if (cs.getConnective().getPrecedence() < getConnective()
					.getPrecedence()) {
				result = "(" + aSentence + ")";
			}
		}

		if (result == null) {
			result = aSentence.toString();
		}

		return result;
	}
}