package aima.gui.framework;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Implements a simple text panel for message display.
 * 
 * @author Ruediger Lunde
 */
public class MessageLoggerPanel extends JPanel implements MessageLogger {
	
	private static final long serialVersionUID = 1L;
	protected JTextArea textArea;
	
	public MessageLoggerPanel() {
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		textArea.setEditable(false);
		// Use a fixed width font to help make the output easier to read.
		textArea.setFont(new Font(Font.MONOSPACED, textArea.getFont().getStyle(), 14));
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);
		createPopupMenu();
	}
	
	/** Removes the text from the text area. */
	public void clear() {
		textArea.setText("");
	}
	
	/** Prints a log message on the text area. */
	public void log(String message) {
		MessageLogger ml = new MessageLogger();
		ml.message = message;
		if (SwingUtilities.isEventDispatchThread()) {
			ml.run();
		} else {
			try {
				//SwingUtilities.invokeAndWait(ml);
				SwingUtilities.invokeLater(ml);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Provides a print stream which can be used to redirect standard
	 * output streams.
	 */
	public PrintStream getPrintStream() {
		return new PrintStream(new TextAreaOutputStream());
	}
	
	/** Responsible for creating a popup menu for the panel. */
	protected void createPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Clear");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clear();
			}
		});
		popup.add(menuItem);
		textArea.addMouseListener(new PopupShower(popup));
	}
	
	
	/////////////////////////////////////////////////////////////////
	// inner classes
	
	/** Helper class which makes logging thread save. */
	private class MessageLogger implements Runnable {
		String message;

		public void run() {
			int start = textArea.getDocument().getLength();
			textArea.append(message + "\n");
			int end = textArea.getDocument().getLength();
			textArea.setSelectionStart(start);
			textArea.setSelectionEnd(end);
		}
	}
	
	/** Writes everything into the text area. */
	private class TextAreaOutputStream extends java.io.OutputStream {
		@Override
		public void write(int b) throws java.io.IOException {
			String s = new String(new char[] { (char) b });
			textArea.append(s);
		}
	}
}
