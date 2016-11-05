package aima.gui.swing.applications.robotics.simple;

import aima.gui.swing.applications.robotics.GenericMonteCarloLocalization2DApp;
import aima.gui.swing.applications.robotics.components.AbstractSettingsListener;
import aima.gui.swing.applications.robotics.components.Settings;

/**
 * This settings listener is used for the environment in {@code aima.gui.swing.demo.robotics.simple}.
 * It applies all setting changes in the settings GUI on the corresponding parameters except the particle count which is managed internally in the {@link GenericMonteCarloLocalization2DApp}.
 * It is extending the {@link AbstractSettingsListener}.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public final class SimpleSettingsListener extends AbstractSettingsListener {

	public static final String MIN_MOVE_DISTANCE_KEY = "MIN_MOVE_DISTANCE";
	public static final String MAX_MOVE_DISTANCE_KEY = "MAX_MOVE_DISTANCE";
	public static final String MOVE_ROTATION_NOISE_KEY = "MOVE_ROTATION_NOISE";
	public static final String MOVE_DISTANCE_NOISE_KEY = "MOVE_DISTANCE_NOISE";
	public static final String RANGE_READING_NOISE_KEY = "RANGE_READING_NOISE";
	public static final String BAD_DELTA_KEY = "BAD_DELTA";
	
	private VirtualRobot robot;
	
	/**
	 * @param settingsGui the {@link Settings} on which this class should register itself.
	 */
	public SimpleSettingsListener(Settings settingsGui) {
		super(settingsGui);
	}
	
	/**
	 * Sets the {@link VirtualRobot} on which the settings will be updated.
	 * @param robot the robot to be kept up to date with the correct parameters.
	 */
	public void setRobot(VirtualRobot robot) {
		this.robot = robot;
	}
	
	@Override
	public void createSettings() {
		settingsGui.registerSetting(PARTICLE_COUNT_KEY, "Particle count", "2000");
		settingsGui.registerSetting(MIN_WEIGHT_KEY, "Min. particle weight", "0.0");
		settingsGui.registerSetting(MAX_DISTANCE_KEY, "Max. particle distance", "35.0");
		settingsGui.registerSetting(SENSOR_RANGE_KEY, "Max. sensor range", "800.0");
		
		settingsGui.registerSetting(MOVE_ROTATION_NOISE_KEY, "Move rotation noise (rad)", "0.12");
		settingsGui.registerSetting(MOVE_DISTANCE_NOISE_KEY, "Move distance noise", "3.0");
		settingsGui.registerSetting(RANGE_READING_NOISE_KEY, "Range reading noise", "1.0");
		settingsGui.registerSetting(MIN_MOVE_DISTANCE_KEY, "Min. move distance", "10.0");
		settingsGui.registerSetting(MAX_MOVE_DISTANCE_KEY, "Max. move distance", "40.0");
		settingsGui.registerSetting(BAD_DELTA_KEY, "Bad range delta", "35.0");
		
		registerAbstractListener();
		registerSimpleListener();
	}
	
	/**
	 * Registers this object as a listener for its settings.
	 */
	protected void registerSimpleListener() {
		settingsGui.registerListener(MOVE_ROTATION_NOISE_KEY, this);
		settingsGui.registerListener(MOVE_DISTANCE_NOISE_KEY, this);
		settingsGui.registerListener(RANGE_READING_NOISE_KEY, this);
		settingsGui.registerListener(MIN_MOVE_DISTANCE_KEY, this);
		settingsGui.registerListener(MAX_MOVE_DISTANCE_KEY, this);
		settingsGui.registerListener(BAD_DELTA_KEY, this);
	}
	
	@Override
	public boolean notifySetting(String key, String value) {
		final boolean superCall = super.notifySetting(key, value);
		try {
			final double valueNumber = Double.parseDouble(value);
			if(key.equals(SENSOR_RANGE_KEY)) {
				SimpleRangeReading.setSensorRange(valueNumber);
			} else if(key.equals(MOVE_ROTATION_NOISE_KEY)) {
				SimpleMove.setRotationNoise(valueNumber);
			} else if(key.equals(MOVE_DISTANCE_NOISE_KEY)) {
				SimpleMove.setMovementNoise(valueNumber);
			} else if(key.equals(RANGE_READING_NOISE_KEY)) {
				SimpleRangeReading.setRangeNoise(valueNumber);
			} else if(key.equals(MIN_MOVE_DISTANCE_KEY)) {
				robot.setMinMoveDistance(valueNumber);
			} else if(key.equals(MAX_MOVE_DISTANCE_KEY)) {
				robot.setMaxMoveDistance(valueNumber);
			} else if(key.equals(BAD_DELTA_KEY)) {
				SimpleRangeReading.setBadDelta(valueNumber);
			} else {
				throw new NumberFormatException();
			}
		} catch(NumberFormatException e) {
			return superCall;
		}
		return true;
	}
}