import java.util.HashMap;

public class ProtocolMap {

  HashMap<String, String> map;

  // default constructor
  public ProtocolMap() { map = new HashMap<>(); }

  // alt constructor
  public ProtocolMap(String str, String delimiter, String separator) {
    generate(str, delimiter, separator);
  }

  // alt constructor
  public ProtocolMap(String str) { generate(str); }

  /**
   * generate -
   * generates a new request map based on the passed in string, where values
   * are split by the delimiter, and separated by separator
   *
   * @param str - string to turn into map
   * @param delimiter - splits key-value pairs
   * @param separator - splits pairs into key and value
   */
  public void generate(String str, String delimiter, String separator) {
    String argArr[] = str.split(delimiter);
    HashMap<String, String> newMap = new HashMap<>();

    for (final String arg : argArr) {
      int sepIndex = arg.indexOf(separator);
      newMap.put(arg.substring(0, sepIndex), arg.substring(sepIndex + 1));
    }

    map = newMap;
  }

  /**
   * generate -
   * generates a new request map assuming the passed in string is a standard
   * protocol messaage
   *
   * @param protocolMsg - a standard email protocol message
   */
  public void generate(String protocolMsg) {
    generate(protocolMsg, EmailUtils.PAIR_DELIM, EmailUtils.PAIR_SEPARATOR);
  }

  /**
   * getValue -
   * gets the values associated with key
   *
   * @param key - key to value
   * @return
   */
  public String get(String key) { return map.get(key); }
}
