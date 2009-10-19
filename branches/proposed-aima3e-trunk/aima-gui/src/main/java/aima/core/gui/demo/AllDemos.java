package aima.core.gui.demo;

import aima.core.gui.demo.learning.LearningDemo;
import aima.core.gui.demo.logic.LogicDemo;
import aima.core.gui.demo.probability.ProbabilityDemo;
import aima.core.gui.demo.search.SearchDemo;

/**
 * @author RaviMohan
 * 
 */

public class AllDemos {
	public static void main(String[] args) {
		SearchDemo.main(null);
		LogicDemo.main(null);
		ProbabilityDemo.main(null);
		LearningDemo.main(null);
	}
}