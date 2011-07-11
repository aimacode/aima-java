package aima.core.logic.fol.parsing.ast;

import java.util.List;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface Term extends FOLNode {
	List<Term> getArgs();

	Term copy();
}
