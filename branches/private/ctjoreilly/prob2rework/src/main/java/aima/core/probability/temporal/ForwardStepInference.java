package aima.core.probability.temporal;

import java.util.List;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.proposition.AssignmentProposition;

public interface ForwardStepInference {

	/**
	 * The FORWARD operator is defined by Equation (15.5).<br>
	 * 
	 * <pre>
	 * <b>P</b>(X<sub>t+1</sub> | e<sub>1:t+1</sub>) 
	 * = &alpha;<b>P</b>(e<sub>t+1</sub> | X<sub>t+1</sub>)&sum;<sub>x<sub>t</sub></sub><b>P</b>(X<sub>t+1</sub> | x<sub>t</sub>, e<sub>1:t</sub>)P(x<sub>t</sub> | e<sub>1:t</sub>)
	 * = &alpha;<b>P</b>(e<sub>t+1</sub> | X<sub>t+1</sub>)&sum;<sub>x<sub>t</sub></sub><b>P</b>(X<sub>t+1</sub> | x<sub>t</sub>)P(x<sub>t</sub> | e<sub>1:t</sub>) (Markov Assumption)
	 * </pre>
	 * 
	 * @param f1_t
	 *            f<sub>1:t</sub>
	 * @param e_tp1
	 *            e<sub>t+1</sub>
	 * @return f<sub>1:t+1</sub>
	 */
	CategoricalDistribution forward(CategoricalDistribution f1_t,
			List<AssignmentProposition> e_tp1);
}
