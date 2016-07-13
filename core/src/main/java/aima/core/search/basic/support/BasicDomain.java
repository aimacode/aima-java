package aima.core.search.basic.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.search.api.Domain;

/**
 * Basic implementation of the Domain interface.
 * 
 * @author Ciaran O'Reilly
 */
public class BasicDomain implements Domain {
	private Set<Object> values = new LinkedHashSet<>();
	private List<Object> listValues;
	
	
	public BasicDomain(Object[] values) {
		for (Object value : values) {
			this.values.add(value);
		}
		this.listValues = Collections.unmodifiableList(new ArrayList<>(this.values));
	}
	
	@Override
	public List<Object> getValues() {
		return listValues;
	}
	
	@Override
	public boolean delete(Object value) {
		boolean deleted = values.remove(value);
		if (deleted) {
			listValues = Collections.unmodifiableList(new ArrayList<>(values));
		}
		return deleted;
	}	
}
