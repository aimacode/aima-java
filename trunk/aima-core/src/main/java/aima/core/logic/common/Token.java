package aima.core.logic.common;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class Token {
	private String text;

	private int type;

	/**
	 * Constructs a token from the specified token-name and attribute-value
	 * 
	 * @param type
	 *            the token-name
	 * @param text
	 *            the attribute-value
	 */
	public Token(int type, String text) {
		this.type = type;
		this.text = text;
	}

	/**
	 * Returns the attribute-value of this token.
	 * 
	 * @return the attribute-value of this token.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns the token-name of this token.
	 * 
	 * @return the token-name of this token.
	 */
	public int getType() {
		return type;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		Token other = (Token) o;
		return ((other.type == type) && (other.text.equals(text)));
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + type;
		result = 37 * result + text.hashCode();
		return 17;
	}

	@Override
	public String toString() {
		return "[ " + type + " " + text + " ]";
	}
}