package aima.search.framework;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.basic.Agent;
import aima.basic.Percept;

/**
 * @author Ravi Mohan
 * 
 */

public class SearchAgent extends Agent {
	List actionList;

	private Iterator actionIterator;

	private Metrics searchMetrics;

	public SearchAgent(Problem p, Search search) throws Exception {
		actionList = search.search(p);
		actionIterator = actionList.iterator();
		searchMetrics = search.getMetrics();

	}

	@Override
	public String execute(Percept p) {
		if (actionIterator.hasNext()) {
			return (String) actionIterator.next();
		} else {
			return "NoOp";
		}
	}

	public List getActions() {
		return actionList;
	}

	public Properties getInstrumentation() {
		Properties retVal = new Properties();
		Iterator iter = searchMetrics.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			String value = searchMetrics.get(key);
			retVal.setProperty(key, value);
		}
		return retVal;
	}

}