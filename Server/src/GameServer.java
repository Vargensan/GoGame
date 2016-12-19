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
    BufferedReader in1;
    PrintWriter out1;
    BufferedReader in2;
    PrintWriter out2;
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
            in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
            out1 = new PrintWriter(client1.getOutputStream(), true);
            out1.println("b");
            in1.readLine();

        }
        catch (IOException e) {
            System.out.println("Accept failed: 4444");
            System.exit(-1);
        }
        try {
            client2 = server.accept();
            in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
            out2 = new PrintWriter(client2.getOutputStream(), true);
            out2.println("w");
            in2.readLine();

            }
        catch (IOException e) {
            System.out.println("Accept failed: 4444"); System.exit(-1);
        }

        int x=-1;
        int y=-1;
        while(true) {
            try {
                if(line!=null) {

                    line = in2.readLine();
                    System.out.println(line);
                   if(line.substring(0,1).equals("p")) {
                        x=Integer.parseInt(in2.readLine().substring(1));
                        y=Integer.parseInt(in2.readLine().substring(1));


                        out1.println("p");
                        out1.println("x"+x);
                        out1.println("y"+y);
                    }

                    line = in1.readLine();
                    if(line.substring(0,1).equals("p"))
                    {
                        x=Integer.parseInt(in1.readLine().substring(1));
                        y=Integer.parseInt(in1.readLine().substring(1));

                        out2.println("p");
                        out2.println("x"+x);
                        out2.println("y"+y);
                    }


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
            in1.close();
            out1.close();
            client1.close();
            server.close();
        }
        catch (IOException e) {
            System.out.println("Could not close."); System.exit(-1);
        }
    }


}
