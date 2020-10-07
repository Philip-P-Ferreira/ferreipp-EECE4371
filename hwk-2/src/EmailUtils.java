public class EmailUtils {
    // constants used in server and client
    public static final String MSG_PREFIX = "type";
    public static final String ARG_DELIM = "&";
    public static final String OK_STATUS = "status: ok";
    public static final String EMAIL_DELIM = "ZZZ";

    public static final String LOG_IN = "log_in";
    public static final String LOG_IN_ACK = "log_in_ack";
    public static final String LOG_OUT = "log_out";
    public static final String LOG_OUT_ACK = "log_out_ack";

    public static final String RETRIEVE_EMAILS = "retrieve_emails";
    public static final String EMAILS = "emails";

    public static final String SEND_EMAIL = "send_email";
    public static final String SEND_EMAIL_ACK = "send_email_ack";

    public static final int PORT = 6789;
    public static final String SERVER_ADDRESS = "18.188.253.19";

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

    // overloaded versions for simplifying code
    public static String constructTcpMessage(String type) {
        String[] tmp = {};
        return constructTcpMessage(type, tmp, tmp);
    }

    public static String constructTcpMessage(String type, String argName, String arg) {
        String argNameArr[] = {argName}, argArr[] = {arg};
        return constructTcpMessage(type,argNameArr,argArr);
    }

    // alternate version for no arg name just one arg (like for ok statuses)
    public static String constructTcpMessage(String type, String arg) {
        return "type=" + type + ARG_DELIM + arg + '\n';
    }
}
