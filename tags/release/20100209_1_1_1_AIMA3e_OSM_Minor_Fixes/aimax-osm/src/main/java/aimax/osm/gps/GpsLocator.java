package aimax.osm.gps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Maintains a connection to a GPS via a NMEA stream, decodes the
 * position messages and publishes the obtained position informations.
 * <p>To run this class, download the rs232 serial port library from
 * http://www.rxtx.org, install it correctly, and rename file
 * <code>NmeaSerialPortReader.txt</code> in package <code>aimax.osm.gps</code>
 * to <code>NmeaSerialPortReader.class</code>.
 * One possible choice for installation is, to add the jar-file
 * to the class path (project properties, add jar), to store the DDL
 * locally (e.g. in project-root/lib), and start the application with
 * VM argument <code>-Djava.library.path=lib</code>.</p>
 * @author R. Lunde
 */
public class GpsLocator implements NmeaReader.NmeaMessageListener {

	private NmeaReader nmeaReader;
	private NmeaReader nmeaSerialPortReader;
	private GpsFix currPosition;
	private List<GpsPositionListener> listeners;
	
	/** Creates a new locator. */
	public GpsLocator() {
		currPosition = new GpsFix(false, 0.0f, 0.0f);
		listeners = new ArrayList<GpsPositionListener>();
	}
	
	/**
	 * Sets the data source for the locator to a file reader.
	 */
	public void openSerialPortConnection() {
		closeConnection();
		if (nmeaSerialPortReader == null)
			nmeaSerialPortReader = NmeaReaderFactory.instance().createSerialPortReader();
		if (nmeaSerialPortReader != null) {
			try {
				nmeaSerialPortReader.openStream();
				nmeaReader = nmeaSerialPortReader;
				nmeaReader.addListener(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets the data source for the locator to a file reader.
	 */
	public void openFileConnection(File file) {
		closeConnection();
		try {
			nmeaReader = NmeaReaderFactory.instance().createFileReader(file);
			if (nmeaReader != null) {
				nmeaReader.addListener(this);
				nmeaReader.openStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return nmeaReader != null;
	}
	
	public void closeConnection() {
		if (isConnected()) {
			try {
				nmeaReader.removeListener(this);
				nmeaReader.closeStream();
				nmeaReader = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Can be used, to check, whether the connection was opened
	 * successfully (result != null).
	 */
	public NmeaReader getNmeaReader() {
		return nmeaReader;
	}
	
	public void addGpsPositionListener(GpsPositionListener listener) {
		listeners.add(listener);
	}
	
	public void removeGpsPositionListener(GpsPositionListener listener) {
		listeners.remove(listener);
	}
	
	/** Returns the last valid position which was provided by the NMEA stream. */
	public GpsFix getCurrPosition() {
		return currPosition;
	}
	
	/**
	 * Parses the position data currently in buffer, updates the
	 * the current position and informs all interested listeners.
	 */
	public void messageReceived(StringBuffer buffer) {
		if (buffer.length() > 60 && buffer.substring(0, 6).equals("$GPGGA")) {
		// System.out.println(buffer.toString());
		// $GPGGA,103131.000,4824.2758,N,00959.9357,E,1,06,1.3,458.5,M,43.9,M,,0000*54
		//        7     13   18   23     30    36     43
		// $GPGGA,044944,4824.3044,N,00959.9409,E,1,05,3.1,451.0,M,46.8,M,,*4E
			boolean posOK = true;
			float lat;
			float lon;
			int x = (buffer.charAt(13) == '.') ? 0 : -4;
			
			if (buffer.charAt(43) == '0')
				posOK = false;
			
			float deg = Float.parseFloat(buffer.substring(18+x, 20+x));
			float min = Float.parseFloat(buffer.substring(20+x, 27+x));
			lat = deg + min / 60.0f;
			if (buffer.charAt(28+x) == 'S')
				lat = -lat;
			
			deg = Float.parseFloat(buffer.substring(30+x, 33+x));
			min = Float.parseFloat(buffer.substring(33+x, 40+x));
			lon = deg + min / 60.0f;
			if (buffer.charAt(28+x) == 'W')
				lon = -lon;
			currPosition = new GpsFix(posOK, lat, lon);
			//System.out.println(currPosition);
			for (GpsPositionListener listener : listeners)
				listener.positionUpdated(currPosition);
		}
	}
	
	
	/////////////////////////////////////////////////////////////////
	// Simple test program...
	
	public static void main(String[] args) {
		try {
			GpsLocator locator = new GpsLocator();
			locator.addGpsPositionListener(new GpsPositionListener() {
				@Override
				public void positionUpdated(GpsFix pos) {
					System.out.println(pos);
				}
			});
			//locator.openFileConnection(new File("nmeaout.txt"));
			locator.openSerialPortConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

