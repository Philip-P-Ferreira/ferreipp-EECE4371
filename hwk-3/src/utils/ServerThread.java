package src.utils;

import static src.utils.EmailProtocol.*;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread implements Runnable {
  // static variable to hold all emails
  private static EmailStorage emailStorage = new EmailStorage();

  // stream held by each individual thread
  private TcpStream serverStream;

  // constructs a thread with a new stream
  public ServerThread(Socket socket) throws IOException {
    serverStream = new TcpStream(socket);
  }

  // "main method" of each thread. Processes client requests
  public void run() {
    long threadID = Thread.currentThread().getId();
    System.out.println(
        "\nClient connected at " + serverStream.getIpAddress() + " on thread " + threadID);

    String userName = "";
    boolean threadOn = true;
    try {
      while (threadOn) {
        // extract request type
        HashMap<String, String> argMap =
            createProtocolMap(serverStream.read(), PAIR_DELIM, PAIR_SEPARATOR);

        // handle based on type
        System.out.print("\nThread " + threadID + " -> ");
        switch (argMap.get(COMMAND_KEY)) {
          case LOG_IN:
            userName = argMap.get(USERNAME_KEY);
            System.out.println("Logged in as user: " + userName);
            sendOkAck(serverStream, LOG_IN_ACK);
            break;

          case SEND_EMAIL:
            System.out.println(userName + ": sending email");
            emailStorage.addEmail(argMap.get(EMAIL_KEY));
            sendOkAck(serverStream, SEND_EMAIL_ACK);
            break;

          case RETRIEVE_EMAILS:
            System.out.println(userName + ": fetching emails");
            sendProtocolMessage(serverStream, RETRIEVE_RESPONSE, EMAIL_LIST_KEY,
                emailStorage.fetchEmails(userName));
            break;

          case LOG_OUT:
            System.out.println("Logging user " + userName + " out");
            threadOn = false;
            sendOkAck(serverStream, LOG_OUT_ACK);
            break;

          default:
            System.out.println("Unrecognized command. Closing thread");
            threadOn = false;
        }
      }
      serverStream.close();
    } catch (IOException e) {
      System.out.println("\nThread " + threadID + " -> Could not reach client");
    }
  }

  // sends an ok acknowledgment to the passed in stream of type ackType
  private static void sendOkAck(TcpStream stream, String ackType) throws IOException {
    sendProtocolMessage(stream, ackType, STATUS_KEY, STATUS_OK_VALUE);
  }
}
