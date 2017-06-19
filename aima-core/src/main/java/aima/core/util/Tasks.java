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
    // attributes

    /** Executes a given task in background. */
    public static Thread executeInBackground(Runnable task) {
        return startThreadFn.apply(task);
    }

    /** Cancels the task which is executed in the given thread. */
    public static void cancel(Thread thread) {
        cancelFn.accept(thread);
    }

    /**
     * Tests whether the current task has been cancelled. Calls of this method can be placed anywhere in the code.
     * They provide safe exits from time-consuming loops if the user is not interested in the result anymore. */
    public static boolean currIsCancelled() {
        return isCancelledFn.get();
    }


    // the default implementation uses CancellableThread

    private static Function<Runnable, Thread> startThreadFn =
            task -> {
                Thread result = new CancellableThread(task);
                result.setDaemon(true);
                result.start();
                return result;
            };

    private static Consumer<Thread> cancelFn =
            thread -> {if (thread instanceof CancellableThread) ((CancellableThread) thread).cancel();};

    private static Supplier<Boolean> isCancelledFn = CancellableThread::currIsCancelled;


    // functionality can be changed at runtime

    public static void setStartThreadFn(Function<Runnable, Thread> startThreadFn) {
        Tasks.startThreadFn = startThreadFn;
    }

    public static void setCancelFn(Consumer<Thread> cancelFn) {
        Tasks.cancelFn = cancelFn;
    }

    public static void setIsCancelledFn(Supplier<Boolean> isCancelledFn) {
        Tasks.isCancelledFn = isCancelledFn;
    }
}
