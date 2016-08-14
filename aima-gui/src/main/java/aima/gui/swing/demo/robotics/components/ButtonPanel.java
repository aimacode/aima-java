package aima.gui.swing.demo.robotics.components;

import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;

import aima.gui.swing.demo.robotics.components.Settings.SpecialSetting;

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
		setSize(Settings.getGuiItemWidth(), Settings.getGuiItemHeight());
		button = new JButton(title);
		button.setLayout(null);
		button.setBounds(getWidth() / 2 - title.length() * 4, getHeight() / 2 - 15, title.length() * 8, 30);
		button.addActionListener(actionListener);
		add(button);
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
