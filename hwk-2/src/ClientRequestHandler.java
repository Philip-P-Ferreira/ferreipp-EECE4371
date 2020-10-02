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
        // socket = new Socket(SERVER_ADDRESS, PORT);
        // outToServer = new DataOutputStream(socket.getOutputStream());
        // readFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        login();
        
    }

    public void login() throws IOException {
        final String logInType = "log_in";

        String argName[] = {"userName"};
        String arg[] = {user};

        sendMessage(logInType, argName, arg);

        // get response from server
        // String serverResponse = readFromServer.readLine();
        // System.out.println(serverResponse);
    }

    public Email[] fetchMail() {
        Email msgs[] = {};
        return msgs;
    }

    public void sendMail(Email mail){
        
    }

    public void close() throws IOException {

        // send lougout message
        // socket.close();
    }

    private void sendMessage(String type, String[] argNames, String[] args) throws IOException {
        String msg = "type=" + type;
        for (int i = 0; i < argNames.length; ++i) {
            msg += "&" + argNames[i] + "=" + args[i];
        }

        // outToServer.writeBytes(msg);
    }

    
}
