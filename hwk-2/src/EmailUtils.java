import java.util.HashMap;

public class EmailUtils {
    // constants used in server and client
    public static final String COMMAND_KEY = "type";
    public static final String PAIR_DELIM = "&";
    public static final String OK_STATUS = "status: ok";
    public static final String EMAIL_DELIM = "ZZZ";
    public static final String PAIR_SEPARATOR = "=";

    public static final String LOG_IN = "log_in";
    public static final String LOG_IN_ACK = "log_in_ack";
    public static final String USERNAME_KEY = "username";
    public static final String LOG_OUT = "log_out";
    public static final String LOG_OUT_ACK = "log_out_ack";
    

    public static final String RETRIEVE_EMAILS = "retrieve_emails";
    public static final String RETRIEVE_RESPONSE = "emails";
    public static final String EMAIL_LIST_KEY = "emails";


    public static final String SEND_EMAIL = "send_email";
    public static final String EMAIL_KEY = "email";
    public static final String SEND_EMAIL_ACK = "send_email_ack";

    public static final int PORT = 6789;
    public static final String SERVER_ADDRESS = "10.66.56.222";

    /**
     * constructTcpMessage - 
     * constructs a simple one pair tcp message
     * 
     * @param type - type value for key-value pair
     * @return - tcp String message
     */
    public static String constructTcpMessage(String type) {
        return COMMAND_KEY + PAIR_SEPARATOR + type + '\n';
    }

    /**
     * constructTcpMessage - 
     * constructs a tcp message with two key value pairs
     * 
     * @param type - value for type key
     * @param argName - key for second argument
     * @param arg - value for second argument
     * @return - tcp String message
     */
    public static String constructTcpMessage(String type, String argName, String arg) {
        return COMMAND_KEY + PAIR_SEPARATOR + type + PAIR_DELIM + 
                    argName + PAIR_SEPARATOR + arg + '\n';
    }

    /**
     * constructTcpMessage -
     * Constructs a tcp message with a pair and a single value (like for ack messages)
     * 
     * @param type - type value
     * @param arg - unpaired value
     * @return - tcp String message
     */
    public static String constructTcpMessage(String type, String arg) {
        return "type" + PAIR_SEPARATOR + type + PAIR_DELIM + arg + '\n';
    }

    /**
     * getPairMap - 
     * Returns a key-value map of the string, where pairs are separated my delimiters, and split by separator
     * 
     * @param str - string to split
     * @param delimiter - breaks up pairs
     * @param separator - breaks up key and value
     * @return - Map of type <String,String>
     */
    public static HashMap<String,String> getPairMap(String str, String delimiter, String separator) {
        String argArr[] = str.split(delimiter);
        HashMap<String,String> argMap = new HashMap<>();

        for (final String arg: argArr) {
            int sepIndex = arg.indexOf(separator);
            argMap.put(arg.substring(0, sepIndex), arg.substring(sepIndex +1));
        }

        return argMap;
    }

    /**
     * getPairMap - 
     * Specific version for tcp messages. Most often used in this way.
     * 
     * @param tcpMessage - a formatted tcp message
     * @return - Map of type <String,String>
     */
    public static HashMap<String,String> getPairMap(String tcpMessage) {
        return getPairMap(tcpMessage, PAIR_DELIM, PAIR_SEPARATOR);
    }
}
