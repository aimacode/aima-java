package aima.gui.swing.applications.robotics.components;

import aima.gui.swing.applications.robotics.GenericMonteCarloLocalization2DApp;

/**
 * This interface defines functionality for a GUI that manages the robot associated with the {@link GenericMonteCarloLocalization2DApp}.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 */
public interface IRobotGui {
	/**
	 * The string that is shown in the connect button by default.
	 */
	String DEFAULT_BUTTON_STRING = "Initialize Robot";
	/**
	 * Lets the robot class connect with or initialize/reset the actual robot. 
	 * @return true if the robot is ready for use.
	 */
	boolean initializeRobot();
	/**
	 * Lets the robot class close the connection with the actual robot. 
	 */
	void destructRobot();
	/**
	 * Informs the user that the robot has to be initialized before the MCL can be used.<br/>
	 * This will be called if the user loads a map and presses on any of the other buttons "Move", "Range Reading" or "Auto Locate" before initializing the robot.
	 */
	void notifyInitialize();
	/**
	 * Returns a string to be shown in the button that calls {@code initializeRobot()}.<br/>
	 * If this method is not used, return this instead: {@code return DEFAULT_BUTTON_STRING;}
	 * @return a not null string.
	 */
	String getButtonString();
	/**
	 * Loads any values that have been stored to the properties file previously.
	 * @param settingsGui the {@link Settings} from which the values will be loaded.
	 */
	void loadSettings(Settings settingsGui);
	/**
	 * Saves all values to the properties file that are to be restored when loading the properties again.
	 * @param settingsGui the {@link Settings} to which the values will be saved.
	 */
	void saveSettings(Settings settingsGui);
}