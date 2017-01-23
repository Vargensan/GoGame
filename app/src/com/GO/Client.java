package src.com.GO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Kinga Krata 2016-12-05.
 */
/**client's class recives signals from server and do what is neccesery to support signal and answer to it*/
public class Client {

       private  Socket socket;
       public PrintWriter out;
       public BufferedReader in;

    /**
     * Method that is responsible for checking if server is turn on
     */
    public void listenSocket(){
            try {
                socket = new Socket("localhost", 4444);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            catch (UnknownHostException e) {
                System.out.println("Unknown host: localhost"); System.exit(1);
            }
            catch  (IOException e) {
                System.out.println("No I/O"); System.exit(1);
            }
        }


    }

