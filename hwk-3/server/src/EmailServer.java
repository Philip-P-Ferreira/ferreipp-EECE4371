import static emailutils.EmailProtocol.*;

import java.io.IOException;
import java.net.*;

public class EmailServer {
  public static void main(String[] args) throws IOException {
    // create a new server socket at PORT
    System.out.println("\nStarting up server...");
    ServerSocket serverSocket = new ServerSocket(PORT);

    // server always on
    boolean serverOn = true;
    while (serverOn) {
      try {
        // thread created when a client connects to server
        Thread serverThread = new Thread(new ServerThread(serverSocket.accept()));
        serverThread.start();
      } catch (IOException e) {
        System.out.println("Failed to connect to client");
      }
    }
    serverSocket.close();
  }
}
