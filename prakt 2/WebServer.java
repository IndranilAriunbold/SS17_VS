
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WebServer {

    String s;
    ServerSocket socket;
    BufferedReader in;
    PrintWriter out;
    String requestString = "";

    WebServer() {
        try {
            socket = new ServerSocket(8080);
            Socket remote = socket.accept();
            // remote is now the connected socket
            System.out.println("Connection, sending data.");
            in = new BufferedReader(new InputStreamReader(
                    remote.getInputStream()));
            out = new PrintWriter(remote.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {

        // HTTP Request ausgeben
        printRequest();

        // HTTP Response senden
        if (requestString.contains("GET /start")) {
            sendResponse();
        }

    }

    public void sendMessage(String s) {
        // Send the HTML page

        out.println("<p>" + s + "</p>");
        out.flush();
    }

    public void sendResponse() {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("\r\n");
        out.println("");
    }

    public void printRequest() {

        try {
            System.out.println("Anfrage vom Browser:");
            while (in.ready() || requestString.length() == 0) {
                requestString += (char) in.read();
            }
            System.out.println();
            System.out.println(requestString);
        } catch (IOException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
