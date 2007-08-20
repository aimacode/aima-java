/*
 * Created on Jul 25, 2005
 *
 */
package aima.learning.inductive;

import aima.learning.framework.Example;
import aima.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public class ConstantDecisonTree extends DecisionTree {
	// represents leaf nodes like "Yes" or "No"
	private String value;

	public ConstantDecisonTree(String value) {
		this.value = value;
	}

	public void addLeaf(String attributeValue, String decision) {
		throw new RuntimeException("cannot add Leaf to ConstantDecisonTree");
	}

	public void addNode(String attributeValue, DecisionTree tree) {
		throw new RuntimeException("cannot add Node to ConstantDecisonTree");
	}

	public Object predict(Example e) {
		return value;
	}

	public String toString() {
		return "DECISION -> " + value;
	}

	public String toString(int depth, StringBuffer buf) {
		buf.append(Util.ntimes("\t", depth + 1));
		buf.append("DECISION -> " + value + "\n");
		return buf.toString();
	}
}
