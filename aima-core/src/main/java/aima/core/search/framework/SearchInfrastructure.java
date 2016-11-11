package aima.core.search.framework;

/**
 * Defines general services for all search algorithm implementations in the AIMAe3 framework. 
 * @author Ruediger Lunde
 */
public interface SearchInfrastructure {

	/**
	 * Returns all the metrics of the search.
	 */
	Metrics getMetrics();
	
	/**
	 * Returns the node expander used by the search. Useful for progress tracing.
	 */
	NodeExpander getNodeExpander();
}