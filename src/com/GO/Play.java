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
    private STATE playState;
    

    Play()
    {
        playState=STATE.BEFORE_GAME;

        playBoard=new Board(19,this);

        window=new ClientGUI(playBoard,this);
        this.clickListener=window.getDrawingBoard().getBoardOnClickListener();



    }
    public void inicializeGameWithServer()
    {
        playState=STATE.GAME;
        String color="";
        String turn ="";
        boolean active=false;

        clientSocket=new Client();
        clientSocket.listenSocket();

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
        if(color.equals("b"))
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
    public STATE getPlayState()
    {
        return playState;
    }
    public void setPlayState(STATE state){
        playState=state;
    }
    public void sendDeadGroups()
    {
        clientSocket.out.println("dead");
        boolean help[][]=playBoard.getDeadTable();
        for(int i=0;i<19;++i){
            for(int j=0;j<19;++j){
                clientSocket.out.println(help[i][j]);
            }

        }

        try {
           setTurn(clientSocket.in.readLine());
        }catch(IOException ex){

        }
    }


    public void changeplayer(String abc){
        clientSocket.out.println(abc);

        try{
            if(clientSocket.in.readLine().equals("play")){

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
    public void passGame() {
        clientSocket.out.println("pass");
        //System.out.println("pass");
        try {
            setTurn(clientSocket.in.readLine());
        }catch(IOException ex)
        {

        }
        String line;
        try {
            if((line=clientSocket.in.readLine()).equals("pass"))
                System.out.println("Game ended");
                else
                System.out.println(line);
        }
        catch(IOException ex)
        {
            System.out.println("problem z polaczeniem");
        }
    }

    private void sendToOther(int x, int y){

        clientSocket.out.println("play");
        clientSocket.out.println("x"+x);
        clientSocket.out.println("y"+y);

    }
    private void reciveTurn(){
        String turn = "";
        try{
            turn = clientSocket.in.readLine();
            //System.out.println("I have recived: "+ turn);
            setTurn(turn);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void setTurn(String turn){
      //  System.out.println("I change your status to "+ turn);
        if(turn.equals("a")){
        //    System.out.print("A ja będę twym aniołem, będę gwiazdą na twym niebie");
            window.setTurn(true);
            window.getDrawingBoard().setterMouseListener(true);
        }else {
         //   System.out.println("let's set my turn to false");
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


            //Jeśli to "play"
            if(line.equals("play")){
                x=Integer.parseInt(clientSocket.in.readLine().substring(1));
                //System.out.println(x);
                y=Integer.parseInt(clientSocket.in.readLine().substring(1));
               // System.out.println(y);

                turn = clientSocket.in.readLine();
                if(playBoard.canAddHere(player_color.getEnemyColor(),x,y)){
                    playBoard.addStone(player_color.getEnemyColor(),x,y);
                }
                window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
            }else if(line.equals("pass")){
                turn = clientSocket.in.readLine();

            }
            else if(line.equals("dead"))
            {
                setPlayState(STATE.ADD_DEAD_GROUPS);
                turn = clientSocket.in.readLine();
                for(int i=0;i<19;++i){
                    for(int j=0;j<19;++j){
                        line=clientSocket.in.readLine();
                        playBoard.setDeadTable(i,j,Boolean.parseBoolean(line));
                    }

                }
            }

            //Tego nie rozumiem, nie lepiej wyslac, odebrac u innego klienta i zmienic u innego kliena, i dac mu ture?
            //Możliwe Bugi

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
