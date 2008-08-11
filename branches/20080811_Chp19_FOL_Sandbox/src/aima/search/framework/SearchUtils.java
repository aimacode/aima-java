package aima.search.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

public class SearchUtils {

	public static List<String> actionsFromNodes(List<Node> nodeList) {
		List<String> stateList = new ArrayList<String>();
		for (int i = 1; i < nodeList.size(); i++) { // ignore root node this has
			// no action hence index starts from 1 not
			// zero
			Node node = nodeList.get(i);
			stateList.add(node.getAction());
		}
		return stateList;
	}

	public static List<String> stringToList(String str) {

		List<String> list = new ArrayList<String>();
		list.add(str);
		return list;

	}

	public static boolean listMatches(List list, String string) {
		return ((list.size() == 1) && (((String) list.get(0)).equals(string)));
	}

}