package aima.core.logic.basic.firstorder.domain;

import java.util.EventObject;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class FOLDomainEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	public FOLDomainEvent(Object source) {
		super(source);
	}

	public abstract void notifyListener(FOLDomainListener listener);
}
