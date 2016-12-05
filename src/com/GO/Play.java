package com.GO;

import GUIGo.ClientGUI;

/**
 * Kinga Krata 2016-12-05.
 */
public class Play {

    private Board playBoard;
    private Client clientSocket;
    private ClientGUI window;
    Play()
    {
         playBoard=new Board(19);

         clientSocket=new Client();
         clientSocket.listenSocket();

         window=new ClientGUI();

         window.startDrawing(playBoard.getGameTable());


         clientSocket.out.println("aaa");



    }
}
