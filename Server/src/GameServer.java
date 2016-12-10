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
    private Socket client1;
    private Socket client2;
    BufferedReader in;
    PrintWriter out;
    String line="";


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
            client1 = server.accept();
            client2 = server.accept();

        }
        catch (IOException e) {
            System.out.println("Accept failed: 4444");
            System.exit(-1);
        }
        try {
            in = new BufferedReader(new InputStreamReader(client1.getInputStream()));
            out = new PrintWriter(client1.getOutputStream(), true);
        }
        catch (IOException e) {
            System.out.println("Accept failed: 4444"); System.exit(-1);
        }
        while(true) {
            try {
                if(line!=null) {
                    line = in.readLine();
                    System.out.println(line);

                    //here what to do with signals
                }
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
            client1.close();
            server.close();
        }
        catch (IOException e) {
            System.out.println("Could not close."); System.exit(-1);
        }
    }


}