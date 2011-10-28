package aima.core.learning.framework;

/**
 * @author Ravi Mohan
 * 
 */
public class NumericAttribute implements Attribute {
	double value;

	private NumericAttributeSpecification spec;

	public NumericAttribute(double rawValue, NumericAttributeSpecification spec) {
		this.value = rawValue;
		this.spec = spec;
	}

	public String valueAsString() {
		return Double.toString(value);
	}

	public String name() {
		return spec.getAttributeName().trim();
	}

	public double valueAsDouble() {
		return value;
	}
}
