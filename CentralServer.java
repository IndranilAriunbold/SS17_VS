package de.fh.darmstadt.vs;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


import de.fh.darmstadt.vs.ExitCodes;


/**
 * CentralServer liest lediglich die von SensorMain gesendeten
 * Sensordaten und gibt sie ueber stdout aus.
 *
 * Die Implementierung von Runnable ist auch hier kein Zufall!!!!
 */

public class CentralServer implements Runnable {
	public static void main(String[] args)
	{
		//
		// Anzahl der Kommandozeilenargumente okay?
		//

		if (args.length != 2) {
			System.out.println("usage: java de.fh.darmstadt.vs.CentralServer " +
                               "<host> <port>");
			System.exit(ExitCodes.INVALID_NUMBER_OF_ARGUMENTS);
		}


		CentralServer cs = new CentralServer(args[0], args[1]);
		cs.run();
		System.exit(ExitCodes.SUCCESS);			
	}



	/**
     * Konstruktor: Wertet Kommandozeilenargumente bzgl.
     * <I>host</I> und <I>port</I>aus.
     */

	public CentralServer(String h, String p)
	{
		try {
			port = Integer.parseInt(p);
		}
		catch (NumberFormatException nfe) {
			port = 8878;
		}
		host = h;
	}



	/**
     * Erzeugen eines (passiven) Sockets und warten auf Sensordaten.
     * Die Daten werden nach Erhalt ueber stdout ausgegeben.
     */

	public void run()
	{
		byte[] data = null;
		byte[] in   = null;




		DatagramPacket packet = null;
		DatagramSocket socket = null;

		try {
			socket = new DatagramSocket(port);
		}
		catch (SocketException se) {
			System.err.println("Cannot create a new socket");
			System.exit(ExitCodes.SOCKET_CREATION);
		}


		while (true) {
			data   = new byte[1024];
			packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			}
			catch (IOException ioe) {
				System.err.println("receive failed");
			}
			in = new byte[1024];
			in = packet.getData();
			String sd = new String(in);
			System.out.println(sd);
			data = null;
			in   = null;
			sd   = null;
		}
	}

	//
	// private data member
	//

	private String host = null;
	private int    port = -1;
}

