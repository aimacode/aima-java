package aima.gui.swing.applications.robotics.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;

import aima.gui.swing.applications.robotics.components.Settings.SpecialSetting;
import aima.gui.swing.framework.util.GuiBase;

/**
 * A button panel is a {@link SpecialSetting} containing a single button on which a {@link ActionListener} can be registered.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public class ButtonPanel extends SpecialSetting {
	
	private static final long serialVersionUID = 1L;
	private JButton button;

	/**
	 * @param title the title displayed in the button.
	 * @param actionListener the action listener which will be registered on the button.
	 */
	public ButtonPanel(String title, ActionListener actionListener) {
		setLayout(new BorderLayout());
		button = new JButton(title);
		button.setBorder(GuiBase.getClearanceBorder());
		button.addActionListener(actionListener);
		button.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(button);
		buttonPanel.add(GuiBase.getClearanceComp());
		buttonPanel.add(button);
		
		add(buttonPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Enables the button.
	 */
	public void enableButton() {
		button.setEnabled(true);
	}
	
	/**
	 * Disables the button.
	 */
	public void disableButton() {
		button.setEnabled(false);
	}
	
	@Override
	public void loadSettings(Properties values) { }

	@Override
	public void saveSettings(Properties values) { }

	@Override
	public void revertGui() { }

	@Override
	public void saveGui() { }

	@Override
	public void notifyChangeListener() { }

}
