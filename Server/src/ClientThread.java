import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientThread extends Thread
{

    private boolean busy = false;
    private boolean activeGame;
    private int numberofthread_active;
    private int sizeOfGame;
    private Socket clientSocket = null;
    private final ClientThread[] threads;
    private int maxClientCount;
    private GamePlay game;
    PrintWriter clientOutput;
    BufferedReader clientInput;

    /*
    Add implementation to 1-1 connection between clients
    Dunno, on same port or what
     */

    public ClientThread(Socket clientSocket, ClientThread[] threads)
    {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientCount = threads.length;
    }


    public void run()
    {
        String size  = "";
        int maxClientCount = this.maxClientCount;
        ClientThread[] threads = this.threads;
        try {
            clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);
            size = clientInput.readLine();
            sizeOfGame = Integer.parseInt(size);

        }catch(IOException e){
            e.printStackTrace();
        }
            /*Opis
            Dla każdego wątku sprawdź czy posiada nie pustą grę
            jeżeli taką posiada, sprawdź czy jest dostępna
            jeżeli jest dostępna to sprawdź liczbę graczy, jeżeli
            liczba graczy to jeden, to dodaj wątek do tej gry;
             */

        synchronized (this)
        {
            for (int i = 0; i < maxClientCount; i++)
            {
                if (threads[i] != null)
                {
                    //if (!threads[i].busy) {
                    if (threads[i].game != null)
                    {
                        if (threads[i].game.getGameStatus() == false)
                        {
                            if (threads[i].game.getNumberOfPlayers() == false)
                            {
                                if(sizeOfGame == threads[i].game.preferedSize) {
                                    activeGame = true;
                                    numberofthread_active = i;
                                }
                            }
                        }
                    }
                }
            }

            if (activeGame)
            {

                //-----------TRY-CATCH FOR SUPPRESSING EXCEPTION PRINT-----------//
                try
                {

                    //  System.out.println("I am coming to game of thread: "+numberofthread_active);
                    threads[numberofthread_active].game.setClient(clientSocket);
                } catch (Exception e) {
                    threads[numberofthread_active].game.killGame();
                }

            } else
            {
                //-----------TRY-CATCH FOR SUPPRESSING EXCEPTION PRINT-----------//
                try
                {
                    game = new GamePlay(threads);
                    busy = true;
                    game.setClient(clientSocket);
                    game.setGameBoardSize(sizeOfGame);
                } catch (Exception e)
                {
                    game.killGame();
                }
            }
        }
    } //synchronized
} // run

