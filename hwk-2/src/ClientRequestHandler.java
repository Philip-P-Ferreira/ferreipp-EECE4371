import java.io.*;
import java.net.*;

/**
 * ClientRequestHandler -
 * A class for abstracting away network calls and responses.
 * An instance of this object represents a single user logged on
 * into the server
 */
public class ClientRequestHandler {
  private String user;
  private Socket socket;
  private DataOutputStream outToServer;
  private BufferedReader readFromServer;

  /**
   * Constructor -
   * Accepts a user name and creates an instance of that user
   * logged into the email server
   *
   * @param userName - string of user name
   * @throws IOException
   */
  public ClientRequestHandler(String userName) throws IOException {
    user = userName;
    socket = new Socket(EmailUtils.SERVER_ADDRESS, EmailUtils.PORT);
    outToServer = new DataOutputStream(socket.getOutputStream());
    readFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    // method to log into server
    login();
  }

  /**
   * login -
   * sends login request to server, listens for resopnse
   *
   * @throws IOException
   */
  public void login() throws IOException {
    sendMessage(EmailUtils.LOG_IN, EmailUtils.USERNAME_KEY, user);
    readFromServer.readLine();
  }

  /**
   * fetchMail -
   * requests the server for emails, fecthes the response, parses it, then
   * returns it as an array of emails
   *
   * @return - Array of type Email representing all Emails for a given user
   * @throws IOException
   */
  public Email[] fetchMail() throws IOException {
    sendMessage(EmailUtils.RETRIEVE_EMAILS);
    return parseEmailResponse(readFromServer.readLine());
  }

  /**
   * sendMail -
   * Send the passed in Email to the server
   *
   * @param mail - Type Email that has the addressed user and the body text
   * @throws IOException
   */
  public void sendMail(Email mail) throws IOException {
    sendMessage(EmailUtils.SEND_EMAIL, EmailUtils.EMAIL_KEY, mail.toString());
    readFromServer.readLine(); // listen for response
  }

  /**
   * close -
   * sends the logout request, listends for a resposne, and closes the tcp
   * connection
   * @throws IOException
   */
  public void close() throws IOException {
    sendMessage(EmailUtils.LOG_OUT);
    readFromServer.readLine(); // listen to response

    socket.close(); // be a good client and close it out
  }

  public String getCurrentUser() {
    return user;
  }

  /**
   * sendMessage -
   * accepts a command type, an argname, and the actual argument, and send a
   * formatted tcp request to the server
   *
   * @param type - type of command
   * @param argName - name of argument
   * @param arg - actual argument
   * @throws IOException
   */
  private void sendMessage(String type, String argName, String arg) throws IOException {
    outToServer.writeBytes(EmailUtils.constructTcpMessage(type, argName, arg));
  }

  /**
   * sendMessage -
   * overloaded method for sending just a command with no arguments
   *
   * @param type - type of command
   * @throws IOException
   */
  private void sendMessage(String type) throws IOException {
    outToServer.writeBytes(EmailUtils.constructTcpMessage(type));
  }

  /**
   * parseEmailResponse -
   * Helper function to parse server resposne for fetching emails. Returns an
   * Email array. ASSUMES serverReposne is a properly formatted response for
   * emails
   *
   * @param serverResponse - Server's resposne as a string of all emails for a
   *     user
   * @return - An array of type Email with all of a user's mail
   */
  private Email[] parseEmailResponse(String serverResponse) {
    Email emailList[] = {};
    ProtocolMap argMap = new ProtocolMap(serverResponse);

    // if empty inbox, return empty array
    String allEmails = argMap.get(EmailUtils.EMAIL_LIST_KEY);
    if (allEmails.equals(EmailUtils.EMAIL_DELIM)) {
      return emailList;
    }

    String splitEmails[] = allEmails.split(EmailUtils.EMAIL_DELIM);
    emailList = new Email[splitEmails.length];

    // for each email, de-serialize
    for (int i = 0; i < splitEmails.length; ++i) {
      emailList[i] = new Email(splitEmails[i]);
    }

    return emailList;
  }
}
