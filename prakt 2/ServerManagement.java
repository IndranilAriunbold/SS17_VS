import java.io.IOException;
import java.net.DatagramSocket;

public class ServerManagement {

    public static final int PORT = 4444;

    public static void main(String[] args) throws IOException {
        new ServerManagement().runServer();
    }

    public void runServer() throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(PORT);
        System.out.println("Server gestartet ...");
        new Server(datagramSocket).start();
    }
}
