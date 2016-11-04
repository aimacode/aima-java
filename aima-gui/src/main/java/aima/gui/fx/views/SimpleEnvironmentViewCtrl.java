package aima.gui.fx.views;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import javafx.application.Platform;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Observable;

/**
 * Controller class for a simple environment view. It logs informations about
 * environment changes on a text area and can be used for any kind of
 * environment. More specific environment views can be created on this base by
 * adding state visualization to the split pane of this implementation.
 * 
 * @author Ruediger Lunde
 *
 */
public class SimpleEnvironmentViewCtrl extends Observable implements EnvironmentView {

	protected SplitPane splitPane;
	protected TextArea textArea;

	/**
	 * Adds a split pane and a text area to the provided pane. The result is
	 * an environment view which prints messages about environment changes on
	 * the text area.
	 */
	public SimpleEnvironmentViewCtrl(StackPane viewRoot) {
		splitPane = new SplitPane();
		textArea = new TextArea();
		textArea.setMinWidth(0.0);
		splitPane.getItems().add(textArea);
		viewRoot.getChildren().add(splitPane);
	}

	public SimpleEnvironmentViewCtrl(StackPane viewRoot, Pane envStateView, double dividerPos) {
		this(viewRoot);
		splitPane.getItems().add(0, envStateView);
		splitPane.setDividerPosition(0, dividerPos);
	}

	public void initialize(Environment env) {
		if (!textArea.getText().isEmpty())
			textArea.appendText("\n");
		updateEnvStateView(env);
	}
	
	/**
	 * Can be called from every thread.
	 */
	@Override
	public void notify(String msg) {
		if (Platform.isFxApplicationThread())
			textArea.appendText("\n" + msg);
		else
			Platform.runLater(() -> textArea.appendText("\n" + msg));
	}

	/**
	 * Should not be called from an FX application thread.
	 */
	@Override
	public void agentAdded(Agent agent, Environment source) {
		Platform.runLater(() -> {
			textArea.appendText("\nAgent added.");
			updateEnvStateView(source);
		});

	}

	/**
	 * Should not be called from an FX application thread.
	 */
	@Override
	public void agentActed(Agent agent, Action action, Environment source) {
		Platform.runLater(() -> {
			textArea.appendText("\nAgent acted: " + action.toString());
			updateEnvStateView(source);
		});
	}

	/**
	 * Is called after agent actions. This implementation just notifies all observers.
	 */
	protected void updateEnvStateView(Environment env) {
		this.notifyObservers();
	}
}
