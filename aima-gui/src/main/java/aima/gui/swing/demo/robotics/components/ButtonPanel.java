package aima.gui.swing.demo.robotics.components;

import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;

import aima.gui.swing.demo.robotics.components.Settings.SpecialSetting;

public class ButtonPanel extends SpecialSetting {
	
	public ButtonPanel(String title, ActionListener actionListener) {
		setSize(Settings.getGuiItemWidth(), Settings.getGuiItemHeight());
		JButton button = new JButton(title);
		button.setLayout(null);
		button.setBounds(getWidth() / 2 - title.length() * 4, getHeight() / 2 - 15, title.length() * 8, 30);
		button.addActionListener(actionListener);
		add(button);
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
