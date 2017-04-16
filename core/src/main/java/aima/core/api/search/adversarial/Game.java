package aima.core.api.search.adversarial;

/*
*  @author Anurag Rai
*/

import aima.core.api.search.Problem;

public interface Game<A,S> extends Problem<A, S> {
	
	double utility(S state);
}
