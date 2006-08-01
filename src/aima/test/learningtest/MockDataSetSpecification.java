/*
 * Created on Jul 26, 2005
 *
 */
package aima.test.learningtest;

import java.util.ArrayList;
import java.util.List;

import aima.learning.framework.DataSetSpecification;

public class MockDataSetSpecification extends DataSetSpecification {
	private String targetAttribute;

	public MockDataSetSpecification(String targetAttributeName){
		this.targetAttribute = targetAttributeName;
	}
	public List<String> getAttributeNames() {
		return new ArrayList<String>();
	}
}
