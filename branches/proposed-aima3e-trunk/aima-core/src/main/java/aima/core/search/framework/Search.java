package aima.core.search.framework;

import java.util.List;

import aima.core.agent.Action;

/**
 * @author Ravi Mohan
 * 
 */
public interface Search {
	List<Action> search(Problem p) throws Exception;

	Metrics getMetrics();
}