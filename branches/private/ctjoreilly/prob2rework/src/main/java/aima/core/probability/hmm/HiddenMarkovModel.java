package aima.core.probability.hmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import aima.core.util.Util;
import aima.core.util.math.Matrix;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 578.<br>
 * <br>
 * 
 * The hidden Markov model, or HMM. An HMM is a temporal probabilistic model in
 * which the state of the process is described by a single discrete random
 * variable. The possible values of the variable are the possible states of the
 * world.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class HiddenMarkovModel {

	private RandomVariable stateVariable = null;
	protected FiniteDomain stateVariableDomain = null;
	private Matrix transitionModel = null;
	private Map<Object, Matrix> sensorModel = null;
	private Matrix prior = null;

	public HiddenMarkovModel(RandomVariable stateVariable,
			Matrix transitionModel, Map<Object, Matrix> sensorModel,
			Matrix prior) {
		if (!stateVariable.getDomain().isFinite()) {
			throw new IllegalArgumentException(
					"State Variable for HHM must be finite.");
		}
		this.stateVariable = stateVariable;
		stateVariableDomain = (FiniteDomain) stateVariable.getDomain();
		if (transitionModel.getRowDimension() != transitionModel
				.getColumnDimension()) {
			throw new IllegalArgumentException(
					"Transition Model row and column dimensions must match.");
		}
		if (stateVariableDomain.size() != transitionModel.getRowDimension()) {
			throw new IllegalArgumentException(
					"Transition Model Matrix does not map correctly to the HMM's State Variable.");
		}
		this.transitionModel = transitionModel;
		for (Matrix smVal : sensorModel.values()) {
			if (smVal.getRowDimension() != smVal.getColumnDimension()) {
				throw new IllegalArgumentException(
						"Sensor Model row and column dimensions must match.");
			}
			if (stateVariableDomain.size() != smVal.getRowDimension()) {
				throw new IllegalArgumentException(
						"Sensor Model Matrix does not map correctly to the HMM's State Variable.");
			}
		}
		this.sensorModel = sensorModel;
		if (transitionModel.getRowDimension() != prior.getRowDimension()
				&& prior.getColumnDimension() != 1) {
			throw new IllegalArgumentException(
					"Prior is not of the correct dimensions.");
		}
		this.prior = prior;
	}

	public RandomVariable getStateVariable() {
		return stateVariable;
	}

	public Matrix getTransitionModel() {
		return transitionModel;
	}

	public Map<Object, Matrix> getSensorModel() {
		return sensorModel;
	}

	public Matrix getPrior() {
		return prior;
	}

	public Matrix createUnitMessage() {
		double[] values = new double[stateVariableDomain.size()];
		Arrays.fill(values, 1.0);
		return new Matrix(values, values.length);
	}

	public Matrix convert(CategoricalDistribution fromCD) {
		double[] values = fromCD.getValues();
		return new Matrix(values, values.length);
	}

	public CategoricalDistribution convert(Matrix fromMessage) {
		return new ProbabilityTable(fromMessage.getRowPackedCopy(),
				stateVariable);
	}

	public List<CategoricalDistribution> convert(List<Matrix> matrixs) {
		List<CategoricalDistribution> cds = new ArrayList<CategoricalDistribution>();
		for (Matrix m : matrixs) {
			cds.add(convert(m));
		}
		return cds;
	}

	public Matrix normalize(Matrix m) {
		double[] values = m.getRowPackedCopy();
		return new Matrix(Util.normalize(values), values.length);
	}

	public Matrix getEvidence(List<AssignmentProposition> evidence) {
		if (evidence.size() != 1) {
			throw new IllegalArgumentException(
					"Only a single evidence observation value should be provided.");
		}
		Matrix e = sensorModel.get(evidence.get(0).getValue());
		if (null == e) {
			throw new IllegalArgumentException(
					"Evidence does not map to sensor model.");
		}
		return e;
	}
}
