package aima.core.logic.common;

/**
 * @author Ravi Mohan
 * 
 */
public class Token {
	private String text;

	private int type;

	public Token(int type, String text) {
		this.type = type;
		this.text = text;
	}

	public String getText() {
		return text;
	}

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