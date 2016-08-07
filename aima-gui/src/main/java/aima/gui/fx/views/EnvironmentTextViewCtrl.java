package aima.gui.fx.views;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class EnvironmentTextViewCtrl implements EnvironmentView {

	TextArea textArea;

	public EnvironmentTextViewCtrl(StackPane pane) {
		textArea = new TextArea();
		pane.getChildren().add(textArea);
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
