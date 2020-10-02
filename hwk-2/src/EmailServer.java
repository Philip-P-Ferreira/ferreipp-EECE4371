import java.io.IOException;
import java.util.*;

class EmailServer {
    enum COMMAND {
        LOG_IN,
        LOG_OUT,
        RETRIEVE_EMAILS,
        SEND_EMAIL,
    };
    public static void main(String[] args) throws IOException {
        // start up the tcp server
        TcpServer mailServer = new TcpServer(EmailUtils.PORT);

        // create a place to store a user's emails
        HashMap<String, ArrayList<Email>> emailStorage = new HashMap<String, ArrayList<Email>>();

        // generate a table to translate text to commands
        HashMap<String,COMMAND> commandTable = new HashMap<String,COMMAND>();
        initCommandTable(commandTable);

        // some vars to keep track of a session
        String currentUser;
        boolean session = true;
        boolean loggedIn = false;

        while (session) {
            if (!loggedIn) {
                // log in a user
                System.out.println("Client connected at " + mailServer.waitForClientConnect());
                currentUser = extractArg(mailServer.listenForRequest(), "username"); 
                System.out.println("Logging in as user: " + currentUser);

                String tmp[] = {};
                mailServer.sendResponse(EmailUtils.constructTcpMessage(EmailUtils.LOG_IN_ACK, tmp, tmp));
                loggedIn = true;
            } else {
                System.out.println(mailServer.listenForRequest());
                mailServer.sendResponse("type=emails&emails=from>alice;body>hey john|from>bob;body>yo dawg\n");
            }
        }
    }



    private static void initCommandTable(HashMap<String,COMMAND> table) {
        table.put(EmailUtils.LOG_IN, COMMAND.LOG_IN);
        table.put(EmailUtils.LOG_OUT, COMMAND.LOG_OUT);
        table.put(EmailUtils.RETRIEVE_EMAILS, COMMAND.RETRIEVE_EMAILS);
        table.put(EmailUtils.SEND_EMAIL, COMMAND.SEND_EMAIL);
    }

    public static String extractArg(String request, String argName) {
        String arr[] = request.split(EmailUtils.ARG_DELIM);

        for (final String slice : arr) {
            if (slice.indexOf(argName) != -1) {
                return slice.substring(slice.indexOf("=")+1);
            }
        }
        return "";
    }
}