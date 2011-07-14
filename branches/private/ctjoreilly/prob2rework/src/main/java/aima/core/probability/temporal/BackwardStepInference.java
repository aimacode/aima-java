package aima.core.probability.temporal;

import java.util.List;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.proposition.AssignmentProposition;

public interface BackwardStepInference {

	/**
	 * The BACKWARD operator is defined by Equation (15.9).<br>
	 * 
	 * <pre>
	 * <b>P</b>(e<sub>k+1:t</sub> | X<sub>k</sub>) 
	 * = &sum;<sub>x<sub>k+1</sub></sub><b>P</b>(e<sub>k+1:t</sub> | X<sub>k</sub>, x<sub>k+1</sub>)<b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>) (conditioning on X<sub>k+1</sub>)
	 * = &sum;<sub>x<sub>k+1</sub></sub>P(e<sub>k+1:t</sub> | x<sub>k+1</sub>)<b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>) (by conditional independence)
	 * = &sum;<sub>x<sub>k+1</sub></sub>P(e<sub>k+1</sub>, e<sub>k+2:t</sub> | x<sub>k+1</sub>)<b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>)
	 * = &sum;<sub>x<sub>k+1</sub></sub>P(e<sub>k+1</sub> | x<sub>k+1</sub>)P(e<sub>k+2:t</sub> | x<sub>k+1</sub>)<b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>)
	 * </pre>
	 * 
	 * @param b_kp2t
	 * @param e_kp1
	 * @return b<sub>k+1:t</sub>
	 */
	CategoricalDistribution backward(CategoricalDistribution b_kp2t,
			List<AssignmentProposition> e_kp1);
}
