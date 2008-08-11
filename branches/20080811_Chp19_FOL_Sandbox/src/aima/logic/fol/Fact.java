/*
 * Created on Sep 22, 2004
 *
 */
package aima.logic.fol;

import aima.logic.fol.parsing.ast.FOLNode;
import aima.logic.fol.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */

public class Fact {

	private Sentence original;

	public Fact(Sentence sentence) {
		this.original = sentence;
	}

	@Override
	public String toString() {
		return original.toString();
	}

	public FOLNode predicate() {
		return original;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		Fact f = (Fact) o;
		return f.predicate().equals(predicate());

	}

	@Override
	public int hashCode() {
		return 0;
	}

}