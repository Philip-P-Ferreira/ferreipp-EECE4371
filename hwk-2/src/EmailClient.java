import java.io.IOException;
import java.util.*;


class EmailClient {

    // Enum of command types
    public static enum COMMANDS {
        RETRIEVE_MAIL,
        COMPOSE_NEW_MAIL,
        LOG_OUT,
    }

    // Array of command types for easy indexing
    public static final COMMANDS COMMAND_LIST[] = {
        COMMANDS.COMPOSE_NEW_MAIL,
        COMMANDS.RETRIEVE_MAIL,
        COMMANDS.LOG_OUT
        };

    public static void main(String[] args) throws IOException {

        // create the objects for getting user input and handling requests
        Scanner console = new Scanner(System.in);
        ClientRequestHandler requestHandler = logUserIn(console);

        // main session loop
        boolean session = true;
        while (session) {
            // get command from user, do corresponding task
            switch (getUserCommand(console)) {
                case RETRIEVE_MAIL:
                    // method to retrieve mail
                    fetchMail(requestHandler);
                    break;
                case COMPOSE_NEW_MAIL:
                    // method to compose and send new email
                    composeNewMail(console, requestHandler);
                    break;
                case LOG_OUT:
                    // method for logging out and closing connections
                    logUserOut(requestHandler);
                    session = false;
                    break;
            }
        }
    }

    /**
     * logUserIn - 
     * Method that accepts input for a username, makes appropriate call to send
     * to server, waits for ack, then returns the username
     * 
     * @param input - Scanner that points to source of input (e.g. console)
     * @return - String of username
     */
    public static ClientRequestHandler logUserIn(Scanner input) throws IOException {
        
        System.out.print("Enter a username to login: ");
        String userName = input.nextLine();

        ClientRequestHandler session = new ClientRequestHandler(userName);
        System.out.println("Logged in as: "+userName);

        return session;
    }

    /**
     * getUserCommand -
     * Prompts user for a number corresponding to a given command. Checks if valid,
     * then returns corresponding command type.
     * 
     * @param input - Scanner that points to source of input (e.g. console)
     * @return - COMMAND enum type. One of 3 commands
     */
    public static COMMANDS getUserCommand(Scanner input) {

        int commandNum = -1;
        boolean validInput = false;

        // loop as long as input isn't valid
        while(!validInput) {
            // prompt user
            System.out.println("\n1. Send Mail\n2. Read Mail\n3. Exit");
            System.out.println("Select a command (input the corresponding number)");
            
            commandNum = input.nextInt();
            if (commandNum < 0 || commandNum > COMMAND_LIST.length) {
                System.out.println("Invalid number");
            } else {     
                validInput = true;
            }
        }
        input.nextLine(); // burn the newline character
        return COMMAND_LIST[commandNum - 1];
    }

    /**
     * fetchMail -
     * Requests the server for the mail of the current user. Prints each email out to console
     * 
     * @param handler - Instance of a connection to a server
     * @throws IOException
     */
    public static void fetchMail(ClientRequestHandler handler) throws IOException {

        Email messages[] = handler.fetchMail();

        System.out.println("Showing all messages...");
        for (final Email mail : messages) {
            System.out.println("\nFrom: " + mail.userField);
            System.out.println("Body: " + mail.body);
        }
    }
    
    /**
     * composeNewMail -
     * Prompts user to input a to user field and a body text field. Delivers mail to specified user
     * 
     * @param input - where to read input from
     * @param handler - instance of connection to server
     * @throws IOException
     */
    public static void composeNewMail(Scanner input, ClientRequestHandler handler) throws IOException{
        String toUser, body;
        
        System.out.print("To: ");
        toUser = input.nextLine();

        System.out.print("Body text: ");
        body = input.nextLine();

        System.out.println("Sending email...");
        Email msgToSend = new Email(toUser, body);
        handler.sendMail(msgToSend);
    }

    /**
     * logUserOut -
     * Logs the current user out and closes the instance of server connection
     * 
     * @param handler - instance of server connection
     * @throws IOException
     */
    public static void logUserOut(ClientRequestHandler handler) throws IOException {
        System.out.println("Logging out...");
        handler.close();
    }
}