package aima.logic.fol;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class StandardizeApartIndexical {
	private String prefix = null;
	private int index = 0;
	
	public StandardizeApartIndexical(String prefix) {
		if (prefix == null || prefix.length() < 0
				|| !Character.isLowerCase(prefix.charAt(0))) {
			throw new IllegalArgumentException("Prefix :" + prefix
					+ " must be a valid FOL Variable identifier.");
		}

		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public int getNextIndex() {
		return index++;
	}
}