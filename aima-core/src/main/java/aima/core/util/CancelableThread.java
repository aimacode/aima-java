package aima.core.util;

/**
 * Implements a thread with an additional flag indicating cancellation.
 * 
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class CancelableThread extends Thread {

	public CancelableThread() {
	}
	
	public CancelableThread(Runnable runnable) {
		super(runnable);
	}
	
	/**
	 * Returns <code>true</code> if the current thread is canceled
	 * 
	 * @return <code>true</code> if the current thread is canceled
	 */
	public static boolean currIsCanceled() {
		if (Thread.currentThread() instanceof CancelableThread)
			return ((CancelableThread) Thread.currentThread()).isCanceled;
		return false;
	}

	private volatile boolean isCanceled;

	/**
	 * Returns <code>true</code> if this thread is canceled
	 * 
	 * @return <code>true</code> if this thread is canceled
	 */
	public boolean isCanceled() {
		return isCanceled;
	}

	/**
	 * Cancels this thread
	 */
	public void cancel() {
		isCanceled = true;
	}
}
