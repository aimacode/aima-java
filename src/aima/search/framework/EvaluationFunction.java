package aima.search.framework;

/**
 * Interface describing an evaluation function which is used to determine which
 * Nodes should be expanded based on the value returned. 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface EvaluationFunction {
	Double getValue(Problem p, Node n);
}
