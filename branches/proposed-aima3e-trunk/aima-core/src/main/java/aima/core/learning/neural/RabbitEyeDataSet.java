package aima.core.learning.neural;

import java.util.ArrayList;

/**
 * @author Ravi Mohan
 * 
 */
public class RabbitEyeDataSet extends NNDataSet {

	@Override
	public void setTargetColumns() {
		// assumed that data from file has been pre processed
		// TODO this should be
		// somewhere else,in the
		// super class.
		// Type != class Aargh! I want more
		// powerful type systems
		targetColumnNumbers = new ArrayList<Integer>();

		targetColumnNumbers.add(1); // using zero based indexing
	}
}
