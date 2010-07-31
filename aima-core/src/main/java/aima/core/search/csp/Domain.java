package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

/**
 * A domain Di consists of a set of allowable values {v1, ... , vk} for
 * the corresponding variable Xi and defines a default order on those values.
 * 
 * @author Ruediger Lunde
 */
public class Domain extends ArrayList<Object > {
	private static final long serialVersionUID = 1L;
	
	public Domain() {
		super();
	}
	
	public Domain(List<?> values) {
		super(values.size());
		for (Object value : values)
			add(value);
	}
	
}