package aima.core.probability.mdp.next.impl;

import java.util.HashMap;
import java.util.Map;

import aima.core.probability.mdp.next.Policy;

/**
 * Default implementation of the Policy interface using an underlying Map to
 * look up an action associated with a state.
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 */
public class LookupPolicy<S, A> implements Policy<S, A> {
	private Map<S, A> policy = new HashMap<S, A>();

	public LookupPolicy(Map<S, A> aPolicy) {
		policy.putAll(aPolicy);
	}

	//
	// START-Policy
	@Override
	public A action(S s) {
		return policy.get(s);
	}

	// END-Policy
	//
}
