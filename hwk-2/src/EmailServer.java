import java.io.IOException;
import java.util.*;

class EmailServer {
  public static void main(String[] args) throws IOException {
    // start up the tcp server
    ServerHandler serverHandler = new ServerHandler(EmailUtils.PORT);
    System.out.println("\nStarting up server...");

    boolean session = true;
    while (session) {
      // map to hold our incoming request as a map
      HashMap<String, String> requestMap;

      if (!serverHandler.isLoggedIn()) {
        // connect to client
        System.out.println("Waiting for client...");
        System.out.println("Client connected at " +
                           serverHandler.waitForClientConnect());

        // extract the username
        requestMap = EmailUtils.getPairMap(serverHandler.listenForRequest());
        String user = requestMap.get(EmailUtils.USERNAME_KEY);

        // login as user
        System.out.println("\nLogging in as user: " + user);
        serverHandler.login(user);

      } else {
        // listen for a command
        requestMap = EmailUtils.getPairMap(serverHandler.listenForRequest());
        System.out.println("\nReqeust: " +
                           requestMap.get(EmailUtils.COMMAND_KEY));

        switch (requestMap.get(EmailUtils.COMMAND_KEY)) {
        case EmailUtils.SEND_EMAIL:
          serverHandler.addEmail(requestMap.get(EmailUtils.EMAIL_KEY));
          System.out.println("Email sent");
          break;
        case EmailUtils.RETRIEVE_EMAILS:
          serverHandler.fetchEmails();
          System.out.println("Emails fetched");
          break;
        case EmailUtils.LOG_OUT:
          serverHandler.logout();
          System.out.println("logged out\n");
          break;
        }
      }
    }
  }
}