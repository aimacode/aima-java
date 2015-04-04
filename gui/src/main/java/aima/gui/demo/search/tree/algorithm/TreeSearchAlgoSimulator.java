package aima.gui.demo.search.tree.algorithm;

import aima.core.api.search.Problem;
import aima.extra.instrument.search.TreeSearchInstrumented;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.concurrent.CancellationException;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchAlgoSimulator<S> extends Service<Void> {
    private ObjectProperty<Problem<S>> problem = new SimpleObjectProperty<>();
    private IntegerProperty currentExecutionIndex = new SimpleIntegerProperty(-1);
    private ObjectProperty<ObservableList<TreeSearchInstrumented.Cmd<S>>> executed = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    public interface Observer<S> {
        void setSimulator(TreeSearchAlgoSimulator<S> simulator);
    }

    public Problem<S> getProblem() {
        return problem.get();
    }

    public void setProblem(Problem<S> problem) {
        this.problem.set(problem);
        restart();
    }

    public ObjectProperty<Problem<S>> problemProperty() {
        return problem;
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

    public ObservableList<TreeSearchInstrumented.Cmd<S>> getExecuted() {
        return executed.get();
    }

    public void setExecuted(ObservableList<TreeSearchInstrumented.Cmd<S>> executed) {
        this.executed.set(executed);
    }

    public ObjectProperty<ObservableList<TreeSearchInstrumented.Cmd<S>>> executedProperty() {
        return executed;
    }

    @Override
    protected Task<Void> createTask() {
        // Restarting the execution
        currentExecutionIndex.set(-1);
        executed.get().clear();

        return new Task<Void>() {
            @Override
            public Void call() {

                TreeSearchInstrumented<S> search = new TreeSearchInstrumented((command) -> {
                    if (isCancelled()) {
                        throw new CancellationException("Cancelled");
                    }
                    executed.get().add(command);
                });

                try {
                    if (getProblem() != null) {
                        search.apply(getProblem());
                    }
                }
                catch (CancellationException ce) {
                    // Expected on cancellation
                }

                return null;
            }
        };
    }
}
