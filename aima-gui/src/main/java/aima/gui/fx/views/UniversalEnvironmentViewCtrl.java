package aima.gui.fx.views;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import javafx.application.Platform;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

/**
 * Controller class for simple text-based environment views. It can be used as
 * base for more advanced environment view implementations for certain
 * environments.
 * 
 * @author Ruediger Lunde
 *
 */
public class UniversalEnvironmentViewCtrl implements EnvironmentView {

	protected SplitPane splitPane;
	protected TextArea textArea;

	/**
	 * Adds a split pane and a text area to the provided pane. The result is a an
	 * environment view which prints messages about environment changes on the
	 * text area.
	 */
	public UniversalEnvironmentViewCtrl(StackPane parent) {
		splitPane = new SplitPane();
		textArea = new TextArea();
		splitPane.getItems().add(textArea);
		parent.getChildren().add(splitPane);
	}

	@Override
	public void notify(String msg) {
		Platform.runLater(() -> textArea.appendText("\n" + msg));
	}

	@Override
	public void agentAdded(Agent agent, Environment source) {
		Platform.runLater(() -> textArea.appendText("\n"));

	}

	@Override
	public void agentActed(Agent agent, Action action, Environment source) {
		Platform.runLater(() -> textArea.appendText("\nAction: " + action.toString()));
	}
}
