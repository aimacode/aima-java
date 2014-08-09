package aima.core.util.datastructure;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 79.<br>
 * <br>
 * The operations on a queue are as follows:<br>
 * <ul>
 * <li>EMPTY?(queue) returns true only if there are no elements in the queue</li>
 * <li>POP(queue) removes the first element of the queue and returns it.</li>
 * <li>INSERT(element, queue) inserts an element and returns the resulting
 * queue.</li>
 * </ul>
 * Note: This extends the java.util.Queue collections interface in order to take
 * advantage of pre-existing implementations. The intent of this interface is
 * purely to provide an interface to Queues that corresponds to what is
 * described in AIMA3e.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public interface Queue<E> extends java.util.Queue<E> {
	/**
	 * EMPTY?(queue)
	 * 
	 * @return true only if there are no elements on the queue.
	 */
	boolean isEmpty();

	/**
	 * POP(queue)
	 * 
	 * @return the first element of the queue.
	 */
	E pop();

	/**
	 * INSERT(element, queue)
	 * 
	 * @param element
	 *            to be inserted in the queue.
	 * @return the resulting queue with the element inserted. null is returned
	 *         if the element could not be inserted.
	 */
	Queue<E> insert(E element);
}