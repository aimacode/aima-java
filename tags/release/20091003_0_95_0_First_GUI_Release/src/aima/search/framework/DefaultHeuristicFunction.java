/*
 * Created on Sep 12, 2004
 *
 */
package aima.search.framework;

/**
 * @author Ravi Mohan
 * 
 */

public class DefaultHeuristicFunction implements HeuristicFunction {

	public double getHeuristicValue(Object state) {
		throw new IllegalStateException(
				"Should not be depending on the DefaultHeuristicFunction.");
		// return 1;
	}

}