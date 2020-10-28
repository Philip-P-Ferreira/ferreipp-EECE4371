import java.io.IOException;
import java.util.Scanner;
import utils.*;

public class tmpClient {
  public static void main(String[] args) throws IOException {
    Scanner console = new Scanner(System.in);
    while (true) {
      String input = console.nextLine();
      TcpStream stream = new TcpStream(EmailProtocol.SERVER_ADDRESS, EmailProtocol.PORT);
      stream.write(input + '\n');
      System.out.println(stream.read());
      stream.close();
    }
  }
}
