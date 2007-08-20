/*
 * Created on Feb 17, 2005
 *
 */
package aima.logic.demos;

import aima.logic.fol.demos.FolDemo;

/**
 * @author Ravi Mohan
 * 
 */
public class LogicDemo {
	public static void main(String[] args) {
		// propostional
		DPLLDemo.main(null);
		PLFCEntailsDemo.main(null);
		PLResolutionDemo.main(null);
		TTEntailsDemo.main(null);
		WalkSatDemo.main(null);

		// firstorder
		FolDemo.main(null);
	}
}
