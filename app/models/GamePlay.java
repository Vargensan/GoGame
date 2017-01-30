package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

//Wątki: Client(Wątek Untyped Actor) -> Listener Thread (Akki IO)
//Server: Tworzy nowe wątki które zawierają gry
//Główny Wątek server, tworzy dzieci
class GamePlay
{
    //Tell us if game was started
    private static final Random randnum = new Random();
    private boolean gamestarted = false;
    //Tell us if game is full
    private boolean gamefull = false;
    private final ClientThread[] threads;
    String line = "";
    int i;
    int x, y;
    //holds size of gameboard
    int preferedSize;
    int blackplayer_int_checker;
    boolean whosturn;
    int stateOfGamePlay;
    String whoisblack;
    String color[] = new String[2];
    BufferedReader clientInput1;
    BufferedReader clientInput2;
    PrintWriter clientOutput1;
    PrintWriter clientOutput2;
    Socket Client1;
    Socket Client2;
    private int threadNumber;

    /**
     * Constructor, initialize game
     * @param threads takes list of threads
     * @param threadNumber takes a current thread Number
     */
    public GamePlay(ClientThread[] threads,int threadNumber)
    {
        this.threads = threads;
        this.threadNumber=threadNumber;
        randnum.setSeed(System.currentTimeMillis());
    }

    //Add Client To Game

    /**
     * Method responsiable for Setting Client to this game
     * @param Client takes client Socket, to connect it with other player
     * @throws IOException unexpected behavior
     */
    public void setClient(Socket Client) throws IOException
    {
        if (Client1 == null)
            this.Client1 = Client;
        else if (Client2 == null)
            this.Client2 = Client;
        updateStateOfGame();
    }

    private void initializeGame() throws IOException
    {
        clientInput1 = new BufferedReader(new InputStreamReader(Client1.getInputStream()));
        clientInput2 = new BufferedReader(new InputStreamReader(Client2.getInputStream()));
        clientOutput1 = new PrintWriter(Client1.getOutputStream(), true);
        clientOutput2 = new PrintWriter(Client2.getOutputStream(), true);
        //Implementacja wylosowania gracza co do koloru
        i = randnum.nextInt(2);
        // System.out.println("Wartość i: "+i);
        if (i == 1)
        {
            color[0] = "b";
            color[1] = "w";
        } else
        {
            color[0] = "w";
            color[1] = "b";
        }
        //nie działa poprawnie wstawianie kiedy wylosuje coś innego niż w poprzedniej
        //"stałej" implementacji
        setGameAsStarted();
        sendStartValues(color);
        startGame();
    }

    private void sendStartValues(String[] color)
    {
        try
        {
            clientOutput1.println(color[0]);
            if (color[0].equals("b"))
                clientOutput1.println("a");
            else
                clientOutput1.println("u");
            //Tutaj wysyłana jest od klienta wiadomość "OK"
            clientInput1.readLine();

            // System.out.println("Checker: "+color[0]);
            if (color[0].equals("b"))
            {
                whoisblack = "Klient Pierwszy jest czarny!";
                blackplayer_int_checker = 1;
            } else
            {
                whoisblack = "Klient Drugi jest czarny!";
                blackplayer_int_checker = 2;
            }


            // System.out.println("Send values to client 1");

            clientOutput2.println(color[1]);
            if (color[1].equals("b"))
                clientOutput2.println("a");
            else
                clientOutput2.println("u");
            //Tutaj wysyłana jest od klienta wiadomość "OK"
            clientInput2.readLine();

            //System.out.println("Send values to client 2");
            x = -1;
            y = -1;

            //  System.out.println(whoisblack);
        } catch (IOException e)
        {
        }
    }


    /*
    Dobra ( Chyba jedyna dobra...) skrótowa implementacja play.game ma przekazywać punkt do gracza przeciwnego
    1) On sam ma sobie go rysować
    2) Ma zmienić ture active/unactive
    Czyli potrzebuje 3 sygnałów

    Pierwszy do gracza przeciwnego -> narysuj
    Drugi do gracza przeciwnego -> zmien stan
    Trzeci do siebie -> zmien stan

    Okej!
     */

    private void startGame() throws IOException
    {
        while (true)
        {
            if (line != null)
            {

                if (blackplayer_int_checker == 1)
                {
                    readPlayerFirst();
                    readPlayerSecond();

                } else
                {
                    readPlayerSecond();
                    readPlayerFirst();

                }
            }
        }
    }

    private void readPlayerFirst() throws IOException
    {
        line = clientInput1.readLine();
        if (line.equals("play"))
        {
            stateOfGamePlay=0;
            System.out.println("play");
            sendBoardMove(clientInput1, clientOutput2);
            sendChangeTurn("u", clientOutput1);
        } else if (line.equals("pass"))
        {
            sendChangeTurn("u", clientOutput1);
            if(stateOfGamePlay==1){
                clientOutput2.println("double-pass");
                clientOutput2.println("a");
                clientOutput1.println("double-pass");
                clientOutput1.println("u");
            }else {
                stateOfGamePlay = 1;
                clientOutput2.println("pass");
                clientOutput2.println("a");
            }
        } else if (line.equals("dead"))
        {
            clientOutput2.println("dead");

            for (int i = 0; i < preferedSize; ++i)
            {
                for (int j = 0; j < preferedSize; ++j)
                {
                    line = clientInput1.readLine().substring(0, 1);
                    if (Integer.parseInt(line) == 1)
                        clientOutput2.println(1);
                    else
                        clientOutput2.println(0);

                }
                // System.out.println();

            }
            clientOutput2.println("a");

            sendChangeTurn("u", clientOutput1);
        } else if(line.equals("terr-change")){
            clientOutput2.println("terr-change");
            sendChangeTurn("a", clientOutput2);
            clientOutput1.println("terr-change");
            sendChangeTurn("u", clientOutput1);
        } else if(line.equals("territory")){

            clientOutput2.println("territory");

            for(int i = 0; i < preferedSize; i++){
                for(int j = 0 ; j < preferedSize; j++){
                    line = clientInput1.readLine().substring(0,1);
                    if(Integer.parseInt(line) == 1)
                        clientOutput2.println(1);
                    else if(Integer.parseInt(line) == 0)
                        clientOutput2.println(0);
                    else
                        clientOutput2.println(2);
                }
            }
            clientOutput2.println("a");
            sendChangeTurn("u",clientOutput1);
        } else if(line.equals("end")){

            clientOutput2.println("end");
            killGame();
        } else if(line.equals("giveup")){
            clientOutput2.println("win");
            clientOutput1.println("lose");
        }

    }

    private void readPlayerSecond() throws IOException
    {
        line = clientInput2.readLine();
        if (line.equals("play"))
        {
            stateOfGamePlay=0;
            System.out.println("play");
            sendBoardMove(clientInput2, clientOutput1);
            sendChangeTurn("u", clientOutput2);
        } else if (line.equals("pass"))
        {
            //System.out.println("pass");
            /*
            zmień swój stan na unactive:
            -Wyślij changeTurn to siebie
            zmień stan przeciwnika na active
            -jestem w stanie słuchania -> wyślij do mnie pass + change turn
             */
            sendChangeTurn("u", clientOutput2);
            if(stateOfGamePlay==1){
                //Implementacja sygnału double pass
                clientOutput1.println("double-pass");
                clientOutput1.println("a");
                clientOutput2.println("double-pass");
                clientOutput2.println("u");
            }else {
                stateOfGamePlay=1;
                clientOutput1.println("pass");
                clientOutput1.println("a");
            }
        } else if (line.equals("dead"))
        {
            clientOutput1.println("dead");

            for (int i = 0; i < preferedSize; ++i)
            {
                for (int j = 0; j < preferedSize; ++j)
                {
                    line = clientInput2.readLine().substring(0, 1);
                    if (Integer.parseInt(line) == 1)
                        clientOutput1.println(1);
                    else
                        clientOutput1.println(0);

                }
                // System.out.println();

            }
            clientOutput1.println("a");

            sendChangeTurn("u", clientOutput2);


        } else if(line.equals("terr-change")){
            clientOutput1.println("terr-change");
            sendChangeTurn("a", clientOutput1);
            clientOutput2.println("terr-change");
            sendChangeTurn("u", clientOutput2);
        } else if(line.equals("territory")){

            clientOutput1.println("territory");

            for(int i = 0; i < preferedSize; i++){
                for(int j = 0 ; j < preferedSize; j++){
                    line = clientInput2.readLine().substring(0,1);
                    if(Integer.parseInt(line) == 1)
                        clientOutput1.println(1);
                    else if(Integer.parseInt(line) == 0)
                        clientOutput1.println(0);
                    else
                        clientOutput1.println(2);
                }
            }
            clientOutput1.println("a");
            sendChangeTurn("u",clientOutput2);
        } else if(line.equals("end")){

            clientOutput1.println("end");
            killGame();
        }else if(line.equals("giveup")){
            clientOutput2.println("lose");
            clientOutput1.println("win");
        }
    }

    private void sendChangeTurn(String a, PrintWriter cout)
    {
        cout.println(a);
    }

    private void sendBoardMove(BufferedReader cin, PrintWriter cout) throws IOException
    {

        x = Integer.parseInt(cin.readLine().substring(1));
        y = Integer.parseInt(cin.readLine().substring(1));

        cout.println("play");
        cout.println("x" + x);
        cout.println("y" + y);
        //Last line for changing turn
        cout.println("a");


    }

    /**
     * Method which sets a game as started
     */
    private void setGameAsStarted()
    {
        gamestarted = true;
    }

    /**
     * Method that updates state of game
     */
    private void updateStateOfGame() throws IOException
    {
        if (Client1 == null || Client2 == null)
        {
            gamefull = false;
        } else
        {
            gamefull = true;
        }
        if (gamestarted == false)
        {
            if (gamefull)
            {
                initializeGame();
            } else
            {
                System.out.println("Game is not full, and will not start!");
            }
        }
    }


    boolean getGameStatus()
    {
        return gamestarted;
    }

    boolean getNumberOfPlayers()
    {
        return gamefull;
    }

    public void setGameBoardSize(int size){
        this.preferedSize = size;
    }

    public void killGame()
    {
        try
        {
            this.Client1.close();
            this.Client2.close();
            threads[threadNumber]=null;

        } catch (Exception e)
        {
        }
    }
}