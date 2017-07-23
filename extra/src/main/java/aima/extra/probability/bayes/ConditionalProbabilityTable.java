package aima.extra.probability.bayes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.util.ListOps;

/**
 * A conditional probability table, or CPT, can be used for representing
 * conditional probabilities for discrete (finite) random variables. Each row in
 * a CPT contains the conditional probability of each node value for a
 * <b>conditioning case</b>.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class ConditionalProbabilityTable extends AbstractProbabilityTable
		implements ConditionalProbabilityDistribution {

	// Internal fields

	/**
	 * The query random variable X in P(X|E) for which the conditional
	 * probability distribution is defined.
	 */
	private RandomVariable on;

	/**
	 * The list of all parent random variables (E<sub>1</sub>,
	 * E<sub>2</sub>,...) in P(X|E<sub>1</sub>,E<sub>2</sub>,...).
	 */
	private List<RandomVariable> parents;

	// Constructor

	/**
	 * Constructor to initialize the ConditionalProbabilityTable.
	 * 
	 * @param on
	 *            is the query random variable X in P(X|E).
	 * @param values
	 *            is the ordered list of probability values that form the CPT.
	 * @param conditionedOn
	 *            is the list of parent random variables.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 */
	public ConditionalProbabilityTable(RandomVariable on, List<ProbabilityNumber> values,
			List<RandomVariable> conditionedOn, Class<? extends ProbabilityNumber> clazz) {
		// The list randomVariables in AbstractProbabilityTable class consists
		// of the conditioning variable first followed by the conditionedOn
		// variables.
		super(ListOps.union(Arrays.asList(on), conditionedOn), values, clazz);
		this.on = on;
		this.parents = new ArrayList<RandomVariable>(conditionedOn);
		boolean isValid = checkEachRowTotalsOne();
		if (!isValid) {
			throw new IllegalArgumentException("Probability values of each row of the CPT do not sum to 1.0");
		}
	}

	// Public methods 
	
	// START-CategoricalDistribution
	
	/**
	 * 
	 */
	@Override
	public ConditionalProbabilityTable normalize() {
		return null;
	}

	@Override
	public ConditionalProbabilityTable marginalize(List<RandomVariable> vars) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// END-CategoricalDistribution
	
	// START-ConditionalProbabilityDistribution

	@Override
	public RandomVariable getOn() {
		return this.on;
	}

	@Override
	public List<RandomVariable> getParents() {
		return this.parents;
	}

	@Override
	public ProbabilityDistribution getConditioningCase(Map<RandomVariable, Object> parentValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProbabilityDistribution getConditioningCase(Predicate<List<Object>> proposition) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// END-ConditionalProbabilityDistribution

	// Private methods

	/**
	 * Check if each row of the CPT sums to one. Method returns true for any
	 * valid CPT.
	 * 
	 * @return true if each row adds upto one, false otherwise.
	 */
	private boolean checkEachRowTotalsOne() {
		ConditionalProbabilityTable summedOut = this.marginalize(Arrays.asList(this.on));
		boolean check = summedOut.values.stream().allMatch(value -> value.isOne());
		return check;
	}
}
