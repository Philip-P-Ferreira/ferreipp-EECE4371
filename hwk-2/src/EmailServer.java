import java.io.IOException;
import java.util.*;

class EmailServer {

  public static void main(String[] args) throws IOException {

    System.out.println("\nStarting up server...");

    // handler to handle reading and writing messages
    // storage to store emails
    ServerHandler handler = new ServerHandler(EmailUtils.PORT);
    EmailStorage emailStorage = new EmailStorage();

    // main loop
    boolean serverIsOn = true;
    while (serverIsOn) {
      // waits for client to connect
      System.out.println("Waiting for client...");
      System.out.println("Client connected at " +
                         handler.waitForClientConnect());

      // map to hold our request
      HashMap<String, String> requestMap = new HashMap<>();

      // while there is a client
      while (handler.isClientConnected()) {
        // get user request, turn into request map
        requestMap = EmailUtils.getPairMap(handler.listenForRequest());
        String requestType = requestMap.get(EmailUtils.COMMAND_KEY);
        System.out.println("\nRequest: " + requestType);

        // handle based on reqeust type
        switch (requestType) {
        case EmailUtils.LOG_IN:
          handler.logUserIn(requestMap.get(EmailUtils.USERNAME_KEY));
          System.out.println("Logged in as user: " + handler.getCurrentUser());
          break;

        case EmailUtils.SEND_EMAIL:
          emailStorage.addEmail(requestMap.get(EmailUtils.EMAIL_KEY));
          handler.sendAck(EmailUtils.SEND_EMAIL_ACK);
          System.out.println("Email Sent");
          break;

        case EmailUtils.RETRIEVE_EMAILS:
          handler.returnFetchedEmails(emailStorage);
          System.out.println("Emails fetched");
          break;

        case EmailUtils.LOG_OUT: {
          handler.logUserOut();
          System.out.println("Logged out\n");
        }
        }
      }
    }
  }
}