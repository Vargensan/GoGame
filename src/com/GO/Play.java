package com.GO;

import GUIGo.BoardOnClickListener;
import GUIGo.ClientGUI;
import GUIGo.DrawingBoard;

import java.io.IOException;

/**
 * Kinga Krata 2016-12-05.
 */
public class Play {

    private Board playBoard;
    private Client clientSocket;
    private ClientGUI window;
    private PLAYER player_color;
    private DrawingBoard clickListener;
    

    Play()
    {
         player_color=PLAYER.BLACK;
         playBoard=new Board(19,this);

         clientSocket=new Client();
         clientSocket.listenSocket();

         window=new ClientGUI(playBoard.getGameTable());
         this.clickListener=window.getDrawingBoard();

    }
    public Board getPlayBoard()
    {
        return playBoard;
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
