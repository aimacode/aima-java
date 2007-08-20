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

	public int getHeuristicValue(Object state) {
		return 1;
	}

}