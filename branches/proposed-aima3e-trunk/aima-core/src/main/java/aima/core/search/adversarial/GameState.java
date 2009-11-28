package aima.core.search.adversarial;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
 */
public class GameState {
	private Hashtable<String, Object> state;

	public GameState() {
		state = new Hashtable<String, Object>();
	}

	@Override
	public boolean equals(Object anotherState) {

		if (this == anotherState) {
			return true;
		}
		if ((anotherState == null)
				|| (this.getClass() != anotherState.getClass())) {
			return false;
		}
		GameState another = (GameState) anotherState;
		Set keySet1 = state.keySet();
		Iterator i = keySet1.iterator();
		Iterator j = another.state.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			boolean keymatched = false;
			boolean valueMatched = false;
			while (j.hasNext()) {
				String key2 = (String) j.next();
				if (key.equals(key2)) {
					keymatched = true;
					if (state.get(key).equals(another.state.get(key2))) {
						valueMatched = true;
					}
					break;
				}
			}
			if (!((keymatched) && valueMatched)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		for (String s : state.keySet()) {
			result = 37 * result + s.hashCode();
			result = 37 * result + state.get(s).hashCode();
		}

		return result;
	}

	public Object get(String key) {
		return state.get(key);
	}

	public void put(String key, Object value) {
		state.put(key, value);

	}
}
