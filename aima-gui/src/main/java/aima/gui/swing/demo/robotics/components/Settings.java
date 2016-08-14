package aima.gui.swing.demo.robotics.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This class can manage parameters of an application bundled in a single GUI.<br/>
 * A single parameter is described by a set of strings. The first string is the value, which will be saved to and restored from a file and displayed in a text field.
 * The next string is a key by which the value is identified. This key has to be unique against all other settings and all special settings.
 * The last string may be a user readable label of the value that will be displayed next to the text field.<br/>
 * If the parameter can not hold its value within a single string, it is possible to add classes extending {@link SpecialSetting} which extends {@link JPanel} to the GUI.<br/>
 * For any parameter that is in use by this class listeners may be added as {@link ISettingsListener}.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public class Settings {
	
	private Properties values = new Properties();
	private HashMap<String,String> names = new HashMap<String,String>();
	private HashMap<String,SettingsListenerList> listeners = new HashMap<String,SettingsListenerList>();
	private HashMap<String,SpecialSetting> specials = new HashMap<String,SpecialSetting>();
	private LinkedList<String> order = new LinkedList<String>();
	private Gui gui = new Gui();
	
	/**
	 * Builds the GUI for all registered settings.
	 * All registered settings will become visible in the GUI.
	 */
	public void buildGui() {
		gui.buildPanels();
	}
	
	/**
	 * Shows the GUI.
	 */
	public void show() {
		gui.showGui();
	}
	
	/**
	 * Restores all values that were saved to the file in a previous instance.
	 * @param file the file in which the values are saved.
	 */
	public void loadSettings(File file) {
		if(!file.canRead()) return;
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			values.load(input);
		} catch (IOException e) {
			
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Saves all values to the file. In addition all {@link SpecialSetting} are saved, too.
	 * @param file the file to be saved to.
	 */
	public void saveSettings(File file) {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!file.canWrite()) return;
		for(SpecialSetting special: specials.values()) {
			special.saveSettings(values);
		}
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			values.store(output,null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Registers a setting identified by a given key which will be displayed with the specified name in the GUI.
	 * The default value will be stored in the values if the settings do not already contain a value for that key.
	 * If you want to force a value into the settings call {@code setSetting}.
	 * 
	 * @param key the key under which the setting is identified.
	 * @param name the text that will be displayed in the GUI next to the text field containing the value.
	 * @param defaultValue the value that should be stored as a default if a value does not already exist.
	 */
	public void registerSetting(String key, String name, String defaultValue) {
		if(!values.containsKey(key)) {
			values.setProperty(key, defaultValue);
			SettingsListenerList list = listeners.get(key);
			if(list != null) list.notify(key, defaultValue);
		}
		if(!order.contains(key)) {
			names.put(key, name);
			order.add(key);
		}
	}
	
	/**
	 * Sets the value of a property identified by the provided key.
	 * This method does not register the property in the GUI. For that purpose call {@code registerSetting}.
	 * 
	 * @param key the key under which the setting is identified.
	 * @param value the value to be set.
	 */
	public void setSetting(String key, String value) {
		values.setProperty(key, value);
		SettingsListenerList list = listeners.get(key);
		if(list != null) list.notify(key, value);
	}
	
	/**
	 * Checks whether a value is stored for a given key.
	 * 
	 * @param key the key which will be searched for.
	 * @return true if a value is stored for the key.
	 */
	public boolean existsSetting(String key) {
		return values.containsKey(key);
	}
	
	/**
	 * Checks whether a setting identified by the given key is registered in the GUI.
	 * 
	 * @param key the key which should be searched for.
	 * @return true if the setting is displayed.
	 */
	public boolean isSettingDisplayed(String key) {
		return order.contains(key);
	}
	
	/**
	 * Returns the value that is stored for the key.
	 * The method returns {@code null} if no value is found for the provided key.
	 * 
	 * @param key the key to search for.
	 * @return the value that is stored for the given key.
	 */
	public String getSetting(String key) {
		return values.getProperty(key);
	}
	
	/**
	 * Returns the value that is stored for the key.
	 * The method returns {@code defaultValue} if no value is found for the provided key.
	 * 
	 * @param key the key to search for.
	 * @param defaultValue the value to be returned if no value is stored for the given key.
	 * @return the value that is stored for the given key.
	 */
	public String getSetting(String key, String defaultValue) {
		String result = values.getProperty(key);
		return result == null ? defaultValue : result;
	}
	
	/**
	 * Registers a special setting identified by a given key. This special setting will be displayed in the GUI.
	 * After it has been registered the settings that were stored before are loaded into the fragment.
	 * 
	 * @param key the key through which the special setting is identified.
	 * @param fragment the actual special setting.
	 */
	public void registerSpecialSetting(String key, SpecialSetting fragment) {
		specials.put(key, fragment);
		order.add(key);
		fragment.loadSettings(values);
	}
	
	/**
	 * Returns the special setting that is stored for the key.
	 * The method returns {@code null} if no special setting can be found for the provided key.
	 * 
	 * @param key the key to search for.
	 * @return the special setting that is stored for the given key.
	 */
	public SpecialSetting getSpecialSetting(String key) {
		return specials.get(key);
	}
	
	/**
	 * Registers a listener for a given key that will be notified if the value of the key changes.
	 * This does not apply to special settings.
	 * 
	 * @param key the key on which is to be listened.
	 * @param listener an {@link ISettingsListener} that will be notified if the value of the key changes.
	 */
	public void registerListener(String key, ISettingsListener listener) {
		SettingsListenerList list = listeners.get(key);
		if(list == null) {
			list = new SettingsListenerList();
			listeners.put(key,list);
		}
		list.add(listener);
	}
	
	/**
	 * Removes the listener from the specified key.
	 * 
	 * @param key the key on which the listener is registered.
	 * @param listener the listener that will be removed from the key.
	 */
	public void removeListener(String key, ISettingsListener listener) {
		SettingsListenerList list = listeners.get(key);
		if(list != null) {
			list.remove(listener);
		}
	}
	
	/**
	 * Notifies all registered listeners about the current value of the key they are listening on.
	 */
	public void notifyAllListeners() {
		for(Entry<String, SettingsListenerList> entry: listeners.entrySet()) {
			entry.getValue().notify(entry.getKey(),values.getProperty(entry.getKey()));
		}
		for(SpecialSetting special:specials.values()) {
			special.notifyChangeListener();
		}
	}
	
	/**
	 * Updates the value for a given key in the GUI from the stored values.
	 * @param key the key on which the GUI will be updated.
	 */
	public void updateGuiSetting(String key) {
		gui.revertSetting(key);
	}
	
	/**
	 * Returns the width which a setting has.
	 * @return the width which a setting has.
	 */
	public static int getGuiItemWidth() {
		return Gui.GUI_ITEM_WIDTH;
	}
	
	/**
	 * Returns the height which a setting should have.
	 * @return the height which a setting should have.
	 */
	public static int getGuiItemHeight() {
		return Gui.GUI_ITEM_HEIGHT;
	}
	
	/**
	 * A basic list that supports add and remove operations. All settings listeners in the list can be notified.
	 * 
	 * @author Arno von Borries
	 * @author Jan Phillip Kretzschmar
	 * @author Andreas Walscheid
	 *
	 */
	private static class SettingsListenerList {
		private LinkedList<ISettingsListener> listeners = new LinkedList<ISettingsListener>();
		
		private void notify(String key, String value) {
			for(ISettingsListener listener: listeners) {
				listener.notifySetting(key, value);
			}
		}
		
		private void add(ISettingsListener listener) {
			listeners.add(listener);
		}
		
		private void remove(ISettingsListener listener) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * A settings listener can be registered for a specific key.
	 * If the value for that key changes all registered listeners on that key will be notified with the new value and the key.
	 * 
	 * @author Arno von Borries
	 * @author Jan Phillip Kretzschmar
	 * @author Andreas Walscheid
	 *
	 */
	public static interface ISettingsListener {
		/**
		 * This method will be called to notify the settings listener.
		 * @param key the key to which the value belongs.
		 * @param value the value that (may have) changed.
		 */
		public void notifySetting(String key, String value);
	}
	
	/**
	 * A special setting is a {@code JPanel}. This panel can be added to the GUI if a simple text field is unsuitable to display and edit a setting.<br/>
	 * Thus this special setting has to take care of storing and managing its values.
	 * This means that it has to support settings listeners on its own and can not register these listeners in the {@link Settings} class.
	 * Values that should be saved on exit and restored on startup of the application can be saved in a provided {@link Properties} however.
	 * The keys used by the special setting to store values have to be unique against all simple settings and other special settings stored values.
	 * 
	 * @author Arno von Borries
	 * @author Jan Phillip Kretzschmar
	 * @author Andreas Walscheid
	 *
	 */
	@SuppressWarnings("serial")
	public static abstract class SpecialSetting extends JPanel {
		/**
		 * Loads values from the provided key value storage that have been saved before. 
		 * @param values the {@link Properties} which contains all values that have been stored.
		 */
		public abstract void loadSettings(Properties values);
		/**
		 * Save values to the provided key value storage.
		 * Be aware that the storage may already contain entries from other settings and from a previous save operation of this special setting.
		 * @param values the {@link Properties} which the values should be stored to.
		 */
		public abstract void saveSettings(Properties values);
		/**
		 * Restores the current value(s) into the GUI. This does not notify any listeners.
		 */
		public abstract void revertGui();
		/**
		 * Saves the value(s) currently set in the GUI to the current value(s). This notifies all registered listeners.
		 */
		public abstract void saveGui();
		/**
		 * Notifies all registered listeners on this special setting with the current value(s).
		 */
		public abstract void notifyChangeListener();
	}
	
	/**
	 * This class shows a GUI where you edit settings. 
	 * 
	 * @author Arno von Borries
	 * @author Jan Phillip Kretzschmar
	 * @author Andreas Walscheid
	 *
	 */
	protected class Gui extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private static final int GUI_WIDTH = 500; 
		private static final int GUI_HEIGHT = 600; 
		private static final int GUI_ITEM_WIDTH = 450;
		private static final int GUI_ITEM_HEIGHT = 35;
		private static final int BTN_PANEL_HEIGHT = 60;
		private static final int WINDOW_TITLE_LINE_HEIGHT = 28;
		private static final int WINDOW_TITLE_LINE_WIDTH = 6;
		private static final int COMPONENT_DISTANCE = 5;
		private static final int BTN_WIDTH = GUI_WIDTH / 4;
		private static final int BTN_HEIGHT = BTN_PANEL_HEIGHT - 4 * COMPONENT_DISTANCE;
		private static final int COMPONENT_DISTANCE_PANEL_X = 10;
		private static final int COMPONENT_DISTANCE_PANEL_Y = 15;
		
		private int nextYPosition = 15;
		private JPanel inScrollPane;
		private JScrollPane scrollPane;
		private HashMap<String,KeyPanel> keyPanels = new HashMap<String,KeyPanel>();
		
		/**
		 * The constructor generates the GUI.
		 * The GUI contains a save button, a revert button and a scroll-able field where the setting are stored.
		 */
		protected Gui() {
			setSize(new Dimension(GUI_WIDTH, GUI_HEIGHT));
			setResizable(false);
			setTitle("MCL Settings");
			setLocationRelativeTo(null);
			getContentPane().setLayout(null);
			
			JPanel btnPanel = new JPanel();
			btnPanel.setLayout(null);
			btnPanel.setBackground(new Color(119,136,153));
			btnPanel.setBounds(0, this.getHeight() - BTN_PANEL_HEIGHT-WINDOW_TITLE_LINE_HEIGHT, this.getWidth(), BTN_PANEL_HEIGHT);
			
			JButton btnSave = new JButton("Save");
			btnSave.setLayout(null);
			btnSave.setBounds(this.getWidth()/2 - BTN_WIDTH - COMPONENT_DISTANCE , (btnPanel.getHeight() - BTN_HEIGHT)/2, BTN_WIDTH, BTN_HEIGHT);
			btnSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					save();
				}
			});
			
			JButton btnAbort = new JButton("Revert");
			btnAbort.setLayout(null);
			btnAbort.setBounds(this.getWidth()/2 + COMPONENT_DISTANCE,(btnPanel.getHeight() - BTN_HEIGHT)/2, BTN_WIDTH, BTN_HEIGHT);
			btnAbort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					revert();
				}
			});
			
			inScrollPane = new JPanel();
			inScrollPane.setLayout(null);
			inScrollPane.setPreferredSize(new Dimension(this.getWidth()- WINDOW_TITLE_LINE_WIDTH - 19, 1200));
			
			scrollPane = new JScrollPane(inScrollPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setBounds(0,0,this.getWidth() - WINDOW_TITLE_LINE_WIDTH , this.getHeight()- btnPanel.getHeight() - WINDOW_TITLE_LINE_HEIGHT);
			
			getContentPane().add(btnPanel);
			getContentPane().add(scrollPane);
			btnPanel.add(btnSave);
			btnPanel.add(btnAbort);
		}

		/**
		 * Creates all key panels and special settings that have been registered.
		 */
		protected void buildPanels() {
			keyPanels.clear();
			inScrollPane.removeAll();
			for(String key: order) {
				String name = names.get(key);
				if(name != null) {
					String value = values.getProperty(key);
					keyPanels.put(key,createKeyPanelOnWindow(name, value));
				} else {
					SpecialSetting special = specials.get(key);
					special.setLayout(null);
					special.setLocation(COMPONENT_DISTANCE_PANEL_X, nextYPosition);
					inScrollPane.add(special);
					nextYPosition = nextYPosition + special.getHeight() + COMPONENT_DISTANCE_PANEL_Y;
				}
			}
			if(nextYPosition > scrollPane.getHeight()) inScrollPane.setPreferredSize(new Dimension(inScrollPane.getWidth(), nextYPosition));
		}
		
		/**
		 * Creates a {@link KeyPanel} for the given values.
		 * The key panel's coordinates are set automatically.
		 * @param name the name of the key.
		 * @param value the value of the key.
		 * @return a positioned key panel. 
		 */
		protected KeyPanel createKeyPanelOnWindow(String name, String value) {
			KeyPanel keyPanel = new KeyPanel(name, value);
			keyPanel.setLayout(null);
			keyPanel.setLocation(COMPONENT_DISTANCE_PANEL_X, nextYPosition);
			inScrollPane.add(keyPanel);
			nextYPosition = nextYPosition + keyPanel.getHeight() + COMPONENT_DISTANCE_PANEL_Y;
			return keyPanel;
		}
		
		/**
		 * Shows the GUI.
		 */
		protected void showGui() {
			this.setVisible(true);
		}
		
		/**
		 * Restores the stored value for a single field.
		 * @param key the key identifying the field and value.
		 */
		protected void revertSetting(String key) {
			KeyPanel kp = keyPanels.get(key);
			kp.setValue(getSetting(key));
		}
		
		/**
		 * Restores the previous values.
		 */
		protected void revert() {
			for(Entry<String, KeyPanel> entry: keyPanels.entrySet()) {
				String name = names.get(entry.getKey());
				if(name != null) {
					entry.getValue().setValue(getSetting(entry.getKey()));
				}
			}
			for(SpecialSetting special: specials.values()) {
				special.revertGui();
			}
		}
		
		/**
		 * Saves all values and special settings from the GUI.
		 */
		protected void save() {
			for(Entry<String, KeyPanel> entry: keyPanels.entrySet()) {
				String name = names.get(entry.getKey());
				if(name != null) {
					setSetting(entry.getKey(), entry.getValue().getValue());
				}
			}
			for(SpecialSetting special: specials.values()) {
				special.saveGui();
			}
		}
	}
}