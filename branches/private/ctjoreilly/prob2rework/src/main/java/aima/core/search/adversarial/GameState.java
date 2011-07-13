package aima.core.search.adversarial;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
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
		Set<String> keySet1 = state.keySet();
		Iterator<String> i = keySet1.iterator();
		Iterator<String> j = another.state.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
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

	/**
	 * Returns the value to which the specified key is mapped in this game
	 * state.
	 * 
	 * @param key
	 *            a key in the game state
	 * 
	 * @return the value to which the key is mapped in this game state;
	 *         <code>null</code> if the key is not mapped to any value in this
	 *         game state.
	 * 
	 * @throws NullPointerException
	 *             if the key is <code>null</code>.
	 */
	public Object get(String key) {
		return state.get(key);
	}

	/**
	 * Maps the specified <code>key</code> to the specified <code>value</code>
	 * in this game state. Neither the key nor the value can be
	 * <code>null</code>. The value can be retrieved by calling the
	 * <code>get</code> method with a key that is equal to the original key.
	 * 
	 * @param key
	 *            the game state key
	 * @param value
	 *            the value
	 * 
	 * @throws NullPointerException
	 *             if the key or value is <code>null</code>
	 */
	public void put(String key, Object value) {
		state.put(key, value);

	}
}
