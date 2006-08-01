package aima.basic;

import java.util.Hashtable;

public class ObjectWithDynamicAttributes {
	private Hashtable<Object,Object> attributes = new Hashtable<Object,Object> ();

	public void setAttribute(Object key, Object value) {
		attributes.put(key, value);
	}

	public Object getAttribute(Object key) {
		return attributes.get(key);
	}
}