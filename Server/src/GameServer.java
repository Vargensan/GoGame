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
        System.out.println("Waiting for connections!");
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

class ClientThread extends Thread{

    boolean bussy = false;
    boolean activegame;
    int numberofthread_active;
    private Socket clientSocket = null;
    private final ClientThread[] threads;
    private int maxClientsCount;
    GamePlay game;
    /*
    Add implementation to 1-1 connection between clients
    Dunno, on same port or what
     */
    public ClientThread(Socket clientSocket, ClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }
    public void run(){
        int maxClientsCount = this.maxClientsCount;
        ClientThread[] threads = this.threads;
            /*Opis
            Dla każdego wątku sprawdź czy posiada nie pustą grę
            jeżeli taką posiada, sprawdź czy jest dostępna
            jeżeli jest dostępna to sprawdź liczbę graczy, jeżeli
            liczba graczy to jeden, to dodaj wątek do tej gry;
             */
        synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if(threads[i]!=null){
                    //if (!threads[i].bussy) {
                    if (threads[i].game != null) {
                        if (threads[i].game.getGameStatus() == false) {
                            if (threads[i].game.getNumberofPlayer() == false) {
                                activegame = true;
                                numberofthread_active = i;
                                System.out.println("Found active game on thread: "+i);
                            }
                        }
                    }
                }
            }

            if (activegame) {
                System.out.println("I am coming to game of thread: "+numberofthread_active);
                threads[numberofthread_active].game.setClient(clientSocket);
            } else {
                System.out.println("I am hosting game...");
                game = new GamePlay(threads);
                bussy = true;
                game.setClient(clientSocket);
            }
        } //synchronized
    } // run
} //end

class GamePlay{
    //Tell us if game was started
    private static final Random randnum = new Random(123456789L);
    private boolean gamestarted = false;
    //Tell us if game is full
    private boolean gamefull = false;
    private final ClientThread[] threads;
    String line = "";
    int i;
    int x,y;
    int blackplayer_int_checker;
    boolean whosturn;
    String whoisblack;
    String color[] = new String [2];
    BufferedReader clientInput1;
    BufferedReader clientInput2;
    PrintWriter clientOutput1;
    PrintWriter clientOutput2;
    Socket Client1;
    Socket Client2;

    /*

     */
    public GamePlay(ClientThread[] threads){
        this.threads = threads;
    }
    //Add Client To Game
    public void setClient(Socket Client){
        if(Client1 == null)
            this.Client1 = Client;
        else if(Client2 == null)
            this.Client2 = Client;
        updateStateofGame();
    }

    private void initializeGame(){
        try {
            clientInput1 = new BufferedReader(new InputStreamReader(Client1.getInputStream()));
            clientInput2 = new BufferedReader(new InputStreamReader(Client2.getInputStream()));
            clientOutput1 = new PrintWriter(Client1.getOutputStream(), true);
            clientOutput2 = new PrintWriter(Client2.getOutputStream(), true);
            //Implementacja wylosowania gracza co do koloru
            i = 1 + randnum.nextInt(1);
            System.out.println("Wartość i: "+i);
            if(i == 1){
                color[0] = "b";
                color[1] = "w";
            }else {
                color[0] = "w";
                color[1] = "b";
            }
            //nie działa poprawnie wstawianie kiedy wylosuje coś innego niż w poprzedniej
            //"stałej" implementacji
            setGameAsStarted();
            sendStartValues(color);
            startGame();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendStartValues(String[] color) {
        try {
            clientOutput1.println(color[0]);
            if(color[0].equals("b"))
                clientOutput1.println("a");
            else
                clientOutput1.println("u");
            //Tutaj wysyłana jest od klienta wiadomość "OK"
            clientInput1.readLine();

            System.out.println("Checker: "+color[0]);
            if(color[0].equals("b")) {
                whoisblack = "Klient Pierwszy jest czarny!";
                blackplayer_int_checker = 1;
            }
            else {
                whoisblack = "Klient Drugi jest czarny!";
                blackplayer_int_checker = 2;
            }


            System.out.println("Send values to client 1");

            clientOutput2.println(color[1]);
            if(color[1].equals("b"))
                clientOutput2.println("a");
            else
                clientOutput2.println("u");
            //Tutaj wysyłana jest od klienta wiadomość "OK"
            clientInput2.readLine();

            System.out.println("Send values to client 2");
            x=-1;
            y=-1;

            System.out.println(whoisblack);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    /*
    Dobra skrótowa implementacja play.game ma przekazywać punkt do gracza przeciwnego
    1) On sam ma sobie go rysować
    2) Ma zmienić ture active/unactive
    Czyli potrzebuje 3 sygnałów

    Pierwszy do gracza przeciwnego -> narysuj
    Drugi do gracza przeciwnego -> zmien stan
    Trzeci do siebie -> zmien stan

    Okej!
     */
    private void startGame(){
        while (true){
            if(line != null) {

                if (blackplayer_int_checker == 1) {
                    readPlayerFirst();
                    readPlayerSecound();

                } else {
                    readPlayerSecound();
                    readPlayerFirst();

                }
            }
        }
    }

    private void readPlayerFirst(){
        try {

            line = clientInput1.readLine();
            if (line.substring(0, 1).equals("p")) {
                sendBoardMove(clientInput1, clientOutput2);
                sendChangeTurn("u", clientOutput1);
            }

        }catch(IOException e){
            System.out.println("Read Failed");
            e.printStackTrace();
            System.exit(1);
        }
    }
    private void readPlayerSecound(){
        try {
            line = clientInput2.readLine();
            if (line.substring(0, 1).equals("p")) {
                sendBoardMove(clientInput2, clientOutput1);
                sendChangeTurn("u", clientOutput2);
            }
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void sendChangeTurn(String a, PrintWriter cout){
        cout.println(a);
    }
    private void sendBoardMove(BufferedReader cin, PrintWriter cout){
        try{
            x=Integer.parseInt(cin.readLine().substring(1));
            y=Integer.parseInt(cin.readLine().substring(1));

            cout.println("p");
            cout.println("x"+x);
            cout.println("y"+y);
            //Last line for changing turn
            cout.println("a");

        }catch(IOException e){
            System.out.println("Read Failed");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void changeTurn(String change){

    }

    /**
     * Method which sets a game as started
     */
    private void setGameAsStarted(){
        gamestarted = true;
    }

    /**
     * Method that updates state of game
     */
    private void updateStateofGame(){
        if(Client1 == null || Client2 == null){
            gamefull = false;
        } else{
            gamefull = true;
        }
        if(gamestarted == false){
            if(gamefull){
                initializeGame();
            } else{
                System.out.println("Game is not full, and will not start!");
            }
        }
    }
    /**
     * Getter for gamestarted condition
     */
    boolean getGameStatus(){
        return gamestarted;
    }

    /**
     * Getter for condition that checks if game is full
     * @return
     */
    boolean getNumberofPlayer(){
        return gamefull;
    }

}
