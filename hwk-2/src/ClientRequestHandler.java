import java.io.*;
import java.net.*;

public class ClientRequestHandler {
    private static final int PORT = 6789;
    private static final String SERVER_ADDRESS = "10.0.2.2";

    private String user;
    private Socket socket;
    private DataOutputStream outToServer;
    private BufferedReader readFromServer;

    public ClientRequestHandler(String userName) throws IOException {
        user = userName;
        socket = new Socket(SERVER_ADDRESS, PORT);
        outToServer = new DataOutputStream(socket.getOutputStream());
        readFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void login() {
        // send message with username;
    }

    public void fetchMail() {}

    public void sendMail(/*email data type*/){
        
    }

    public void close() throws IOException {
        // send lougout message
        socket.close();
    }


}
