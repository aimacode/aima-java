package aima.core.probability.hmm;

import java.util.List;
import java.util.Map;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
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
public interface HiddenMarkovModel {

	/**
	 * Return the single discrete random variable used to describe the process
	 * state.
	 * 
	 * @return the single discrete random variable used to describe the process
	 *         state.
	 */
	RandomVariable getStateVariable();

	/**
	 * Return the transition model:<br>
	 * <b>P</b>(X<sub>t</sub> | X<sub>t-1</sub>)<br>
	 * is represented by an S * S matrix <b>T</b> where<br>
	 * <b>T</b><sub>ij</sub> = P(X<sub>t</sub> = j | X<sub>t-1</sub> = i).
	 * 
	 * @return the transition model in Matrix form.
	 */
	Matrix getTransitionModel();

	/**
	 * Return the sensor model in matrix form:<br>
	 * P(e<sub>t</sub> | X<sub>t</sub> = i) for each state i.<br>
	 * For mathematical convenience we place each of these values into an S * S
	 * diagonal matrix.
	 * 
	 * @return the sensor model in matrix form.
	 */
	Map<Object, Matrix> getSensorModel();

	/**
	 * Return the prior distribution represented as a column vector in Matrix
	 * form.
	 * 
	 * @return the prior distribution represented as a column vector in Matrix
	 *         form.
	 */
	Matrix getPrior();

	/**
	 * Get the specific evidence matrix based on assigned evidence value.
	 * 
	 * @param evidence
	 *            the evidence assignment e.
	 * @return the Matrix representation of this evidence assignment from the
	 *         sensor model.
	 */
	Matrix getEvidence(List<AssignmentProposition> evidence);

	/**
	 * Return a new column vector in matrix form with all values set to 1.0.
	 * 
	 * @return a new column vector in matrix form with all values set to 1.0.
	 */
	Matrix createUnitMessage();

	/**
	 * Convert a Categorical Distribution into a column vector in Matrix form.
	 * 
	 * @param fromCD
	 *            the categorical distribution to be converted.
	 * @return a column vector in Matrix form of the passed in categorical
	 *         distribution.
	 */
	Matrix convert(CategoricalDistribution fromCD);

	/**
	 * Convert a column vector in Matrix form to a Categorical Distribution.
	 * 
	 * @param fromMessage
	 *            the column vector in Matrix form to be converted.
	 * @return a categorical distribution representation of the passed in column
	 *         vector.
	 */
	CategoricalDistribution convert(Matrix fromMessage);

	/**
	 * Convert a list of column vectors in Matrix form into a corresponding list
	 * of Categorical Distributions.
	 * 
	 * @param matrixs
	 *            the column vectors in matrix form to be converted.
	 * @return a corresponding list of Categorical Distribution representation
	 *         of the passed in column vectors.
	 */
	List<CategoricalDistribution> convert(List<Matrix> matrixs);

	/**
	 * Create a normalized column vector in matrix form of the passed in column
	 * vector.
	 * 
	 * @param m
	 *            a column vector representation in matrix form.
	 * @return a normalized column vector of the passed in column vector.
	 */
	Matrix normalize(Matrix m);
}
