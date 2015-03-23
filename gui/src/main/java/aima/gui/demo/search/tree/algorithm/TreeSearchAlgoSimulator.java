package aima.gui.demo.search.tree.algorithm;

import aima.core.api.search.Problem;
import aima.extra.instrument.search.TreeSearchCmdInstr;
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
    private IntegerProperty currentExecutionIndex = new SimpleIntegerProperty(0);
    private ObjectProperty<ObservableList<TreeSearchCmdInstr.Cmd<S>>> executed = new SimpleObjectProperty<>(FXCollections.observableArrayList());


    public Problem<S> getProblem() {
        return problem.get();
    }

    public void setProblem(Problem<S> problem) {
        this.problem.set(problem);
    }

    public ObjectProperty<Problem<S>> problemProperty() {
        return problem;
    }

    public int getCurrentExecutionIndex() {
        return currentExecutionIndex.get();
    }

    public void setCurrentExecutionIndex(int index) {
        currentExecutionIndex.set(index);
    }

    public IntegerProperty currentExecutionIndexProperty() {
        return currentExecutionIndex;
    }

    public boolean isExecutionStarted() {
        return executed.get().size() > 0;
    }

    public ObservableList<TreeSearchCmdInstr.Cmd<S>> getExecuted() {
        return executed.get();
    }

    public void setExecuted(ObservableList<TreeSearchCmdInstr.Cmd<S>> executed) {
        this.executed.set(executed);
    }

    public ObjectProperty<ObservableList<TreeSearchCmdInstr.Cmd<S>>> executedProperty() {
        return executed;
    }

    @Override
    protected Task<Void> createTask() {
// TODO - cleanup
System.out.println("createTask");
        return new Task<Void>() {
            @Override
            public Void call() {

                TreeSearchCmdInstr<S> search = new TreeSearchCmdInstr(new TreeSearchCmdInstr.Listener<S>() {
                    public void cmd(TreeSearchCmdInstr.Cmd<S> command) {
                        if (isCancelled()) {
                            throw new CancellationException("Cancelled");
                        }
System.out.println(command.commandId());
                        executed.get().add(command);
                    }
                });

                try {
                    search.apply(getProblem());
                }
                catch (CancellationException ce) {
                    // Expected on cancellation
                }

                return null;
            }
        };
    }
}
