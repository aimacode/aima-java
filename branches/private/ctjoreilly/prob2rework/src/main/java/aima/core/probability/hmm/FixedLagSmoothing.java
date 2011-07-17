package aima.core.probability.hmm;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 580.<br>
 * <br>
 * 
 * <pre>
 * function FIXED-LAG-SMOOTHING(e<sub>t</sub>, hmm, d) returns a distribution over <b>X</b><sub>t-d</sub>
 *   inputs: e<sub>t</sub>, the current evidence from time step t
 *           hmm, a hidden Markov model with S * S transition matrix <b>T</b>
 *           d, the length of the lag for smoothing
 *   persistent: t, the current time, initially 1
 *               <b>f</b>, the forward message <b>P</b>(X<sub>t</sub> | e<sub>1:t</sub>), initially hmm.PRIOR
 *               <b>B</b>, the d-step backward transformation matrix, initially the identity matrix
 *               e<sub>t-1d:t</sub>, double-ended list of evidence from t-d to t, initially empty
 *   
 *   add e<sub>t</sub> to the end of e<sub>t-d:t</sub>
 *   <b>O</b><sub>t</sub> <- diagonal matrix containing <b>P</b>(e<sub>t</sub> | X<sub>t</sub>)
 *   if t > d then
 *        <b>f</b> <- FORWARD(<b>f</b>, e<sub>t</sub>)
 *        remote e<sub>t-d-1</sub> from the beginning of e<sub>t-d:t</sub>
 *        <b>O</b><sub>t-d</sub> <- diagonal matrix containing <b>P</b>(e<sub>t-d</sub> | X<sub>t-d</sub>)
 *        <b>B</b> <- <b>O</b><sup>-1</sup><sub>t-d</sub><b>B</b><b>T</b><b>O</b><sub>t</sub>
 *   else <b>B</b> <- <b>BTO</b><sub>t</sub>
 *   t <- t + 1
 *   if t > d then return NORMALIZE(<b>f</b> * <b>B1</b>) else return null
 * </pre>
 * 
 * Figure 15.6 An algorithm for smoothing with a fixed time lag of d steps,
 * implemented as an online algorithm that outputs the new smoothed estimate
 * given the observation for a new time step. Notice that the final output
 * NORMALIZE(<b>f</b> * <b>B1</b>) is just &alpha;<b>f</b>*<b>b</b>, by Equation
 * (15.14).
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class FixedLagSmoothing {
	/**
	 * <pre>
	 * // This implementation is almost certainly wrong (see comments below).
	 * // This is faithful to the algorithm in the book but I think there are
	 * // some errors in the algorithmas published. Need to clarify with Dr N.
	 * 
	 * private HiddenMarkovModel hmm;
	 * 
	 * private int timelag;
	 * 
	 * private ArrayList&lt;String&gt; evidenceFromSmoothedStepToPresent;
	 * 
	 * private int time;
	 * 
	 * private VarDistribution forwardMessage;
	 * 
	 * private Matrix B;
	 * 
	 * public FixedLagSmoothing(HiddenMarkovModel hmm, int timelag) {
	 * 	this.hmm = hmm;
	 * 	this.timelag = timelag;
	 * 	this.evidenceFromSmoothedStepToPresent = new ArrayList&lt;String&gt;();
	 * 	this.time = 1;
	 * 	this.forwardMessage = hmm.prior();
	 * 	this.B = hmm.transitionModel().unitMatrix();
	 * }
	 * 
	 * public VarDistribution smooth(String perception) {
	 * 
	 * 	evidenceFromSmoothedStepToPresent.add(perception);
	 * 	Matrix O_t = hmm.sensorModel().asMatrix(perception);
	 * 	Matrix transitionMatrix = hmm.transitionModel().asMatrix();
	 * 	if (time &gt; timelag) {
	 * 
	 * 		forwardMessage = hmm.forward(forwardMessage, perception); // This
	 * 		// seems
	 * 		// WRONG
	 * 		// I think this should be
	 * 		// forwardMessage = hmm.forward(forwardMessage,
	 * 		// evidenceFromSmoothedStepToPresent.get(0));
	 * 		// this the perception at t-d. the book's algorithm
	 * 		// uses the latest perception.
	 * 		evidenceFromSmoothedStepToPresent.remove(0);
	 * 		Matrix O_t_minus_d = hmm.sensorModel().asMatrix(
	 * 				evidenceFromSmoothedStepToPresent.get(0));
	 * 
	 * 		B = O_t_minus_d.inverse().times(
	 * 				transitionMatrix.inverse().times(
	 * 						B.times(transitionMatrix.times(O_t))));
	 * 
	 * 	} else {
	 * 
	 * 		B = B.times(transitionMatrix.times(O_t));
	 * 
	 * 	}
	 * 	time += 1;
	 * 	if (time &gt; timelag) {
	 * 
	 * 		Matrix one = hmm.prior().createUnitBelief().asMatrix();
	 * 		Matrix forwardMatrix = forwardMessage.asMatrix();
	 * 		VarDistribution result = hmm.prior().duplicate();
	 * 		Matrix backwardMessage = (B.times(one));
	 * 
	 * 		result.updateFrom(forwardMatrix.arrayTimes(backwardMessage));
	 * 
	 * 		result.normalize();
	 * 		return result;
	 * 	} else {
	 * 		return null;
	 * 	}
	 * }
	 * </pre>
	 */
}
