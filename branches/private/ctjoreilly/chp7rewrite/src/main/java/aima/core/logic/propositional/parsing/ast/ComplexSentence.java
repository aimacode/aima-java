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
	private int hashCode = -1;
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
			throw new IllegalArgumentException(
					"> 0 simpler sentences must be specified.");
		}
		this.connective = connective;
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

	/**
	 * 
	 * @return the number of simpler sentences contained in this complex
	 *         sentence.
	 */
	public int getNumberSimplerSentences() {
		return simplerSentences.length;
	}

	/**
	 * Get the simplified sentence, at the specified offset (starts at 0),
	 * contained by this complex sentence.
	 * 
	 * @param offset
	 *            the offset of the contained simplified sentence to retrieve.
	 * @return the simplified sentence, at the specified offset, contained by
	 *         this complex sentence.
	 */
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
		if (hashCode == -1) {
			hashCode = toString().hashCode();
		}

		return hashCode;
	}

	@Override
	public String toString() {
		if (concreteSyntax == null) {
			if (isUnarySentence()) {
				concreteSyntax = getConnective()
						+ bracketSentenceIfNecessary(getConnective(), getSimplerSentence(0));
			} else if (isBinarySentence()) {
				concreteSyntax = bracketSentenceIfNecessary(getConnective(), getSimplerSentence(0))
						+ " "
						+ getConnective()
						+ " "
						+ bracketSentenceIfNecessary(getConnective(), getSimplerSentence(1));
			}
		}
		return concreteSyntax;
	}
}