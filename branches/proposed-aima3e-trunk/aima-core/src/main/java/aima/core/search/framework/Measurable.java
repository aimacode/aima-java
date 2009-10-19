package aima.core.search.framework;

/**
 * @author Ravi Mohan
 * 
 */
public interface Measurable {
	void setSearchMetric(String name, Object value);

	Object getSearchMetric(String name);
}