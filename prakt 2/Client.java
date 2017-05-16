import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {

    private String name;
    String ipAddress;
    private int wert;

    Client(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public void run() {
        wert = 100;

        try {
            /*
            ** Paket zum Senden vorbereiten **
             */

            // Empfänger des Pakets
            DatagramSocket socket = new DatagramSocket();
            InetAddress ia = InetAddress.getByName(ipAddress);
            byte[] data = Integer.toString(getWert()).getBytes();

            // IPAdresse und Port des entfernten Rechners angeben
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, ia, 4444);

            while (true) {
                TimeUnit.SECONDS.sleep(2);
                String daten = name + ":" + Integer.toString(getWert())+" %";
                datagramPacket.setData(daten.getBytes());
                socket.send(datagramPacket);

                // Füllstand um Zufallswert verringern
                wert -= new Random().nextInt(30);

                if (wert < 0) {
                    wert = 0;
                    String s = name + ":" + Integer.toString(getWert())+" %";
                    datagramPacket.setData(s.getBytes());
                    socket.send(datagramPacket);
                    socket.close();
                    break;
                }

            }

        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException | InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the name
     */
    public String getProductName() {
        return name;
    }

    /**
     * @return the wert
     */
    public int getWert() {
        return wert;
    }
}