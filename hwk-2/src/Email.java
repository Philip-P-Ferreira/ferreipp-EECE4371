public class Email {
  // member variables
  public String to;
  public String from;
  public String body;

  // private constants
  private static final String FIELD_DELIM = ";";
  private static final String FIELD_SEPARATOR = ">";
  private static final String TO_FIELD = "to";
  private static final String FROM_FIELD = "from";
  private static final String BODY_FIELD = "body";

  /**
   * Construtor -
   * Basic constructor for an Email
   *
   * @param to - user adressed to
   * @param from - user that sent message
   * @param body - body text of emails
   */
  public Email(String to, String from, String body) {
    this.to = to;
    this.from = from;
    this.body = body;
  }

  /**
   * Alt Constructor -
   * Constructs an emails from a serialized (tcp) email string
   *
   * @param serializedEmail - a serialized email
   */
  public Email(String serializedEmail) {
    ProtocolMap fieldMap = new ProtocolMap(serializedEmail, FIELD_DELIM, FIELD_SEPARATOR);
    to = fieldMap.get(TO_FIELD);
    from = fieldMap.get(FROM_FIELD);
    body = fieldMap.get(BODY_FIELD);
  }

  /**
   * toString -
   * Converts an email into its tcp message string.
   */
  public String toString() {
    return TO_FIELD + FIELD_SEPARATOR + to + FIELD_DELIM + FROM_FIELD + FIELD_SEPARATOR + from
        + FIELD_DELIM + BODY_FIELD + FIELD_SEPARATOR + body;
  }
}
