package aima.gui.fx.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * Builder class for task execution panes. To add suitable graphical elements
 * to a given pane and obtain a corresponding controller class, just create a builder,
 * call the define methods to specify what you need, and then get the result with
 * {@link #getResultFor}.
 * 
 * @author Ruediger Lunde
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class TaskExecutionPaneBuilder {

	protected List<Parameter> parameters = new ArrayList<>();
	protected Optional<Node> stateView = Optional.empty();
	/** Should return true if initialization was successful. */
	protected Optional<Runnable> initMethod = Optional.empty();
	protected Optional<Runnable> taskMethod = Optional.empty();

	public final void defineParameters(List<Parameter> params) {
		parameters.clear();
		parameters.addAll(params);
	}

	public final void defineStateView(Node stateView) {
		this.stateView = Optional.of(stateView);
	}

	public final void defineInitMethod(Runnable initMethod) {
		this.initMethod = Optional.of(initMethod);
	}

	public final void defineTaskMethod(Runnable taksMethod) {
		this.taskMethod = Optional.of(taksMethod);
	}

	/**
	 * Adds a toolbar, a state view, and a status label to the provided pane and returns
	 * a controller class instance. The toolbar contains combo boxes to control parameter settings
	 * and buttons for task execution control. The controller class instance handles user events and provides
	 * access to user settings (parameter settings, execution speed, status text, ...).
	 */
	public TaskExecutionPaneCtrl getResultFor(BorderPane pane) {
		List<ComboBox<String>> combos = new ArrayList<>();
		parameters.add(createExecutionSpeedParam());
		for (Parameter param : parameters) {
			ComboBox<String> combo = new ComboBox<>();
			combo.setId(param.getName());
			combo.getItems().addAll(param.getValueNames());
			combo.getSelectionModel().select(param.getDefaultValueIndex());
			combos.add(combo);
		}

		Button executeBtn = new Button();

		Node[] tools = new Node[combos.size() + 2];
		for (int i = 0; i < combos.size() - 1; i++)
			tools[i] = combos.get(i);
		tools[combos.size() - 1] = new Separator();
		tools[combos.size() + 0] = combos.get(combos.size() - 1);
		tools[combos.size() + 1] = executeBtn;
		ToolBar toolBar = new ToolBar(tools);

		Label statusLabel = new Label();
		statusLabel.setMaxWidth(Double.MAX_VALUE);
		statusLabel.setAlignment(Pos.CENTER);
		statusLabel.setFont(Font.font(16));

		pane.setTop(toolBar);
		if (stateView.isPresent()) {
			if (stateView.get() instanceof Canvas) {
				// make canvas resizable
				Canvas canvas = (Canvas) stateView.get();
				Pane canvasPane = new Pane();
				canvasPane.getChildren().add(canvas);
				canvas.widthProperty().bind(canvasPane.widthProperty());
				canvas.heightProperty().bind(canvasPane.heightProperty());
				pane.setCenter(canvasPane);
				pane.setStyle("-fx-background-color: white");
			} else
				pane.setCenter(stateView.get());
		}

		pane.setBottom(statusLabel);

		if (!initMethod.isPresent())
			throw new IllegalStateException("No initialization method defined.");
		if (!taskMethod.isPresent())
			throw new IllegalStateException("No task method defined.");

		return new TaskExecutionPaneCtrl(parameters, combos, initMethod.get(), taskMethod.get(),
				executeBtn, statusLabel);
	}

	/**
	 * Factory method defining the execution speed options. Value
	 * <code>Integer.MAX_VALUE</code> is used for step mode.
	 */
	protected Parameter createExecutionSpeedParam() {
		Parameter result = new Parameter
				(TaskExecutionPaneCtrl.PARAM_EXEC_SPEED, 20, 100, 400, 800, Integer.MAX_VALUE);
		result.setValueNames("VeryFast", "Fast", "Medium", "Slow", "StepMode");
		result.setDefaultValueIndex(2);
		return result;
	}
}
