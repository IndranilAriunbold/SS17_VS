
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Thread {

    DatagramSocket datagramSocket;
    String s, product = "", value1 = "", value2 = "", value3 = "", value4 = "";

    Server(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        try {

            WebServer w = new WebServer();
            w.start();

            /*
             ** Paket zum Empfang vorbereiten **
             */
            // Speicherplatz angeben
            byte[] data = new byte[65536];
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length);

            while (true) {
                //Warten bis ein Paket eintrifft. receive() legt die empfangenen Daten in datagramPacket ab
                datagramSocket.receive(datagramPacket);
                s = new String(data, 0, datagramPacket.getLength());

                echo(s);

                // Wenn nach 5 Sekunden kein Paket empfangen wurde -> Exception 
                datagramSocket.setSoTimeout(5000);

                //Inhalt des Pakets ausgeben
                product = s.substring(0, s.indexOf(":"));
                switch (product) {
                    case "Milch":
                        value1 += s.substring(s.indexOf(":") + 1, s.indexOf("%") + 1);
                        w.sendMessage(product + ": " + value1);
                        break;
                    case "Salami":
                        value2 += s.substring(s.indexOf(":") + 1, s.indexOf("%") + 1);
                        w.sendMessage(product + ": " + value2);
                        break;
                    case "Wasser":
                        value3 += s.substring(s.indexOf(":") + 1, s.indexOf("%") + 1);
                        w.sendMessage(product + ": " + value3);
                        break;
                    case "Eier":
                        value4 += s.substring(s.indexOf(":") + 1, s.indexOf("%") + 1);
                        w.sendMessage(product + ": " + value4);
                        break;
                }
            }

        } catch (IOException ex) {
            System.out.println("Timeout abgelaufen. Socket wird geschlossen.");
            datagramSocket.close();
        }
    }

    public static void echo(String msg) {
        System.out.println(msg);
    }
}
