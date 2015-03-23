package aima.gui.demo.search.tree.algorithm;

import aima.extra.instrument.search.TreeSearchCmdInstr;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchAlgoSimulator<S> {
    private IntegerProperty currentExecutionIndex = new SimpleIntegerProperty(0);
    private ObjectProperty<ObservableList<TreeSearchCmdInstr.Cmd<S>>> executed = new SimpleObjectProperty<>(FXCollections.observableArrayList());


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
}
