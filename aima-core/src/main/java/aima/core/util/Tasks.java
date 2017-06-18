package aima.core.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Simple facade for cancellable task management.
 *
 * @author Ruediger Lunde
 */
public class Tasks {

    // simple API to execute cancellable tasks in background - functionality is provided by three functional
    // attributes.

    /** Tests whether the current task has been cancelled. */
    public static boolean currIsCancelled() {
        return isCancelledFn.get();
    }

    /** Executes a given task in background. */
    public static Thread runInBackground(Runnable task) {
        return startThreadFn.apply(task);
    }

    /** Cancels the task which is executed in the given thread. */
    public static void cancel(Thread thread) {
        cancelFn.accept(thread);
    }


    // the default implementation uses CancellableThread

    private static Supplier<Boolean> isCancelledFn = CancellableThread::currIsCancelled;

    private static Function<Runnable, Thread> startThreadFn =
            task -> {
                Thread result = new CancellableThread(task);
                result.setDaemon(true);
                result.start();
                return result;
            };

    private static Consumer<Thread> cancelFn =
            thread -> {if (thread instanceof CancellableThread) ((CancellableThread) thread).cancel();};


    // functionality can be changed at runtime

    public static void setIsCancelledFn(Supplier<Boolean> isCancelledFn) {
        Tasks.isCancelledFn = isCancelledFn;
    }

    public static void setStartThreadFn(Function<Runnable, Thread> startThreadFn) {
        Tasks.startThreadFn = startThreadFn;
    }

    public static void setCancelFn(Consumer<Thread> cancelFn) {
        Tasks.cancelFn = cancelFn;
    }
}
