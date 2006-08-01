package aima.search.framework;

import java.util.List;

public interface Search {
	List search(Problem p) throws Exception;

	Metrics getMetrics();
}