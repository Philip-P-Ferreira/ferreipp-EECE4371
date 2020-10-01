import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


class EmailServer {
    private ServerSocket mServerSocket;
    private Socket mClientSocket;

    public void TcpServer(int port) throws IOException {
        mServerSocket = new ServerSocket(port);
    }

    public void listen() throws IOException {
        boolean quit = false;
        while(!quit) {
            waitForConnection();

            BufferedReader clientReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            DataOutputStream clietWriter = new DataOutputStream(mClientSocket.getOutputStream());

            
        }
    }

    private void waitForConnection() throws IOException {
        System.out.println("waitinf fora connection");
        mClientSocket = mServerSocket.accept();
        System.out.println("connected!");
        InetAddress clientAddress = mClientSocket.getInetAddress();
        System.out.println("  client at: " + clientAddress.toString() + ":" + mClientSocket.getPort());
    }
}