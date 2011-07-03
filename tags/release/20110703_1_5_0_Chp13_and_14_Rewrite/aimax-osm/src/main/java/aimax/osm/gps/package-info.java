/**
 * This package contains components for accessing GPS systems
 * using a serial port connection and the NMEA protocol. It relies
 * on the RXTX serial port library. To use the functionality, it is
 * therefore necessary, to download the rs232 serial port library
 * from http://www.rxtx.org/ and to install it correctly. After that,
 * rename <code>NmeaSerialPortReader.txt</code> to
 * <code>NmeaSerialPortReader.java</code>.
 *
 * <p>Class <code>GpsLocator</code> is used to publish position
 * informations. It receives NMEA messages via a <code>NmeaReader</code>.
 * The message source can either be a serial port or a file. A
 * reader factory exists, which hides implementation details. The
 * serial port reader, which is provided as text file, initially scans
 * all serial ports and selects the first which provides NEMEA messages.
 * When connecting a second time, it uses the information of the last
 * successful scan. 
 */
package aimax.osm.gps;