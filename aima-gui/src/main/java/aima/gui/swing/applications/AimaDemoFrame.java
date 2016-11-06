package aima.gui.swing.applications;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import aima.gui.swing.framework.MessageLoggerPanel;

/**
 * Provides a simple frame for starting agent applications and command line demos.
 * 
 * @author Ruediger Lunde
 */
public class AimaDemoFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	protected JMenuBar menubar = new JMenuBar();
	JMenu appsMenu = new JMenu("Apps");
	JMenu demosMenu = new JMenu("Demos");
	MessageLoggerPanel textPanel = new MessageLoggerPanel();
	JComponent currPanel;
	PrintStream outStream;

	/** Standard constructor. */
	public AimaDemoFrame() {
		setTitle("Artificial Intelligence a Modern Approach 3rd ed. Java Demos (AIMA3e-Java)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(menubar);
		menubar.add(appsMenu);
		menubar.add(demosMenu);
		outStream = System.out;
	}

	/**
	 * Adds a new agent application to the menu. The class is expected to be
	 * part of a package and to provide a <code>constructApplicationFrame</code>
	 * method.
	 */
	public void addApp(Class<?> appClass) {
		JMenuItem item = addDemoToMenu(appsMenu, appClass);
		item.addActionListener(new AppStarter(appClass));
	}

	/**
	 * Adds a new command line demo to the menu. The class is expected to be
	 * part of a package and to provide a static main method.
	 */
	public void addDemo(Class<?> demoClass) {
		JMenuItem item = addDemoToMenu(demosMenu, demoClass);
		item.addActionListener(new DemoStarter(demoClass));
	}

	/**
	 * Adds a new program (agent application or command line demo) to the specified
	 * menu.
	 */
	private JMenuItem addDemoToMenu(JMenu menu, Class<?> progClass) {
		JMenuItem item = new JMenuItem(progClass.getSimpleName());
		JMenu subMenu = null;
		String packageName = progClass.getPackage().getName();
		Component[] menuComps = menu.getMenuComponents();
		int i;
		for (i = 0; i < menuComps.length; i++) {
			JMenu comp = (JMenu) menuComps[i];
			if (comp.getText().equals(packageName))
				subMenu = comp;
			else if (comp.getText().compareTo(packageName) > 0)
				break;
		}
		if (subMenu == null) {
			subMenu = new JMenu(packageName);
			menu.add(subMenu, i);
		}
		subMenu.add(item);
		return item;
	}


	// ///////////////////////////////////////////////////////////////
	// inner classes

	/**
	 * Implements an action listener which starts an agent application.
	 * 
	 * @author Ruediger Lunde
	 */
	protected class AppStarter implements ActionListener {
		Class<?> appClass;

		AppStarter(Class<?> ac) {
			appClass = ac;
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				if (currPanel != null)
					getContentPane().remove(currPanel);
				System.setOut(outStream);
				Object instance = appClass.newInstance();
				Method m = appClass.getMethod("constructApplicationFrame",
						new Class[0]);
				JFrame af = (JFrame) m.invoke(instance, (Object[]) null);
				currPanel = (JComponent) af.getContentPane().getComponent(0);
				af.getContentPane().remove(currPanel);
				getContentPane().add(currPanel, BorderLayout.CENTER);
				validate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Implements an action listener which starts a command line demo.
	 * 
	 * @author Ruediger Lunde
	 */
	protected class DemoStarter implements ActionListener {
		Class<?> demoClass;

		DemoStarter(Class<?> dc) {
			demoClass = dc;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (currPanel == textPanel)
					textPanel.clear();
				else {
					if (currPanel != null)
						getContentPane().remove(currPanel);
					getContentPane().add(textPanel, BorderLayout.CENTER);
					// redirect the standard output into the text area
					System.setOut(textPanel.getPrintStream());
					// System.setErr(panel.getPrintStream());
					validate();
					currPanel = textPanel;
				}
				Method m = demoClass.getMethod("main", String[].class);
				m.invoke(null, (Object) new String[] {});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
