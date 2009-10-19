package aima.core.search.framework;

import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

public interface SuccessorFunction {

	List<Successor> getSuccessors(Object state);

}