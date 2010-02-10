package aima.core.learning.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */
public class DataSetSpecification {
	List<AttributeSpecification> attributeSpecifications;

	private String targetAttribute;

	public DataSetSpecification() {
		this.attributeSpecifications = new ArrayList<AttributeSpecification>();
	}

	public boolean isValid(List<String> uncheckedAttributes) {
		if (attributeSpecifications.size() != uncheckedAttributes.size()) {
			throw new RuntimeException("size mismatch specsize = "
					+ attributeSpecifications.size() + " attrbutes size = "
					+ uncheckedAttributes.size());
		}
		Iterator<AttributeSpecification> attributeSpecIter = attributeSpecifications
				.iterator();
		Iterator<String> valueIter = uncheckedAttributes.iterator();
		while (valueIter.hasNext() && attributeSpecIter.hasNext()) {
			if (!(attributeSpecIter.next().isValid(valueIter.next()))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return Returns the targetAttribute.
	 */
	public String getTarget() {
		return targetAttribute;
	}

	public List<String> getPossibleAttributeValues(String attributeName) {
		for (AttributeSpecification as : attributeSpecifications) {
			if (as.getAttributeName().equals(attributeName)) {
				return ((StringAttributeSpecification) as)
						.possibleAttributeValues();
			}
		}
		throw new RuntimeException("No such attribute" + attributeName);
	}

	public List<String> getAttributeNames() {
		List<String> names = new ArrayList<String>();
		for (AttributeSpecification as : attributeSpecifications) {
			names.add(as.getAttributeName());
		}
		return names;
	}

	public void defineStringAttribute(String name, String[] attributeValues) {
		attributeSpecifications.add(new StringAttributeSpecification(name,
				attributeValues));
		setTarget(name);// target defaults to last column added
	}

	/**
	 * @param target
	 *            The targetAttribute to set.
	 */
	public void setTarget(String target) {
		this.targetAttribute = target;
	}

	public AttributeSpecification getAttributeSpecFor(String name) {
		for (AttributeSpecification spec : attributeSpecifications) {
			if (spec.getAttributeName().equals(name)) {
				return spec;
			}
		}
		throw new RuntimeException("no attribute spec for  " + name);
	}

	public void defineNumericAttribute(String name) {
		attributeSpecifications.add(new NumericAttributeSpecification(name));
	}

	public List<String> getNamesOfStringAttributes() {
		List<String> names = new ArrayList<String>();
		for (AttributeSpecification spec : attributeSpecifications) {
			if (spec instanceof StringAttributeSpecification) {
				names.add(spec.getAttributeName());
			}
		}
		return names;
	}
}
