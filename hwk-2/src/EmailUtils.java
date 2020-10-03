public class EmailUtils {
    // constants used in server and client
    public static final String MSG_PREFIX = "type";
    public static final String ARG_DELIM = "&";

    public static final String LOG_IN = "log_in";
    public static final String LOG_IN_ACK = "log_in_ack";
    public static final String LOG_OUT = "log_out";
    public static final String LOG_OUT_ACK = "log_out_ack";

    public static final String RETRIEVE_EMAILS = "retrieve_emails";
    public static final String EMAILS = "emails";

    public static final String SEND_EMAIL = "send_email";
    public static final String SEND_EMAIL_ACK = "send_email_ack";

    public static final int PORT = 6789;
    public static final String SERVER_ADDRESS = "13.59.192.42";
    // 13.59.192.42 for amazon
    // 10.66.186.17 for this laptop

    /**
     * constructTcpMessage - 
     * creates a tcp messsage based on passed in string parameters
     * ASSUME both arrays are the same length
     * 
     * @param type - type of command
     * @param argNames - string array of name of arguments
     * @param args - string array or arguments 
     * @return -- tcp message string
     */
    public static String constructTcpMessage(String type, String[] argNames, String[] args) {
        // loop through each arg name and arg and construct a formatted request string
        String msg = EmailUtils.MSG_PREFIX + "=" + type;
        for (int i = 0; i < argNames.length; ++i) {
            msg += EmailUtils.ARG_DELIM + argNames[i] + "=" + args[i];
        }
        return msg + '\n';
    }
}
