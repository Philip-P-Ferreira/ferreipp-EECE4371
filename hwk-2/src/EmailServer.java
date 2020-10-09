import java.io.IOException;
import java.util.*;

class EmailServer {
    public static void main(String[] args) throws IOException {
        // start up the tcp server
        TcpServer mailServer = new TcpServer(EmailUtils.PORT);
        System.out.println("Starting up server...");

        // create a place to store a user's emails
        HashMap<String, ArrayList<Email>> emailStorage = new HashMap<String, ArrayList<Email>>();

        // some vars to keep track of a session
        String currentUser = "";
        boolean session = true;
        boolean loggedIn = false;

        while (session) {
            // map to hold our incoming request as a map
            HashMap<String,String> requestMap;

            if (!loggedIn) {
                // connect to client
                System.out.println("Waiting for client...");
                System.out.println("Client connected at " + mailServer.waitForClientConnect());

                // extract the username
                requestMap = EmailUtils.getPairMap(mailServer.listenForRequest());
                currentUser = requestMap.get(EmailUtils.USERNAME_KEY);
                System.out.println("\nLogging in as user: " + currentUser);

                // respond to client
                mailServer.sendResponse(EmailUtils.constructTcpMessage(EmailUtils.LOG_IN_ACK, EmailUtils.OK_STATUS));
                loggedIn = true;

            } else {
                // listen for a command
                requestMap = EmailUtils.getPairMap(mailServer.listenForRequest());
                System.out.println("Reqeust: " + requestMap.get(EmailUtils.COMMAND_KEY));

                switch (requestMap.get(EmailUtils.COMMAND_KEY)) {
                    case EmailUtils.SEND_EMAIL:
                        sendEmail(requestMap, emailStorage);
                        mailServer.sendResponse(EmailUtils.constructTcpMessage(EmailUtils.SEND_EMAIL_ACK, EmailUtils.OK_STATUS));
                        System.out.println("Email sent");
                        break;
                    case EmailUtils.RETRIEVE_EMAILS:
                        mailServer.sendResponse(fetchEmails(currentUser, emailStorage));
                        System.out.println("Emails fetched");    
                        break;
                    case EmailUtils.LOG_OUT:
                        mailServer.sendResponse(EmailUtils.constructTcpMessage(EmailUtils.LOG_OUT_ACK, EmailUtils.OK_STATUS));
                        currentUser = "";
                        loggedIn = false;
                        System.out.println("logged out\n");
                        break;
                }
            }
        }
    }

    /**
     * sendEmail - 
     * takes in a send email request and delivers it to the user's mail box
     * 
     * @param request - request string
     * @param user - name of user that fills "from" field
     * @param emails - where all emails are stored
     */
    public static void sendEmail(HashMap<String,String> requestMap, HashMap<String, ArrayList<Email>> emails) {
        // adds to user mailbox, or creates a new inbox if necessary
        Email emailToInstert = new Email(requestMap.get(EmailUtils.EMAIL_KEY));
        if (emails.containsKey(emailToInstert.to)){
            emails.get(emailToInstert.to).add(emailToInstert);
        } else {
            ArrayList<Email> newList = new ArrayList<Email>();
            newList.add(emailToInstert);
            emails.put(emailToInstert.to, newList);
        }
    }

    /**
     * fetchEmail -
     * turns a user's inbox into a full tcp string response
     * 
     * @param request - 
     * @param user - user to whom mailbox belongs
     * @param emails - emails storage
     * @return
     */
    public static String fetchEmails(String user, HashMap<String, ArrayList<Email>> emails) {
        ArrayList<Email> userMsgs;
        String arg = EmailUtils.EMAIL_DELIM;

        // consturct string if user has a mailbox
        if (emails.containsKey(user)) {
            userMsgs = emails.get(user);
            String partialRequest = "";

            for (final Email msg : userMsgs) {
                partialRequest += msg.toString() + EmailUtils.EMAIL_DELIM;
            }
            arg = partialRequest.substring(0,partialRequest.length()-EmailUtils.EMAIL_DELIM.length()); // get rid of extra delim at end
        }

        return EmailUtils.constructTcpMessage(EmailUtils.RETRIEVE_RESPONSE, EmailUtils.EMAIL_LIST_KEY, arg);
    }
}