package aimax.osm.gps;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Reads NMEA data from file.
 * @author Ruediger Lunde
 *
 */
public class NmeaFileReader extends NmeaReader {
	private File file;
	private long delay = 0; //1000l;
	
	public NmeaFileReader(File file) {
		this.file = file;
	}
	
	@Override
	public void openStream() throws Exception {
		inputStream = new BufferedInputStream(new FileInputStream(file));
		Thread thread = new FileReaderThread();
		thread.run();
	}
	
	private class FileReaderThread extends Thread {
		@Override
		public void run() {
			try {
				while (inputStream.available() > 0) {
					readFromStream(true);
					if (delay > 0)
						sleep(delay);
				}
				closeStream();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
