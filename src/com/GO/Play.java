package com.GO;

/**
 * Kinga Krata 2016-12-05.
 */
public class Play {

    private Board playBoard;
    private Client clientSocket;
    Play()
    {
         playBoard=new Board(19);
         clientSocket=new Client();
         clientSocket.listenSocket();

         clientSocket.out.println("aaa");



    }
}
