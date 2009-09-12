package aima.gui.framework;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

/**
 * <p>
 * Universal frame for building graphical agent applications. It provides
 * buttons for controlling the application and two panels and a status bar to
 * visualize agent, environment, and general simulation state.
 * </p>
 * <p>
 * To make the frame fit to your needs, you will at least have to add some
 * selectors and replace the dummy agent view pane by a view which is capable to
 * visualize your agent. The frame is configurable at run-time, so subclassing
 * will not always be necessary.
 * </p>
 * 
 * @author R. Lunde
 */
public class AgentAppFrame extends javax.swing.JFrame implements
		AgentAppModel.ModelChangedListener {

	/** The controller, which executes the domain-level commands. */
	protected Controller controller;
	/** The model, which provides data about agent and environment state. */
	private AgentAppModel model;
	/** Extra waiting time after each model change, used for animation. */
	private int updateDelay;
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
	protected JTextArea textArea;
	protected AbstractAgentView agentView;

	private JLabel statusLabel;

	/** Standard constructor. */
	public AgentAppFrame() {
		initComponents();
		pack();
		// redirect the standard output into the text area
		System.setOut(new PrintStream(new TextAreaOutputStream()));
		// System.setErr(new PrintStream(new TextAreaOutputStream()));
		updateDelay = 0;
		setButtonsEnabled(true);
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
		Controller cont = controller;
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
		Controller cont = controller;
		controller = null; // suppress reactions on parameter changes.
		selectors.setSelectorItems(selectorName, items, defaultIdx);
		controller = cont;
	}

	/** Adjusts selection state of all selectors. */
	public void setDefaultSelection() {
		Controller cont = controller;
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
	 * Template method, replacing the agent view. The agent view is the panel to
	 * the left of the splitbar. It typically implements a 2D-visualization of
	 * the agent in its environment.
	 */
	public void setAgentView(AbstractAgentView view) {
		agentView = view;
		centerPane.add(centerPane.LEFT, new JScrollPane(agentView));
	}

	/** Specifies how to distribute extra space when resizing the split pane. */
	public void setSplitPaneResizeWeight(double value) {
		centerPane.setResizeWeight(value);
	}

	/** Defines, who should react on button and selection events. */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/** Gives the frame access to agent and environment data. */
	public void setModel(AgentAppModel model) {
		this.model = model;
		agentView.updateView(model);
	}

	/** Sets a delay for displaying model changes. Used for animation. */
	public void setUpdateDelay(int msec) {
		updateDelay = msec;
	}

	/** Displays a text in the status bar. */
	public void setStatus(String status) {
		statusLabel.setText(status);
	}

	/** Updates the agent view after the state of the model has changed. */
	public void modelChanged() {
		agentView.updateView(model);
		try {
			Thread.sleep(updateDelay);
		} catch (Exception e) {
			logMessage("Error: Something went wrong when updating "
					+ "the view after a model change (" + e + ").");
			e.printStackTrace();
		}
	}

	/** Prints a log message on the text area. */
	public void logMessage(String message) {
		int start = textArea.getDocument().getLength();
		textArea.append(message + "\n");
		int end = textArea.getDocument().getLength();
		textArea.setSelectionStart(start);
		textArea.setSelectionEnd(end);
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
		// toolbar.add(selectors.selectorPanel);
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

		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollTPane = new JScrollPane(textArea);
		// scrollTPane.setPreferredSize(new java.awt.Dimension(900, 600));
		agentView = new AbstractAgentView() {
			@Override
			public void updateView(AgentAppModel model) {
			} // dummy
			// implementation
		};
		JScrollPane scrollGPane = new JScrollPane(agentView);
		centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		// centerPane.setDividerLocation(0.7);
		centerPane.add(JSplitPane.LEFT, scrollGPane);
		centerPane.add(JSplitPane.RIGHT, scrollTPane);
		centerPane.setDividerSize(5);
		centerPane.setResizeWeight(0.8);
		getContentPane().add(centerPane, BorderLayout.CENTER);

		JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel("");
		statusLabel.setBorder(new javax.swing.border.EtchedBorder()); // BevelBorder(javax.swing.border.BevelBorder.RAISED));
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
	private void setButtonsEnabled(boolean b) {
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
			controller.prepareAgent();
			isPrepared = true;
		}
	}

	// ////////////////////////////////////////////////////////
	// inner classes

	/** Sends commands to the controller. */
	private class FrameActionListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			String text = "";
			try {
				if (controller != null) {
					setStatus("");
					Object source = evt.getSource();
					if (source == clearButton /* || source == clearMenuItem */) {
						// additionally clear the text area
						text = "when clearing the views ";
						javax.swing.text.Document doc = textArea.getDocument();
						doc.remove(0, doc.getLength());
						statusLabel.setText("");
						controller.clearAgent();
						// isPrepared = false;
					} else if (source == prepareButton) {
						text = "when preparing the agent ";
						controller.prepareAgent();
						isPrepared = true;
					} else if (source == runButton) {
						text = "when preparing the agent ";
						if (isPrepared == false)
							controller.prepareAgent();
						text = "when running the agent ";
						setButtonsEnabled(false);
						agentThread = new AgentThread();
						agentThread.start();
						isPrepared = false;
					} else if (source == cancelButton) {
						text = "when cancelling the agent ";
						if (agentThread != null) {
							agentThread.stop(); // quick and dirty!
							agentThread = null;
							setStatus("Task cancelled.");
							setButtonsEnabled(true);
						}
						isPrepared = false;
					} else if (selectors.combos.contains(source)) {
						text = "when preparing the agent ";
						selectionChanged();
					}
				}
			} catch (Exception e) {
				logMessage("Error: Something went wrong " + text + "(" + e
						+ ").");
				e.printStackTrace();
			}
		}
	}

	/** Thread, which helps to perform GUI updates during simulation. */
	private class AgentThread extends Thread {
		@Override
		public void run() {
			agentThread = this;
			try {
				controller.runAgent();
			} catch (Exception e) {
				logMessage("Error: Somthing went wrong running the agent (" + e
						+ ").");
				e.printStackTrace(); // for debugging
			}
			setButtonsEnabled(true);
			agentThread = null;
		}
	}

	/** Writes everything into the text area. */
	private class TextAreaOutputStream extends java.io.OutputStream {
		@Override
		public void write(int b) throws java.io.IOException {
			String s = new String(new char[] { (char) b });
			textArea.append(s);
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

		/** Sets the selection state of a specified selector to a specified item. */
		void setValue(int selectorIdx, int valIdx) {
			selIndices.set(selectorIdx, valIdx);
		}

		/** Sets the selection state of a specified selector to a specified item. */
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

	/** Base class for all agent views. */
	public static abstract class AbstractAgentView extends JPanel {
		/** Called by the agent application frame after model changes. */
		public abstract void updateView(AgentAppModel model);
	}

	/**
	 * The agent application frame delegates the execution of all domain-level
	 * commands to a controller. Any class implementing this interface is in
	 * principle suitable.
	 */
	public static interface Controller {
		public abstract void clearAgent();

		public abstract void prepareAgent();

		public abstract void runAgent();
	}

	// ///////////////////////////////////////////////////////////////
	// for testing...

	public static void main(String[] args) {
		AgentAppFrame frame = new AgentAppFrame();
		frame.setVisible(true);
	}
}
