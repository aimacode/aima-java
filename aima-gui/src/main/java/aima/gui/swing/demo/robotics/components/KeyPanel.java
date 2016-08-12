package aima.gui.swing.demo.robotics.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	private static final int COMPONENT_DISTANCE = 5;
	private static final float KEY_TITLE_LABEL_WIDTH_PERCENT = 0.5f;
	
	private JLabel jLKeyTitle;
	private JTextField jTFValue;
	
	/**
	 * @param keyTitle the name of the key.
	 * @param value the value of the key.
	 */
	public KeyPanel(String keyTitle, String value) {
		setSize(Settings.getGuiItemWidth(), Settings.getGuiItemHeight());
		jLKeyTitle = new JLabel(keyTitle);
		jLKeyTitle.setLayout(null);
		jLKeyTitle.setText(keyTitle);
		jLKeyTitle.setBounds(COMPONENT_DISTANCE ,COMPONENT_DISTANCE , (int) (getWidth() * KEY_TITLE_LABEL_WIDTH_PERCENT  - 2 * COMPONENT_DISTANCE) , getHeight() - 2 * COMPONENT_DISTANCE);
		jTFValue = new JTextField();
		jTFValue.setLayout(null);
		jTFValue.setText(value);
		jTFValue.setBounds((int) (getWidth() * KEY_TITLE_LABEL_WIDTH_PERCENT + COMPONENT_DISTANCE) , COMPONENT_DISTANCE, (int) (getWidth() * (1 - KEY_TITLE_LABEL_WIDTH_PERCENT) - 2 * COMPONENT_DISTANCE), getHeight() - 2 * COMPONENT_DISTANCE);
		add(jLKeyTitle);
		add(jTFValue);
		setVisible(true);
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
