import static emailutils.EmailProtocol.*;

import emailutils.*;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import server.utils.*;

public class ServerThread implements Runnable {
  // static variables to hold server instance info
  private static EmailStorage emailStorage = new EmailStorage();
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
    System.out.print('\n' + serverStream.getIpAddress() + '@' + serverStream.getPort() + " -> ");

    try {
      // extract request type
      HashMap<String, String> argMap =
          createProtocolMap(serverStream.read(), PAIR_DELIM, PAIR_SEPARATOR);

      // handle based on type
      switch (argMap.get(COMMAND_KEY)) {
        case LOG_IN:
          logUserIn(argMap, serverStream);
          break;

        case SEND_EMAIL:
          sendEmail(argMap, serverStream);
          break;

        case RETRIEVE_EMAILS:
          fetchEmails(argMap, serverStream);
          break;

        case LOG_OUT:
          logUserOut(argMap, serverStream);
          break;

        default:
          System.out.println("Unrecognized command.");
      }
      serverStream.close();
    } catch (IOException e) {
      System.out.println("Could not reach client");
    }
  }

  // performs all necessary actions to log user in
  private static void logUserIn(final HashMap<String, String> argMap, TcpStream stream)
      throws IOException {
    // username, password, and map to store response
    String userName = argMap.get(USERNAME_KEY);
    String password = argMap.get(PASSWORD_KEY);
    HashMap<String, String> responseArgMap = new HashMap<>();

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

  // attempts to resolve a log out request
  private static void logUserOut(final HashMap<String, String> argMap, TcpStream stream)
      throws IOException {
    // get relavant strings, map to hold resposne
    final String token = argMap.get(TOKEN_KEY);
    final String user = tokenManager.getUser(token);
    HashMap<String, String> responseMap = new HashMap<>();

    // check if token is invalid (no associated user)
    if (user == null) {
      System.out.println("Invalid token");

      responseMap.put(STATUS_KEY, INVALID_TOKEN_VALUE);

    } else {
      System.out.println("Logging out user " + user);

      tokenManager.destroyToken(token);
      responseMap.put(STATUS_KEY, STATUS_OK_VALUE);
    }

    sendProtocolMessage(stream, LOG_OUT_ACK, responseMap);
  }

  // attempts to send email
  private static void sendEmail(final HashMap<String, String> argMap, TcpStream stream)
      throws IOException {
    // map to hold response args
    HashMap<String, String> responseMap = new HashMap<>();

    // check if token is valid, send email
    if (tokenManager.getUser(argMap.get(TOKEN_KEY)) == null) {
      System.out.println("Invalid token");

      responseMap.put(STATUS_KEY, INVALID_TOKEN_VALUE);
    } else {
      System.out.println("Sending email...");

      emailStorage.addEmail(argMap.get(EMAIL_KEY));
      responseMap.put(STATUS_KEY, STATUS_OK_VALUE);
    }

    sendProtocolMessage(stream, SEND_EMAIL_ACK, responseMap);
  }

  // attempts to fetch emails
  private static void fetchEmails(final HashMap<String, String> argMap, TcpStream stream)
      throws IOException {
    // relavent strings, response map
    final String user = tokenManager.getUser(argMap.get(TOKEN_KEY));
    HashMap<String, String> responseMap = new HashMap<>();

    // check if token is valid, send email
    if (user == null) {
      System.out.println("Invalid token");

      responseMap.put(STATUS_KEY, INVALID_TOKEN_VALUE);
    } else {
      System.out.println("Fetching emails for " + user);

      responseMap.put(EMAIL_LIST_KEY, emailStorage.fetchEmails(user));
      responseMap.put(STATUS_KEY, STATUS_OK_VALUE);
    }

    sendProtocolMessage(stream, RETRIEVE_RESPONSE, responseMap);
  }
}
