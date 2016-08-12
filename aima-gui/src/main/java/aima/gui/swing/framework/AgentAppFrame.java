package aima.gui.swing.framework;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * <p>
 * Universal frame for building graphical agent applications. It provides
 * buttons for controlling the application and two panels and a status bar to
 * visualize agent, environment, and general simulation state.
 * </p>
 * <p>
 * To make the frame fit to your needs, you will at least have to add some
 * selectors. The frame is configurable at run-time, so subclassing will not
 * always be necessary.
 * </p>
 * 
 * @author Ruediger Lunde
 */
public class AgentAppFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	/** The controller, which executes the domain-level commands. */
	protected AgentAppController controller;
	/** Background thread for simulation. */
	private SimulationThread simulationThread;
	/** Contains selector specification and resulting comboboxes. */
	private SelectorContainer selectors;
	/**
	 * Listens to all button events and delegates most of the work to the
	 * controller.
	 */
	protected FrameActionListener actionListener;

	private JToolBar toolbar;
	private JButton clearButton;
	private JButton prepareButton;
	private JButton runButton;
	protected JButton stepButton;
	private JToggleButton pauseButton;
	private JButton cancelButton;
	private JLabel statusLabel;

	protected JSplitPane centerPane;
	private MessageLoggerPanel messageLogger;
	private AgentAppEnvironmentView envView;

	/** Standard constructor. */
	public AgentAppFrame() {
		actionListener = new FrameActionListener();
		initComponents();
		pack();
		// redirect the standard output into the text area
		System.setOut(messageLogger.getPrintStream());
		// System.setErr(messageLogger.getPrintStream());
		updateEnabledState();
	}

	/** Returns a logger which displays messages in a message log pane. */
	public MessageLoggerPanel getMessageLogger() {
		return messageLogger;
	}

	/**
	 * Specifies a set of combo boxes to be added to the toolbar. Each combobox
	 * has a name, which is used to access its selection state on software level
	 * and optionally a tool tip, which is shown to the user.
	 * 
	 * @param tooltips
	 *            Array of strings or null.
	 * 
	 */
	public void setSelectors(String[] selectorNames, String[] tooltips) {
		AgentAppController cont = controller;
		controller = null; // suppress reactions on parameter changes.
		selectors.setSelectors(selectorNames, tooltips);
		controller = cont;
	}

	/**
	 * Sets the choice items and the default value of a specified selector. The
	 * first item has index 0.
	 */
	public void setSelectorItems(String selectorName, Object[] items,
			int defaultIdx) {
		AgentAppController cont = controller;
		controller = null; // suppress reactions on parameter changes.
		selectors.setSelectorItems(selectorName, items, defaultIdx);
		controller = cont;
	}

	/** Adjusts selection state of all selectors. */
	public void setDefaultSelection() {
		AgentAppController cont = controller;
		controller = null; // suppress reactions on parameter changes.
		selectors.setDefaults();
		if (cont != null) {
			controller = cont;
			selectionChanged(null);
		}
	}

	/**
	 * Returns an object which represents the current selection state of all
	 * selectors.
	 */
	public SelectionState getSelection() {
		return selectors.getSelection();
	}

	/**
	 * Returns the environment view which is currently used to display the
	 * agents in their environment.
	 */
	public AgentAppEnvironmentView getEnvView() {
		return envView;
	}

	/**
	 * Replaces the environment view. The environment view is the panel to the
	 * left of the splitbar. It typically implements a 2D-visualization of
	 * agents in their environment.
	 */
	public void setEnvView(AgentAppEnvironmentView view) {
		envView = view;
		centerPane.add(JSplitPane.LEFT, envView);
		envView.setController(controller);
	}

	/** Specifies how to distribute extra space when resizing the split pane. */
	public void setSplitPaneResizeWeight(double value) {
		centerPane.setResizeWeight(value);
	}

	/** Defines, who should react on button and selection events. */
	public void setController(AgentAppController controller) {
		this.controller = controller;
		if (envView != null)
			envView.setController(controller);
	}

	/** Displays a text in the status bar. */
	public void setStatus(String status) {
		statusLabel.setText(status);
	}

	/** Returns the current simulation thread or null (no simulation running). */
	protected SimulationThread getSimulationThread() {
		return simulationThread;
	}

	/**
	 * Sets the current agent thread and updates the enabled state.
	 * 
	 * @param thread
	 *            A thread or null.
	 */
	protected void setSimulationThread(SimulationThread thread) {
		simulationThread = thread;
		updateEnabledState();
	}

	/** Returns true if simulation pause button was pressed. */
	public boolean simulationPaused() {
		return pauseButton.isSelected();
	}

	/** Assembles the inner structure of the frame. */
	private void initComponents() {
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				System.exit(0);
			}
		});
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		selectors = new SelectorContainer();
		toolbar.add(Box.createHorizontalGlue());

		clearButton = new JButton("Clear");
		clearButton.setToolTipText("Clear Views");
		clearButton.addActionListener(actionListener);
		toolbar.add(clearButton);
		prepareButton = new JButton("Prepare");
		prepareButton.setToolTipText("Prepare Simulation");
		prepareButton.addActionListener(actionListener);
		toolbar.add(prepareButton);
		runButton = new JButton("Run");
		runButton.setToolTipText("Run Simulation");
		runButton.addActionListener(actionListener);
		toolbar.add(runButton);
		stepButton = new JButton("Step");
		stepButton.setToolTipText("Execute Simulation Step");
		stepButton.addActionListener(actionListener);
		toolbar.add(stepButton);
		pauseButton = new JToggleButton("Pause");
		pauseButton.setToolTipText("Pause Simulation");
		pauseButton.addActionListener(actionListener);
		toolbar.add(pauseButton);
		contentPanel.add(toolbar, java.awt.BorderLayout.NORTH);

		messageLogger = new MessageLoggerPanel();

		centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		centerPane.add(JSplitPane.RIGHT, messageLogger);
		centerPane.setDividerSize(5);
		centerPane.setResizeWeight(0.7);
		contentPanel.add(centerPane, BorderLayout.CENTER);

		JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel("");
		statusLabel.setBorder(new javax.swing.border.EtchedBorder()); // BevelBorder
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Cancel Simulation");
		cancelButton.addActionListener(actionListener);
		cancelButton.setPreferredSize(new java.awt.Dimension(80, 20));
		cancelButton.setBorder(new javax.swing.border.EtchedBorder());
		statusPanel.add(cancelButton, BorderLayout.EAST);
		contentPanel.add(statusPanel, BorderLayout.SOUTH);
	}

	/** Enables/disables combos and buttons. */
	public void updateEnabledState() {
		boolean b = (getSimulationThread() == null);
		boolean prep = b && controller != null && controller.isPrepared();
		clearButton.setEnabled(b);
		prepareButton.setEnabled(b);
		runButton.setEnabled(prep);
		if (b)
			pauseButton.setSelected(false);
		pauseButton.setEnabled(!b);
		stepButton.setEnabled(prep);
		cancelButton.setEnabled(!b);
		for (JComboBox<Object> combo : selectors.combos)
			combo.setEnabled(b);
	}

	/** Tells the controller to prepare simulation and updates enabled state. */
	protected void selectionChanged(String changedSelector) {
		if (controller != null) {
			controller.prepare(changedSelector);
			updateEnabledState();
		}
	}

	// ////////////////////////////////////////////////////////
	// inner classes

	/** Sends commands to the controller. */
	private class FrameActionListener implements ActionListener {
		@SuppressWarnings({ "deprecation", "unchecked" })
		public void actionPerformed(ActionEvent evt) {
			String err = "";
			try {
				if (controller != null) {
					setStatus("");
					Object source = evt.getSource();
					if (source == clearButton) {
						// additionally clear the text area
						err = "when clearing the views ";
						messageLogger.clear();
						statusLabel.setText("");
						controller.clear();
					} else if (source == prepareButton) {
						err = "when preparing simulation ";
						controller.prepare(null);
					} else if (source == runButton) {
						err = "when running simulation ";
						setStatus("");
						setSimulationThread(new SimulationThread(
								AgentAppFrame.this, controller, false));
						getSimulationThread().start();
					} else if (source == stepButton) {
						err = "when executing simulation step ";
						setStatus("");
						setSimulationThread(new SimulationThread(
								AgentAppFrame.this, controller, true));
						getSimulationThread().start();
					} else if (source == cancelButton) {
						err = "when canceling simulation ";
						SimulationThread at = getSimulationThread();
						if (at != null) {
							if (!at.isCanceled()) {
								at.interrupt();
							} else {
								// agent has ignored the interrupt
								at.stop();
								setStatus("Task stopped.");
								setSimulationThread(null);
							}
						}
					} else if (selectors.combos.contains(source)) {
						err = "when preparing the agent ";
						selectionChanged(selectors
								.getName((JComboBox<Object>) source));
					}
				}
			} catch (Exception e) {
				messageLogger.log("Error: Something went wrong " + err + "("
						+ e + ").");
				e.printStackTrace();
			}
			updateEnabledState();
		}
	}

	/** Maintains all selector comboboxes. */
	private class SelectorContainer {
		String[] selectorNames = new String[] {};
		int[] selectorDefaults = new int[] {};
		// JPanel selectorPanel = new JPanel();
		List<JComboBox<Object>> combos = new ArrayList<JComboBox<Object>>();

		public void setSelectors(String[] selectorNames, String[] tooltips) {
			this.selectorNames = selectorNames;
			this.selectorDefaults = new int[selectorNames.length];
			for (JComboBox<Object> combo : combos)
				toolbar.remove(combo);
			combos.clear();
			for (int i = 0; i < selectorNames.length; i++) {
				JComboBox<Object> combo = new JComboBox<Object>();
				combo.addActionListener(actionListener);
				combo.setVisible(false);
				combos.add(combo);
				toolbar.add(combo, i);
				if (tooltips != null)
					combo.setToolTipText(tooltips[i]);
			}
		}

		public void setSelectorItems(String selectorName, Object[] items,
				int defaultIdx) {
			JComboBox<Object> combo = getCombo(selectorName);
			combo.removeAllItems();
			for (Object item : items) {
				combo.addItem(item);
			}
			selectorDefaults[combos.indexOf(combo)] = defaultIdx;
			combo.setVisible(items.length > 0);
		}

		public void setDefaults() {
			for (int i = 0; i < selectorDefaults.length; i++) {
				if (combos.get(i).getItemCount() > 0)
					combos.get(i).setSelectedIndex(selectorDefaults[i]);
			}
		}

		public SelectionState getSelection() {
			SelectionState result = new SelectionState(selectorNames);
			for (int i = 0; i < result.size(); i++) {
				result.setState(i, combos.get(i).getSelectedIndex(), combos
						.get(i).getSelectedItem());
			}
			return result;
		}

		JComboBox<Object> getCombo(String selectorName) {
			for (int i = 0; i < selectorNames.length; i++)
				if (selectorNames[i].equals(selectorName))
					return combos.get(i);
			return null;
		}

		String getName(JComboBox<Object> combo) {
			int idx = combos.indexOf(combo);
			if (idx != -1)
				return selectorNames[idx];
			else
				return null;
		}
	}

	// ////////////////////////////////////////////////////////
	// static inner classes

	/**
	 * Contains the names of all selectors and the indices of their selected
	 * items. Instances are used to communicate the selection state between the
	 * frame and the controller.
	 */
	public static class SelectionState {
		private final List<String> selectors = new ArrayList<String>();
		private final List<Integer> selIndices = new ArrayList<Integer>();
		private final List<Object> selItems = new ArrayList<Object>();

		protected SelectionState(String[] selectors) {
			for (String sel : selectors) {
				this.selectors.add(sel);
				this.selIndices.add(null);
				this.selItems.add(null);
			}
		}

		/** Returns the number of selectors currently available. */
		public int size() {
			return selectors.size();
		}

		/**
		 * Sets the selection state of a specified selector to a specified item.
		 */
		void setState(int selectorIdx, int valIdx, Object value) {
			selIndices.set(selectorIdx, valIdx);
			selItems.set(selectorIdx, value);
		}

		/** Returns the index of the selected item of a specified selector. */
		public int getIndex(int selectorIdx) {
			return selIndices.get(selectorIdx);
		}

		/** Returns the index of the selected item of a specified selector. */
		public int getIndex(String selector) {
			return selIndices.get(selectors.indexOf(selector));
		}
		
		/** Returns the selected item of a specified selector. */
		public Object getValue(String selector) {
			return selItems.get(selectors.indexOf(selector));
		}

		/** Returns a readable representation of the selection state. */
		@Override
		public String toString() {
			StringBuffer result = new StringBuffer("State[ ");
			for (int i = 0; i < size(); i++)
				result.append(selectors.get(i) + "=" + selIndices.get(i) + " ");
			result.append("]");
			return result.toString();
		}
	}
}
