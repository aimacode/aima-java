/*
 * Created on Jul 17, 2004
 *
 */
package aima.search.framework;

import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

public interface NodeStore {
	public void add(Node anItem);

	public Node remove();

	public void add(List<Node> nodes);

	public boolean isEmpty();

	public int size();
}