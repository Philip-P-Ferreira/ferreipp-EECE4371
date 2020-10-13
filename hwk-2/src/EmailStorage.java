import java.util.*;

public class EmailStorage {
  HashMap<String, ArrayList<Email>> emails;

  // Default constructor
  public EmailStorage() {
    emails = new HashMap<>();
  }

  /**
   * addEmail -
   * adds an Email to the internal email storage. Creates a new inbox if
   * necessary
   *
   * @param email - serialized email
   */
  public void addEmail(String serializedEmail) {
    // adds to user mailbox, or creates a new inbox if necessary
    Email emailToInsert = new Email(serializedEmail);
    if (emails.containsKey(emailToInsert.to)) {
      emails.get(emailToInsert.to).add(emailToInsert);
    } else {
      ArrayList<Email> newList = new ArrayList<Email>();
      newList.add(emailToInsert);
      emails.put(emailToInsert.to, newList);
    }
  }

  /**
   * fetchEmails -
   * Returns a string serilization of all the emails in the current user's
   * inbox. Empty inbox is denoted as the lone email delimeter (ZZZ)
   *
   * @throws IOException
   */
  public String fetchEmails(String user) {
    ArrayList<Email> userMsgs;
    String arg = EmailUtils.EMAIL_DELIM;

    // construct string if user has a mailbox
    if (emails.containsKey(user)) {
      userMsgs = emails.get(user);
      String partialRequest = "";

      for (final Email msg : userMsgs) {
        partialRequest += msg.toString() + EmailUtils.EMAIL_DELIM;
      }
      arg = partialRequest.substring(0,
          partialRequest.length()
              - EmailUtils.EMAIL_DELIM.length()); // get rid of extra delim at end
    }
    return arg;
  }
}
