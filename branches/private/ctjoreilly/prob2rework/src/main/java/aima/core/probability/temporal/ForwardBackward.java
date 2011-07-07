package aima.core.probability.temporal;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 576.<br>
 * <br>
 * 
 * <pre>
 * function FORWARD-BACKWARD(ev, prior) returns a vector of probability distributions
 *   inputs: ev, a vector of evidence values for steps 1,...,t
 *           prior, the prior distribution on the initial state, <b>P</b>(X<sub>0</sub>)
 *   local variables: fv, a vector of forward messages for steps 0,...,t
 *                    b, a representation of the backward message, initially all 1s
 *                    sv, a vector of smoothed estimates for steps 1,...,t
 *                    
 *   fv[0] <- prior
 *   for i = 1 to t do
 *       fv[i] <- FORWARD(fv[i-1], ev[i])
 *   for i = t downto 1 do
 *       sv[i] <- NORMALIZE(fv[i] * b)
 *       b <- BACKWARD(b, ev[i])
 *   return sv
 * </pre>
 * 
 * Figure 15.4 The forward-backward algorithm for smoothing: computing posterior
 * probabilities of a sequence of states given a sequence of observations. The
 * FORWARD and BACKWARD operators are defined by Equations (15.5) and (15.9),
 * respectively.<br>
 * <br>
 * 
 * @author Ciaran O'Reilly
 */
public class ForwardBackward {

}
