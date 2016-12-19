package com.GO;

import GUIGo.BoardOnClickListener;
import GUIGo.ClientGUI;
import GUIGo.DrawingBoard;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * Kinga Krata 2016-12-05.
 */
public class Play {

    private Board playBoard;
    private Client clientSocket;
    private ClientGUI window;
    private PLAYER player_color;
    private BoardOnClickListener clickListener;
    

    Play()
    {


         clientSocket=new Client();
         clientSocket.listenSocket();
         String color="";

         try {
              color=clientSocket.in.readLine();

         }
         catch(IOException ex)
         {
            System.out.println("Problem z polaczeniem");
            exit(-1);
         }
         if(color.substring(0,1).equals("b"))
        {
            System.out.println(color);
            player_color=PLAYER.BLACK;
        }
        else
        {
            player_color=PLAYER.WHITE;
        }
        clientSocket.out.println("ok");
        playBoard=new Board(19,this);

         window=new ClientGUI(playBoard);
         //this.clickListener=window.getDrawingBoard().getBoardOnClickListener();


    }
    public void game()
    {
        int x=-1,y=-1;


            try {
                if(clientSocket.in.readLine().substring(0,1)=="p")
                {
                    x=Integer.parseInt(clientSocket.in.readLine().substring(1,2));
                    y=Integer.parseInt(clientSocket.in.readLine().substring(2,3));
                }

            }
            catch(NumberFormatException ex)
            {

            }
            catch(IOException ex)
            {

            }
            playBoard.addStone(PLAYER.BLACK,x,y);
            clickListener.setIsClicable(true);


    }
    public Board getPlayBoard()
    {
        return playBoard;
    }
    public PLAYER get_player_color()
    {
        return player_color;
    }




}
