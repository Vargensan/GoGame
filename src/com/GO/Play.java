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
            player_color=PLAYER.BLACK;
        }
        else
        {
            player_color=PLAYER.WHITE;
        }
        clientSocket.out.println("ok");
        playBoard=new Board(19,this);

         window=new ClientGUI(playBoard,this);
         this.clickListener=window.getDrawingBoard().getBoardOnClickListener();
         if(player_color==PLAYER.BLACK)
         {
             startBlackPlayer();
         }


    }
    public void startBlackPlayer()
    {
        int x=0,y=0;


        try {
            if(clientSocket.in.readLine().substring(0,1).equals("p"))
            {
                x=Integer.parseInt(clientSocket.in.readLine().substring(1));
                y=Integer.parseInt(clientSocket.in.readLine().substring(1));
            }

        }
        catch(NumberFormatException ex)
        {

        }
        catch(IOException ex)
        {

        }
        playBoard.addStone(player_color.getEnemyColor(),x,y);
        window.getDrawingBoard().update();

    }

    public void game(int x, int y)
    {
         clientSocket.out.println("p");

        clientSocket.out.println("x"+x);
        clientSocket.out.println("y"+y);


            try {
                if(clientSocket.in.readLine().substring(0,1).equals("p"))
                {
                    x=Integer.parseInt(clientSocket.in.readLine().substring(1));
                    System.out.println(x);
                    y=Integer.parseInt(clientSocket.in.readLine().substring(1));
                    System.out.println(y);
                }

            }
            catch(NumberFormatException ex)
            {

            }
            catch(IOException ex)
            {

            }
            playBoard.addStone(player_color.getEnemyColor(),x,y);
            window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());



    }

    /**
     * Method that is responsiable for getting play board
     * @return play board
     */
    public Board getPlayBoard()
    {
        return playBoard;
    }

    /**
     * Getter for player color
     * @return color of player
     */
    public PLAYER get_player_color()
    {
        return player_color;
    }




}
