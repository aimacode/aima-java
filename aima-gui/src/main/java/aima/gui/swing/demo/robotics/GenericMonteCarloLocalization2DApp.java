package aima.gui.swing.demo.robotics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import aima.core.robotics.IMclRobot;
import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.RobotException;
import aima.core.robotics.impl.MonteCarloLocalization;
import aima.core.robotics.impl.Particle;
import aima.core.robotics.impl.datatypes.AbstractRangeReading;
import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.datatypes.IPose2D;
import aima.core.robotics.impl.map.MclCartesianPlot2D;
import aima.core.robotics.impl.simple.VirtualRobot;
import aima.core.util.math.geom.shapes.IGeometric2D;
import aima.core.util.math.geom.shapes.Rect2D;
import aima.gui.swing.demo.robotics.components.AbstractSettingsListener;
import aima.gui.swing.demo.robotics.components.IRobotGui;
import aima.gui.swing.demo.robotics.components.Settings;
import aima.gui.swing.demo.robotics.util.GraphicsTransfer2D;
import aima.gui.swing.demo.robotics.util.GuiBase;
import aima.gui.swing.demo.robotics.util.ListTableModel;

/**
 * This generic class provides a graphical user interface for the {@link MonteCarloLocalization} in a two-dimensional environment.<br/>
 * It makes use of the {@link Settings} class to store and retrieve all its modifiable parameters (e.g. size of the particle cloud).<br/>
 * Please note that an {@code ISettingsListener} is registered for {@link AbstractSettingsListener}{@code .PARTICLE_COUNT_KEY} in the settings
 * to be able to change the particle cloud's size while "Auto Locate" is running. Thus one should not register another listener to manage the particle cloud size.<br/>
 * Additionally, the key {@link AbstractSettingsListener}{@code .MAP_FILE_KEY} is used to store the last used map path in the settings.<br/>
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <P> a pose implementing {@link IPose2D}.
 * @param <M> a movement (or sequence of movements) of the robot, implementing {@link IMclMove}. 
 * @param <R> a range measurement, implementing {@link AbstractRangeReading}.
 */
public class GenericMonteCarloLocalization2DApp<P extends IPose2D<P,M>,M extends IMclMove<M>, R extends AbstractRangeReading> {
	
	protected ExecutorService backgroundThread = Executors.newSingleThreadExecutor();
	protected MonteCarloLocalization<P,Angle,M,AbstractRangeReading> mcl;
	protected MclCartesianPlot2D<P, M, AbstractRangeReading> map;
	protected IMclRobot<Angle,M,AbstractRangeReading> robot;
	protected IRobotGui robotGui;
	protected Settings settingsGui;
	protected MclCore core;
	protected MclGui gui;
	protected File lastMapFile = null;
	
	/**
	 * @param mcl an instance of {@link MonteCarloLocalization} for the type parameters {@code <P>}, {@code Angle}, {@code <M>}, {@code AbstractRangeReading}.
	 * @param map an instance of {@link MclCartesianPlot2D} for the type parameters {@code <P>}, {@code <M>}, {@code AbstractRangeReading}.
	 * @param robot an instance of a class implementing {@link IMclRobot} for the type parameters {@code Angle}, {@code <M>}, {@code AbstractRangeReading}.
	 * @param robotGui an instance of a class implementing {@link IRobotGui} that is able to manage the robot used with this Monte-Carlo-Localization.
	 * @param settingsGui an instance of {@link Settings} which has all those settings loaded that should be accessible for manipulation by the user and
	 * those settings mentioned in this class' documentation.
	 */
	public GenericMonteCarloLocalization2DApp(MonteCarloLocalization<P,Angle,M,AbstractRangeReading> mcl, MclCartesianPlot2D<P, M, AbstractRangeReading> map, IMclRobot<Angle,M,AbstractRangeReading> robot, IRobotGui robotGui, Settings settingsGui) {
		this.mcl = mcl;
		this.map = map;
		this.robot = robot;
		this.robotGui = robotGui;
		this.settingsGui = settingsGui;
		this.core = new MclCore();
		this.gui = new MclGui();
		String mapPath = settingsGui.getSetting(AbstractSettingsListener.MAP_FILE_KEY);
		if(mapPath != null) this.lastMapFile = new File(mapPath);
		this.settingsGui.registerListener(AbstractSettingsListener.PARTICLE_COUNT_KEY, this.core);
	}
	
	/**
	 * Shows the application.
	 */
	public void show() {
		gui.setVisible(true);
	}
	
	/**
	 * Creates the {@code JFrame} of the application and returns it.
	 * @return the main frame of the application.
	 */
	public JFrame constructApplicationFrame() {
		gui.buildPanels();
		return gui;
	}
	
	/**
	 * The core manages the particle cloud size and issues commands to the robot whilst updating the GUI according to the results of these commands.
	 * 
	 */
	protected class MclCore implements Runnable, Settings.ISettingsListener  {
		
		private JButton button;
		private Semaphore guiNotify = new Semaphore(0);
		private Semaphore runningLock = new Semaphore(1);
		private boolean running = false;
		
		private void waitGui() {
			try {
				guiNotify.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		private void clearGuiWaiting() {
			guiNotify.drainPermits();
		}

		private void successGuiWaiting() {
			if(guiNotify.availablePermits() == 0) guiNotify.release();
		}

		private void setButton(JButton button) {
			this.button = button;
		}
		
		private void setRunning(boolean running) {
			try {
				runningLock.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.running = running;
			runningLock.release();
		}

		private boolean isRunning() {
			try {
				runningLock.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gui.enableButtons(gui.buttonStateInit);
			final boolean result = running;
			runningLock.release();
			return result;
		}
		
		private synchronized void updateCloudSize(int size) {
			mcl.setParticleCount(size);
			if(map.isLoaded()) { 
				mcl.generateCloud();
				gui.displayParticles();
			}
		}
		
		private synchronized void generateParticles() {
			mcl.generateCloud();
			gui.displayParticles();
		}
		
		private synchronized void move() throws RobotException {
			M move = robot.performMove();
			if(move == null) {
				throw new NullPointerException();
			}
			mcl.applyMove(move);
			gui.displayMove(move);
			gui.displayParticles();
		}
		
		private synchronized void rangeReading() throws RobotException {
			AbstractRangeReading[] rangeReadings = robot.getRangeReadings();
			if(rangeReadings == null) {
				throw new NullPointerException();
			}
			mcl.weightParticles(rangeReadings);
			gui.displayRangeReadings(rangeReadings);
			gui.displayParticles();
		}
		
		private synchronized P resample() {
			mcl.resampleParticles();
			gui.displayParticles();
			return mcl.getPose();
		}
		/**
		 * Called upon pressing "Auto Locate". This performs the individual steps of the Monte-Carlo-Localization algorithm looped one after the other until 
		 * the terminating condition is met, that is, the robot is located with sufficient accuracy. 
		 */
		@Override
		public void run() {
			clearGuiWaiting();
			P result = null;
			try {
				while(result == null) {
					//1. Move the Robot:
					move();
					waitGui();
					if(!running) break;
					//2. WeightParticles through the ranges:
					rangeReading();
					waitGui();
					if(!running) break;
					//3. Reselection of particles:
					result = resample();
					waitGui();
					if(!running) break;
				}
				gui.displayResult(result);
				waitGui();
			} catch (NullPointerException e) {
				/*A NullPointerException may happen if the robot wasn't initialized before.*/
				robotGui.notifyInitialize();
			} catch (RobotException e) {
				/*A RobotException may be thrown if the robot disconnected for some reason.*/
			}
			try {
				runningLock.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			running = false;
			gui.enableButtons(gui.buttonStateNormal);
			button.setText(gui.autoLocateTitle);
			runningLock.release();
		}

		@Override
		public void notifySetting(String key, String value) {
			final int valueNumber = Integer.parseInt(value);
			if(key.equals(AbstractSettingsListener.PARTICLE_COUNT_KEY)) {
				updateCloudSize(valueNumber);
			}
		}
	}
	
	/**
	 * The main application {@code JFrame}.
	 */
	protected class MclGui extends JFrame {
		private static final long serialVersionUID = 1L;
		private static final int WINDOW_WIDTH = 800;
		private static final int WINDOW_HEIGHT = 500;
		private static final int CLEARANCE = 5;
		private static final float BUTTON_HEIGHT_BREAK = 1.0f / 16.0f; 
		private static final int MAP_CLEARANCE = 20;
		private static final float PANEL_SIZE_PERCENT = 1.0f / 6.0f;
		private final String autoLocateTitle = "Auto Locate";
		private final String autoLocateStopTitle = "Stop";
		private final boolean[] buttonStateStart  = {true,  true,  false, false, false, false, false, true};
		private final boolean[] buttonStateNormal = {true,  true,  true,  true,  true,  true,  true,  true};
		private final boolean[] buttonStateInit   = {false, false, false, false, false, false, false, true};
		private final boolean[] buttonStateAuto   = {false, false, false, false, false, true,  false, true};
		
		protected JPanel leftPanel;
		protected JPanel centerPanel;
		protected JPanel rightPanel;
		protected JButton[] buttons;
		protected boolean[] previousButtonState;
		protected JLabel localizationResult;
		protected JLabel jLRangeReading;
		protected JTextArea jtARangeReading;
		protected ListTableModel movesModel;
		protected JTable jTMoves;
		protected JScrollPane movesScrollPane;
		protected JSlider jSliderZoom;
		protected JScrollBar horizontalScroll;
		protected JScrollBar verticalScroll;
		protected ScrollListener scrollListener = new ScrollListener();
		protected MapDrawer md;
		/**
		 * Called upon pressing "Initialize Robot".
		 */
		protected Runnable runInitRobot = new Runnable() {
			@Override
			public void run() {
				md.drawRobot(robotGui.initializeRobot());
				buttons[0].setText(robotGui.getButtonString());
				enableButtons(previousButtonState);
			}
		};
		/**
		 * Called upon pressing "Load Map".
		 */
		protected MapLoader mapLoader = new MapLoader();
		/**
		 * Called upon pressing "Move".
		 */
		protected Runnable runMove = new Runnable() {
			@Override
			public void run() {
				try {
					core.clearGuiWaiting();
					core.move();
					core.waitGui();
				} catch(NullPointerException e) {
					/*A NullPointerException may happen if the robot wasn't initialized before.*/
					robotGui.notifyInitialize();
				} catch(RobotException e) {
					/*A RobotException may be thrown if the robot disconnected for some reason.*/
				}
				enableButtons(gui.buttonStateNormal);
			}
		};
		/**
		 * Called upon pressing "Range Reading".
		 */
		protected Runnable runRangeReading = new Runnable() {
			@Override
			public void run() {
				try {
					core.clearGuiWaiting();
					core.rangeReading();
					core.waitGui();
				} catch(NullPointerException e) {
					/*A NullPointerException may happen if the robot wasn't initialized before.*/
					robotGui.notifyInitialize();
				} catch(RobotException e) {
					/*A RobotException may be thrown if the robot disconnected for some reason.*/
				}
				enableButtons(gui.buttonStateNormal);
			}
		};
		/**
		 * Called upon pressing "Resample".
		 */
		protected Runnable runResample = new Runnable() {
			@Override
			public void run() {
				core.clearGuiWaiting();
				P result = core.resample();
				core.waitGui();
				gui.displayResult(result);
				enableButtons(gui.buttonStateNormal);
			}
		};
		/**
		 * Called upon pressing "Clear GUI".
		 */
		protected Runnable runClean = new Runnable() {
			@Override
			public void run() {
				movesModel.clear();
				md.clearMap();
				core.generateParticles();
				localizationResult.setText("Result:");
				jtARangeReading.setText("");
				enableButtons(gui.buttonStateNormal);
			}
		};
		
		private double mapWidth = 1.0d;
		private double mapHeight = 1.0d;
		private double translateX = 0.0d;
		private double translateY = 0.0d;
		
		/**
		 * Creates all components and action listeners for the GUI.
		 */
		protected void buildPanels() {
			setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
			setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
			setTitle("Monte-Carlo-Localization");
			getContentPane().setLayout(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			leftPanel = new JPanel();
			leftPanel.setLayout(null);
			leftPanel.setBackground(new Color(119,136,153));
			
			buttons = new JButton[8];
			previousButtonState = new boolean[buttons.length];
			
			buttons[0] = new JButton(IRobotGui.DEFAULT_BUTTON_STRING);
			buttons[0].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					saveButtonState();
					enableButtons(gui.buttonStateInit);
					backgroundThread.execute(runInitRobot);
				}
			});
			buttons[1] = new JButton("Load Map");
			buttons[1].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					saveButtonState();
					enableButtons(gui.buttonStateInit);
					backgroundThread.execute(mapLoader);
				}
			});
			buttons[2] = new JButton("Move");
			buttons[2].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					enableButtons(gui.buttonStateInit);
					backgroundThread.execute(runMove);
				}
			});
			buttons[3] = new JButton("Range Reading");
			buttons[3].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					enableButtons(gui.buttonStateInit);
					backgroundThread.execute(runRangeReading);
				}
			});
			buttons[4] = new JButton("Resample");
			buttons[4].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					enableButtons(gui.buttonStateInit);
					backgroundThread.execute(runResample);
				}
			});
			buttons[5] = new JButton(autoLocateTitle);
			buttons[5].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(!core.isRunning()) {
						JButton button = (JButton) arg0.getSource();
						button.setText(autoLocateStopTitle);
						core.setButton(button);
						core.setRunning(true);
						if(md.clearResult()) {
							core.generateParticles();
						}
						enableButtons(buttonStateAuto);
						backgroundThread.execute(core);
					} else {
						core.setRunning(false);
					}
				}
			});
			buttons[6] = new JButton("Clear GUI");
			buttons[6].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					enableButtons(gui.buttonStateInit);
					backgroundThread.execute(runClean);
				}
			});
			buttons[7] = new JButton("Settings");
			buttons[7].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					settingsGui.show();
				}
			});
			for(JButton button:buttons) button.setLayout(null);
			
			localizationResult = new JLabel("Result: ");
			localizationResult.setLayout(null);
			
			centerPanel = new JPanel();
			centerPanel.setLayout(null);
			
			md = new MapDrawer();
			
			jSliderZoom = new JSlider(1, 100);
            jSliderZoom.setValue(1);
            jSliderZoom.addChangeListener(new ChangeListener() { 
            	@Override
                public void stateChanged(ChangeEvent e) {
                	md.scaleMap();
                 }
             });
            jSliderZoom.setLayout(null);
            
            horizontalScroll = new JScrollBar(JScrollBar.HORIZONTAL);
            horizontalScroll.setEnabled(false);
            horizontalScroll.addAdjustmentListener(scrollListener);
            horizontalScroll.setLayout(null);
            verticalScroll = new JScrollBar(JScrollBar.VERTICAL);
            verticalScroll.setEnabled(false);
            verticalScroll.addAdjustmentListener(scrollListener);
            verticalScroll.setLayout(null);
			
			rightPanel = new JPanel();
			rightPanel.setLayout(null);
			
			jLRangeReading = new JLabel("Range Reading:");
			jLRangeReading.setLayout(null);
			jtARangeReading = new JTextArea();
			jtARangeReading.setLayout(null);
			jtARangeReading.setEditable(false);
			jtARangeReading.setLineWrap(true);
			jtARangeReading.setWrapStyleWord(true);
			
			movesModel = new ListTableModel("Moves");
			jTMoves = new JTable(movesModel);
			jTMoves.setFillsViewportHeight(true);
			movesScrollPane = new JScrollPane(jTMoves, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jTMoves.setRowHeight((int) (jTMoves.getRowHeight() * 2.5d));
            
			enableButtons(buttonStateStart);
			for(JButton button:buttons) leftPanel.add(button);
			leftPanel.add(localizationResult);
			leftPanel.setLocation(CLEARANCE, CLEARANCE);
			
			centerPanel.add(md);
			centerPanel.add(verticalScroll);
			centerPanel.add(horizontalScroll);
			centerPanel.add(jSliderZoom);
			
			rightPanel.add(movesScrollPane);
			rightPanel.add(jtARangeReading);
			rightPanel.add(jLRangeReading);
			
			getContentPane().add(leftPanel);
			getContentPane().add(centerPanel);
			getContentPane().add(rightPanel);
			
			
			
			addComponentListener(new ComponentListener() {
				@Override
				public void componentResized(ComponentEvent e) {
					leftPanel.setSize((int) ((getWidth() - getInsets().left - getInsets().right) * PANEL_SIZE_PERCENT) - CLEARANCE, getHeight() - 2 * CLEARANCE - getInsets().top - getInsets().bottom);
					centerPanel.setBounds(leftPanel.getWidth() + 2 * CLEARANCE, CLEARANCE, (getWidth() - getInsets().left - getInsets().right) - 2 * leftPanel.getWidth() - 4 * CLEARANCE, leftPanel.getHeight());
					rightPanel.setBounds(centerPanel.getX() + centerPanel.getWidth() + CLEARANCE, CLEARANCE, leftPanel.getWidth(), leftPanel.getHeight());	
					
					final int buttonHeight = (int) (leftPanel.getHeight() * BUTTON_HEIGHT_BREAK);
					for(int i=0; i < buttons.length; i++) buttons[i].setBounds(0, i * (buttonHeight + CLEARANCE), leftPanel.getWidth(), buttonHeight);
					
					localizationResult.setBounds(0 , buttons[buttons.length - 1].getY() + buttons[buttons.length - 1].getHeight() + CLEARANCE, leftPanel.getWidth(), 60);
					
					jLRangeReading.setSize(rightPanel.getWidth(), 20);
					jtARangeReading.setBounds(0, CLEARANCE + jLRangeReading.getHeight(), rightPanel.getWidth(), rightPanel.getHeight() / 2 - CLEARANCE - jLRangeReading.getHeight());
					
					movesScrollPane.setBounds(0, jtARangeReading.getY() + jtARangeReading.getHeight() + CLEARANCE, rightPanel.getWidth(), rightPanel.getHeight() / 2 - CLEARANCE);
					jTMoves.setSize(movesScrollPane.getWidth(), movesScrollPane.getHeight());
					
					md.setSize(centerPanel.getWidth() - MAP_CLEARANCE, centerPanel.getHeight() - 2 * MAP_CLEARANCE - CLEARANCE);
					
					horizontalScroll.setBounds(0, md.getHeight() + CLEARANCE, md.getWidth(), MAP_CLEARANCE - CLEARANCE);
					verticalScroll.setBounds(md.getWidth() + CLEARANCE, 0, MAP_CLEARANCE - CLEARANCE, md.getHeight());
					
					jSliderZoom.setBounds(0, horizontalScroll.getY() + MAP_CLEARANCE, md.getWidth(), MAP_CLEARANCE - CLEARANCE);
					
					//Invalidate the values: Scrollbars only repaint on a change of these values!
					horizontalScroll.setValues(-1, 0, -1, -1);
					verticalScroll.setValues(-1, 0, -1, -1);
					md.scaleMap();
				}
				@Override
				public void componentHidden(ComponentEvent arg0) { }
				@Override
				public void componentMoved(ComponentEvent arg0) { }
				@Override
				public void componentShown(ComponentEvent arg0) { }
			});
		}
		
		/**
		 * (de)activate all buttons. For each button a boolean describing its state is passed.
		 * @param buttonStates one of the four boolean arrays {@code buttonStateStart, buttonStateNormal, buttonStateInit, buttonStateAuto}.
		 */
		protected void enableButtons(boolean[] buttonStates) {
			for(int i=0; i < buttons.length; i++) buttons[i].setEnabled(buttonStates[i]);
		}
		
		/**
		 * Saves the current activation state of all buttons into {@code previousButtonState}.
		 */
		protected void saveButtonState() {
			previousButtonState = new boolean[buttons.length];
			for(int i=0; i < buttons.length; i++) previousButtonState[i] = buttons[i].isEnabled();
		}
		
		/**
		 * Tells the {@link MapDrawer} {@code md} to display the loaded {@link MclCartesianPlot2D} {@code map}.
		 */
		protected void createMap() {
			findMapSize();
			if(mapWidth <= 1.0 || mapHeight <= 1.0){
				GuiBase.showMessageBox("Map size could not be calculated!");
				return;
			}
			md.drawMap();
			md.scaleMap();
			enableButtons(buttonStateNormal);
		}
		
		/**
		 * Determines the map size by the smallest and greatest values of the shapes to be drawn. 
		 */
		protected void findMapSize() {
			Iterator<Rect2D> areaIterator = map.getAreaBoundaries();
			Iterator<Rect2D> obstacleIterator = map.getObstacleBoundaries();
			
			double minX = Double.POSITIVE_INFINITY;
			double maxX = Double.NEGATIVE_INFINITY;
			double minY = Double.POSITIVE_INFINITY;
			double maxY = Double.NEGATIVE_INFINITY;
			
			while(areaIterator.hasNext()) {
				Rect2D rect = areaIterator.next();
				minX = minX > rect.getLowerLeft().getX() ? rect.getLowerLeft().getX() : minX;
				minY = minY > rect.getLowerLeft().getY() ? rect.getLowerLeft().getY() : minY;
				maxX = maxX < rect.getUpperRight().getX() ? rect.getUpperRight().getX() : maxX;
				maxY = maxY < rect.getUpperRight().getY() ? rect.getUpperRight().getY() : maxY;
			}
			while(obstacleIterator.hasNext()) {
				Rect2D rect = obstacleIterator.next();
				minX = minX > rect.getLowerLeft().getX() ? rect.getLowerLeft().getX() : minX;
				minY = minY > rect.getLowerLeft().getY() ? rect.getLowerLeft().getY() : minY;
				maxX = maxX < rect.getUpperRight().getX() ? rect.getUpperRight().getX() : maxX;
				maxY = maxY < rect.getUpperRight().getY() ? rect.getUpperRight().getY() : maxY;
			}
			mapWidth  = maxX - minX;
			mapHeight = maxY - minY;
			final double mapBorder = 0.1d * mapWidth > mapHeight ? mapWidth : mapHeight;
			translateX = -minX + mapBorder;
			translateY = -minY + mapBorder;
			
			mapWidth += 2 * mapBorder;
			mapHeight += 2 * mapBorder;
		}
		
		/**
		 * Shows a set of {@link AbstractRangeReading}.
		 * @param rangeReadings the range readings to be displayed.
		 */
		protected void displayRangeReadings(AbstractRangeReading[] rangeReadings) {
			String ranges = "";
			for(AbstractRangeReading rangeReading: rangeReadings) ranges +=  rangeReading.toString() + "\n";
			jtARangeReading.setText(ranges);
		}
		
		/**
		 * Shows a {@code M} move.
		 * @param move the move to be displayed.
		 */
		protected void displayMove(final M move) {
			movesModel.add("<HTML>" + move.toString() + "</HTML>");
		}
		
		/**
		 * Displays the particle cloud on the map.
		 */
		protected void displayParticles() {	
			md.drawParticles(mcl.getParticles());
			
		}

		/**
		 * Shows the localized position of the robot on the map and in a label.
		 * @param result the position of the robot to be displayed.
		 */
		protected void displayResult(P result) {
			if(result != null) {
				String resultOutputString = "( " + GuiBase.getFormat().format(result.getX()) + " | " + GuiBase.getFormat().format(result.getY()) + " )";
				localizationResult.setText("<HTML>Result:<BR>" + resultOutputString + "</HTML>");
				md.showResult(result);
			} else {
				core.successGuiWaiting();
			}
		}
		
		/**
		 * The scroll listener is registered on both of the map's scroll bars. It is used to scroll over the map.
		 */
		protected class ScrollListener implements AdjustmentListener {
            	
            	private boolean notifyMapDrawer = false;
            	
				@Override
				public void adjustmentValueChanged(AdjustmentEvent arg0) {
					if(notifyMapDrawer) {
						md.repaint();
					}
				}
				
				/**
				 * Sets whether the map should be notified of an adjustment event.
				 * @param b true if the map should be notified.
				 */
				protected void setNotify(boolean b) {
					notifyMapDrawer = b;
				}
		}
		
		/**
		 * Hands off the loading of a map file to a different thread without freezing the GUI. 
		 * Handles exceptions during the loading and processing of a file.
		 */
		protected class MapLoader implements Runnable {
			
			private JFileChooser chooser;
			
			/**
			 * Constructs a new map loader.
			 */
			protected MapLoader() {
				chooser = new JFileChooser();
				//Change the background color of the component holding the "Search in:" label.
				for(int i=0;i<chooser.getComponentCount();i++) {
					Component component = chooser.getComponent(i);
					if(component instanceof JComponent) {
						((JComponent) component).setBackground(GuiBase.getBackgroundColor());
					}
				}
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			      "Map (*.svg)",  "svg");
			    chooser.setFileFilter(filter);
			    GuiBase.initMessageBox();
			}
			
			@Override
			public void run() {
			    if(lastMapFile != null) chooser.setSelectedFile(lastMapFile);
			    int returnVal = chooser.showDialog(null, null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	lastMapFile = chooser.getSelectedFile();
			    	settingsGui.setSetting(AbstractSettingsListener.MAP_FILE_KEY, lastMapFile.getPath());
			    	GuiBase.showMessageBox("Loading map, please wait...", false);
			    	try {
			    		loadMap();
			    		return;
			    	} catch (Exception e) {
			    		GuiBase.showMessageBox(e.getMessage());
					}
			    }
			    enableButtons(previousButtonState);
			}

			/**
			 * Tries to load the map file into the {@link MclCartesianPlot2D} {@code map}.
			 * @throws Exception may be thrown if either the map file is not found or is not valid in some way.
			 */
			protected void loadMap() throws Exception {
				map.loadMap(new FileInputStream(lastMapFile), new FileInputStream(lastMapFile));
				if(!map.getAreas().hasNext()) {
					throw new Exception("Map has no valid area.");
				}
				else if(!map.getObstacles().hasNext()) { 
					throw new Exception("Map has no obstacles.");
				}
				core.generateParticles();
				gui.createMap();
				GuiBase.hideMessageBox();
			}
		}
		
		/**
		 * This class is a panel where the map, the particles and the robot are displayed.
		 */
		protected class MapDrawer extends JPanel {
			
			private static final long serialVersionUID = 1L;
			private static final int POSE_WIDTH = 4;
			private static final int POSE_HEIGHT = 4;
			private static final int RESULT_WIDTH = 10;
			private static final int RESULT_HEIGHT = 10;
			
			private boolean robotInitialized = false;
			private boolean mapLoaded = false;
			private ArrayList<Particle<P, Angle, M>> particles;
			private P locResult;
			private boolean gotResult = false;
			private double minScaleFactor = 1.0d;
			private double realScaleFactor = 1.0d;
			
			/**
			 * Default constructor. Sets the background color to the GUI foreground color.
			 */
			protected MapDrawer() {
				this.setBackground(GuiBase.getTextColor());
			}
	        
			/**
			 * With this method, the {@code MapDrawer} is told that it can commence the drawing of the map.
			 */
			protected void drawMap() {
	        	mapLoaded = true;
	        	repaint();
	        }
	      
	        /**
			*This method clears the panel of any map data.
			*/
			protected void deleteMap() {
	        	mapLoaded = false;
	    		repaint();
	    	}
	        
			/**
			 * Deletes everything but the map data from the panel.
			 */
			protected void clearMap() {
	        	particles = null;
	        	gotResult = false;
	        	repaint();
	        }
	        
	        /**
	         * Draws the particle cloud.
	         * @param particles the {@code ArrayList} containing the particles.
	         */
	        @SuppressWarnings("unchecked")
	        protected void drawParticles(final ArrayList<Particle<P, Angle, M>> particles) {
	        	this.particles = (ArrayList<Particle<P, Angle, M>>) particles.clone();
	        	repaint();
	        }
	    	
	        /**
	         * Draws the localized position of the robot.
	         * @param result the estimated position of the robot.
	         */
	        protected void showResult(P result) {
	    	   locResult = result;
	    	   gotResult = true;
	    	   repaint();
	        }
	        
	        /**
	         * Deletes any previous localization result.
	         * @return true if a result was actually deleted.
	         */
	        protected boolean clearResult() {
	        	if(gotResult) {
	        		gotResult = false;
	        		return true;
	        	}
	        	return false;
	        }
	        
	        /**
	         * Draws the actual position of the robot.
	         * @param initialized true if the robot is to be drawn.
	         */
	        protected void drawRobot(boolean initialized) {
				robotInitialized = initialized;
				repaint();
			}
	        
	        /**
	         * This method calculates the scale factors to scale the map by.
	         */
	        protected void scaleMap() {
	        	final double calcXScaleFactor = (double) getWidth()  / mapWidth;
				final double calcYScaleFactor = (double) getHeight() / mapHeight;
				if((Double.isInfinite(calcXScaleFactor) && Double.isInfinite(calcYScaleFactor)) || (Double.isNaN(calcXScaleFactor) && Double.isNaN(calcYScaleFactor))) {
					minScaleFactor = 1.0d;
				} else if(calcXScaleFactor >= calcYScaleFactor || Double.isNaN(calcXScaleFactor)) {
					minScaleFactor = calcYScaleFactor - 0.0010d;
				} else {
					minScaleFactor = calcXScaleFactor - 0.0005d;
				}
				realScaleFactor = jSliderZoom.getValue() == 1 ? minScaleFactor : jSliderZoom.getValue();
				double realMapWidth = mapWidth * realScaleFactor;
				double realMapHeight = mapHeight * realScaleFactor;
				scrollListener.setNotify(false);
				if(realMapWidth <= getWidth()) {
					horizontalScroll.setEnabled(false);
					horizontalScroll.setValues(0,0,0,0);
				} else {
					realMapWidth *= 1.2d;
					horizontalScroll.setValues((int) (horizontalScroll.getValue() + getWidth() > realMapWidth ? realMapWidth - getWidth() : horizontalScroll.getValue()), getWidth(), 0, (int) realMapWidth);
					horizontalScroll.setEnabled(true);
				}
				if(realMapHeight <= getHeight()) {
					verticalScroll.setEnabled(false);
					verticalScroll.setValues(0,0,0,0);
				} else {
					realMapHeight *= 1.2d;
					verticalScroll.setValues((int) (verticalScroll.getValue() + getHeight() > realMapHeight ? realMapHeight - getHeight() : verticalScroll.getValue()), getHeight(), 0, (int) realMapHeight);
					verticalScroll.setEnabled(true);
				}
				scrollListener.setNotify(true);
	        	repaint();
	        }
	        
			@Override
			public void paint(Graphics gra) {
				super.paint(gra);
				if(mapLoaded) {
					Graphics2D g2d = (Graphics2D) gra;
					g2d.translate(translateX*realScaleFactor-horizontalScroll.getValue(),translateY*realScaleFactor-verticalScroll.getValue());
					g2d.scale(realScaleFactor, realScaleFactor);
					//draw the map:
					Iterator<IGeometric2D> areaIterator = map.getAreas();
					Iterator<IGeometric2D> obstacleIterator = map.getObstacles();
					while (areaIterator.hasNext()) {
						g2d.setColor(GuiBase.getAreaColor());
						g2d.draw(GraphicsTransfer2D.transfer(areaIterator.next()));
					}
					while (obstacleIterator.hasNext()) {
						g2d.setColor(GuiBase.getBackgroundColor());
						Shape shape = GraphicsTransfer2D.transfer(obstacleIterator.next());
						g2d.draw(shape);
						g2d.fill(shape);
					}
					if(particles != null) {
						//Draw the particles onto the map:
						for (Particle<P, Angle, M> p: particles) {
							//Color goes from red to green according to the weight:
							g2d.setColor(Color.getHSBColor(p.getWeight() / 3.0f, 1.0f, 1.0f));
							g2d.fillOval((int) p.getPose().getX() - POSE_WIDTH/2, (int) p.getPose().getY() - POSE_HEIGHT/2, POSE_WIDTH, POSE_HEIGHT);
							final double h = p.getPose().getHeading();
							final int x2 = (int) (p.getPose().getX() + POSE_WIDTH * Math.cos(h));
							final int y2 = (int) (p.getPose().getY() + POSE_HEIGHT * Math.sin(h));
							g2d.drawLine((int) p.getPose().getX(), (int) p.getPose().getY(), x2, y2);
						}
					}
					if(robotInitialized && robot instanceof VirtualRobot) {
						g2d.setColor(Color.BLACK);
						g2d.fillOval((int) ((VirtualRobot) robot).getPose().getX() - POSE_WIDTH/2, (int) ((VirtualRobot) robot).getPose().getY() - POSE_HEIGHT/2, POSE_WIDTH, POSE_HEIGHT);
					}
					if(gotResult) {
						g2d.setColor(Color.BLUE);
						g2d.drawOval((int) locResult.getX() - RESULT_WIDTH / 2, (int) locResult.getY() - RESULT_HEIGHT / 2, RESULT_WIDTH, RESULT_HEIGHT);
					}
				}
				core.successGuiWaiting();
			}
		}
	}
}
