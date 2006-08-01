package aima.search.framework;

import java.util.List;

public interface SuccessorFunction {

	List getSuccessors(Object state);

}