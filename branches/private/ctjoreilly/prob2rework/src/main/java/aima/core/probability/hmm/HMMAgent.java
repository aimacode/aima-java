package aima.core.probability.hmm;

/**
 * @author Ravi Mohan
 * 
 */
public class HMMAgent {

	/**
	 * <pre>
	 * public HMMAgent(HiddenMarkovModel hmm) {
	 * 	this.hmm = hmm;
	 * 	this.belief = hmm.prior().duplicate();
	 * }
	 * 
	 * public VarDistribution belief() {
	 * 	return belief;
	 * }
	 * 
	 * public void act(String action) {
	 * 	belief = hmm.predict(belief, action);
	 * }
	 * 
	 * public void waitWithoutActing() {
	 * 	act(HmmConstants.DO_NOTHING);
	 * }
	 * 
	 * public void perceive(String perception) {
	 * 	belief = hmm.perceptionUpdate(belief, perception);
	 * }
	 * </pre>
	 */
}
