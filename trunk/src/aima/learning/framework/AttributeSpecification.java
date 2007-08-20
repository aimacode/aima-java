/*
 * Created on Aug 5, 2005
 *
 */
package aima.learning.framework;

/**
 * @author Ravi Mohan
 * 
 */

public interface AttributeSpecification {

	boolean isValid(String string);

	String getAttributeName();

	Attribute createAttribute(String rawValue);

}
