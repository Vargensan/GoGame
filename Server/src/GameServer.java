import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Kinga Krata 2016-12-05.
 */

public class GameServer {

    //-----------STARTS SERVER ON PORT 4444-----------//
    private static int PORT=4444;
    private static GameServer gameServer;

    //-----------STARTS SERVER ON PORT 4444-----------//
    private final static int maxClientCount = 20;

    private ServerSocket server;
    private Socket clientSocket;


    private static final ClientThread[] threads = new ClientThread[maxClientCount];

    /**
     * Constructor, invokes start of server socket

     */

    private GameServer(){
        startServer();
        serverListen();
    }
    public static GameServer getGameServer(){
        if(gameServer==null){
            gameServer=new GameServer();
        }
        return gameServer;
    }

    /**
     * Method responsible for setting Server Socket, initialize
     * it's port etc
     */
    public void startServer(){
        try{
            server = new ServerSocket(PORT);
        }
        catch(IOException e){

        }

        System.out.println("Waiting for connections!");

    }

    /**
     * Method responsible for accepting connections from clients
     * creating for them thread so they can connect to other players
     * and start play a game
     */
    public void serverListen(){
        while(true){
            try {
                clientSocket = server.accept();
                int i = 0;
                for(i = 0; i < maxClientCount; i++) {
                    if(threads[i] == null){
                        threads[i] = new ClientThread(clientSocket,threads,i);
                        threads[i].start();
                        break;
                    }

                }
                if(i == maxClientCount){
                    PrintStream clientOutput =new PrintStream(clientSocket.getOutputStream());
                    clientOutput.println("Sever busy!");
                    clientOutput.close();
                    clientSocket.close();
                }
            }catch(IOException e) {

            }
        }
    }
}

//-----------EOF-----------//


