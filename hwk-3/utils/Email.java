package utils;

import static utils.EmailProtocol.*;

import java.util.HashMap;

public class Email {
  // class fields
  public String to;
  public String from;
  public String body;

  // constants used in email serialization
  private static final String FIELD_DELIM = ";";
  private static final String FIELD_SEPARATOR = ">";
  private static final String TO_FIELD = "to";
  private static final String FROM_FIELD = "from";
  private static final String BODY_FIELD = "body";

  // constructor
  public Email(String to, String from, String body) {
    this.to = to;
    this.from = from;
    this.body = body;
  }

  // Alt constructor (from string)
  public Email(String emailString) {
    HashMap<String, String> fieldMap = createProtocolMap(emailString, FIELD_DELIM, FIELD_SEPARATOR);
    to = fieldMap.get(TO_FIELD);
    from = fieldMap.get(FROM_FIELD);
    body = fieldMap.get(BODY_FIELD);
  }

  /**
   * toString -
   * Converts the Email into it's serialized string form
   *
   * @return - serialized email string
   */
  public String toString() {
    return TO_FIELD + FIELD_SEPARATOR + to + FIELD_DELIM + FROM_FIELD + FIELD_SEPARATOR + from
        + FIELD_DELIM + BODY_FIELD + FIELD_SEPARATOR + body;
  }
}
