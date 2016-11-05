package aima.gui.swing.applications.robotics.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import aima.gui.swing.framework.util.GuiBase;
/**
 * A GUI panel that allows the user to set a value for a given key.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public class KeyPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JTextField jTFValue;
	
	/**
	 * @param keyTitle the name of the key.
	 * @param value the value of the key.
	 */
	public KeyPanel(String keyTitle, String value) {
		setBorder(GuiBase.getClearanceBorder());
		setLayout(new GridLayout(1,2,GuiBase.getClearance(),GuiBase.getClearance()));
		
		JLabel jLKeyTitle = new JLabel(keyTitle);
		jLKeyTitle.setText(keyTitle);
		jTFValue = new JTextField();
		jTFValue.setText(value);
		jLKeyTitle.setLabelFor(jTFValue);
		
		add(jLKeyTitle);
		add(jTFValue);
	}
	
	/**
	 * Sets the value that is displayed in the text field.
	 * @param value the new value.
	 */
	public void setValue(String value) {
		jTFValue.setText(value);
	}
	
	/**
	 * Returns the current value of the text field.
	 * @return the current value of the text field.
	 */
	public String getValue() {
		return jTFValue.getText();
	}
}
