package aima.core.search.api;

import java.util.function.Function;

/**
 * Description of a Search function that looks for a conditional plan (due to nondeterministic outcome of actions).
 *
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the state space
 *
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 * @author Anurag Rai
 * 
 */
@FunctionalInterface
public interface SearchForConditionalPlanFunction<A, S> extends Function<NondeterministicProblem<A, S>, ConditionalPlan<A, S>> {
	@Override
	ConditionalPlan<A, S> apply(NondeterministicProblem<A, S> problem);
}
