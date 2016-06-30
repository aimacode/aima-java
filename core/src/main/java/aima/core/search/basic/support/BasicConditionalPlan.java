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
	List<Step> steps;

	public BasicConditionalPlan() {
		steps = new ArrayList<>();
	}

	public BasicConditionalPlan(A action, ConditionalPlan<A, S> planToExtend) {
		steps = new ArrayList<>();
		steps.add(new ActionStep(action));
		steps.addAll(((BasicConditionalPlan<A, S>) planToExtend).steps);
	}

	public BasicConditionalPlan(List<Pair<S, ConditionalPlan<A, S>>> conditionedPlans) {
		steps = conditionedPlans.stream().map(statePlan -> {
			return new ConditionedSubPlan(new Pair<S, BasicConditionalPlan<A, S>>(statePlan.getFirst(),
					(BasicConditionalPlan<A, S>) statePlan.getSecond()));
		}).collect(Collectors.toList());
	}

	// e.g. [Suck, if State =5 then [Right,Suck] else []]
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true, lastStepAction = false;
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			if (step.isAction()) {
				sb.append(step.getAction());
				lastStepAction = true;
			} else {
				if (lastStepAction) {
					sb.append("if ");
				} else {
					Step nextStep = i + 1 < steps.size() ? steps.get(i + 1) : null;
					if (nextStep == null || nextStep.isAction()) {
						sb.append("else ");
					} else {
						sb.append("else if ");
					}
				}
				sb.append(step.getConditionedSubPlan().getFirst());
				sb.append(" ");
				sb.append(step.getConditionedSubPlan().getSecond());
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