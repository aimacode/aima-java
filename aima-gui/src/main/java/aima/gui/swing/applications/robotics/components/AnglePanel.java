package aima.gui.swing.applications.robotics.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import aima.core.robotics.impl.datatypes.Angle;
import aima.gui.swing.framework.util.GuiBase;
import aima.gui.swing.framework.util.ListTableModel;
/**
 * This class is a panel where the user can set angles for whatever use you may find entertaining and/or productive.
 * One can add to, edit and delete these angles.
 *  
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 */
public class AnglePanel extends Settings.SpecialSetting  {
	
	/**
	 * The default key that is used to store the angles in the properties file when using the convenience constructor {@code AnglePanel(String title)}.
	 */
	public static final String DEFAULT_ANGLES_KEY = "ANGLES";
	
	private static final long serialVersionUID = 1L;
	private static final String JL_ANGLE_TEXT ="Selected Angle:";
	private static final String JL_NUMBER_OF_ANGLES_TEXT = "Angle Count: ";
	private static final String BTN_ADD_ANGLE_TEXT = "Add Angle";
	private static final String BTN_DELETE_ANGLE_TEXT = "Delete Angle";
	private static final String TEXT_SHIFT_ANGLE = "Shift Angle by:";
	private static final String ANGLES_COLUMN_NAME = "Angles";
	private static final int RADIUS = 75;
	
	private final String anglesKey;
	private JLabel jLAngleCount;
	private JTextField jTFChangeAngle;
	private JButton btnDeleteAngle;
	private JTextField jTShiftValueAngle;
	private CirclePanel circlePanel;
	private ListTableModel angleModel = new ListTableModel(ANGLES_COLUMN_NAME);
	private JTable jTAngles;
	private int selectedAngleIndex;
	private double[] previousAngles;
	private double[] angles = {-90.0d,-45.0d,0.0d,45.0d,90.0d};
	private ChangeListener listener;
	
	/**
	 * Convenience constructor using a default key for the properties file.
	 * @param title the title of the angle panel.
	 */
	public AnglePanel(String title) {
		this(DEFAULT_ANGLES_KEY, title);
	}
	
	/**
	 * Default constructor with no parameters.
	 * @param key the key identifying the angles in the properties.
	 * @param title the title of the angle panel.
	 */
	public AnglePanel(String key, String title) {
		anglesKey = key;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel jLTitle = new JLabel(title);
		jLTitle.setBorder(GuiBase.getClearanceBorder());
		
		//leftPanel:
		JLabel jLAngle = new JLabel(JL_ANGLE_TEXT);
		jTFChangeAngle = new JTextField();
		jTFChangeAngle.addKeyListener(new KeyAdapter() {
			@Override
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_ENTER && angles != null) {
		    		try{
		    			angles[selectedAngleIndex] = validateInput(jTFChangeAngle);
		    			updateGui(selectedAngleIndex);
		    		}
		    		catch(NumberFormatException f) {
		    			GuiBase.showMessageBox("Please enter a valid number!");
		    			jTFChangeAngle.setText(angles[selectedAngleIndex] + "\u00BA");
		    		}
		    	}
		    }
		});
		jLAngleCount = new JLabel(JL_NUMBER_OF_ANGLES_TEXT + "0");
		JButton btnAddAngle = new JButton(BTN_ADD_ANGLE_TEXT);
		btnAddAngle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAngle();
			}
		});
		JButton btnDeleteAngle = new JButton(BTN_DELETE_ANGLE_TEXT);
		btnDeleteAngle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteAngle();
			}
		});
		JLabel jLShiftAngle = new JLabel(TEXT_SHIFT_ANGLE);
		jTShiftValueAngle = new JTextField();
		JButton btnShiftAngleCreate = new JButton(BTN_ADD_ANGLE_TEXT);
		btnShiftAngleCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					final double ang = validateInput(jTShiftValueAngle);
					final double angleValue = angles[selectedAngleIndex];
					addAngle();
					angles[selectedAngleIndex] = angleValue + ang;
					updateGui(selectedAngleIndex);
				} catch(NumberFormatException e) {
					GuiBase.showMessageBox("Please enter a valid number!");
				}
			}
		});
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(GuiBase.getClearanceBorder());
		//leftPanel.setPreferredSize(new Dimension(100,1));
		leftPanel.setLayout(new GridLayout(0,1,GuiBase.getClearance(),GuiBase.getClearance()));
		leftPanel.add(jLAngle);
		leftPanel.add(jTFChangeAngle);
		leftPanel.add(jLAngleCount);
		leftPanel.add(btnAddAngle);
		leftPanel.add(btnDeleteAngle);
		leftPanel.add(jLShiftAngle);
		leftPanel.add(jTShiftValueAngle);
		leftPanel.add(btnShiftAngleCreate);
		
		//circlePanel:
		circlePanel = new CirclePanel(); 
		circlePanel.setBorder(GuiBase.getClearanceBorder());
		
		//rightPanel:
		jTAngles = new JTable(angleModel);
		jTAngles.setFillsViewportHeight(true);
		jTAngles.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
			@Override
		    public void valueChanged(ListSelectionEvent e) {
		    	selectedAngleIndex = jTAngles.getSelectedRow();  
		    	jTFChangeAngle.setText(angles[selectedAngleIndex] + "\u00BA");
		    	repaint();
		    }
		});
		
		JScrollPane scrollPane = new JScrollPane(jTAngles, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(GuiBase.getClearanceBorder());
		rightPanel.setPreferredSize(new Dimension(100,1));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(scrollPane);
		
		//Put all panels together:
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createLineBorder(GuiBase.getTextColor(), 1));
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		jLTitle.setLabelFor(mainPanel);
		
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(circlePanel, BorderLayout.CENTER);
		mainPanel.add(rightPanel, BorderLayout.EAST);
		
		add(jLTitle);
		add(mainPanel);
		
		for(double angle: angles) {
			angleModel.add(angle + "\u00BA");
		}
	}
	
	private double validateInput(JTextField textField) {
		//delete the degree sign if it is included in the text:
		if(textField.getText().contains("\u00BA")) {
			textField.setText(textField.getText().replace("\u00BA", ""));
		}
		//if dots and commas are in the text field a NumberFormatException will be thrown
		if(textField.getText().contains(",") && textField.getText().contains(".")) {
			throw new NumberFormatException();
		}
		//if the number contains only a comma it will be replaced with a dot
		if(textField.getText().contains(",")) {
			textField.setText(textField.getText().replace(",", "."));
		}
		double ang = Double.parseDouble(jTShiftValueAngle.getText());
		ang = ang % 360;
		if(ang > 180.0d) ang -= 360.0d;
		else if(ang < -180.0d) ang += 360.0d;
		return ang;
	}
	
	/**
	 * Returns all angles as an array of {@link Angle}. 
	 * @return an array of {@link Angle}.
	 */
	public Angle[] getAngles() {
		if(angles == null) {
			final Angle[] result = {new Angle(0.0d)};
			return result;
		}
		Angle[] result = new Angle[angles.length];
		for(int i=0; i < angles.length; i++) {
			result[i] = new Angle(Math.toRadians(angles[i]));
		}
		Arrays.sort(result);
		return result;
	}
	
	@Override
	public void notifyChangeListener() {
		listener.notify(getAngles());
	}
	
	/**
	 * Sets the change listener that will be notified of any changes, like how change listeners are supposed to work.
	 * @param listener the listener to be registered.
	 */
	public void setChangeListener(ChangeListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Adds an angle and updates all relevant GUI components.
	 */
	private void addAngle() {
		if(angles == null) {
			angles = new double[1];
			selectedAngleIndex = 0;
			btnDeleteAngle.setEnabled(true);
		} else {
			double[] tmpAngles = new double[angles.length + 1];
			for(int i = 0 ; i < angles.length; i++) {
				tmpAngles[i] = angles[i];
			}
			angles = tmpAngles;
		}
		angles[angles.length -1] = 0.0d;
		angleModel.add(angles[angles.length -1] + "");
		selectedAngleIndex = angles.length - 1;
		jTFChangeAngle.setText(angles[selectedAngleIndex] + "\u00BA");
		jLAngleCount.setText(JL_NUMBER_OF_ANGLES_TEXT + angles.length);
		repaint();
	}
	
	/**
	 * Deletes the selected angle and updates all relevant GUI components.
	 */
	private void deleteAngle() {
		if(angles == null) return;
		if(angles.length == 1) {
			angles = null;
			jTFChangeAngle.setText("");
			angleModel.removeValueAt(0);
			jLAngleCount.setText(JL_NUMBER_OF_ANGLES_TEXT +"0");
			btnDeleteAngle.setEnabled(false);
		} else {
			double[] tmpAngles = new double[angles.length -1];
			for(int i = 0; i < angles.length - 1; i++){
				if(i >= selectedAngleIndex) {
					tmpAngles[i] = angles[i + 1];
				} else {
					tmpAngles[i] = angles[i];
				}
			}
			angles = tmpAngles;
			angleModel.removeValueAt(selectedAngleIndex);
			selectedAngleIndex = 0;
			jTFChangeAngle.setText(angles[selectedAngleIndex] + "\u00BA");
			jLAngleCount.setText("Count of Angles: " + angles.length);
		}
		repaint();
	}
	
	/**
	 * Updates all GUI components.
	 */
	private void updateGui() {
		angleModel.clear();
		selectedAngleIndex = 0;
		jTFChangeAngle.setText(GuiBase.getFormat().format(angles[selectedAngleIndex]) + "\u00BA");
		for(int i = 0; i < angles.length; i++) angleModel.add(angles[i] + "\u00BA");
		jLAngleCount.setText(JL_NUMBER_OF_ANGLES_TEXT + angleModel.getRowCount());
		repaint();
	}
	
	/**
	 * Updates the GUI components for the angle with the passed index.
	 * @param angleIndex the index of the angle.
	 */
	private void updateGui(int angleIndex) {
		jTFChangeAngle.setText(GuiBase.getFormat().format(angles[angleIndex]) + "\u00BA");
		angleModel.setValueAt(angleIndex, angles[angleIndex] + "\u00BA");
		repaint();
	}
	
	@Override
	public void loadSettings(Properties values) {
		String saveString = values.getProperty(anglesKey);
		if(saveString != null) {
			try {
				ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(saveString.getBytes()));
				angles = (double[]) stream.readObject();
				previousAngles = angles;
				updateGui();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void saveSettings(Properties values) {
		String saveString = null;
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream stream = new ObjectOutputStream(byteStream);
			stream.writeObject(angles);
			saveString = byteStream.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(saveString != null) values.put(anglesKey, saveString);
	}

	@Override
	public void revertGui() {
		angles = previousAngles;
		repaint();
		updateGui();
	}

	@Override
	public void saveGui() {
		if(listener != null) {
			Angle[] radiantAngles = new Angle[angles.length];
			for(int i=0; i<angles.length;i++) {
				radiantAngles[i] = new Angle(Math.toRadians(angles[i]));
			}
			Arrays.sort(radiantAngles, null);
			listener.notify(radiantAngles);
		}
		previousAngles = angles;
	}
	
	/**
	 * Defines the method infrastructure of any and all possible and legal implementations of a ChangeListener which can be registered in an {@link AnglePanel}.
	 */
	public interface ChangeListener {
		/**
		 * Notifies the ChangeListener of a change in the angle array.
		 * @param angles the array of {@link Angle}.
		 */
		public void notify(Angle[] angles);
	}
	
	/**
	 * A simple drawing all angles onto a circle / polar coordinate system. 
	 */
	private class CirclePanel extends JPanel implements MouseListener {
		
		private static final long serialVersionUID = 1L;

		private int centerX;
		private int centerY;
		
		private CirclePanel() {
			addMouseListener(this);
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(GuiBase.getTextColor());
			
			centerX = (int) (getWidth() / 2);
			centerY = (int) (getHeight() / 2);
			
			//paints a coordinate system and draws a circle 
			//around the zero point
			g2d.drawOval(centerX - RADIUS, centerY - RADIUS, 2 * RADIUS, 2 * RADIUS);
			g2d.drawLine(centerX - (int) (1.5 * RADIUS), centerY, centerX + (int) (1.5 * RADIUS), centerY);
			g2d.drawLine(centerX, centerY - (int) (1.5 * RADIUS), centerX, centerY + (int) (1.5 * RADIUS));
		
			//paints every angle on the circular track
			if(angles != null) {
				int i=0;
				for(; i < selectedAngleIndex; i++) {
					drawAngle(g2d, i);
				}
				i++;
				for(; i < angles.length; i++) {
					drawAngle(g2d, i);
				}
				//Selected angle has to be drawn last:
				g2d.setColor(Color.RED);
				drawAngle(g2d, selectedAngleIndex);
			}
		}
		
		/**
		 * Draws an angle onto the provided {@link Graphics2D}.
		 * @param g2d the graphics of this panel onto which will be drawn.
		 * @param i the index of the angle to be drawn.
		 */
		private void drawAngle(Graphics2D g2d, int i) {
			final int xCircle = (int) (centerX + RADIUS * Math.cos(Math.toRadians(angles[i])));
			final int yCircle = (int) (centerY + RADIUS * Math.sin(Math.toRadians(angles[i])));
			g2d.drawLine(getWidth() / 2, getHeight() / 2, xCircle, flipY(yCircle));
			g2d.fillOval(xCircle - 10, flipY(yCircle) - 10, 20, 20);
		}
		
		/**
		 * Flips the y value from a Cartesian coordinate system into a java coordinate system.
		 * @param y the Y-coordinate form a Cartesian coordinate system.
		 * @return the Y-coordinate for the coordinate system in java.
		 */
		private int flipY(int y) {
			return getHeight() -  y;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			//At a double mouse click, the index of the clicked angle is searched for. If an index is found, the angle is marked with selectedAngleIndex.
			if(arg0.getClickCount() == 2) {			
				if(angles != null) {
					int i = 0;
					for(; i < angles.length; i++) {
						int xCircle = (int) (centerX + RADIUS * Math.cos(Math.toRadians(angles[i])));
						int yCircle = (int) (centerY + RADIUS * Math.sin(Math.toRadians(angles[i])));
						yCircle = flipY(yCircle);
						if((arg0.getX() <= xCircle + 10 && arg0.getX() >= xCircle - 10) && (arg0.getY() <= yCircle + 10 && arg0.getY() >= yCircle - 10)) {
							break;
						}
					}
					if(i < angles.length) {
						selectedAngleIndex = i;
						repaint();
						jTFChangeAngle.setText(angles[selectedAngleIndex] + "\u00BA");
					}
				}   
		    }
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) { }

		@Override
		public void mouseExited(MouseEvent arg0) { }

		@Override
		public void mousePressed(MouseEvent arg0) { }

		@Override
		public void mouseReleased(MouseEvent arg0) { }
	}
}