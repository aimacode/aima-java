package aima.gui.demo.search.tree.algorithm;

import aima.core.search.api.Problem;
import aima.extra.instrument.search.TreeSearchInstrumented;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Semaphore;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchAlgoSimulator<A, S> extends Service<Void> {

    public enum SimulatorState {
        NOT_STARTED,
        SEARCHING_FOR_SOLUTUION,
        SOLUTION_FOUND,
        FAILURE_ENCOUNTERED
    }

    private ObjectProperty<Problem<A, S>> problem = new SimpleObjectProperty<>();
    private ObjectProperty<SimulatorState> state = new SimpleObjectProperty<>(SimulatorState.NOT_STARTED);
    private BooleanProperty atSolution = new SimpleBooleanProperty(false);
    private BooleanProperty atFailure = new SimpleBooleanProperty(false);
    private ObjectProperty<List<A>> solution = new SimpleObjectProperty<>(Collections.emptyList());
    private LongProperty executionDelay = new SimpleLongProperty(0);
    private IntegerProperty currentExecutionIndex = new SimpleIntegerProperty(-1);
    private ObjectProperty<ObservableList<TreeSearchInstrumented.Cmd<A, S>>> executed = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    //
    private Semaphore callLock = new Semaphore(1);

    public interface Observer<A, S> {
        void setSimulator(TreeSearchAlgoSimulator<A, S> simulator);
    }

    public TreeSearchAlgoSimulator() {

        atSolution.bind(Bindings.createBooleanBinding(() -> {
            boolean result = false;
            if (isExecutionStarted()
                    && isCurrentExecutionIndexAtEnd()
                    && executed.get().get(executed.get().size()-1).commandId() == TreeSearchInstrumented.CMD_SOLUTION) {
                result = true;
            }
            return result;
        }, currentExecutionIndexProperty()));
        atFailure.bind(Bindings.createBooleanBinding(() -> {
            boolean result = false;
            if (isExecutionStarted()
                    && isCurrentExecutionIndexAtEnd()
                    && executed.get().get(executed.get().size()-1).commandId() == TreeSearchInstrumented.CMD_FAILURE) {
                result = true;
            }
            return result;
        }, currentExecutionIndexProperty()));
    }

    public Problem<A, S> getProblem() {
        return problem.get();
    }

    public void setProblem(Problem<A, S> problem) {
        cancel();
        this.problem.set(problem);
        restart();
    }

    public ObjectProperty<Problem<A, S>> problemProperty() {
        return problem;
    }

    public SimulatorState getSimulatorState() {
        return state.get();
    }

    public ReadOnlyObjectProperty<SimulatorState> simulatorStateProperty() {
        return state;
    }

    public boolean isAtSolution() {
        return atSolution.get();
    }

    public ReadOnlyBooleanProperty atSolutionProperty() {
        return atSolution;
    }

    public boolean isAtFailure() {
        return atFailure.get();
    }

    public ReadOnlyBooleanProperty atFailureProperty() {
        return atFailure;
    }

    public List<A> getSolution() {
        return solution.get();
    }

    public ReadOnlyObjectProperty<List<A>> solutionProperty() {
        return solution;
    }

    public long getExecutionDelay() {
        return executionDelay.get();
    }

    public void setExecutionDelay(long executionDelay) {
        this.executionDelay.set(executionDelay);
    }

    public LongProperty executionDelayProperty() {
        return executionDelay;
    }

    public boolean isCurrentExecutionIndexAtEnd() {
        return currentExecutionIndex.get() >= executed.get().size() -1;
    }

    public void incCurrentExecutionIndex() {
        if (isExecutionStarted() && getCurrentExecutionIndex() < executed.get().size()-1) {
            currentExecutionIndex.set(currentExecutionIndex.get()+1);
        }
    }

    public void decCurrentExecutionIndex() {

        if (isExecutionStarted() && getCurrentExecutionIndex() > 0) {
            currentExecutionIndex.set(currentExecutionIndex.get()-1);
        }
    }

    public int getCurrentExecutionIndex() {
        return currentExecutionIndex.get();
    }

    public void setCurrentExecutionIndex(int index) {
        if (isExecutionStarted() && index >= 0 && index < executed.get().size()) {
            currentExecutionIndex.set(index);
        }
    }

    public void setCurrentExecutionIndexFirst() {

        if (isExecutionStarted()) {
            currentExecutionIndex.set(0);
        }
    }

    public void setCurrentExecutionIndexLast() {
        if (isExecutionStarted()) {
            currentExecutionIndex.set(executed.get().size() - 1);
        }
    }

    public IntegerProperty currentExecutionIndexProperty() {
        return currentExecutionIndex;
    }

    public boolean isExecutionStarted() {
        return executed.get().size() > 0;
    }

    public ObservableList<TreeSearchInstrumented.Cmd<A, S>> getExecuted() {
        return executed.get();
    }

    public void setExecuted(ObservableList<TreeSearchInstrumented.Cmd<A, S>> executed) {
        this.executed.set(executed);
    }

    public ObjectProperty<ObservableList<TreeSearchInstrumented.Cmd<A, S>>> executedProperty() {
        return executed;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            public Void call() {
                try {
                    callLock.acquire();
                }
                catch (Throwable t) {
                    return null;
                }

                Platform.runLater(() -> {
                    state.set(SimulatorState.NOT_STARTED);
                    // Restarting the execution
                    currentExecutionIndex.set(-1);
                    executed.get().clear();
                });

                StringBuilder lastCommandId = new StringBuilder();
                Platform.runLater(() -> state.set(SimulatorState.SEARCHING_FOR_SOLUTUION));
                TreeSearchInstrumented<A, S> search = new TreeSearchInstrumented<>((command) -> {
                    if (isCancelled()) {
                        throw new CancellationException("Cancelled");
                    }
                    lastCommandId.setLength(0);
                    lastCommandId.append(command.commandId());
                    Platform.runLater(() -> executed.get().add(command));
                    if (getExecutionDelay() > 0) {
                        try {
                            Thread.sleep(getExecutionDelay());
                        } catch (InterruptedException ie) {
                            // Can be interrupted
                        }
                    }
                });

                try {
                    if (getProblem() != null) {
                        List<A> result = search.apply(getProblem());
                        Platform.runLater(() -> solution.set(Collections.unmodifiableList(result)));
                    }
                }
                catch (CancellationException ce) {
                    // Expected on cancellation
                }
                finally {
                    SimulatorState toReport = SimulatorState.NOT_STARTED;
                    if (lastCommandId.toString().equals(TreeSearchInstrumented.CMD_SOLUTION)) {
                        toReport = SimulatorState.SOLUTION_FOUND;
                    } else if (lastCommandId.toString().equals(TreeSearchInstrumented.CMD_FAILURE)) {
                        toReport = SimulatorState.FAILURE_ENCOUNTERED;
                    }

                    final SimulatorState fToReport = toReport;
                    Platform.runLater(() -> state.set(fToReport));

                    callLock.release();
                }

                return null;
            }
        };
    }
}
