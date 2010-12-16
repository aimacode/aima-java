package aima.core.probability.proposed.model.impl.bayes;

import aima.core.probability.proposed.model.RandomVariable;

public class FiniteDiscreteNode extends Node {

	private ConditionalProbabilityTable cpt = null;
	
	public FiniteDiscreteNode(RandomVariable var, double[] distribution) {
		this(var, distribution, (Node[]) null);
	}
	
	public FiniteDiscreteNode(RandomVariable var, double[] values, Node... parents) {
		super(var, parents);
		
		RandomVariable[] conditionedOn = new RandomVariable[getParents().size()];
		int i = 0;
		for (Node p : getParents()) {
			conditionedOn[i++] = p.getRandomVariable();
		}
		
		cpt = new ConditionalProbabilityTable(var, values, conditionedOn);
	}
	
	public ConditionalProbabilityTable getCPT() {
		return cpt;
	}
}
