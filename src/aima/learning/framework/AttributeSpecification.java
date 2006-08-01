/*
 * Created on Aug 5, 2005
 *
 */
package aima.learning.framework;


public interface AttributeSpecification {

	boolean isValid(String string);

	String getAttributeName();
	
	Attribute createAttribute(String rawValue);

}
