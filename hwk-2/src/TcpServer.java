import java.io.*;
import java.net.*;

public class TcpServer {

    // initialized in constructor
    private ServerSocket mServerSocket;

    // initialized later on
    private Socket mClientSocket;
    private BufferedReader clientReader;
    private DataOutputStream clientWriter;


    /**
     * Construtor -
     * Establishes a server socket based on the passed in port
     * @param port
     * @throws IOException
     */
    public TcpServer(int port) throws IOException {
        mServerSocket = new ServerSocket(port);
    }

    /**
     * listenforRequest -
     * Listens to the client for a request
     * 
     * @return - returns the string of the client's request
     * @throws IOException
     */
    public String listenForRequest() throws IOException {
        return clientReader.readLine();
    }

    /**
     * waitForClientConnect -
     * Waits for the client to connect. Initializes the data streams
     * 
     * @return - String of Client's IP address
     * @throws IOException
     */
    public String waitForClientConnect() throws IOException {
        mClientSocket = mServerSocket.accept();
        clientReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
        clientWriter = new DataOutputStream(mClientSocket.getOutputStream());

        return mClientSocket.getInetAddress().toString();
    }

    /**
     * sendResponse -
     * Sends a response to the client
     * 
     * @param msg - String of response
     * @throws IOException
     */
    public void sendResponse(String msg) throws IOException {
        clientWriter.writeBytes(msg);
    }
    
}
