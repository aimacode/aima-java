package aima.core.logic.basic.firstorder;

/**
 * @author Ravi Mohan
 * @author Anurag Rai
 */
public class Connectors {
	public static final String AND = "&";

	public static final String OR = "|";

	public static final String NOT = "~";

	public static final String IMPLIES = "=>";

	public static final String BICOND = "<=>";

	public static boolean isAND(String connector) {
		return AND.equals(connector);
	}

	public static boolean isOR(String connector) {
		return OR.equals(connector);
	}

	public static boolean isNOT(String connector) {
		return NOT.equals(connector);
	}

	public static boolean isIMPLIES(String connector) {
		return IMPLIES.equals(connector);
	}

	public static boolean isBICOND(String connector) {
		return BICOND.equals(connector);
	}
}