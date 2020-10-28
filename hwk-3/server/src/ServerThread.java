import static utils.EmailProtocol.*;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import utils.*;
import server.utils.*;

public class ServerThread implements Runnable {
  // static variables to hold server instance info
  // private static EmailStorage emailStorage = new EmailStorage();
  private static TokenManager tokenManager = new TokenManager();
  private static AccountManager accounts = new AccountManager();
  

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

    try {
        // extract request type
        HashMap<String, String> argMap =
            createProtocolMap(serverStream.read(), PAIR_DELIM, PAIR_SEPARATOR);

        // handle based on type
        System.out.print("\nThread " + threadID + " -> ");
        switch (argMap.get(COMMAND_KEY)) {
          case LOG_IN:
            logUserIn(argMap, serverStream);
            break;

          case SEND_EMAIL:
            
            break;

          case RETRIEVE_EMAILS:
            
            break;

          case LOG_OUT:
          
            break;

          default:
            System.out.println("Unrecognized command. Closing thread");
        }
      serverStream.close();
    } catch (IOException e) {
      System.out.println("\nThread " + threadID + " -> Could not reach client");
    }
  }

  // performs all necessary actions to log user in
  private static void logUserIn(final HashMap<String,String> argMap, TcpStream stream) throws IOException{
    // username, password, and map to store response
    String userName = argMap.get(USERNAME_KEY);
    String password = argMap.get(PASSWORD_KEY);
    HashMap<String, String> responseArgMap = new HashMap<String,String>();
    
    // checks if username and password are valid
    // OR cretes new user with given password
    if (accounts.userExists(userName)) {
      if (accounts.passwordMatches(userName, password)) {
        System.out.println("Logging in as user: " + userName);

        responseArgMap.put(STATUS_KEY, STATUS_OK_VALUE);
        responseArgMap.put(TOKEN_KEY, tokenManager.newToken(userName));

      } else {
        System.out.println("Invalid username and password");

        responseArgMap.put(STATUS_KEY, STATUS_FAIL_VALUE);
      }

    } else {
      System.out.println("Creating new user: " + userName);

      accounts.addUser(userName, password);
      responseArgMap.put(STATUS_KEY, STATUS_OK_VALUE);
      responseArgMap.put(TOKEN_KEY, tokenManager.newToken(userName));
    }
    sendProtocolMessage(stream, LOG_IN_ACK, responseArgMap);
  }
}
