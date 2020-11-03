
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import emailutils.*;

public class tmpClient {
  public static void main(String[] args) throws IOException {
    Scanner console = new Scanner(System.in);
    TcpStream stream = new TcpStream(EmailProtocol.SERVER_ADDRESS, EmailProtocol.PORT);

    HashMap<String,String> argMap = new HashMap<>();
    argMap.put(EmailProtocol.USERNAME_KEY, "bruh");
    argMap.put(EmailProtocol.PASSWORD_KEY, "1234");

    EmailProtocol.sendProtocolMessage(stream, EmailProtocol.LOG_IN, argMap);
    HashMap<String,String> resMap = EmailProtocol.createProtocolMap(stream.read(), EmailProtocol.PAIR_DELIM, EmailProtocol.PAIR_SEPARATOR);
    stream.close();

    String token = resMap.get(EmailProtocol.TOKEN_KEY);
  
    boolean run = true;
    while (run) {
      console.nextLine();
      stream = new TcpStream(EmailProtocol.SERVER_ADDRESS, EmailProtocol.PORT);
      
      argMap = new HashMap<>();
      argMap.put(EmailProtocol.TOKEN_KEY, token);
      argMap.put(EmailProtocol.EMAIL_KEY, (new Email("jeff", "bruh", "jeff jeff")).toString());
      EmailProtocol.sendProtocolMessage(stream, EmailProtocol.SEND_EMAIL, argMap);
      
      System.out.println(stream.read());
      stream.close();
    }
    console.close();
  }
}
