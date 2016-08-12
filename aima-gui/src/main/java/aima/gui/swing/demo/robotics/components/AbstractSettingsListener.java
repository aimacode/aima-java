package aima.gui.swing.demo.robotics.components;

import aima.core.robotics.impl.MonteCarloLocalization;
import aima.core.robotics.impl.map.MclCartesianPlot2D;
import aima.gui.swing.demo.robotics.GenericMonteCarloLocalization2DApp;
import aima.gui.swing.demo.robotics.components.Settings.ISettingsListener;

/**
 * This abstract settings listener can be used for a basic set of settings. It applies all setting changes in the settings GUI onto the corresponding parameters.<br/>
 * The keys
 * <ul>
 * <li>{@code REMEMBER_FACTOR_KEY}</li>
 * <li>{@code SENSOR_RANGE_KEY}</li>
 * <li>{@code MIN_WEIGHT_KEY}</li>
 * <li>{@code MAX_DISTANCE_KEY}</li>
 * </ul>
 * have to be registered when using this settings listener. Keep in mind that {@code PARTICLE_COUNT_KEY} may have to be set, too, for {@link GenericMonteCarloLocalization2DApp}.
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 */
@SuppressWarnings("javadoc")
public abstract class AbstractSettingsListener implements ISettingsListener {

	public static final String PARTICLE_COUNT_KEY = "PARTICLE_COUNT";
	public static final String MAP_FILE_KEY = "MAP_FILE";
	public static final String REMEMBER_FACTOR_KEY = "REMEMBER_FACTOR";
	public static final String SENSOR_RANGE_KEY = "SENSOR_RANGE";
	public static final String MIN_WEIGHT_KEY = "MIN_WEIGHT";
	public static final String MAX_DISTANCE_KEY = "MAX_DISTANCE";
	
	protected Settings settingsGui;
	protected MclCartesianPlot2D<?,?,?> map;
	protected MonteCarloLocalization<?,?,?,?> mcl;
	
	/**
	 * @param settingsGui the {@link Settings} in which this class registers itself and those of its settings which will be used.
	 */
	public AbstractSettingsListener(Settings settingsGui) {
		this.settingsGui = settingsGui;
	}
	
	/**
	 * Sets the {@link MclCartesianPlot2D} in which the settings will be updated.
	 * @param map the map to be kept up to date with the correct parameters.
	 */
	public void setMap(MclCartesianPlot2D<?,?,?> map) {
		this.map = map;
	}
	
	/**
	 * Sets the {@link MonteCarloLocalization} in which the settings will be updated.
	 * @param mcl the MCL to be kept up to date with the correct parameters.
	 */
	public void setMcl(MonteCarloLocalization<?,?,?,?> mcl) {
		this.mcl = mcl;
	}
	
	/**
	 * Creates the settings that will be used. Registers {@code this} object as a listener for these settings.
	 */
	public abstract void createSettings();
	
	/**
	 * Registers {@code this} object as a listener for:
	 * <ul>
	 * <li>{@code REMEMBER_FACTOR_KEY}</li>
	 * <li>{@code SENSOR_RANGE_KEY}</li>
	 * <li>{@code MIN_WEIGHT_KEY}</li>
	 * <li>{@code MAX_DISTANCE_KEY}</li>
	 * </ul>
	 */
	protected void registerAbstractListener() {
		settingsGui.registerListener(REMEMBER_FACTOR_KEY, this);
		settingsGui.registerListener(SENSOR_RANGE_KEY, this);
		settingsGui.registerListener(MIN_WEIGHT_KEY, this);
		settingsGui.registerListener(MAX_DISTANCE_KEY, this);
	}
	
	@Override
	public void notifySetting(String key, String value) {
		final double valueNumber = Double.parseDouble(value);
		if(key.equals(REMEMBER_FACTOR_KEY)) {
			mcl.setRememberFactor(valueNumber);
		} else if(key.equals(SENSOR_RANGE_KEY)) {
			map.setSensorRange(valueNumber);
		} else if(key.equals(MIN_WEIGHT_KEY)) {
			mcl.setWeightCutOff(valueNumber);
		} else if(key.equals(MAX_DISTANCE_KEY)) {
			mcl.setMaxDistance(valueNumber);
		}
	}
}