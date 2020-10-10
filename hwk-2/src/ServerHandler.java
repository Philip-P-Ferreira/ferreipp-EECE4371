import java.io.*;
import java.net.*;

public class ServerHandler {

  // initialized in constructor
  private ServerSocket mServerSocket;

  // initialized later on
  private Socket mClientSocket;
  private BufferedReader clientReader;
  private DataOutputStream clientWriter;
  private String currentUser;
  private boolean clientConnected;

  /**
   * Construtor -
   * Establishes a server socket based on the passed in port
   * @param port
   * @throws IOException
   */
  public ServerHandler(int port) throws IOException {
    mServerSocket = new ServerSocket(port);
  }

  /**
   * isClientConnected -
   * returns boolean flag denoting if a client is connected
   *
   * @return - boolean, is client connected
   */
  public boolean isClientConnected() { return clientConnected; }

  /**
   * listenforRequest -
   * Listens to the client for a request
   *
   * @return - returns the string of the client's request
   * @throws IOException
   */
  public String listenForRequest() throws IOException {
    return clientReader.readLine();
  }

  /**
   * waitForClientConnect -
   * Waits for the client to connect. Initializes the data streams
   *
   * @return - String of Client's IP address
   * @throws IOException
   */
  public String waitForClientConnect() throws IOException {
    mClientSocket = mServerSocket.accept();
    clientReader = new BufferedReader(
        new InputStreamReader(mClientSocket.getInputStream()));
    clientWriter = new DataOutputStream(mClientSocket.getOutputStream());
    clientConnected = true;

    return mClientSocket.getInetAddress().toString();
  }

  /**
   * logUserIn -
   * accepts a username and logs that user in, storing their username until
   * the logout message is received. Sends an ack
   *
   * @param user - username to log in
   * @throws IOException
   */
  public void logUserIn(String user) throws IOException {
    currentUser = user;
    sendAck(EmailUtils.LOG_IN_ACK);
  }

  /**
   * sendAck -
   * sends an ok status ack to client with the type value equal to the string
   * passed in
   *
   * @param ackTypeValue - a string with the type value for the ack
   * @throws IOException
   */
  public void sendAck(String ackTypeValue) throws IOException {
    sendResponse(
        EmailUtils.constructTcpMessage(ackTypeValue, EmailUtils.OK_STATUS));
  }

  /**
   * logout -
   * sends a logout ack to client. Sets username to blank and loggedin flag to
   * false;
   *
   * @throws IOException
   */
  public void logUserOut() throws IOException {
    sendAck(EmailUtils.LOG_OUT_ACK);
    currentUser = "";
    clientConnected = false;
  }

  /**
   * getCurrentUser -
   * returns the current user
   */
  public String getCurrentUser() { return currentUser; }

  /**
   * returnFetchedEmails -
   * accepts an EmailStorage and send the client the the emails belonging to
   * the current user
   *
   * @param emails - EmailStorage that contains users' inboxes
   * @throws IOException
   */
  public void returnFetchedEmails(EmailStorage emails) throws IOException {
    sendResponse(EmailUtils.constructTcpMessage(
        EmailUtils.RETRIEVE_RESPONSE, EmailUtils.EMAIL_LIST_KEY,
        emails.fetchEmails(currentUser)));
  }

  /**
   * sendResponse -
   * Sends a response to the client
   *
   * @param msg - String of response
   * @throws IOException
   */
  private void sendResponse(String msg) throws IOException {
    clientWriter.writeBytes(msg);
  }
}
