import java.io.*;
import java.net.*;
import java.util.*;

public class ServerHandler {

    // initialized in constructor
    private ServerSocket mServerSocket;
    private HashMap<String, ArrayList<Email>> emails;

    // initialized later on
    private Socket mClientSocket;
    private BufferedReader clientReader;
    private DataOutputStream clientWriter;

    private String currentUser;
    private boolean loggedIn;


    /**
     * Construtor -
     * Establishes a server socket based on the passed in port
     * @param port
     * @throws IOException
     */
    public ServerHandler(int port) throws IOException {
        mServerSocket = new ServerSocket(port);
        emails = new HashMap<>();
        loggedIn = false;
    }

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
        clientReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
        clientWriter = new DataOutputStream(mClientSocket.getOutputStream());

        return mClientSocket.getInetAddress().toString();
    }

    /**
     * sendResponse -
     * Sends a response to the client
     * 
     * @param msg - String of response
     * @throws IOException
     */
    public void sendResponse(String msg) throws IOException {
        clientWriter.writeBytes(msg);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * login -
     * log the user in. Sets username and loggedin flag
     * 
     * @param username - String of username
     * @throws IOException
     */
    public void login(String username) throws IOException {
        currentUser = username;
        loggedIn = true;
        sendResponse(EmailUtils.constructTcpMessage(EmailUtils.LOG_IN_ACK, EmailUtils.OK_STATUS));
    }

    /**
     * addEmail -
     * adds an Email to the internal email storage. Creates a new inbox if necessary
     * 
     * @param email - serialized email
     * @throws IOException
     */
    public void addEmail(String email) throws IOException {
        // adds to user mailbox, or creates a new inbox if necessary
        Email emailToInstert = new Email(email);
        if (emails.containsKey(emailToInstert.to)){
            emails.get(emailToInstert.to).add(emailToInstert);
        } else {
            ArrayList<Email> newList = new ArrayList<Email>();
            newList.add(emailToInstert);
            emails.put(emailToInstert.to, newList);
        }

        sendResponse(EmailUtils.constructTcpMessage(EmailUtils.SEND_EMAIL_ACK, EmailUtils.OK_STATUS));
    }

    /**
     * fetchEmails -
     * Returns a string serilization of all the emails in the current user's inbox.
     * Empty inbox is denoted as the lone email delimeter (ZZZ)
     * 
     * @throws IOException
     */
    public void fetchEmails() throws IOException{
        ArrayList<Email> userMsgs;
        String arg = EmailUtils.EMAIL_DELIM;

        // construct string if user has a mailbox
        if (emails.containsKey(currentUser)) {
            userMsgs = emails.get(currentUser);
            String partialRequest = "";

            for (final Email msg : userMsgs) {
                partialRequest += msg.toString() + EmailUtils.EMAIL_DELIM;
            }
            arg = partialRequest.substring(0,partialRequest.length()-EmailUtils.EMAIL_DELIM.length()); // get rid of extra delim at end
        }

         sendResponse(EmailUtils.constructTcpMessage(EmailUtils.RETRIEVE_RESPONSE, EmailUtils.EMAIL_LIST_KEY, arg));
    }

    /**
     * logout -
     * sends a logout ack to client. Sets username to blank and loggedin flag to false;
     * 
     * @throws IOException
     */
    public void logout() throws IOException{
        sendResponse(EmailUtils.constructTcpMessage(EmailUtils.LOG_OUT_ACK, EmailUtils.OK_STATUS));
        currentUser = "";
        loggedIn = false;


    }
    
}
