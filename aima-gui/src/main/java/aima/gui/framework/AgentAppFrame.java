package aima.gui.framework;

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
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * <p>
 * Universal frame for building graphical agent applications. It provides
 * buttons for controlling the application and two panels and a status bar to
 * visualize agent, environment, and general simulation state.
 * </p>
 * <p>
 * To make the frame fit to your needs, you will at least have to add some
 * selectors. The frame is configurable at run-time, so subclassing
 * will not always be necessary.
 * </p>
 * 
 * @author R. Lunde
 */
public class AgentAppFrame extends JFrame {

	/** The controller, which executes the domain-level commands. */
	protected AgentAppController controller;
	/** Thread running the agent. */
	protected AgentThread agentThread;
	/** Flag, indicating whether the agent is ready for running. */
	protected boolean isPrepared;
	/** Contains selector specification and resulting comboboxes. */
	private SelectorContainer selectors;

	private JToolBar toolbar;
	private JButton clearButton;
	private JButton prepareButton;
	private JButton runButton;
	private JButton cancelButton;

	JSplitPane centerPane;
	MessageLoggerPanel messageLogger;
	protected AgentAppEnvironmentView envView;

	private JLabel statusLabel;

	/** Standard constructor. */
	public AgentAppFrame() {
		initComponents();
		pack();
		// redirect the standard output into the text area
		System.setOut(messageLogger.getPrintStream());
		// System.setErr(messageLogger.getPrintStream());
		setButtonsEnabled(true);
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
	public void setSelectorItems(String selectorName, String[] items,
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
			selectionChanged();
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
	 * Returns the environment view which is currently used to display
	 * the agents in their environment.
	 */
	public AgentAppEnvironmentView getEnvView() {
		return envView;
	}
	
	/**
	 * Replaces the environment view. The environment view is the 
	 * panel to the left of the splitbar. It typically implements a
	 * 2D-visualization of agents in their environment.
	 */
	public void setEnvView(AgentAppEnvironmentView view) {
		envView = view;
		centerPane.add(JSplitPane.LEFT, envView);
	}

	/** Specifies how to distribute extra space when resizing the split pane. */
	public void setSplitPaneResizeWeight(double value) {
		centerPane.setResizeWeight(value);
	}

	/** Defines, who should react on button and selection events. */
	public void setController(AgentAppController controller) {
		this.controller = controller;
	}

	/** Displays a text in the status bar. */
	public void setStatus(String status) {
		statusLabel.setText(status);
	}

	/** Assembles the inner structure of the frame. */
	private void initComponents() {
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				System.exit(0);
			}
		});
		toolbar = new JToolBar();
		// toolbar.setFloatable(false);
		selectors = new SelectorContainer();
		toolbar.add(Box.createHorizontalGlue());

		clearButton = new JButton("Clear");
		clearButton.setToolTipText("Clear Views");
		clearButton.addActionListener(new FrameActionListener());
		toolbar.add(clearButton);
		prepareButton = new JButton("Prepare");
		prepareButton.setToolTipText("Prepare Agent");
		prepareButton.addActionListener(new FrameActionListener());
		toolbar.add(prepareButton);
		runButton = new JButton("Run");
		runButton.setToolTipText("Run Agent");
		runButton.addActionListener(new FrameActionListener());
		toolbar.add(runButton);
		getContentPane().add(toolbar, java.awt.BorderLayout.NORTH);

		messageLogger = new MessageLoggerPanel();

		centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		centerPane.add(JSplitPane.RIGHT, messageLogger);
		centerPane.setDividerSize(5);
		centerPane.setResizeWeight(0.7);
		getContentPane().add(centerPane, BorderLayout.CENTER);

		JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel("");
		statusLabel.setBorder(new javax.swing.border.EtchedBorder()); // BevelBorder
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("Cancel Agent");
		cancelButton.addActionListener(new FrameActionListener());
		cancelButton.setPreferredSize(new java.awt.Dimension(80, 20));
		cancelButton.setBorder(new javax.swing.border.EtchedBorder());
		statusPanel.add(cancelButton, BorderLayout.EAST);
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
	}

	/** Enables/disables all combos and buttons. */
	public void setButtonsEnabled(boolean b) {
		clearButton.setEnabled(b);
		prepareButton.setEnabled(b);
		runButton.setEnabled(b);
		cancelButton.setEnabled(!b);
		for (JComboBox combo : selectors.combos)
			combo.setEnabled(b);
	}

	/** Tells the controller to prepare the agent. */
	protected void selectionChanged() {
		if (controller != null) {
			controller.prepare();
			isPrepared = true;
		}
	}

	// ////////////////////////////////////////////////////////
	// inner classes

	/** Sends commands to the controller. */
	private class FrameActionListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			String err = "";
			try {
				if (controller != null) {
					setStatus("");
					Object source = evt.getSource();
					if (source == clearButton /* || source == clearMenuItem */) {
						// additionally clear the text area
						err = "when clearing the views ";
						messageLogger.clear();
						statusLabel.setText("");
						controller.clear();
						// isPrepared = false;
					} else if (source == prepareButton) {
						err = "when preparing the agent ";
						controller.prepare();
						isPrepared = true;
					} else if (source == runButton) {
						err = "when preparing the agent ";
						if (isPrepared == false)
							controller.prepare();
						err = "when running the agent ";
						setButtonsEnabled(false);
						setStatus("");
						agentThread = new AgentThread(AgentAppFrame.this, controller);
						agentThread.start();
						isPrepared = false;
					} else if (source == cancelButton) {
						err = "when cancelling the agent ";
						if (agentThread != null) {
							if (agentThread.isCanceled()) {
								agentThread.stop(); // agent has ignored the interrupt
								setButtonsEnabled(true);
								setStatus("Task stopped.");
								agentThread = null;
							} else
								agentThread.interrupt();
						}
						isPrepared = false;
					} else if (selectors.combos.contains(source)) {
						err = "when preparing the agent ";
						selectionChanged();
					}
				}
			} catch (Exception e) {
				messageLogger.log("Error: Something went wrong " + err + "(" + e
						+ ").");
				e.printStackTrace();
			}
		}
	}

	/** Maintains all selector comboboxes. */
	private class SelectorContainer {
		String[] selectorNames = new String[] {};
		int[] selectorDefaults = new int[] {};
		// JPanel selectorPanel = new JPanel();
		List<JComboBox> combos = new ArrayList<JComboBox>();

		public void setSelectors(String[] selectorNames, String[] tooltips) {
			this.selectorNames = selectorNames;
			this.selectorDefaults = new int[selectorNames.length];
			for (JComboBox combo : combos)
				toolbar.remove(combo);
			combos.clear();
			for (int i = 0; i < selectorNames.length; i++) {
				JComboBox combo = new JComboBox();
				combo.addActionListener(new FrameActionListener());
				combos.add(combo);
				toolbar.add(combo, i);
				if (tooltips != null)
					combo.setToolTipText(tooltips[i]);
			}
		}

		public void setSelectorItems(String selectorName, String[] items,
				int defaultIdx) {
			JComboBox combo = getCombo(selectorName);
			combo.removeAllItems();
			for (String item : items)
				combo.addItem(item);
			selectorDefaults[combos.indexOf(combo)] = defaultIdx;
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
				result.setValue(i, combos.get(i).getSelectedIndex());
			}
			return result;
		}

		JComboBox getCombo(String selectorName) {
			for (int i = 0; i < selectorNames.length; i++)
				if (selectorNames[i].equals(selectorName))
					return combos.get(i);
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

		protected SelectionState(String[] selectors) {
			for (String sel : selectors) {
				this.selectors.add(sel);
				this.selIndices.add(null);
			}
		}

		/** Returns the number of selectors currently available. */
		public int size() {
			return selectors.size();
		}

		/**
		 * Sets the selection state of a specified selector to a specified item.
		 */
		void setValue(int selectorIdx, int valIdx) {
			selIndices.set(selectorIdx, valIdx);
		}

		/**
		 * Sets the selection state of a specified selector to a specified item.
		 */
		void setValue(String selector, int valIdx) {
			selIndices.set(selectors.indexOf(selector), valIdx);
		}

		/** Returns the index of the selected item of a specified selector. */
		public int getValue(int selectorIdx) {
			return selIndices.get(selectorIdx);
		}

		/** Returns the index of the selected item of a specified selector. */
		public int getValue(String selector) {
			return selIndices.get(selectors.indexOf(selector));
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
