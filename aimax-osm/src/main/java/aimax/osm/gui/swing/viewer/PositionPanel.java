package aimax.osm.gui.swing.viewer;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Simple panel for editing position information.
 * @author Ruediger Lunde
 */
public class PositionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	JComboBox<String> latCombo;
	JTextField latDegField;
	JTextField latMinField;
	JComboBox<String> lonCombo;
	JTextField lonDegField;
	JTextField lonMinField;
	
	public PositionPanel() {
		//setLayout(new GridLayout(2, 3));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		latCombo = new JComboBox<String>(new String[] {"N", "S"});
		add(latCombo);
		latDegField = new JTextField(3);
		add(latDegField);
		add(new JLabel('\u00B0' + " "));
		latMinField = new JTextField(4);
		add(latMinField);
		add(new JLabel("' "));
		
		lonCombo = new JComboBox<String>(new String[] {"E", "W"});
		add(lonCombo);
		lonDegField = new JTextField(3);
		add(lonDegField);
		add(new JLabel('\u00B0' + " "));
		lonMinField = new JTextField(4);
		add(lonMinField);
		add(new JLabel("' "));
	}

	public void setEnabled(boolean state) {
		super.setEnabled(state);
		latCombo.setEnabled(state);
		latDegField.setEnabled(state);
		latMinField.setEnabled(state);
		lonCombo.setEnabled(state);
		lonDegField.setEnabled(state);
		lonMinField.setEnabled(state);
	}
	
	public void setPosition(float lat, float lon) {
		DecimalFormat f = new DecimalFormat("#0.000");
		latCombo.setSelectedIndex(lat >= 0 ? 0 : 1);
		latDegField.setText(Integer.toString(Math.abs((int) lat)));
		latMinField.setText(f.format(Math.abs(lat-((int) lat))*60f));
		lonCombo.setSelectedIndex(lon >= 0 ? 0 : 1);
		lonDegField.setText(Integer.toString(Math.abs((int) lon)));
		lonMinField.setText(f.format(Math.abs(lon-((int) lon))*60f));
	}
	
	/** Returns a latitude value or Float.NaN. */
	public float getLat() { 
		return textToNumber(latCombo.getSelectedIndex(),
				latDegField.getText(), latMinField.getText());
	}
	
	/** Returns a longitude value or Float.NaN. */
	public float getLon() { 
		return textToNumber(lonCombo.getSelectedIndex(),
				lonDegField.getText(), lonMinField.getText());
	}
	
	private float textToNumber(int selIdx, String deg, String min) {
		float result;
		try {
			result = Float.parseFloat(deg);
			if (!min.isEmpty())
				result += Float.parseFloat(min) / 60f;
			if (selIdx == 1)
				result = -result;
		} catch (NumberFormatException e) {
			result = Float.NaN;
		}
		return result;
	}
	
	/** For testing... */
	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		PositionPanel panel = new PositionPanel();
		//panel.setEnabled(false);
		panel.setPosition(50.5f, -30.9f);
		int res = JOptionPane.showConfirmDialog(null, panel, "Specify a Position", JOptionPane.OK_CANCEL_OPTION);
		if (res == JOptionPane.OK_OPTION) {
			System.out.println(panel.getLat());
			System.out.println(panel.getLon());
		}
	}
}
