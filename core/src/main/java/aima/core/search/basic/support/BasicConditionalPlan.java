package aima.core.search.basic.support;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import aima.core.search.api.ConditionalPlan;
import aima.core.util.datastructure.Pair;

/**
 * Basic implementation of a conditional plan.
 * 
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the state space
 *
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 * @author Anurag Rai
 */
public class BasicConditionalPlan<A, S> implements ConditionalPlan<A, S> {
	List<Step> steps = new ArrayList<>();

	public BasicConditionalPlan() {
	}

	public BasicConditionalPlan(A action, ConditionalPlan<A, S> planToExtend) {
		steps.add(new ActionStep(action));
		steps.addAll(((BasicConditionalPlan<A, S>) planToExtend).steps);
	}

	public BasicConditionalPlan(List<Pair<S, ConditionalPlan<A, S>>> conditionedPlans) {
		if (conditionedPlans.size() == 1) {
			// Only one conditioned plan means no state needs to be tested (i.e.
			// like a trailing else)
			BasicConditionalPlan<A, S> bcp = (BasicConditionalPlan<A, S>) conditionedPlans.get(0).getSecond();
			steps.addAll(bcp.steps);
		} else {
			steps = conditionedPlans.stream().map(statePlan -> {
				return new ConditionedSubPlan(new Pair<S, BasicConditionalPlan<A, S>>(statePlan.getFirst(),
						(BasicConditionalPlan<A, S>) statePlan.getSecond()));
			}).collect(Collectors.toList());
		}
	}

	@Override
	public ConditionalPlan.Interator<A, S> iterator() {
		return new ConditionalPlan.Interator<A, S>() {
			int idx = 0;
			ConditionalPlan.Interator<A, S> subIterator = null;

			public A next(S state) {
				A action = null;

				if (idx < steps.size() && subIterator == null) {
					Step s = steps.get(idx++);
					if (s.isAction()) {
						action = s.getAction();
					} else {
						while (!state.equals(s.getConditionedSubPlan().getFirst())) {
							if (idx >= steps.size()) {
								s = null;
								break;
							}
							s = steps.get(idx++);
						}
						if (s != null) {
							subIterator = s.getConditionedSubPlan().getSecond().iterator();
						}
					}
				}

				if (subIterator != null) {
					action = subIterator.next(state);
				}

				return action;
			}
		};
	}

	// e.g. [Suck, if State =5 then [Right,Suck] else []]
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean lastStepAction = true;
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			Step nextStep = i + 1 < steps.size() ? steps.get(i + 1) : null;
			if (step.isAction()) {
				sb.append(step.getAction());
				lastStepAction = true;
				if (nextStep != null) {
					sb.append(", ");
				}
			} else {
				if (lastStepAction) {
					sb.append("if State = ");
					sb.append(step.getConditionedSubPlan().getFirst());
					sb.append(" then ");
					sb.append(step.getConditionedSubPlan().getSecond());
				} else {
					if (nextStep == null || nextStep.isAction()) {
						sb.append(" else ");
						sb.append(step.getConditionedSubPlan().getSecond());
					} else {
						sb.append(" else if State = ");
						sb.append(step.getConditionedSubPlan().getFirst());
						sb.append(" then ");
						sb.append(step.getConditionedSubPlan().getSecond());
					}
				}

				lastStepAction = false;
			}
		}

		sb.append("]");

		return sb.toString();
	}

	//
	// Supporting Code
	abstract class Step {
		boolean isAction() {
			return false;
		}

		A getAction() {
			throw new UnsupportedOperationException("getAction()");
		}

		boolean isConditionedSubPlan() {
			return false;
		}

		Pair<S, BasicConditionalPlan<A, S>> getConditionedSubPlan() {
			throw new UnsupportedOperationException("getSubPlan");
		}
	}

	class ActionStep extends Step {
		A action;

		ActionStep(A action) {
			this.action = action;
		}

		@Override
		boolean isAction() {
			return true;
		}

		@Override
		A getAction() {
			return action;
		}
	}

	class ConditionedSubPlan extends Step {
		Pair<S, BasicConditionalPlan<A, S>> conditionedSubPlan;

		ConditionedSubPlan(Pair<S, BasicConditionalPlan<A, S>> conditionedSubPlan) {
			this.conditionedSubPlan = conditionedSubPlan;
		}

		@Override
		public boolean isConditionedSubPlan() {
			return true;
		}

		@Override
		Pair<S, BasicConditionalPlan<A, S>> getConditionedSubPlan() {
			return conditionedSubPlan;
		}
	}
}