import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Kinga Krata 2016-12-05.
 */

public class GameServer {
    static int maxClientsCount = 20;
    private ServerSocket server;
    private Socket clientSocket;

    private static final ClientThread[] threads = new ClientThread[maxClientsCount];
    int port=4444;

    GameServer(int port){
        this.port = port;
        startServer();
        serverListen();
    }

    public void startServer(){
        try{
            server = new ServerSocket(port);
        }
        catch(IOException e){

        }
      //  System.out.println("Waiting for connections!");
    }
    public void serverListen(){
        while(true){
            try {
                clientSocket = server.accept();
                int i = 0;
                for(i = 0; i < maxClientsCount; i++) {
                    if(threads[i] == null){
                        threads[i] = new ClientThread(clientSocket,threads);
                        threads[i].start();
                        break;
                    }

                }
                if(i == maxClientsCount){
                    PrintStream clientOutput =new PrintStream(clientSocket.getOutputStream());
                    clientOutput.println("Sever bussy!");
                    clientOutput.close();
                    clientSocket.close();
                }
            }catch(IOException e) {

            }
        }
    }
}

//end


