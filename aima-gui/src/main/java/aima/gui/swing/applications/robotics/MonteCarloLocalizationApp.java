package aima.gui.swing.applications.robotics;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;

import aima.core.robotics.MonteCarloLocalization;
import aima.core.robotics.impl.datatypes.AbstractRangeReading;
import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.map.MclCartesianPlot2D;
import aima.core.util.JavaRandomizer;
import aima.core.util.math.geom.SVGGroupParser;
import aima.gui.swing.applications.robotics.components.AnglePanel;
import aima.gui.swing.applications.robotics.components.AnglePanel.ChangeListener;
import aima.gui.swing.applications.robotics.simple.SimpleMove;
import aima.gui.swing.applications.robotics.simple.SimplePose;
import aima.gui.swing.applications.robotics.simple.SimplePoseFactory;
import aima.gui.swing.applications.robotics.simple.SimpleRangeReading;
import aima.gui.swing.applications.robotics.simple.SimpleRangeReadingFactory;
import aima.gui.swing.applications.robotics.simple.SimpleSettingsListener;
import aima.gui.swing.applications.robotics.simple.VirtualRobot;
import aima.gui.swing.applications.robotics.simple.VirtualRobotGui;
import aima.gui.swing.framework.util.GuiBase;
import aima.gui.swing.applications.robotics.components.IRobotGui;
import aima.gui.swing.applications.robotics.components.Settings;

/**
 * Provides the {@link GenericMonteCarloLocalization2DApp} for the simple environment in {@code aima.gui.swing.demo.robotics.simple}.
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
	protected static final String RANGE_READING_ANGLES_TITLE = "Range reading angles";
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
		app.constructBasicApplicationFrame();
		app.notifyAllListeners();
		app.show();
	}
	
	protected void notifyAllListeners() {
		settingsGui.notifyAllListeners();
	}

	/**
	 * Loads the provided settings file into a new {@link Settings} object.
	 * @param settingsFile the file containing the settings to be loaded. 
	 * @return the {@link Settings} that were loaded.
	 */
	protected static Settings buildSettings(File settingsFile) {
		Settings settingsGui = new Settings();
		if(settingsFile != null) {
			if(settingsFile.isFile() && settingsFile.canRead()) {
				settingsGui.loadSettings(settingsFile);
			}
		}
		return settingsGui;
	}
	
	/**
	 * This Constructor creates a application that does not loads settings from or saves settings to a file.
	 */
	public MonteCarloLocalizationApp() {
		this(null);
	}
	
	/**
	 * @param settingsFile the file containing the settings for this Monte-Carlo-Localization. To that file the settings will be stored when exiting the application.
	 */
	public MonteCarloLocalizationApp(File settingsFile) {
		GuiBase.activateSystemStyle();
		GuiBase.setUIColors();
		this.settingsFile = settingsFile;
		settingsGui = buildSettings(settingsFile);
		initialize();
		robotGui.loadSettings(settingsGui);
		settingsGui.buildGui();
	}
	
	/**
	 * Creates a {@link GenericMonteCarloLocalization2DApp} and stores it in {@code app}.<br/>
	 * In addition the corresponding {@link IRobotGui} is created and stored in {@code robotGui}. The function {@code robotGui.destructRobot()} will be called when the application window closes to allow closing any open connections gracefully.
	 */
	protected void initialize() {
		SimpleSettingsListener settingsListener = new SimpleSettingsListener(settingsGui);
		settingsListener.createSettings();
		
		AnglePanel angles = new AnglePanel(RANGE_READING_ANGLES_TITLE);
		settingsGui.registerSpecialSetting(RANGE_READING_ANGLES_KEY, angles);
		
		MclCartesianPlot2D<SimplePose, SimpleMove, AbstractRangeReading> map = new MclCartesianPlot2D<SimplePose,SimpleMove,AbstractRangeReading>(new SVGGroupParser(),new SVGGroupParser(),new SimplePoseFactory(),new SimpleRangeReadingFactory());
		VirtualRobot robot = new VirtualRobot(map);
		robotGui = new VirtualRobotGui(robot);
		
		MonteCarloLocalization<SimplePose,Angle,SimpleMove,AbstractRangeReading> mcl = new MonteCarloLocalization<SimplePose, Angle, SimpleMove, AbstractRangeReading>(map, new JavaRandomizer());
		app = new GenericMonteCarloLocalization2DApp<SimplePose,SimpleMove,SimpleRangeReading>(mcl, map, robot, robotGui, settingsGui);
		
		angles.setChangeListener((ChangeListener) robotGui);
		settingsListener.setMap(map);
		settingsListener.setMcl(mcl);
		settingsListener.setRobot(robot);
	}
	
	/**
	 * Makes the application visible.
	 */
	public void show() {
		app.show();
	}
	
	/**
	 * Creates the {@code JFrame} for the {@link AimaDemoApp}. {@code constructBasicApplicationFrame} should be called in any other case. 
	 * @return the main frame of the application.
	 */
	public JFrame constructApplicationFrame() {
		JFrame frame = constructBasicApplicationFrame();
		//Load the virtual environment resource:
		try {
			app.map.loadMap(this.getClass().getResourceAsStream("virtual_environment.svg"),this.getClass().getResourceAsStream("virtual_environment.svg"));
			app.settingsGui.notifyAllListeners();
			app.gui.createMap();
			app.gui.enableButtons(app.gui.buttonStateNormal);
			/* This is bad style.
			 * On the other hand this is the only situation in which the GUI
			 * is modified from outside of GenericMonteCarloLocalization to load a map from the resources.
			 */
		} catch (Exception e) {
			/* A Exception may be thrown when this example program is not launched from within a jar file.
			 * This happens because the ClassLoader does not find the requested resource.
			 */
		}
		return frame;
	}
	
	/**
	 * Creates the {@code JFrame} for the application. A window listener is registered. 
	 * @return the main frame of the application.
	 */
	public JFrame constructBasicApplicationFrame() {
		JFrame frame = app.constructApplicationFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				robotGui.saveSettings(settingsGui);
				if(settingsFile != null) settingsGui.saveSettings(settingsFile);
				robotGui.destructRobot();
			}
		});
		return frame;
	}
}
