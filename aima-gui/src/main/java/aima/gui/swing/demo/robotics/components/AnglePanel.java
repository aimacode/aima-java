package aima.gui.swing.demo.robotics.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import aima.core.robotics.impl.datatypes.Angle;
import aima.gui.swing.demo.robotics.util.GuiBase;
import aima.gui.swing.demo.robotics.util.ListTableModel;
/**
 * This class is a panel where the user can set angles for whatever use you may find entertaining and/or productive.
 * One can add to, edit and delete these angles.
 *  
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 */
public class AnglePanel extends Settings.SpecialSetting implements MouseListener  {
	
	private static final long serialVersionUID = 1L;
	private static final String ANGLES_KEY = "ANGLES";
	private static final String JL_ANGLE_TEXT ="Selected Angle:";
	private static final String JL_NUMBER_OF_ANGELS_TEXT = "Number of Angles: ";
	private static final String BTN_ADD_ANGLE_TEXT = "Add Angle";
	private static final String BTN_DELETE_ANGLE_TEXT = "Delete Angle";
	private static final String ANGLES_COLUMN_NAME = "Angles";
	private static final int RADIUS = 75;
	
	private final int circleCenterX;
	private final int circleCenterY;
	
	private JLabel jLAngle;
	private JLabel jLAngleCount;
	private JTextField jTFChangeAngle; 
	private JButton btnAddAngle;
	private JScrollPane scrollPane;
	private JButton btnDeleteAngle;
	private ListTableModel angleModel = new ListTableModel(ANGLES_COLUMN_NAME);
	private JTable jTAngles;
	private int selectedAngleIndex;
	private double[] previousAngles;
	private double[] angles = {-90.0d,-45.0d,0.0d,45.0d,90.0d};
	private ChangeListener listener;
	
	/**
	 * Default constructor with no parameters.
	 */
	public AnglePanel() {
		setSize(Settings.getGuiItemWidth(), Settings.getGuiItemHeight()*8);
		circleCenterX = getWidth() / 2;
		circleCenterY = getHeight() / 2;
		
		addMouseListener(this);
		jLAngle = new JLabel(JL_ANGLE_TEXT);
		jLAngle.setBounds(5, 5, 100, 30);
		jLAngle.setLayout(null);
		
		jTFChangeAngle = new JTextField();
		jTFChangeAngle.setBounds(5, 35, 100, 30);
		jTFChangeAngle.setLayout(null);
		jTFChangeAngle.addKeyListener(new KeyAdapter() {
			@Override
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_ENTER && angles != null) {
		    		try{
		    			//delete the degree sign if it is included in the text:
		    			if(jTFChangeAngle.getText().contains("°")) {
		    				jTFChangeAngle.setText(jTFChangeAngle.getText().replace("°", ""));
		    			}
		    			//if dots and commas are in the text field a NumberFormatException will be thrown
		    			if(jTFChangeAngle.getText().contains(",") && jTFChangeAngle.getText().contains(".")) {
		    				throw new NumberFormatException();
		    			}
		    			//if the number contains only a comma it will be replaced with a dot
		    			if(jTFChangeAngle.getText().contains(",")) {
		    				jTFChangeAngle.setText(jTFChangeAngle.getText().replace(",", "."));
		    			}
		    			double ang = Double.parseDouble(jTFChangeAngle.getText());
		    			angles[selectedAngleIndex] = ang % 360;
		    			if(angles[selectedAngleIndex] > 180.0d) angles[selectedAngleIndex] -= 360.0d;
		    			else if(angles[selectedAngleIndex] < -180.0d) angles[selectedAngleIndex] += 360.0d;
		    			updateGui(selectedAngleIndex);
		    		}
		    		catch(NumberFormatException e1) {
		    			JOptionPane.showMessageDialog(null, "Please enter a valid number!");
		    			jTFChangeAngle.setText(angles[selectedAngleIndex] + "°");
		    		}
		    	}
		    }
		});
		
		jLAngleCount = new JLabel(JL_NUMBER_OF_ANGELS_TEXT + "0");
		jLAngleCount.setBounds(8,60 + jTFChangeAngle.getHeight(), 100, 30);
		jLAngleCount.setLayout(null);
		
		btnAddAngle = new JButton(BTN_ADD_ANGLE_TEXT);
		btnAddAngle.setLayout(null);
		btnAddAngle.setBounds(5, 115, 100, 30);
		btnAddAngle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAngle();
			}
		});
		
		btnDeleteAngle = new JButton(BTN_DELETE_ANGLE_TEXT);
		btnDeleteAngle.setLayout(null);
		btnDeleteAngle.setBounds(5, 150, 100, 30);
		btnDeleteAngle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteAngle();
			}
		});
		
		jTAngles = new JTable(angleModel);
		jTAngles.setFillsViewportHeight(true);
		jTAngles.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
			@Override
		    public void valueChanged(ListSelectionEvent e) {
		    	selectedAngleIndex = jTAngles.getSelectedRow();  
		    	jTFChangeAngle.setText(angles[selectedAngleIndex] + "°");
		    	repaint();
		    }
		});
		
		scrollPane = new JScrollPane(jTAngles, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(340, 5, 100, this.getHeight() - 40);
		jTAngles.setBounds(0,0, scrollPane.getWidth(), scrollPane.getHeight());
		
		add(btnDeleteAngle);		
		add(jLAngle);
		add(jLAngleCount);
		add(jTFChangeAngle);
		add(btnAddAngle);
		add(scrollPane);
		
		for(double angle: angles) {
			angleModel.add(angle + "°");
		}
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
		jTFChangeAngle.setText(angles[selectedAngleIndex] + "°");
		this.jLAngleCount.setText(JL_NUMBER_OF_ANGELS_TEXT + this.angles.length);
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
			this.jLAngleCount.setText(JL_NUMBER_OF_ANGELS_TEXT +"0");
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
			jTFChangeAngle.setText(angles[selectedAngleIndex] + "°");
			this.jLAngleCount.setText("Count of Angles: " + angles.length);
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(GuiBase.getTextColor());
		
		//paints a coordinate system and draws a circle 
		//around the zero point
		g2d.drawOval(circleCenterX - RADIUS,circleCenterY - RADIUS, 2 * RADIUS, 2 * RADIUS);
		g2d.drawLine(circleCenterX - (int) (1.5 * RADIUS), circleCenterY, circleCenterX + (int) (1.5 * RADIUS), circleCenterY);
		g2d.drawLine(circleCenterX, circleCenterY - (int) (1.5 * RADIUS),circleCenterX, circleCenterY + (int) (1.5 * RADIUS));
	
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
			g2d.setColor(Color.RED);//green new Color(0, 150 , 0)
			drawAngle(g2d, selectedAngleIndex);
		}
	}

	/**
	 * Draws an angle onto the provided {@link Graphics2D}.
	 * @param g2d the graphics of this panel onto which will be drawn.
	 * @param i the index of the angle to be drawn.
	 */
	private void drawAngle(Graphics2D g2d, int i) {
		final int xCircle = (int) (circleCenterX + RADIUS * Math.cos(Math.toRadians(angles[i])));
		final int yCircle = (int) (circleCenterY + RADIUS * Math.sin(Math.toRadians(angles[i])));
		g2d.drawLine(circleCenterX, circleCenterY, xCircle, flipY(yCircle));
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
	
	/**
	 * Derives the index of the angle which was clicked on from the given mouse coordinates.
	 * If it is not found -1 will be returned.
	 * @param mouseX the X-Coordinate of the mouse pointer.
	 * @param mouseY the Y-Coordinate of the mouse pointer.
	 * @return the index of the clicked angle.
	 */
	private int findAngleIndex(int mouseX, int mouseY) {
		for(int i = 0; i < angles.length; i++) {
			int xCircle = (int) (circleCenterX + RADIUS * Math.cos(Math.toRadians(angles[i])));
			int yCircle = (int) (circleCenterY + RADIUS * Math.sin(Math.toRadians(angles[i])));
			yCircle = flipY(yCircle);
			if((mouseX <= xCircle + 10 && mouseX >= xCircle - 10) && (mouseY <= yCircle + 10 && mouseY >= yCircle - 10)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Updates all GUI components.
	 */
	private void updateGui() {
		angleModel.clear();
		selectedAngleIndex = 0;
		jTFChangeAngle.setText(GuiBase.getFormat().format(angles[selectedAngleIndex]) + "°");
		for(int i = 0; i < angles.length; i++) angleModel.add(angles[i] + "");
		repaint();
	}
	
	/**
	 * Updates the GUI components for the angle with the passed index.
	 * @param angleIndex the index of the angle.
	 */
	private void updateGui(int angleIndex) {
		jTFChangeAngle.setText(GuiBase.getFormat().format(angles[angleIndex]) + "°");
		angleModel.setValueAt(angleIndex, angles[angleIndex] + "°");
		repaint();
	}
	
	@Override
	public void loadSettings(Properties values) {
		String saveString = values.getProperty(ANGLES_KEY);
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
		if(saveString != null) values.put(ANGLES_KEY, saveString);
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//At a double mouse click, the index of the clicked angle is searched for. If an index is found, the angle is marked with selectedAngleIndex.
		if(arg0.getClickCount() == 2) {			
			if(angles != null) {
				final int index = findAngleIndex(arg0.getX() , arg0.getY());
				if(index != -1) {
					selectedAngleIndex = index;
					repaint();
					jTFChangeAngle.setText(angles[selectedAngleIndex] + "°");
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
}