package aima.gui.swing.demo.robotics;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;

import aima.core.robotics.impl.MonteCarloLocalization;
import aima.core.robotics.impl.datatypes.AbstractRangeReading;
import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.map.MclCartesianPlot2D;
import aima.core.robotics.impl.simple.SimpleMove;
import aima.core.robotics.impl.simple.SimplePose;
import aima.core.robotics.impl.simple.SimplePoseFactory;
import aima.core.robotics.impl.simple.SimpleRangeReading;
import aima.core.robotics.impl.simple.SimpleRangeReadingFactory;
import aima.core.robotics.impl.simple.VirtualRobot;
import aima.core.util.math.geom.SVGGroupParser;
import aima.gui.swing.demo.robotics.components.AnglePanel;
import aima.gui.swing.demo.robotics.components.AnglePanel.ChangeListener;
import aima.gui.swing.demo.robotics.components.IRobotGui;
import aima.gui.swing.demo.robotics.components.Settings;
import aima.gui.swing.demo.robotics.components.SimpleSettingsListener;
import aima.gui.swing.demo.robotics.components.VirtualRobotGui;
import aima.gui.swing.demo.robotics.util.GuiBase;

/**
 * Provides the {@link GenericMonteCarloLocalization2DApp} for the simple environment in {@code aima.core.robotics.impl.simple}.
 * This environment is intended for a {@link VirtualRobot}.<br/>
 * It can be used for other 2D environments by overriding {@code initialize()} and using a main method with the inheriting class.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public class MonteCarloLocalizationApp {

	protected static final File DEFAULT_SETTINGS_FILE = new File(System.getProperty("user.dir"),"mcl_settings.cache");
	protected static final String RANGE_READING_ANGLES_KEY = "RANGE_READING_ANGLES";
	
	protected Settings settingsGui;
	protected IRobotGui robotGui;
	protected GenericMonteCarloLocalization2DApp<?,?,?> app;
	
	private File settingsFile;
	
	/**
	 * Starts the application.
	 * @param args a path to a file containing settings may be passed as the first argument. Otherwise the default settings file {@code mcl_settings.cache} in the working directory is used.
	 */
	public static void main(String[] args) {
		File settingsFile = args.length > 0 ? new File(args[0]) : DEFAULT_SETTINGS_FILE;
		MonteCarloLocalizationApp app = new MonteCarloLocalizationApp(settingsFile);
		app.constructApplicationFrame();
		app.show();
		
	}
	
	/**
	 * Loads the provided settings file into a new {@link Settings} object.
	 * @param settingsFile the file containing the settings to be loaded. 
	 * @return the {@link Settings} that were loaded.
	 */
	protected static Settings buildSettings(File settingsFile) {
		Settings settingsGui = new Settings();
		if(settingsFile.isFile() && settingsFile.canRead()) {
			settingsGui.loadSettings(settingsFile);
		}
		return settingsGui;
	}
	
	/**
	 * @param settingsFile the file containing the settings for this Monte-Carlo-Localization.
	 */
	public MonteCarloLocalizationApp(File settingsFile) {
		GuiBase.activateSystemStyle();
		GuiBase.setUIColors();
		this.settingsFile = settingsFile;
		settingsGui = buildSettings(settingsFile);
		initialize();
		robotGui.loadSettings(settingsGui);
		settingsGui.notifyAllListeners();
		settingsGui.buildGui();
	}
	
	/**
	 * Creates a {@link GenericMonteCarloLocalization2DApp} and stores it in {@code app}.<br/>
	 * In addition the corresponding {@link IRobotGui} is created and stored in {@code robotGui}. The function {@code robotGui.destructRobot()} will be called when the application window closes to allow closing any open connections gracefully.
	 */
	protected void initialize() {
		SimpleSettingsListener settingsListener = new SimpleSettingsListener(settingsGui);
		settingsListener.createSettings();
		
		AnglePanel angles = new AnglePanel();
		settingsGui.registerSpecialSetting(RANGE_READING_ANGLES_KEY, angles);
		
		MclCartesianPlot2D<SimplePose, SimpleMove, AbstractRangeReading> map = new MclCartesianPlot2D<SimplePose,SimpleMove,AbstractRangeReading>(new SVGGroupParser(),new SVGGroupParser(),new SimplePoseFactory(),new SimpleRangeReadingFactory());
		VirtualRobot robot = new VirtualRobot(map);
		robotGui = new VirtualRobotGui(robot);
		
		MonteCarloLocalization<SimplePose,Angle,SimpleMove,AbstractRangeReading> mcl = new MonteCarloLocalization<SimplePose, Angle, SimpleMove, AbstractRangeReading>(map, robot);
		app = new GenericMonteCarloLocalization2DApp<SimplePose,SimpleMove,SimpleRangeReading>(mcl, map, robot, robotGui, settingsGui);
		
		angles.setChangeListener((ChangeListener) robotGui);
		settingsListener.setMap(map);
		settingsListener.setMcl(mcl);
		settingsListener.setRobot(robot);
		
		//Load the virtual environment resource:
		try {
			map.loadMap(this.getClass().getResourceAsStream("virtual_environment.svg"),this.getClass().getResourceAsStream("virtual_environment.svg"));
			app.gui.createMap();//bad style.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Makes the application visible.
	 */
	public void show() {
		app.show();
	}
	
	/**
	 * Creates the {@code JFrame} of the application, registers a window listener and returns the {@code JFrame}.
	 * @return the main frame of the application.
	 */
	public JFrame constructApplicationFrame() {
		JFrame frame = app.constructApplicationFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				robotGui.saveSettings(settingsGui);
				settingsGui.saveSettings(settingsFile);
				robotGui.destructRobot();
			}
		});
		return frame;
	}
}
