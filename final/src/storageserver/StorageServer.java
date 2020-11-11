import java.io.*;
import java.util.HashMap;

import commonutils.TcpStream;

import static commonutils.ServerProtocol.*;

public class StorageServer {

    // file constants
    public static final String STORAGE_PATH = "~/usb/storage_root/";
    public static final File STORAGE_PATH_FILE = new File(STORAGE_PATH);

    // number constants
    private static final int SLEEP_TIME = 1000;
    private static final int REPEAT_COUNT = 4;

    // instance tcp stream
    private static TcpStream interStream;
    private static int attemptCount = 0; // keep track of connect attempts

    public static void main(String[] args) throws IOException {

        // vars to keep track of server connection status
        boolean connectedToServer = false;
        HashMap<String, String> requestMap;

        // server always on
        boolean serverOn = true;
        while (serverOn) {

            // method to attempt connect, returns corresponding boolean
            connectedToServer = connectToInterServer();

            // repeat while connection is still active
            while (connectedToServer) {
                try {
                    // handle request type
                    requestMap = createProtocolMap(interStream.readMessage(), PAIR_DELIM, PAIR_SEPARATOR);
                    switch (requestMap.get(REQUEST_KEY)) {

                        case UPLOAD_START_VAL:
                            handleUpload(requestMap);
                            break;

                        default:
                            break;
                    }
                } catch (IOException e) {
                    System.out.println(StorageStrings.CONNECTION_BROKEN);
                    connectedToServer = false;
                    sleep();
                }
            }
        }
        interStream.close();
    }

    // helper method to connect to server, returns corresponding boolean
    private static boolean connectToInterServer() {

        try {
            if (attemptCount == 0) {
                clear_console();
                System.out.print(StorageStrings.ATTEMPTING_RECONNECT);
            }
            interStream = new TcpStream(INTERSERVER_ADDRESS, STORAGE_PORT);
            
            // on success, set relevant variables / output
            attemptCount = 0;
            System.out.println('\n' + StorageStrings.CONNECTED_TO_INTER);
            return true;

        } catch (IOException e) {
            sleep();
            ++attemptCount;

            // add to output based on how many times we've tried
            if (attemptCount == REPEAT_COUNT) {
                clear_console();
                attemptCount = 0;
            } else if (attemptCount != 0) {
                System.out.print('.');
            }
            return false;
        }
    }

    private static void handleUpload(HashMap<String, String> req) throws IOException {

        // get name of file from request
        String filename = STORAGE_PATH + req.get(FILENAME_KEY);
        String filenameZip = filename + ZIP_SUFFIX;

        // send ack back
        HashMap<String, String> res = new HashMap<>();
        res.put(STATUS_KEY, STATUS_OK_VAL);
        sendProtocolMessage(interStream, UPLOAD_START_ACK_VAL, res);
        System.out.println(StorageStrings.UPLOAD_READY);

        // write socket in to file
        FileOutputStream outToFile = new FileOutputStream(filenameZip);
        interStream.readToOutputStream(outToFile);

        // reconnect to server (file write breaks socket)
        interStream = new TcpStream(INTERSERVER_ADDRESS, STORAGE_PORT);
        System.out.println(StorageStrings.UPLOAD_RECEIVED);
    }

    // helper method to easily clear console
    private static void clear_console() {
        System.out.print("\033[H\033[2J");
    }

    // helper method to sleep execution
    private static void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException ignored) {
            // don't care about exception
        }
    }
}