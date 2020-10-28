package utils;

import java.io.IOException;
import java.util.*;

public class EmailProtocol {
  // constants used in server and client
  public static final String COMMAND_KEY = "type";
  public static final String PAIR_DELIM = "&";
  public static final String STATUS_KEY = "status";
  public static final String STATUS_OK_VALUE = "ok";
  public static final String STATUS_FAIL_VALUE = "failed";
  public static final String EMAIL_DELIM = "ZZZ";
  public static final String PAIR_SEPARATOR = "=";

  public static final String TOKEN_KEY = "token";

  public static final String LOG_IN = "log_in";
  public static final String LOG_IN_ACK = "log_in_ack";
  public static final String USERNAME_KEY = "username";
  public static final String PASSWORD_KEY = "password";
  public static final String LOG_OUT = "log_out";
  public static final String LOG_OUT_ACK = "log_out_ack";

  public static final String RETRIEVE_EMAILS = "retrieve_emails";
  public static final String RETRIEVE_RESPONSE = "emails";
  public static final String EMAIL_LIST_KEY = "emails";

  public static final String SEND_EMAIL = "send_email";
  public static final String EMAIL_KEY = "email";
  public static final String SEND_EMAIL_ACK = "send_email_ack";

  // change these to change server port / address
  public static final int PORT = 6789;
  public static final String SERVER_ADDRESS = "127.0.0.1";

  /**
   * createProtocolMap -
   * create a hashmap of String-String pairs, where pairs are delimited by
   * delimiter, and separated by separator.
   *
   * @param str - String to turn into hashmap
   * @param delimiter - splits String-String pairs
   * @param separator - breaks pair into key and value
   * @return
   */
  public static HashMap<String, String> createProtocolMap(
      String str, String delimiter, String separator) {
    String argArr[] = str.split(delimiter);
    HashMap<String, String> map = new HashMap<>();

    for (final String arg : argArr) {
      int sepIndex = arg.indexOf(separator);
      map.put(arg.substring(0, sepIndex), arg.substring(sepIndex + 1));
    }

    return map;
  }

  public static void sendProtocolMessage(TcpStream stream, String type, HashMap<String,String> argMap) throws IOException {
    String msg = COMMAND_KEY + PAIR_SEPARATOR + type;
    
    for (final Map.Entry<String,String> pair : argMap.entrySet() ) {
      msg += PAIR_DELIM + pair.getKey() + PAIR_SEPARATOR + pair.getValue();
    }
    stream.write(msg + '\n');

  }
}
