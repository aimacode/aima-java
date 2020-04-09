package aima.gui.fx.views;

import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentListener;
import aima.core.agent.impl.AbstractEnvironment;
import javafx.application.Platform;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Controller class for a simple environment view. It logs information about
 * environment changes on a text area and can be used for any kind of
 * environment. More specific environment views can be created on this base by
 * adding state visualization to the split pane of this implementation.
 *
 * @param <P> Typ used for percepts
 * @param <A> Typ used for actions
 * @author Ruediger Lunde
 *
 */
public class SimpleEnvironmentViewCtrl<P, A> implements EnvironmentListener<P, A> {

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

	public void initialize(AbstractEnvironment<? extends P, ? extends A> env) {
		if (!textArea.getText().isEmpty())
			textArea.appendText("\n\n");
		updateEnvStateView(env);
	}
	
	/**
	 * Can be called from every thread.
	 */
	@Override
	public void notify(String msg) {
		if (Platform.isFxApplicationThread())
			textArea.appendText("\n" + msg + "\n");
		else
			Platform.runLater(() -> textArea.appendText("\n" + msg+ "\n"));
	}

	/**
	 * Can be called from every thread.
	 */
	@Override
	public void agentAdded(Agent<?, ?> agent, Environment<?, ?> source) {
		Runnable r = () -> {
			int agentId = source.getAgents().indexOf(agent) + 1;
			textArea.appendText("\nAgent " + agentId + " added.");
			updateEnvStateView(source);
		};
		if (Platform.isFxApplicationThread())
			r.run();
		else
			Platform.runLater(r);
	}

	/**
	 * Can be called from every thread.
	 */
	@Override
	public void agentActed(Agent<?, ?> agent, P percept, A action, Environment<?, ?> source) {
		Runnable r = () -> {
			int agentId = source.getAgents().indexOf(agent) + 1;
			textArea.appendText("\nAgent " + agentId + " acted.");
			textArea.appendText("\n   Percept: " + percept.toString());
			textArea.appendText("\n   Action: " + action.toString());
			updateEnvStateView(source);
		};
		if (Platform.isFxApplicationThread())
			r.run();
		else
			Platform.runLater(r);
	}

	/**
	 * Is called after agent actions. This implementation does nothing.
	 */
	protected void updateEnvStateView(Environment<?, ?> env) { }
}
