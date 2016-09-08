package aima.gui.swing.framework.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * This class manages the look and feel for the whole application.<br/>
 * {@code activateSystemStyle()} and {@code setUIColors()} should be called as soon as possible
 * to prevent different looks in any part of the GUI which's construction may be missed otherwise.
 * These functions only have to be called once. 
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 */
public class GuiBase {
	
	private static final DecimalFormat FORMAT = new DecimalFormat("#0.00000"); 
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color BACKGROUND_COLOR = new Color(119,136,153);
	private static final Color AREA_COLOR = new Color(147,221,255);
	private static final int CLEARANCE = 5;
	
	private static JDialog MESSAGE_BOX = null;
	private static JLabel MESSAGE_LABEL;
	private static JButton MESSAGE_OK_BUTTON;
	
	/**
	 * This class does not need to be instantiated.
	 */
	private GuiBase() { }
	
	/**
	 * Tries to activate the system default look and feel.
	 */
	public static void activateSystemStyle() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
	}
	
	/**
	 * Sets the default background and foreground colors for all elements of the application to provide a uniform look and feel.
	 */
	public static void setUIColors() {
		 UIManager.put("OptionPane.background",BACKGROUND_COLOR);
		 UIManager.put("ScrollPane.background",BACKGROUND_COLOR);
		 UIManager.put("Panel.background",BACKGROUND_COLOR);
		 UIManager.put("Slider.background",BACKGROUND_COLOR);
		 UIManager.put("OptionPane.messageForeground",TEXT_COLOR);
		 UIManager.put("Label.foreground", TEXT_COLOR);
	}
	
	/**
	 * Returns the {@link DecimalFormat} that should be used to format all numbers that will be displayed to the user.
	 * @return a {@link DecimalFormat}.
	 */
	public static DecimalFormat getFormat() {
		return FORMAT;
	}
	
	/**
	 * Returns the {@link Color} that should be used as a foreground color on the provided background color.
	 * @return a {@link Color}.
	 */
	public static Color getTextColor() {
		return TEXT_COLOR;
	}
	
	/**
	 * Returns the {@link Color} that should be used as a background color.
	 * @return a {@link Color}.
	 */
	public static Color getBackgroundColor() {
		return BACKGROUND_COLOR;
	}
	
	/**
	 * Returns the {@link Color} that can be used as a secondary color in connection with the background color.
	 * @return a {@link Color}.
	 */
	public static Color getAreaColor() {
		return AREA_COLOR;
	}
	
	/**
	 * Returns the clearance that has to be used between any graphical elements to maintain a uniform look and feel.
	 * @return the clearance that has to be used.
	 */
	public static int getClearance() {
		return CLEARANCE;
	}
	
	/**
	 * Returns a invisible {@link Component} that can be used to maintain a uniform clearance between any graphical elements.
	 * @return a invisible clearance {@link Component}.
	 */
	public static Component getClearanceComp() {
		return Box.createRigidArea(new Dimension(CLEARANCE,CLEARANCE));
	}
	
	/**
	 * Returns a {@link Border} that can be used to maintain a uniform clearance between any graphical elements.
	 * @return a invisible clearance {@link Border}.
	 */
	public static Border getClearanceBorder() {
		return BorderFactory.createEmptyBorder(CLEARANCE,CLEARANCE,CLEARANCE,CLEARANCE);
	}
	
	/**
	 * Initializes the dialog for the message box.
	 */
	public static void initMessageBox() {
		MESSAGE_BOX = new JOptionPane().createDialog("MCL");
		final JPanel panel = new JPanel();
		panel.setBounds(5, 5, 345, 95);
		MESSAGE_LABEL = new JLabel("", JLabel.CENTER);
		Dimension dimension = new Dimension(350,100);
		panel.setLayout(new BorderLayout());
		panel.add(MESSAGE_LABEL, BorderLayout.CENTER);
		MESSAGE_OK_BUTTON = new JButton("OK");
		MESSAGE_OK_BUTTON.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MESSAGE_BOX.setVisible(false);
			}
		});
		panel.add(MESSAGE_OK_BUTTON, BorderLayout.SOUTH);
		MESSAGE_BOX.add(panel);
		MESSAGE_BOX.setContentPane(panel);
		MESSAGE_BOX.setMinimumSize(dimension);
		MESSAGE_BOX.setResizable(false);
		MESSAGE_BOX.setAlwaysOnTop(true);
		MESSAGE_BOX.pack();
		MESSAGE_BOX.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		MESSAGE_BOX.setModalityType(JDialog.ModalityType.MODELESS);
	}
	
	/**
	 * Shows a message box with a OK button without blocking.
	 * @param message the string to be displayed.
	 */
	public static void showMessageBox(final String message) {
		showMessageBox(message,true);
	}
	
	/**
	 * Show a message box without blocking.
	 * @param message the message to be shown.
	 * @param buttonVisible true if the OK button should be visible.
	 */
	public static void showMessageBox(final String message, boolean buttonVisible) {
		if(MESSAGE_BOX == null) initMessageBox();
		MESSAGE_BOX.setVisible(false);
		MESSAGE_LABEL.setText(message);
		MESSAGE_OK_BUTTON.setVisible(buttonVisible);
		MESSAGE_BOX.setVisible(true);
	}
	
	/**
	 * Enables the OK button of the message box.
	 */
	public static void showOKButtonMessageBox() {
		MESSAGE_OK_BUTTON.setVisible(true);
	}
	
	/**
	 * Hides the message box.
	 */
	public static void hideMessageBox() {
		MESSAGE_BOX.setVisible(false);
	}
}