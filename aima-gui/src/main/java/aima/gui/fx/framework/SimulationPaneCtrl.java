package aima.gui.fx.framework;

import java.util.List;
import java.util.Optional;

import aima.core.util.CancelableThread;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;

/**
 * Controller class for simulation panes. It is responsible for maintaining the
 * current parameter settings, the simulation state, the current simulation
 * thread, and for handling events (parameter change events and simulation
 * button events). If not otherwise stated, methods must be called from the FX
 * Application Thread.
 * 
 * @author Ruediger Lunde
 */
public class SimulationPaneCtrl {

	public static enum State {
		READY, RUNNING, FINISHED, PAUSED, CANCELED
	}

	public final static String PARAM_SIM_SPEED = "simSpeed";

	private Button simBtn;
	private Label statusLabel;

	private List<Parameter> params;
	private List<ComboBox<String>> paramCombos;
	private Runnable initMethod;
	private Runnable simMethod;

	private ObjectProperty<State> state = new SimpleObjectProperty<State>();
	private Optional<CancelableThread> simThread = Optional.empty();

	/** Should only be called by the SimulationPaneBuilder. */
	public SimulationPaneCtrl(List<Parameter> params, List<ComboBox<String>> paramCombos, Runnable initMethod,
			Runnable simMethod, Button simBtn, Label statusLabel) {
		this.params = params;
		this.paramCombos = paramCombos;
		this.initMethod = initMethod;
		this.simMethod = simMethod;
		this.simBtn = simBtn;
		this.statusLabel = statusLabel;
		ChangeListener<String> listener = (obs, o, n) -> onParamChanged();
		for (ComboBox<String> combo : paramCombos)
			// allow simSpeed adjustments during simulation (without
			// re-initialization)
			if (!combo.getId().equals(PARAM_SIM_SPEED))
				combo.getSelectionModel().selectedItemProperty().addListener(listener);
		simBtn.setOnAction(ev -> onSimButtonAction());
		simBtn.setOnMouseClicked(ev -> {
			if (ev.getButton() == MouseButton.SECONDARY)
				setParamValue(PARAM_SIM_SPEED, Integer.MAX_VALUE);
		});
		updateParamVisibility();
		state.addListener((obs, o, n) -> onStateChanged());
		setState(State.READY);
	}

	public Optional<Parameter> getParam(String paramName) {
		return Parameter.find(params, paramName);
	}
	
	public int getParamValueIndex(String paramName) {
		int valIdx = -1;
		int paramIdx = Parameter.indexOf(params, paramName);
		if (paramIdx != -1)
			valIdx = paramCombos.get(paramIdx).getSelectionModel().getSelectedIndex();
		if (valIdx == -1)
			throw new IllegalStateException("No selected value for parameter " + paramName);
		return valIdx;
	}

	public Object getParamValue(String paramName) {
		int valIdx = getParamValueIndex(paramName);
		return Parameter.find(params, paramName).get().getValues().get(valIdx);
	}

	public int getParamAsInt(String paramName) {
		return (Integer) getParamValue(paramName);
	}

	public double getParamAsDouble(String paramName) {
		return (Double) getParamValue(paramName);
	}
	
	public boolean isParamVisible(String paramName) {
		int paramIdx = Parameter.indexOf(params, paramName);
		return paramCombos.get(paramIdx).isVisible();
	}

	/** Call this only for parameters which do not depend on other parameters! */
	public void setParamDisable(String paramName, boolean value) {
		int paramIdx = Parameter.indexOf(params, paramName);
		paramCombos.get(paramIdx).setDisable(value);
	}

	public void setParam(String paramName, int valueIdx) {
		int idx = Parameter.indexOf(params, paramName);
		if (idx != -1)
			paramCombos.get(idx).getSelectionModel().select(valueIdx);
		else
			throw new IllegalStateException("Parameter " + paramName + " not found.");
	}

	public void setParamValue(String paramName, Object value) {
		int pIdx = Parameter.indexOf(params, paramName);
		if (pIdx != -1) {
			List<Object> values = params.get(pIdx).getValues();
			for (int i = 0; i < values.size(); i++)
				if (values.get(i).equals(value)) {
					paramCombos.get(pIdx).getSelectionModel().select(i);
					break;
				}
		} else
			throw new IllegalStateException("Parameter " + paramName + " not found.");
	}

	/** Sets status label text (can be called from any thread). */
	public void setStatus(final String text) {
		if (Platform.isFxApplicationThread())
			statusLabel.setText(text);
		else
			Platform.runLater(() -> statusLabel.setText(text));
	}

	/**
	 * Sleeps as specified in simulation speed parameter. If the value is
	 * <code>Integer.MAX_VALUE</code> the simulation state is set to paused.
	 * Caller is typically not the FX Application Thread.
	 */
	public void waitAfterStep() {
		try {
			int msec = getParamAsInt(PARAM_SIM_SPEED);
			if (msec == Integer.MAX_VALUE)
				setState(State.PAUSED);
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			// nothing to do here.
		}
	}

	/**
	 * Tries to stop simulation. This will only have an effect, if all loops in
	 * the running simulation method check {@link CancelableThread#isCanceled()}
	 * in every time-consuming loop.
	 */
	public void cancelSimulation() {
		if (simThread.isPresent() && simThread.get().isAlive()) {
			simThread.get().cancel();
			setState(State.CANCELED);
			if (state.get() == State.PAUSED)
				simThread.get().interrupt();
		}
	}

	/** Can be called from FX application threads and other threads as well. */
	private void setState(State newState) {
		if (Platform.isFxApplicationThread())
			state.set(newState);
		else
			Platform.runLater(() -> state.set(newState));
	}

	private void onParamChanged() {
		cancelSimulation();
		setStatus("");
		updateParamVisibility();
		initMethod.run();
		setState(State.READY);
	}

	private void onStateChanged() {
		if (state.get() == State.READY)
			simBtn.setText("Start");
		else if (state.get() == State.RUNNING)
			simBtn.setText("Cancel");
		else if (state.get() == State.PAUSED)
			simBtn.setText("Continue");
		else if (state.get() == State.CANCELED)
			simBtn.setText("Stop");
		else if (state.get() == State.FINISHED)
			simBtn.setText("Init");

		boolean disable = state.get() != State.READY && state.get() != State.FINISHED;
		for (ComboBox<String> combo : paramCombos)
			if (!combo.getId().equals(PARAM_SIM_SPEED))
				combo.setDisable(disable);
	}

	@SuppressWarnings("deprecation")
	private void onSimButtonAction() {
		if (state.get() == State.FINISHED) {
			onParamChanged();
		} else if (!simThread.isPresent() || !simThread.get().isAlive()) {
			simThread = Optional.of(new CancelableThread(this::runSimulation));
			simThread.get().setDaemon(true);
			simThread.get().start();
		} else if (state.get() == State.PAUSED) {
			simThread.get().interrupt();
			setState(State.RUNNING);
		} else if (simThread.get().isCanceled()) {
			simThread.get().stop();
			setState(State.READY);
		} else {
			cancelSimulation();
		}
	}

	private void runSimulation() {
		try {
			setState(State.RUNNING);
			simMethod.run();
		} catch (Exception e) {
			setStatus("Sorry, something went wrong during simulation: " + e.getClass().getSimpleName());
			e.printStackTrace();
		} catch (Error e) {
			setStatus("Sorry, something went totally wrong during simulation: " + e.getClass().getSimpleName());
			e.printStackTrace();
		}
		setState(State.FINISHED);
	}
	
	private void updateParamVisibility() {
		for (int i = 0; i < params.size(); i++) {
			String depParam = params.get(i).getDependencyParameter();
			if (depParam != null) {
				Parameter para = params.get(i);
				ComboBox<String> combo = paramCombos.get(i);
				combo.setVisible(para.getDependencyValues().contains(getParamValue(depParam)));
			}
		}
	}
}
