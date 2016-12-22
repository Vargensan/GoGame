import java.net.Socket;

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
                               // System.out.println("Found active game on thread: "+i);
                            }
                        }
                    }
                }
            }

            if (activegame) {
              //  System.out.println("I am coming to game of thread: "+numberofthread_active);
                threads[numberofthread_active].game.setClient(clientSocket);
            } else {
                //System.out.println("I am hosting game...");
                game = new GamePlay(threads);
                bussy = true;
                game.setClient(clientSocket);
            }
        } //synchronized
    } // run
}
