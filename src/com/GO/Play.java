package com.GO;

import GUIGo.ClientGUI;

import java.io.IOException;

/**
 * Kinga Krata 2016-12-05.
 */
public class Play {

    private Board playBoard;
    private Client clientSocket;
    private ClientGUI window;
    private PLAYER player_color;
    Play()
    {
         player_color=PLAYER.BLACK;
         playBoard=new Board(19,this);

         clientSocket=new Client();
         clientSocket.listenSocket();

         window=new ClientGUI(playBoard.getGameTable());

    }
    public PLAYER get_player_color()
    {
        return player_color;
    }

    public void move_done()
    {
        clientSocket.out.println(playBoard);
        try {
            if (clientSocket.in.readLine() == "OK") ;
        }
        catch(IOException ex)
        {

        }
    }

}
