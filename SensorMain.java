package de.fh.darmstadt.vs;


//
// alle notwendigen imports expliziet aufgefuehrt
//

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;


import de.fh.darmstadt.vs.ExitCodes;
import de.fh.darmstadt.vs.Sensor;


/**
 * SensorMain steuert die Sensoren!
 * <UL>
 * <LI>Kommandozeilenargumente validieren</LI>
 * <LI>Sensoren-Property-Datei einlesen und in das Properties-Objekt ablegen</LI>
 * <LI>SensorMain-Objekt instanziieren</LI>
 * <LI>run()-Methode ausfuehren. Der Name run ist KEIN Zufall! Es kann sein, dass
 *     SensorMain auch mal Multithreading unterstuetzt! Daher auch die Implementierung
 *     des Runnable-Interfaces (enthaelt nur public void run())</LI>
 * </UL>
 */

public class SensorMain implements Runnable {
	public static void main(String[] args)
	{
		if (args.length != 2) {
			System.out.println(
				"\nusage: java de.fh.darmstadt.vs.SensorMain <property file> <port>\n");
			System.exit(ExitCodes.INVALID_NUMBER_OF_ARGUMENTS);
		}


		SensorMain st = new SensorMain(args[0], args[1]);
		st.run();
		System.exit(ExitCodes.SUCCESS);
	}



	/**
     * Konstruktor: Ermittelt anhand der Kommandozeilenargumente die
     * zu ladende property-Datei und den port.
     */

	public SensorMain(String name, String p)
	{
		try {
			port = Integer.parseInt(p);
		}
		catch (NumberFormatException nfe) {
			port = 7778;
		}


		//
		// Propertydatei in sensorProps einlesen
		//

		sensorProps = new Properties();


		//
		// read sensor properties
		//

		InputStream in = null;
		try {
			in = new FileInputStream(name);
			sensorProps.load(in);
		}
		catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			ioe.printStackTrace();
			System.exit(ExitCodes.CANNOT_LOAD_FILE);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException ioe) {}
			}
		}
	}



	/**
     * Hauptaufgabe von SensorMain!
     * <UL>
     * <LI>Bestimmt die Anzahl der Sensorobjekte</LI>
     * <LI>Instanziiert die Objekte mit ID und Artikelname</LI>
     * <LI>Bestimmt aus den properties die "Schlafenszeit", also das
     *     Zeitintervall zwischen dem Senden von Sensorendaten an die
     *     Zentrale (CentralServer)</LI>
     * <LI>Ermittelt die notwendigen Angaben von der Zentrale (Host, port, ...)</LI>
     * <LI>Baut die Verbindung zur Zentrale auf</LI>
     * <LI>Schickt die Sensordaten in der Form "ID, Artikel, Fuellgrad" and die Zentrale</LI>
     * </UL>
     */

	public void run()
	{
		int n = -1;


		try {
			n = Integer.parseInt(sensorProps.getProperty(".number.of.articles"));
		}
		catch (NumberFormatException nfe) {
			n = 4;
		}
		Sensor[] sensors = new Sensor[n];
		for (int i = 0; i < n; ++i) {
			sensors[i] = new Sensor(i, 
									 sensorProps.getProperty("sensor.article." + i));
		}
			                                                 

		long sleepingTime = -1L;
		try {
			sleepingTime = Long.parseLong(sensorProps.getProperty("sleep.time"));
		}
		catch (NumberFormatException nfe) {
			sleepingTime = 50000L;
		}



		//
		// CentralServer ist der Serverprozess! Um ihn zu kontaktieren
		// werden der host und port ermittelt (beides in der property-Datei)
		//

		int receivePort = -1;
		try {
			receivePort = Integer.parseInt(sensorProps.getProperty("zentral.server.port"));
		}
		catch (NumberFormatException nfe) {
			receivePort = 8878;
		}


		String host = sensorProps.getProperty("zentral.server.address");

		InetAddress receiveAddr = null;
		try {
			receiveAddr = InetAddress.getByName(host);
		}
		catch (UnknownHostException uhe) {
			System.err.println("Kenne den Host " + host + " nicht");
			System.exit(ExitCodes.UNKNOWN_HOST);
		}



		//
		// Aufbau der Socketverbindung (UDP, kein TCP, daher DatagramSocket)
		//

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(port);
		}
		catch (SocketException se) {
			System.err.println("Kann keine Socket erzeugen");
			System.exit(ExitCodes.SOCKET_CREATION);
		}
		byte[] sendData = new byte[1024];
		while (true) {
			for (int i = 0; i < n; ++i) {
				sendData = sensors[i].toString().getBytes();


				//
				// im Gegensatz zu (TCP-) Sockets gibt es als Datenformat
				// nur DatagramPacket. Die Daten des Sensors kommen als String
				// und werden via getBytes in einen byte-Array geschrieben, der
				// dem DatagramPacket uebergeben wird
				// 
				// Der Sender muss die Adresse und den Port des Empfaengers
				// (receivers) angeben
				//

				DatagramPacket out = new DatagramPacket(sendData, 
														sendData.length, 
														receiveAddr,
														receivePort);
				try {
					socket.send(out);	
				}
				catch (IOException ioe) {
					System.err.println("Socketfehler! Konnte kein 'send' ausfuehren");
				}
			}



			//
			// etwas warten, dann die Fuellmenge reduzieren
			//

			try {
				Thread.sleep(sleepingTime);
			}
			catch (InterruptedException ire) {}
			for (int i = 0; i < n; ++i)
				sensors[i].reduceFillingDegree(i + 5);
		}
	}



	//
	// private data member
	//

	private Properties sensorProps = null;
	private int        port        = -1;
}

