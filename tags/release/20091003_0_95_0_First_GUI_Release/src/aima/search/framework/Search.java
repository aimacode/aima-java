package aima.search.framework;

import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

public interface Search {
	List search(Problem p) throws Exception;

	Metrics getMetrics();
}