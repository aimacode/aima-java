package aima.core.search.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.problem.Problem;

/**
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate in the state space
 *
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class SearchAgent<S, A extends Action> extends AbstractAgent {
	protected List<Action> actionList;

	private Iterator<Action> actionIterator;

	private Metrics searchMetrics;

	public SearchAgent(Problem<S, A> p, SearchForActions<S, A> search) throws Exception {
		actionList = new ArrayList<>();
		actionList.addAll(search.findActions(p));
		if (actionList.size() == 1 && actionList.get(0) == null)
			actionList.set(0, NoOpAction.NO_OP); // search uses null to represent the stop command.

		actionIterator = actionList.iterator();
		searchMetrics = search.getMetrics();
	}

	@Override
	public Action execute(Percept p) {
		if (actionIterator.hasNext())
			return actionIterator.next();
		return NoOpAction.NO_OP; // no success
	}

	public boolean isDone() {
		return !actionIterator.hasNext();
	}

	public List<Action> getActions() {
		return actionList;
	}

	public Properties getInstrumentation() {
		Properties retVal = new Properties();
		for (String key : searchMetrics.keySet()) {
			String value = searchMetrics.get(key);
			retVal.setProperty(key, value);
		}
		return retVal;
	}
}