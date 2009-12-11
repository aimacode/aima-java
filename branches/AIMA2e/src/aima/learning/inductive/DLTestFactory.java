/*
 * Created on Aug 2, 2005
 *
 */
package aima.learning.inductive;

/**
 * @author Ravi Mohan
 * 
 */
import java.util.ArrayList;
import java.util.List;

import aima.learning.framework.DataSet;

public class DLTestFactory {

	public List<DLTest> createDLTestsWithAttributeCount(DataSet ds, int i) {
		if (i != 1) {
			throw new RuntimeException(
					"For now DLTests with only 1 attribute can be craeted , not"
							+ i);
		}
		List<String> nonTargetAttributes = ds.getNonTargetAttributes();
		String targetAttribute = ds.getTargetAttributeName();
		List<String> targetValues = ds
				.getPossibleAttributeValues(targetAttribute);
		List<DLTest> tests = new ArrayList<DLTest>();
		for (String ntAttribute : nonTargetAttributes) {
			List<String> ntaValues = ds.getPossibleAttributeValues(ntAttribute);
			for (String ntaValue : ntaValues) {

				DLTest test = new DLTest();
				test.add(ntAttribute, ntaValue);
				tests.add(test);

			}
		}
		return tests;
	}

}
