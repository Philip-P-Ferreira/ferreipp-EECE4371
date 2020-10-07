// a simple data structure for storing an email
public class Email {
    public String to;
    public String from;
    public String body;

    public Email(String to, String from, String body) {
        this.to = to;
        this.from = from;
        this.body = body;
    }

    public String toString() {
        return "to>" + to + ";from>" + from + ";body>" + body;
    }

    public static Email stringToEmail(String serializedEmail) {
        String fields[] = serializedEmail.split(";");
        String to = fields[0].substring(fields[0].indexOf(">")+1);
        String from = fields[1].substring(fields[1].indexOf(">")+1);
        String body = fields[2].substring(fields[2].indexOf(">")+1);

        return new Email(to, from, body);
    }
}
