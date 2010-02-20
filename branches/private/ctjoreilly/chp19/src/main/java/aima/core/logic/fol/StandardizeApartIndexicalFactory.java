package aima.core.logic.fol;

import java.util.HashMap;
import java.util.Map;

/**
 * This class ensures unique standardize apart indexicals are created. 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class StandardizeApartIndexicalFactory {
	private static Map<Character, Integer> _assignedIndexicals = new HashMap<Character, Integer>();

	// For use in test cases, where predictable behavior is expected.
	public static void flush() {
		synchronized (_assignedIndexicals) {
			_assignedIndexicals.clear();
		}
	}

	public static StandardizeApartIndexical newStandardizeApartIndexical(
			Character preferredPrefix) {
		char ch = preferredPrefix.charValue();
		if (!(Character.isLetter(ch) && Character.isLowerCase(ch))) {
			throw new IllegalArgumentException("Preferred prefix :"
					+ preferredPrefix + " must be a valid a lower case letter.");
		}

		StringBuilder sb = new StringBuilder();
		synchronized (_assignedIndexicals) {
			Integer currentPrefixCnt = _assignedIndexicals.get(preferredPrefix);
			if (null == currentPrefixCnt) {
				currentPrefixCnt = 0;
			} else {
				currentPrefixCnt += 1;
			}
			_assignedIndexicals.put(preferredPrefix, currentPrefixCnt);
			sb.append(preferredPrefix);
			for (int i = 0; i < currentPrefixCnt; i++) {
				sb.append(preferredPrefix);
			}
		}

		return new StandardizeApartIndexicalImpl(sb.toString());
	}
}

class StandardizeApartIndexicalImpl implements StandardizeApartIndexical {
	private String prefix = null;
	private int index = 0;

	public StandardizeApartIndexicalImpl(String prefix) {
		this.prefix = prefix;
	}

	//
	// START-StandardizeApartIndexical
	public String getPrefix() {
		return prefix;
	}

	public int getNextIndex() {
		return index++;
	}
	// END-StandardizeApartIndexical
	//
}