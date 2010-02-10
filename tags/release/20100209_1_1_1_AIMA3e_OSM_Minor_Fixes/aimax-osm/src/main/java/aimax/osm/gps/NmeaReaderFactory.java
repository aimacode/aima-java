package aimax.osm.gps;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton, which creates NMEA reader instances and hides their
 * implementation. Reflection is used, to avoid compilation errors even
 * if the needed serial port reader class does not exist.
 * @author R. Lunde
 */
public class NmeaReaderFactory {
	
	private static final int TIMEOUT  = 1000; // 0.5 seconds
	private static NmeaReaderFactory instance;
	
	public static NmeaReaderFactory instance() {
		if (instance == null)
			instance = new NmeaReaderFactory();
		return instance;
	}
	
	/** Returns the factory. */
	public static void setInstance(NmeaReaderFactory factory) {
		instance = factory;
	}
	
	/** Creates the factory instance. */
	private NmeaReaderFactory() {
	}
	
	/**
	 * Creates a NMEA file reader. The file stream is not automatically
	 * opened!
	 */
	public NmeaReader createFileReader(File file) {
		return new NmeaFileReader(file);
	}
	
	/**
	 * Creates a new NMEA serial port reader.
	 * The implementation uses reflection to create an instance of class
	 * <code>aimax.osm.gps.NmeaSerialPortReader</code>. Null is returned,
	 * if the class does not exist, but no compilation error.
	 * @return A NmeaReader or null.
	 */
	public NmeaReader createSerialPortReader() {
		NmeaReader result = null;
		try {
			Class cls = Class.forName("aimax.osm.gps.NmeaSerialPortReader");
	        Class partypes[] = new Class[] {String.class, Integer.TYPE};
	        result = (NmeaReader) cls.newInstance();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}
}
