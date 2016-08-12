package aima.gui.swing.framework;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Implements a simple text panel for message display.
 * 
 * @author Ruediger Lunde
 */
public class MessageLoggerPanel extends JPanel implements MessageLogger {

	private static final long serialVersionUID = 1L;
	protected JTextArea textArea;
	private boolean logLater;

	public MessageLoggerPanel() {
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		textArea.setEditable(false);
		// Use a fixed width font to help make the output easier to read.
		textArea.setFont(new Font(Font.MONOSPACED, textArea.getFont()
				.getStyle(), 14));
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);
		createPopupMenu();
	}

	/**
	 * Controls whether method <code>log</code> waits until the message is
	 * printed (value false) or does the work in background (value true).
	 * Waiting can slow down working threads significantly but the other option
	 * might cause deadlocks when log messages are printed before the frame was
	 * made visible under Java7.
	 */
	public void setLogLater(boolean b) {
		logLater = b;
	}

	/** Removes the text from the text area. */
	public void clear() {
		textArea.setText("");
	}

	/** Prints a log message on the text area. */
	public void log(final String message) {
		if (EventQueue.isDispatchThread()) {
			printMessage(message);
		} else {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					printMessage(message);
				}
			};
			try {
				if (logLater)
					EventQueue.invokeLater(r);
				else
					EventQueue.invokeAndWait(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void printMessage(String message) {
		int start = textArea.getDocument().getLength();
		textArea.append(message + "\n");
		int end = textArea.getDocument().getLength();
		textArea.setSelectionStart(start);
		textArea.setSelectionEnd(end);
	}

	/**
	 * Provides a print stream which can be used to redirect standard output
	 * streams.
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

	// ///////////////////////////////////////////////////////////////
	// nested classes

	/** Writes everything into the text area. */
	private class TextAreaOutputStream extends java.io.OutputStream {
		@Override
		public void write(int b) throws java.io.IOException {
			String s = new String(new char[] { (char) b });
			textArea.append(s);
		}
	}
}
