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
        
        sendMessage(EmailUtils.LOG_IN, "username", user);
        readFromServer.readLine();
    }

    /**
     * fetchMail -
     * requests the server for emails, fecthes the response, parses it, then returns it as an array of emails
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
    public void sendMail(Email mail) throws IOException{
        
        String argNames[] = {"to", "from", "body"};
        String args[] = {mail.to, mail.from, mail.body};

        sendMessage(EmailUtils.SEND_EMAIL, argNames, args);
        readFromServer.readLine(); // listen for response
    }

    /**
     * close -
     * sends the logout request, listends for a resposne, and closes the tcp connection
     * @throws IOException
     */
    public void close() throws IOException {

        sendMessage(EmailUtils.LOG_OUT);
        readFromServer.readLine(); // listend to response

        socket.close(); // be a good client and close it out
    }

    public String getCurrentUser() {
        return user;
    }

    /**
     * sendMessage -
     * Helper function for sending a request message to the server
     * ASSUMES argName and args are of same length
     * 
     * @param type - name of request, string
     * @param argNames - String array of all of the argument names
     * @param args - String array of all of the arguments
     * @throws IOException
     */
    private void sendMessage(String type, String[] argNames, String[] args) throws IOException {
        outToServer.writeBytes(EmailUtils.constructTcpMessage(type, argNames, args));
    }

    // overloaded versions for simplicity
    private void sendMessage(String type, String argName, String arg) throws IOException {
        outToServer.writeBytes(EmailUtils.constructTcpMessage(type, argName, arg));
    }

    private void sendMessage(String type) throws IOException {
        outToServer.writeBytes(EmailUtils.constructTcpMessage(type));
    }


    /**
     * parseEmailResponse -
     * Helper function to parse server resposne for fetching emails. Returns an Email array.
     * ASSUMES serverReposne is a properly formatted response for emails
     * 
     * @param serverResponse - Server's resposne as a string of all emails for a user
     * @return - An array of type Email with all of a user's mail
     */
    private Email[] parseEmailResponse(String serverResponse) {
        
        // create a var to hold email array, get emails from server response
        Email emailList[] = {};
        String emails = serverResponse.substring(serverResponse.lastIndexOf("=")+1);

        // if the emails aren't the emtpy inbox (ZZZ in this case)
        if (!emails.equals(EmailUtils.EMAIL_DELIM)) {

            // split by the delimiter
            String plainEmails[] = emails.split(EmailUtils.EMAIL_DELIM);
            emailList = new Email[plainEmails.length];

            // parse out the email, add to array
            for (int i = 0; i < plainEmails.length; ++i) {
                emailList[i] = Email.stringToEmail(plainEmails[i]);
            }
        }
        
        return emailList;
    }
}
