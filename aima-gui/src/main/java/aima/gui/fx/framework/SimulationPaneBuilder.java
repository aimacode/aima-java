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
 * Builder class for simulation applications. To create a simulation
 * application, just create a builder, call the define methods to specify what
 * you need, and then get the result with {@link #getResultFor}.
 * 
 * @author Ruediger Lunde
 */
public class SimulationPaneBuilder {

	protected List<Parameter> parameters = new ArrayList<Parameter>();
	protected Optional<Node> stateView = Optional.empty();
	/** Should return true if initialization was successful. */
	protected Optional<Runnable> initMethod = Optional.empty();
	protected Optional<Runnable> simMethod = Optional.empty();

	public final void defineParameters(List<Parameter> params) {
		parameters.clear();
		parameters.addAll(params);
	}

	public final void defineStateView(Node stateView) {
		this.stateView = Optional.of(stateView);
	}

	public final void defineSimMethod(Runnable simMethod) {
		this.simMethod = Optional.of(simMethod);
	}

	public final void defineInitMethod(Runnable initMethod) {
		this.initMethod = Optional.of(initMethod);
	}

	public SimulationPaneCtrl getResultFor(BorderPane pane) {
		List<ComboBox<String>> combos = new ArrayList<ComboBox<String>>();
		parameters.add(createSimSpeedParam());
		for (Parameter param : parameters) {
			ComboBox<String> combo = new ComboBox<>();
			combo.setId(param.getName());
			combo.getItems().addAll(param.getValueNames());
			combo.getSelectionModel().select(param.getDefaultValueIndex());
			combos.add(combo);
		}

		Button simBtn = new Button();

		Node[] tools = new Node[combos.size() + 2];
		for (int i = 0; i < combos.size() - 1; i++)
			tools[i] = combos.get(i);
		tools[combos.size() - 1] = new Separator();
		tools[combos.size() + 0] = combos.get(combos.size() - 1);
		tools[combos.size() + 1] = simBtn;
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
		if (!simMethod.isPresent())
			throw new IllegalStateException("No simulation method defined.");

		SimulationPaneCtrl result = new SimulationPaneCtrl(parameters, combos, initMethod.get(), simMethod.get(),
				simBtn, statusLabel);
		return result;
	}

	/**
	 * Factory method defining the simulation speed options. Value
	 * <code>Integer.MAX_VALUE</code> is used for pause.
	 */
	protected Parameter createSimSpeedParam() {
		Parameter result = new Parameter(SimulationPaneCtrl.PARAM_SIM_SPEED, 20, 100, 400, 800, Integer.MAX_VALUE);
		result.setValueNames("VeryFast", "Fast", "Medium", "Slow", "Pause");
		result.setDefaultValueIndex(2);
		return result;
	}
}
