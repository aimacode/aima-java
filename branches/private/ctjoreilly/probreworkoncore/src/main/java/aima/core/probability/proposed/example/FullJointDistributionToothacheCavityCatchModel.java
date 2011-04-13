package aima.core.probability.proposed.example;

import java.util.ArrayList;
import java.util.List;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.BooleanDomain;
import aima.core.probability.proposed.model.full.FullJointDistributionModel;

public class FullJointDistributionToothacheCavityCatchModel extends
		FullJointDistributionModel {

	private RandomVariable toothacheRV = null;
	private RandomVariable cavityRV = null;
	private RandomVariable catchRV = null;

	public FullJointDistributionToothacheCavityCatchModel() {
		super(new double[] {
				// Toothache = true,  Cavity = true,  Catch = true
				0.108,
				// Toothache = true,  Cavity = true,  Catch = false
				0.012,
				// Toothache = true,  Cavity = false, Catch = true
				0.016,
				// Toothache = true,  Cavity = false, Catch = false
				0.064,
				// Toothache = false, Cavity = true,  Catch = true
				0.072,
				// Toothache = false, Cavity = true,  Catch = false
				0.008,
				// Toothache = false, Cavity = false, Catch = true
				0.144,
				// Toothache = false, Cavity = false, Catch = false
				0.576
		}, new RandomVariable("Toothache", new BooleanDomain()),
				new RandomVariable("Cavity", new BooleanDomain()),
				new RandomVariable("Catch", new BooleanDomain()));

		List<RandomVariable> vars = new ArrayList<RandomVariable>(
				getRepresentation());
		toothacheRV = vars.get(0);
		cavityRV = vars.get(1);
		catchRV = vars.get(2);
	}

	public RandomVariable getToothache() {
		return toothacheRV;
	}

	public RandomVariable getCavity() {
		return cavityRV;
	}

	public RandomVariable getCatch() {
		return catchRV;
	}
}
