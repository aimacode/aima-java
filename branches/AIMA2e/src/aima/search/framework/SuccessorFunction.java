package aima.search.framework;

import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

public interface SuccessorFunction {

	List getSuccessors(Object state);

}