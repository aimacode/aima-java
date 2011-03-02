package aimax.osm.gps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class which provides general functionality for reading messages
 * from a NMEA input stream. 
 * @author Ruediger Lunde
 */
public abstract class NmeaReader {
	protected InputStream inputStream;
	private StringBuffer buffer = new StringBuffer();
	private boolean messageReceived;
	private List<NmeaMessageListener> listeners = new ArrayList<NmeaMessageListener>();
	
	public void addListener(NmeaMessageListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(NmeaMessageListener listener) {
		listeners.remove(listener);
	}
	
	public abstract void openStream() throws Exception;
	
	public boolean messageReceived() {
		return messageReceived;
	}
	
	/**
	 * Reads characters from the stream and informs all interested
	 * listeners about received NMEA messages. Should be called
	 * when new data is available on the input stream.
	 */
	public void readFromStream(boolean onlyNextMessage) {
		try {
			int  newData;
			char mybyte;
			while((newData = inputStream.read()) != -1) {
				mybyte = (char) newData;
				if(mybyte == '$' && buffer.length() > 5) {
					if (buffer.charAt(0) == '$') {
						//System.out.println("Available: " + inputStream.available());
						String message = buffer.toString();
						for (NmeaMessageListener listener : listeners)
							listener.messageReceived(message);
					}
					messageReceived = true;
					buffer.delete(0, buffer.length());
					buffer.append(mybyte);
					if (onlyNextMessage)
						break;
				} else {
					buffer.append(mybyte);
				}
			}
		} catch(IOException ex) {
			System.err.println(ex);
		}
	}
	
	public void closeStream() throws IOException {
		messageReceived = false;
		if (inputStream != null)
			inputStream.close();
		buffer.delete(0, buffer.length());
	}

	public static interface NmeaMessageListener {
		public void messageReceived(String message);
	}
	
	
	/////////////////////////////////////////////////////////////////
	// inner classes...
	
	/** Simple listener which writes every received message to file. */
	public static class MessageToFileListener implements NmeaMessageListener {
		private OutputStreamWriter streamWriter;
		int count = 0;
		
		public void openFile(File file) throws IOException {
			streamWriter = new OutputStreamWriter(new FileOutputStream(file));
		}
		
		public void closeFile() {
			try {
				streamWriter.close();
				streamWriter = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void messageReceived(String message) {
			try {
				if (streamWriter != null) {
					streamWriter.write(message);
					if (++count % 20 == 0)
						streamWriter.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
				closeFile();
			}
		}
	}
	
}
