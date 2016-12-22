package com.GO;

import GUIGo.BoardOnClickListener;
import GUIGo.ClientGUI;
import GUIGo.DrawingBoard;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;

import static java.lang.System.exit;
import static java.lang.System.lineSeparator;

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
        String color="";
        String turn ="";
        boolean active=false;

         clientSocket=new Client();
         clientSocket.listenSocket();

        playBoard=new Board(19,this);

        window=new ClientGUI(playBoard,this);
        this.clickListener=window.getDrawingBoard().getBoardOnClickListener();

         try {
              color=clientSocket.in.readLine();
              turn=clientSocket.in.readLine();
             if(turn.substring(0,1).equals("u"))
                 active = false;
             else
                 active = true;

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
        window.getDrawingBoard().setterMouseListener(active);
        window.setTurn(active);

         if(player_color==PLAYER.WHITE)
         {
             recivefromOther();
         }


    }


    public void changeplayer(String abc){
        clientSocket.out.println(abc);

        try{
            if(clientSocket.in.readLine().substring(0,1).equals("p")){

           }
        }catch(IOException e){
            System.out.println("Error");
        }
    }

    /*
    abc -> zrobimy z tego opcję
    i-> insert czyli wstawienie
    Dobra czarny wykonuje ruch -> wysyła a później nasłuchuje
    W tym czasie biały musi być cały czas w stanie nasłuchiwania -> przy inicjalizacji damy go do tego stanu
    jak już wysłucha co ma do powiedzenia serwer, to będzie wiedział że ma ruch
    Wykona ruch czyli zrobi senda() a później znowu będzie nasłuchiwał, czyli wszystko git!
     */
    public void game(int x, int y)
    {
        //This send
        sendToOther(x,y);
        //change your state
        reciveTurn();
        //And wait for the answer from other player
        recivefromOther();
    }
    private void sendToOther(int x, int y){

        clientSocket.out.println("p");
        clientSocket.out.println("x"+x);
        clientSocket.out.println("y"+y);

    }
    private void reciveTurn(){
        String turn = "";
        try{
            turn = clientSocket.in.readLine();
            //System.out.println("I have recived: "+ turn);
            turn.substring(0,1);
            setTurn(turn);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void setTurn(String turn){
        System.out.println("I change your status to "+ turn);
        if(turn.equals("a")){
            System.out.print("A ja będę twym aniołem, będę gwiazdą na twym niebie");
            window.setTurn(true);
            window.getDrawingBoard().setterMouseListener(true);
        }else {
            System.out.println("let's set my turn to false");
            window.setTurn(false);
            window.getDrawingBoard().setterMouseListener(false);
        }
    }

    private void recivefromOther(){
        String line = "";
        String turn = "";
        int x = -1;
        int y = -1;
        try {
            //Otrzymaj teskt
            line = clientSocket.in.readLine();
            //Utnij go do 1 znaku
            line.substring(0,1);

            //Jeśli to "p"
            if(line.equals("p")){
                x=Integer.parseInt(clientSocket.in.readLine().substring(1));
                System.out.println(x);
                y=Integer.parseInt(clientSocket.in.readLine().substring(1));
                System.out.println(y);
                turn = clientSocket.in.readLine().substring(0,1);
            }
            //Tego nie rozumiem, nie lepiej wyslac, odebrac u innego klienta i zmienic u innego kliena, i dac mu ture?
            //Możliwe Bugi
            if(playBoard.canAddHere(player_color.getEnemyColor(),x,y)){
                playBoard.addStone(player_color.getEnemyColor(),x,y);
            }
            window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
            setTurn(turn);

        }catch(IOException e){

        }
    }

    public void informTurnChange(){

    }
    /**
     * If KO situation is detected it prints it to the client
     */
    public void informKO(){
        window.getDrawingBoard().set_KO_Points(playBoard.getKO_Points());
        window.getDrawingBoard().set_KO_Situation(playBoard.getKO_Status());
        if(playBoard.getKO_Status()){
            window.showWarningKO_Situation();
        }
    }
    public void giveWarningMessage(){
        window.WarnningMessage();
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
    public PLAYER get_player_color() {return player_color;}




}
