package aima.basic;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * @author Ravi Mohan
 * 
 */
public class ObjectWithDynamicAttributes {
	private Hashtable<Object, Object> attributes = new Hashtable<Object, Object>();

	public void setAttribute(Object key, Object value) {
		attributes.put(key, value);
	}

	public Object getAttribute(Object key) {
		return attributes.get(key);
	}

	public Iterator<Object> getSortedAttributeKeys() {
		// Want to guarantee the keys are returned back ordered
		// Note: This is an inefficient implementation as it creates a new
		// TreeSet each time it is called
		// Ideally you be an instance variable that is only updated on the
		// setAttribute() call.
		return (new TreeSet<Object>(attributes.keySet())).iterator();
	}
}
