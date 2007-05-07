package aima.probability.decision;

import java.util.Hashtable;

public class MDPPolicy<STATE_TYPE,ACTION_TYPE> {
	Hashtable<STATE_TYPE,ACTION_TYPE> stateToAction;
	
	public MDPPolicy(){
		stateToAction = new Hashtable<STATE_TYPE, ACTION_TYPE>();
	}
	
	public ACTION_TYPE getAction(STATE_TYPE state){
		return stateToAction.get(state);
	}
	
	public void setAction(STATE_TYPE state, ACTION_TYPE action){
		stateToAction.put(state, action);
	}

}
