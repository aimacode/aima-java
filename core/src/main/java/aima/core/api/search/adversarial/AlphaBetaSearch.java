package aima.core.api.search.adversarial;

/**
 * Artificial Intelligence A Modern Approach (4th Ed.): Page ???.<br>
 * 
 * <pre>
 * <code>
 * function ALPHA-BETA-SEARCH(state) returns an action
 *   v = MAX-VALUE(state, -infinity, +infinity)
 *   return the action in ACTIONS(state) with value v
 *   
 * function MAX-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = -infinity
 *   for each a in ACTIONS(state) do
 *     v = MAX(v, MIN-VALUE(RESULT(s, a), alpha, beta))
 *     if v >= beta then return v
 *     alpha = MAX(alpha, v)
 *   return v
 *   
 * function MIN-VALUE(state, alpha, beta) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = infinity
 *   for each a in ACTIONS(state) do
 *     v = MIN(v, MAX-VALUE(RESULT(s,a), alpha, beta))
 *     if v <= alpha then return v
 *     beta = MIN(beta, v)
 *   return v
 * </code>
 * </pre>
 * 
 * Figure ??.?? The alpha-beta search algorithm. Notice that these routines are
 * the same as the MINIMAX functions in Figure ??.??, except for the two lines in
 * each of MIN-VALUE and MAX-VALUE that maintain alpha and beta (and the
 * bookkeeping to pass these parameters along).
 * 
 * @author Anurag Rai
 * 
 */


public interface AlphaBetaSearch<A,S> extends AdversarialSearch<A,S> {
    /**
     * @return the action which appears to be the best at the given state
     * 
     */
	@Override
	 default A apply(S state) {
    	A result = null;
    	double resultValue = Double.NEGATIVE_INFINITY;
		for (A action : getGame().actions(state)) {
			double value = minValue(getGame().result(state, action), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (value > resultValue) {
				result = action;
				resultValue = value;
			}
		}
		return result;
    }
 
	//function MIN-VALUE(state, alpha, beta) returns a utility value
	default double minValue(S state, double alpha, double beta) {
		//if TERMINAL-TEST(state) then return UTILITY(state)
		if ( getGame().isGoalState(state) )
			return getGame().utility(state);
		//v = infinity
		double v = Double.POSITIVE_INFINITY;
		//for each a in ACTIONS(state) do
		for (A action : getGame().actions(state)) {
			//v = MIN(v, MAX-VALUE(RESULT(s,a), alpha, beta))
			v = Math.min(v, maxValue( getGame().result(state, action), alpha, beta));
			//if v <= alpha then return v
			if (v <= alpha)
				return v;
			//beta = MIN(beta, v)
			beta = Math.min(beta, v);
		}
		//return v
		return v;
	}
   
	//function MAX-VALUE(state, alpha, beta) returns a utility value
	default double maxValue(S state,double alpha,double beta) {
		//if TERMINAL-TEST(state) then return UTILITY(state)
		if ( getGame().isGoalState(state) )
			return getGame().utility(state);
		//v = -infinity
		double v = Double.NEGATIVE_INFINITY;
		//for each a in ACTIONS(state) do
		for (A action : getGame().actions(state)) {
			//v = MAX(v, MIN-VALUE(RESULT(s, a), alpha, beta))
			v = Math.max(v, minValue( getGame().result(state, action), alpha, beta));
			//if v >= beta then return v
			if (v >= beta)
				return v;
			//alpha = MAX(alpha, v)
			alpha = Math.max(alpha, v);
		}
		//return v
		return v;
	}

	Game<A,S> getGame();
}
