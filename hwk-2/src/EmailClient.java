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
        COMMANDS.RETRIEVE_MAIL,
        COMMANDS.COMPOSE_NEW_MAIL,
        COMMANDS.LOG_OUT
        };

    public static void main(String[] args) {

        // establish a connection to the server

        Scanner console = new Scanner(System.in);
        ClientRequestHandler requestHandler = logUserIn(console);

        boolean session = true;
        while (session) {
            switch (getUserCommand(console)) {
                case RETRIEVE_MAIL:
                    // method to retrieve mail
                    break;
                case COMPOSE_NEW_MAIL:
                    // method to compose new email
                    composeNewMail(console, curUser);
                    break;
                case LOG_OUT:
                    // method to log out
                    session = false;
                    break;
            }
        }


        // DC tcp connection

    }

    /**
     * logUserIn - 
     * Method that accepts input for a username, makes appropriate call to send
     * to server, waits for ack, then returns the username
     * 
     * @param input - Scanner that points to source of input (e.g. console)
     * @return - String of username
     */
    public static ClientRequestHandler logUserIn(Scanner input) {
        
        System.out.print("Enter a username to login: ");
        String userName = input.nextLine();
        // method to construct outgoing message
        // method to send message to server
        // listen for ack
        System.out.println("Logged in as user '"+username +".'");
        return new ClientRequestHandler(userName);   
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
            
            // get input, catch exception of non-int input
            try {
                commandNum = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                input.next();
                continue;
            } 

            // check if valid command number
            if (commandNum < 0 || commandNum > COMMAND_LIST.length) {
                System.out.println("Invalid number");
            } else {     
                validInput = true;
            }
        }
        return COMMAND_LIST[commandNum - 1];
    }
    
    public static void composeNewMail(Scanner input, String userName){
    }
}