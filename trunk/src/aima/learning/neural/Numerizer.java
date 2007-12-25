/*
 * Created on Aug 6, 2005
 *
 */
package aima.learning.neural;

import java.util.List;

import aima.learning.framework.Example;
import aima.util.Pair;

/**
 * @author Ravi Mohan
 * 
 */

public interface Numerizer {
	// A Numerizer understands how to convert an example from a particular
	// dataset
	// into a Pair of lists of doubles .The first represents the input to the
	// neural network and the second represents the desired output
	// See IrisDataSetNumerizer for a concrete example
	Pair<List<Double>, List<Double>> numerize(Example e);

	String denumerize(List<Double> outputValue);

}
