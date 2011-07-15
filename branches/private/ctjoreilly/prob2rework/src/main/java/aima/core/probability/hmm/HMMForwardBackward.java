package aima.core.probability.hmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.domain.FiniteDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.temporal.ForwardBackwardInference;
import aima.core.probability.util.ProbabilityTable;
import aima.core.util.Util;
import aima.core.util.math.Matrix;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class HMMForwardBackward implements ForwardBackwardInference {

	protected RandomVariable stateVariable = null;
	protected FiniteDomain stateVariableDomain = null;
	protected Matrix transitionModel = null;
	protected Map<Object, Matrix> sensorModel = null;

	public HMMForwardBackward(RandomVariable stateVariable,
			Matrix transitionModel, Map<Object, Matrix> sensorModel) {
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
	}

	//
	// START-ForwardBackwardInference
	@Override
	public List<CategoricalDistribution> forwardBackward(
			List<List<AssignmentProposition>> ev, CategoricalDistribution prior) {
		// local variables: fv, a vector of forward messages for steps 0,...,t
		List<Matrix> fv = new ArrayList<Matrix>(ev.size() + 1);
		// b, a representation of the backward message, initially all 1s
		Matrix b = initBackwardMessage();
		// sv, a vector of smoothed estimates for steps 1,...,t
		List<Matrix> sv = new ArrayList<Matrix>(ev.size());

		// fv[0] <- prior
		fv.add(convert(prior));
		// for i = 1 to t do
		for (int i = 0; i < ev.size(); i++) {
			// fv[i] <- FORWARD(fv[i-1], ev[i])
			fv.add(forward(fv.get(i), getEvidence(ev.get(i))));
		}
		// for i = t downto 1 do
		for (int i = ev.size() - 1; i >= 0; i--) {
			// sv[i] <- NORMALIZE(fv[i] * b)
			sv.add(0, normalize(fv.get(i + 1).arrayTimes(b)));
			// b <- BACKWARD(b, ev[i])
			b = backward(b, getEvidence(ev.get(i)));
		}

		// return sv
		return convert(sv);
	}

	@Override
	public CategoricalDistribution forward(CategoricalDistribution f1_t,
			List<AssignmentProposition> e_tp1) {
		return convert(forward(convert(f1_t), getEvidence(e_tp1)));
	}

	@Override
	public CategoricalDistribution backward(CategoricalDistribution b_kp2t,
			List<AssignmentProposition> e_kp1) {
		return convert(backward(convert(b_kp2t), getEvidence(e_kp1)));
	}

	// END-ForwardBackwardInference
	//

	public Matrix forward(Matrix f1_t, Matrix e_tp1) {
		return normalize(e_tp1.times(transitionModel.transpose().times(f1_t)));
	}

	public Matrix backward(Matrix b_kp2t, Matrix e_kp1) {
		return transitionModel.times(e_kp1).times(b_kp2t);
	}

	//
	// PROTECTED METHODS
	//
	protected Matrix initBackwardMessage() {
		double[] values = new double[stateVariableDomain.size()];
		Arrays.fill(values, 1.0);
		return new Matrix(values, values.length);
	}

	protected Matrix convert(CategoricalDistribution fromCD) {
		double[] values = fromCD.getValues();
		return new Matrix(values, values.length);
	}

	protected CategoricalDistribution convert(Matrix fromMessage) {
		return new ProbabilityTable(fromMessage.getRowPackedCopy(),
				stateVariable);
	}

	protected List<CategoricalDistribution> convert(List<Matrix> matrixs) {
		List<CategoricalDistribution> cds = new ArrayList<CategoricalDistribution>();
		for (Matrix m : matrixs) {
			cds.add(convert(m));
		}
		return cds;
	}

	protected Matrix normalize(Matrix m) {
		double[] values = m.getRowPackedCopy();
		return new Matrix(Util.normalize(values), values.length);
	}

	protected Matrix getEvidence(List<AssignmentProposition> evidence) {
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
