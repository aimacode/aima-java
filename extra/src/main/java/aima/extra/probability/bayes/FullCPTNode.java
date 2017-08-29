package aima.extra.probability.bayes;

import java.util.List;
import java.util.stream.Collectors;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;

/**
 * Default implementation of the FiniteNode interface that uses a fully
 * specified ConditionalProbabilityTable to represent the node's conditional
 * distribution.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class FullCPTNode extends AbstractNode implements FiniteNode {

	// Internal fields

	/**
	 * Conditional distribution associated with the node.
	 */
	private ConditionalProbabilityTable cpt;

	/**
	 * Class type of the ProbabilityNumber values constituting the cpt.
	 */
	private Class<? extends ProbabilityNumber> clazz;

	// Constructors

	/**
	 * Constructor initializes ConditionalProbabilityTable.
	 * 
	 * @param nodeVariable
	 *            is the random variable of the node.
	 * @param valueDistribution
	 *            is the conditional probability distribution for the node given
	 *            its parents.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 */
	public FullCPTNode(RandomVariable nodeVariable, List<ProbabilityNumber> valueDistribution,
			Class<? extends ProbabilityNumber> clazz) {
		this(nodeVariable, valueDistribution, null, clazz);
	}

	/**
	 * Constructor initializes ConditionalProbabilityTable.
	 * 
	 * @param nodeVariable
	 *            is the random variable of the node.
	 * @param valueDistribution
	 *            is the conditional probability distribution for the node given
	 *            its parents.
	 * @param parents
	 *            list of parent nodes of this node.
	 * @param clazz
	 *            specifies the class type of the ProbabilityNumber
	 *            implementation to use.
	 */
	public FullCPTNode(RandomVariable nodeVariable, List<ProbabilityNumber> valueDistribution, List<Node> parents,
			Class<? extends ProbabilityNumber> clazz) {
		super(nodeVariable, parents);
		List<RandomVariable> parentRandomVariables = this.parents.stream().map(node -> node.getNodeVariable())
				.collect(Collectors.toList());
		this.clazz = clazz;
		this.cpt = new ConditionalProbabilityTable(nodeVariable, valueDistribution, parentRandomVariables, this.clazz);
	}

	// Public methods

	// START-Node

	@Override
	public ConditionalProbabilityDistribution getCPD() {
		return getCPT();
	}

	// END-Node

	// START-FiniteNode

	@Override
	public ConditionalProbabilityTable getCPT() {
		return cpt;
	}

	// END-FiniteNode
}
