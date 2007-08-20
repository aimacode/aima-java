/*
 * Created on Aug 5, 2005
 *
 */
package aima.learning.framework;

/**
 * @author Ravi Mohan
 * 
 */
import java.util.Arrays;
import java.util.List;

public class StringAttributeSpecification implements AttributeSpecification {
	String attributeName;

	List<String> attributePossibleValues;

	public StringAttributeSpecification(String attributeName,
			List<String> attributePossibleValues) {
		this.attributeName = attributeName;
		this.attributePossibleValues = attributePossibleValues;
	}

	public StringAttributeSpecification(String attributeName,
			String[] attributePossibleValues) {
		this(attributeName, Arrays.asList(attributePossibleValues));
	}

	public boolean isValid(String value) {
		return (attributePossibleValues.contains(value));
	}

	/**
	 * @return Returns the attributeName.
	 */
	public String getAttributeName() {
		return attributeName;
	}

	public List<String> possibleAttributeValues() {
		return attributePossibleValues;
	}

	public Attribute createAttribute(String rawValue) {
		return new StringAttribute(rawValue, this);
	}
}
