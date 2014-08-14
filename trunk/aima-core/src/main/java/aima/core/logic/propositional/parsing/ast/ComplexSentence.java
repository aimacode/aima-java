package aima.core.logic.propositional.parsing.ast;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 244.<br>
 * <br>
 * <b>Complex Sentence:</b> are constructed from simpler sentences, using
 * parentheses (and square brackets) and logical connectives.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan 
 */
public class ComplexSentence extends Sentence {

	private Connective connective;
	private Sentence[] simplerSentences;
	// Lazy initialize these values.
	private int cachedHashCode = -1;
	private String cachedConcreteSyntax = null;

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
		assertLegalArguments(connective, sentences);
		
		this.connective = connective;
		simplerSentences = new Sentence[sentences.length];
		for (int i = 0; i < sentences.length; i++) {
			simplerSentences[i] = sentences[i];
		}
	}
	
	/**
	 * Convenience constructor for binary sentences.
	 * 
	 * @param sentenceL
	 * 			the left hand sentence.
	 * @param binaryConnective
	 * 			the binary connective.
	 * @param sentenceR
	 *  		the right hand sentence.
	 */
	public ComplexSentence(Sentence sentenceL, Connective binaryConnective, Sentence sentenceR) {
		this(binaryConnective, sentenceL, sentenceR);
	}

	@Override
	public Connective getConnective() {
		return connective;
	}

	@Override
	public int getNumberSimplerSentences() {
		return simplerSentences.length;
	}

	@Override
	public Sentence getSimplerSentence(int offset) {
		return simplerSentences[offset];
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
			if (other.getConnective().equals(this.getConnective())
					&& other.getNumberSimplerSentences() == this
							.getNumberSimplerSentences()) {
				// connective and # of simpler sentences match
				// assume match and then test each simpler sentence
				result = true;
				for (int i = 0; i < this.getNumberSimplerSentences(); i++) {
					if (!other.getSimplerSentence(i).equals(
							this.getSimplerSentence(i))) {
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
		if (cachedHashCode == -1) {
			cachedHashCode = 17 * getConnective().hashCode();
			for (Sentence s : simplerSentences) {
				cachedHashCode = (cachedHashCode * 37) + s.hashCode();
			}
		}

		return cachedHashCode;
	}

	@Override
	public String toString() {
		if (cachedConcreteSyntax == null) {
			if (isUnarySentence()) {
				cachedConcreteSyntax = getConnective()
						+ bracketSentenceIfNecessary(getConnective(), getSimplerSentence(0));
			} else if (isBinarySentence()) {
				cachedConcreteSyntax = bracketSentenceIfNecessary(getConnective(), getSimplerSentence(0))
						+ " "
						+ getConnective()
						+ " "
						+ bracketSentenceIfNecessary(getConnective(), getSimplerSentence(1));
			}
		}
		return cachedConcreteSyntax;
	}
	
	//
	// PRIVATE
	//
	private void assertLegalArguments(Connective connective, Sentence...sentences) {
		if (connective == null) {
			throw new IllegalArgumentException("Connective must be specified.");
		}
		if (sentences == null) {
			throw new IllegalArgumentException("> 0 simpler sentences must be specified.");
		}
		if (connective == Connective.NOT) {
			if (sentences.length != 1) {
				throw new IllegalArgumentException("A not (~) complex sentence only take 1 simpler sentence not "+sentences.length);
			}
		}
		else {
			if (sentences.length != 2) {
				throw new IllegalArgumentException("Connective is binary ("+connective+") but only "+sentences.length + " simpler sentences provided");
			}
		}
	}
}