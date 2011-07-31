package aima.core.probability.temporal;

import java.util.List;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.proposition.AssignmentProposition;

/**
 * * The FORWARD operator is defined by Equation (15.5).<br>
 * 
 * <pre>
 * <b>P</b>(<b>X</b><sub>t+1</sub> | <b>e</b><sub>1:t+1</sub>) 
 * = &alpha;<b>P</b>(<b>e</b><sub>t+1</sub> | <b>X</b><sub>t+1</sub>)&sum;<sub><b>x</b><sub>t</sub></sub><b>P</b>(<b>X</b><sub>t+1</sub> | <b>x</b><sub>t</sub>, <b>e</b><sub>1:t</sub>)P(<b>x</b><sub>t</sub> | <b>e</b><sub>1:t</sub>)
 * = &alpha;<b>P</b>(<b>e</b><sub>t+1</sub> | <b>X</b><sub>t+1</sub>)&sum;<sub><b>x</b><sub>t</sub></sub><b>P</b>(<b>X</b><sub>t+1</sub> | <b>x</b><sub>t</sub>)P(<b>x</b><sub>t</sub> | <b>e</b><sub>1:t</sub>) (Markov Assumption)
 * </pre>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface ForwardStepInference {

	/**
	 * The FORWARD operator is defined by Equation (15.5).<br>
	 * 
	 * <pre>
	 * <b>P</b>(<b>X</b><sub>t+1</sub> | <b>e</b><sub>1:t+1</sub>) 
	 * = &alpha;<b>P</b>(<b>e</b><sub>t+1</sub> | <b>X</b><sub>t+1</sub>)&sum;<sub><b>x</b><sub>t</sub></sub><b>P</b>(<b>X</b><sub>t+1</sub> | <b>x</b><sub>t</sub>, <b>e</b><sub>1:t</sub>)P(<b>x</b><sub>t</sub> | <b>e</b><sub>1:t</sub>)
	 * = &alpha;<b>P</b>(<b>e</b><sub>t+1</sub> | <b>X</b><sub>t+1</sub>)&sum;<sub><b>x</b><sub>t</sub></sub><b>P</b>(<b>X</b><sub>t+1</sub> | <b>x</b><sub>t</sub>)P(<b>x</b><sub>t</sub> | <b>e</b><sub>1:t</sub>) (Markov Assumption)
	 * </pre>
	 * 
	 * @param f1_t
	 *            f<sub>1:t</sub>
	 * @param e_tp1
	 *            <b>e</b><sub>t+1</sub>
	 * @return f<sub>1:t+1</sub>
	 */
	CategoricalDistribution forward(CategoricalDistribution f1_t,
			List<AssignmentProposition> e_tp1);
}
