import java.util.*;

class Notes {
    public static final String POST_NOTE_STRING = "POST";
    public static final String RETRIEVE_NOTE_STRING = "RETRIEVE";
    public static final String INVALID_COMMAND = "INVALID";
    public static final int POST_COMMAND_SIZE = 3;
    public static final int RETRIEVE_COMMAND_SIZE = 2;

    public static enum COMMANDS {
        POST_NOTE,
        RETRIEVE_NOTE,
        INVALID_COMMAND
    }

    public static void main(String[] args) {

        boolean run = true;

        ArrayList<String> inputArr;
        Scanner console = new Scanner(System.in);
        HashMap<String,String> notesMap = new HashMap<String,String>();

        while (run) {
            // prompt user
            System.out.print("\nJava Notes\n1.Post {note name}: {note content}\n2.Retrieve {note name}\n\nEnter command: ");

            // parse input
            inputArr = convertInput(console.nextLine());

            // interpret input, perform action based on resulting command
            switch (interpret(inputArr)) {

                case POST_NOTE:
                    notesMap.put(inputArr.get(1), inputArr.get(2));
                    break;

                case RETRIEVE_NOTE:
                    String noteToFetch = inputArr.get(1);
                    if (notesMap.containsKey(noteToFetch)) {
                        
                        System.out.println("\n" + noteToFetch + ": " + notesMap.get(noteToFetch));
                    } else {
                        System.out.println("\nNote not found");
                    }
                    break;

                case INVALID_COMMAND:
                    System.out.println("\nInvalid command. Please try again");
                    break;
            }
        }
        console.close();
    }
    public static COMMANDS interpret(final ArrayList<String> inputArr) {

        switch (inputArr.get(0).toUpperCase()) {
            case POST_NOTE_STRING:
                if (inputArr.size() != POST_COMMAND_SIZE) {
                    return COMMANDS.INVALID_COMMAND;
                }
                return COMMANDS.POST_NOTE;
            case RETRIEVE_NOTE_STRING:
                if (inputArr.size() != RETRIEVE_COMMAND_SIZE) {
                    return COMMANDS.INVALID_COMMAND;
                }
                return COMMANDS.RETRIEVE_NOTE;
            default:
                return COMMANDS.INVALID_COMMAND;
        }
    }

    public static ArrayList<String> convertInput(final String input) {
        ArrayList<String> returnArr = new ArrayList<String>();
        int firstSpace = input.indexOf(" ");
        int colonIdx = input.indexOf(":");

        returnArr.add(input.substring(0, firstSpace).trim());
        if (colonIdx != -1) {
            returnArr.add(input.substring(firstSpace, colonIdx).trim());
            returnArr.add(input.substring(colonIdx + 1).trim());
        } else {
            returnArr.add(input.substring(firstSpace).trim());
        }
        return returnArr;
    }
}