import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Kinga Krata 2016-12-05.
 */
public class GameServer {
    private ServerSocket server;
    private Socket client;
    BufferedReader in;
    PrintWriter out;
    String line="";


    String pomoc;

    private String drzewo="a";


    /**server initializing*/
    GameServer() {

        try {
            server = new ServerSocket(4444);
        }
        catch (IOException e) {
            System.out.println("Could not listen on port 4444");
            System.exit(-1);
        }
    }
    /**comunication support*/
    public void listenSocket() {
        try {
            client = server.accept();
        }
        catch (IOException e) {
            System.out.println("Accept failed: 4444");
            System.exit(-1);
        }
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        }
        catch (IOException e) {
            System.out.println("Accept failed: 4444"); System.exit(-1);
        }
        while(true) {
            try {
                line = in.readLine();
                System.out.println(line);

                //here what to do with signals
                 }

            catch (IOException e) {
                System.out.println("Read failed"); System.exit(-1);
            }
        }
    }
    /// server closing
    protected void finalize() {
        try {
            in.close();
            out.close();
            client.close();
            server.close();
        }
        catch (IOException e) {
            System.out.println("Could not close."); System.exit(-1);
        }
    }


}
