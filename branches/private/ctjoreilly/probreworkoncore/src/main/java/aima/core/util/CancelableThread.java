package aima.core.util;

/**
 * Implements a thread with an additional flag indicating cancellation.
 * 
 * @author R. Lunde
 * 
 */
public class CancelableThread extends Thread {

	public static boolean currIsCanceled() {
		if (Thread.currentThread() instanceof CancelableThread)
			return ((CancelableThread) Thread.currentThread()).isCanceled;
		return false;
	}

	private boolean isCanceled;

	public boolean isCanceled() {
		return isCanceled;
	}

	public void cancel() {
		isCanceled = true;
	}
}
