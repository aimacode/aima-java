package aima.gui.fx.framework;

import java.io.IOException;
import java.io.PrintStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Controller class which provides functionality to use a text area as view for
 * log messages and text console applications.
 * 
 * @author Ruediger Lunde
 */
public class MessagePaneCtrl {

	protected TextArea textArea;

	public MessagePaneCtrl(TextArea textArea) {
		this.textArea = textArea;
	}

	/** Removes the text from the text area. */
	public void clear() {
		textArea.setText("");
	}

	/** Prints a log message on the text area. */
	public void log(final String message) {
		if (Platform.isFxApplicationThread()) {
			append(message);
		} else {
			try {
				Platform.runLater(() -> append(message));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void append(String text) {
		textArea.appendText(text);
		if (textArea.getLength() > 0)
			textArea.selectRange(textArea.getLength() - 1, textArea.getLength());
	}

	/**
	 * Provides a print stream which can be used to redirect standard output
	 * streams.
	 */
	public PrintStream getPrintStream() {
		return new PrintStream(new TextAreaOutputStream());
	}

	// ///////////////////////////////////////////////////////////////
	// nested classes

	/** Writes everything into the text area. */
	private class TextAreaOutputStream extends java.io.OutputStream {
		StringBuffer buffer = new StringBuffer();
		String eol = System.getProperty("line.separator");

		@Override
		public void write(int b) throws java.io.IOException {
			buffer.append(new char[] { (char) b });
			if (buffer.toString().contains(eol)) {
				writeBuffer();
			}
		}

		private void writeBuffer() {
			if (Platform.isFxApplicationThread())
				append(buffer.toString());
			else {
				final String str = buffer.toString();
				Platform.runLater(() -> append(str));
			}
			buffer = new StringBuffer();
		}

		@Override
		public void flush() throws IOException {
			writeBuffer();
			super.flush();
		}

	}
}
