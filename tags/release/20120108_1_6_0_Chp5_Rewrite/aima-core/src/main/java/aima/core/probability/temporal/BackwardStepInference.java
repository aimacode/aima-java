package aima.core.probability.temporal;

import java.util.List;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.proposition.AssignmentProposition;

/**
 * The BACKWARD operator<br>
 * 
 * <pre>
 * <b>b</b><sub>k+1:t</sub> = <b>P</b>(<b>e</b><sub>k+1:t</sub> | <b>X</b><sub>k</sub>)
 * </pre>
 * 
 * is defined by Equation (15.9).<br>
 * 
 * <pre>
 * <b>P</b>(e<sub>k+1:t</sub> | X<sub>k</sub>) 
 * = &sum;<sub><b>x</b><sub>k+1</sub></sub><b>P</b>(<b>e</b><sub>k+1:t</sub> | <b>X</b><sub>k</sub>, <b>x</b><sub>k+1</sub>)<b>P</b>(<b>x</b><sub>k+1</sub> | <b>X</b><sub>k</sub>) (conditioning on <b>X</b><sub>k+1</sub>)
 * = &sum;<sub><b>x</b><sub>k+1</sub></sub>P(<b>e</b><sub>k+1:t</sub> | <b>x</b><sub>k+1</sub>)<b>P</b>(<b>x</b><sub>k+1</sub> | <b>X</b><sub>k</sub>) (by conditional independence)
 * = &sum;<sub><b>x</b><sub>k+1</sub></sub>P(<b>e</b><sub>k+1</sub>, <b>e</b><sub>k+2:t</sub> | <b>x</b><sub>k+1</sub>)<b>P</b>(<b>x</b><sub>k+1</sub> | <b>X</b><sub>k</sub>)
 * = &sum;<sub><b>x</b><sub>k+1</sub></sub>P(<b>e</b><sub>k+1</sub> | <b>x</b><sub>k+1</sub>)P(<b>e</b><sub>k+2:t</sub> | <b>x</b><sub>k+1</sub>)<b>P</b>(<b>x</b><sub>k+1</sub> | <b>X</b><sub>k</sub>)
 * </pre>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface BackwardStepInference {

	/**
	 * The BACKWARD operator<br>
	 * 
	 * <pre>
	 * <b>b</b><sub>k+1:t</sub> = <b>P</b>(<b>e</b><sub>k+1:t</sub> | <b>X</b><sub>k</sub>)
	 * </pre>
	 * 
	 * is defined by Equation (15.9).<br>
	 * 
	 * <pre>
	 * <b>P</b>(e<sub>k+1:t</sub> | X<sub>k</sub>) 
	 * = &sum;<sub><b>x</b><sub>k+1</sub></sub><b>P</b>(<b>e</b><sub>k+1:t</sub> | <b>X</b><sub>k</sub>, <b>x</b><sub>k+1</sub>)<b>P</b>(<b>x</b><sub>k+1</sub> | <b>X</b><sub>k</sub>) (conditioning on <b>X</b><sub>k+1</sub>)
	 * = &sum;<sub><b>x</b><sub>k+1</sub></sub>P(<b>e</b><sub>k+1:t</sub> | <b>x</b><sub>k+1</sub>)<b>P</b>(<b>x</b><sub>k+1</sub> | <b>X</b><sub>k</sub>) (by conditional independence)
	 * = &sum;<sub><b>x</b><sub>k+1</sub></sub>P(<b>e</b><sub>k+1</sub>, <b>e</b><sub>k+2:t</sub> | <b>x</b><sub>k+1</sub>)<b>P</b>(<b>x</b><sub>k+1</sub> | <b>X</b><sub>k</sub>)
	 * = &sum;<sub><b>x</b><sub>k+1</sub></sub>P(<b>e</b><sub>k+1</sub> | <b>x</b><sub>k+1</sub>)P(<b>e</b><sub>k+2:t</sub> | <b>x</b><sub>k+1</sub>)<b>P</b>(<b>x</b><sub>k+1</sub> | <b>X</b><sub>k</sub>)
	 * </pre>
	 * 
	 * @param b_kp2t
	 *            <b>b</b><sub>k+2:t</sub>
	 * @param e_kp1t
	 *            <b>e</b><sub>k+1:t</sub>
	 * @return <b>b</b><sub>k+1:t</sub>
	 */
	CategoricalDistribution backward(CategoricalDistribution b_kp2t,
			List<AssignmentProposition> e_kp1t);
}
