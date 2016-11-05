package aima.gui.swing.applications.robotics.simple;

import aima.core.robotics.impl.datatypes.Angle;
import aima.gui.swing.applications.robotics.components.AnglePanel;
import aima.gui.swing.applications.robotics.components.IRobotGui;
import aima.gui.swing.applications.robotics.components.Settings;
import aima.gui.swing.framework.util.GuiBase;

/**
 * An implementation of {@link IRobotGui} for {@link VirtualRobot}.<br/>
 * As the virtual robot does not need a graphical user interface, this class is a minimalist implementation.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class VirtualRobotGui implements IRobotGui, AnglePanel.ChangeListener {

	private VirtualRobot robot;
	
	/**
	 * @param robot the robot that will be managed.
	 */
	public VirtualRobotGui(VirtualRobot robot) {
		this.robot = robot;
	}

	@Override
	public boolean initializeRobot() {
		try {
		robot.setRandomPose();
		return true;
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	public void destructRobot() { }

	@Override
	public void notifyInitialize() {
		GuiBase.showMessageBox("The robot has to be initialized first!");
	}
	
	@Override
	public String getButtonString() {
		return DEFAULT_BUTTON_STRING;
	}

	@Override
	public void loadSettings(Settings settingsGui) { }

	@Override
	public void saveSettings(Settings settingsGui) { }
	
	@Override
	public void notify(Angle[] angles) {
		robot.setRangeReadingAngles(angles);
	}
}
