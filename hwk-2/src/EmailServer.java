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

        // any empty string array for later use

        while (session) {
            if (!loggedIn) {
                System.out.println("Waiting for client...");
                // log in a user
                System.out.println("Client connected at " + mailServer.waitForClientConnect());
                currentUser = extractArg(mailServer.listenForRequest(), "username"); 
                System.out.println("\nLogging in as user: " + currentUser);

                // respond to client
                mailServer.sendResponse(EmailUtils.constructTcpMessage(EmailUtils.LOG_IN_ACK, EmailUtils.OK_STATUS));
                loggedIn = true;
            } else {

                // listen for a command
                String request = mailServer.listenForRequest();
                System.out.println("\nRequest: " + extractCommand(request));

                switch (extractCommand(request)) {
                    case EmailUtils.SEND_EMAIL:
                        sendEmail(request, emailStorage);
                        mailServer.sendResponse(EmailUtils.constructTcpMessage(EmailUtils.SEND_EMAIL_ACK, EmailUtils.OK_STATUS));
                        System.out.println("Email sent");
                        break;
                    case EmailUtils.RETRIEVE_EMAILS:
                        mailServer.sendResponse(fetchEmails(request, currentUser, emailStorage));
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
     * extractArg -
     * Extracts a named arg from a tcp request
     * 
     * @param request - a formatted tcp request
     * @param argName - name of arg to extract
     * @return - string with arg
     */
    public static String extractArg(String request, String argName) {
        String str = "";
        String arr[] = request.split(EmailUtils.ARG_DELIM);
        for (final String slice : arr) {
            if (slice.indexOf(argName) != -1) {
                str = slice.substring(slice.indexOf("=")+1);
            }
        }
        return str;
    }

    /**
     * extractComman - 
     * extras the command from a tcp reqeust
     * 
     * @param request - a formatted tcp request
     * @return - string with command
     */
    public static String extractCommand(String request) {
        int beginIdx = request.indexOf("="), endIdx = request.indexOf("&");
        return request.substring(beginIdx + 1, endIdx == -1 ? request.length() : endIdx);
    }

    /**
     * sendEmail - 
     * takes in a send email request and delivers it to the user's mail box
     * 
     * @param request - request string
     * @param user - name of user that fills "from" field
     * @param emails - where all emails are stored
     */
    public static void sendEmail(String request, HashMap<String, ArrayList<Email>> emails) 
        // adds to user mailbox, or creates a new inbox if necessary
        Email emailToInstert = Email.stringToEmail(extractArg(request, "email");
        if (emails.containsKey(toUser)){
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
    public static String fetchEmails(String request, String user, HashMap<String, ArrayList<Email>> emails) {
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

        return EmailUtils.constructTcpMessage(EmailUtils.EMAILS, "emails", arg);
    }
}