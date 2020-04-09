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

	public NmeaFileReader(File file) {
		this.file = file;
	}
	
	@Override
	public void openStream() throws Exception {
		inputStream = new BufferedInputStream(new FileInputStream(file));
		Thread thread = new FileReaderThread();
		thread.start();
	}
	
	private class FileReaderThread extends Thread {
		@Override
		public void run() {
			try {
				while (inputStream.available() > 0) {
					readFromStream(true);
					//1000l;
					long delay = 1;
					sleep(delay);
				}
				closeStream();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
