package com.GO;

import GUIGo.BoardOnClickListener;
import GUIGo.ClientGUI;
import GUIGo.DrawingBoard;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

    private int hasGameEnded;
    private boolean accept_status;
    private int giveUpstatus;
    

    Play()
    {
        hasGameEnded = 0;
        giveUpstatus = 0;
        playState=STATE.BEFORE_GAME;

        //playBoard=new Board(5,this);
        //Gracz musi wybrac plansze z posrod 19x19,9x9,7x7
        //Dlatego utworzenie ClientaGUI, i pobranie wartości
        window=new ClientGUI();
        window.initialize(this);
        requestSizeConfirmation();
        //a pozniej utworzenie Board
        playBoard = new Board(window.getSizeOfPlayBoard(),this);
        //I inicjacja całej reszty
        //window.initialize(this);
        window.createDrawingBoard(playBoard);
        this.clickListener=window.getDrawingBoard().getBoardOnClickListener();



    }
    public void requestSizeConfirmation(){
        while(!window.getConfirmationofSize()){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int isGiveUpstatus(){
        return giveUpstatus;
    }
    public void setGiveUpstatus(){
        clientSocket.out.println("giveup");
        recivefromOther();
    }


    public boolean getAcceptStatus(){
        return accept_status;
    }
    public void setAccept_status(String turn){
        if(turn.equals("a")){
            accept_status=false;
        }
        else if(turn.equals("u")){
            accept_status=true;
        }
    }
    public void setConfirmation(){
        window.confirmSize();
    }
    public boolean getConfirmation(){
        return window.getConfirmationofSize();
    }
    public void inicializeGameWithServer()
    {
        System.out.println("I went here!");
        playState=STATE.GAME;
        String color="";
        String turn ="";
        boolean active=false;

        clientSocket=new Client();
        clientSocket.listenSocket();
        clientSocket.out.println(window.getSizeOfPlayBoard());

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
            window.setJPlayerColor("Player Black");

        }
        else
        {
            player_color=PLAYER.WHITE;
            window.setJPlayerColor("Player White");

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
        for(int i=0;i<window.getSizeOfPlayBoard();++i){
            for(int j=0;j<window.getSizeOfPlayBoard();++j){
                if(help[i][j]==true)
                    clientSocket.out.println(1);
                else
                    clientSocket.out.println(0);

                if(help[i][j]==true)
                    System.out.print(help[i][j]);
                else
                    System.out.print("F\t");

            }
            System.out.println();
        }
        reciveTurn();
        recivefromOther();
        //try {
        //   setTurn(clientSocket.in.readLine());
        //}catch(IOException ex){

        //}
    }

    //0-white
    //1-black
    public void sendTerritory(){
        clientSocket.out.println("territory");
        PLACE help[][]=playBoard.getTerritoryTable();
        for(int i=0;i<window.getSizeOfPlayBoard();i++){
            for(int j=0;j<window.getSizeOfPlayBoard();j++){
                if(help[i][j].equals(PLACE.BLACK))
                    clientSocket.out.println(1);
                else if(help[i][j].equals(PLACE.WHITE))
                    clientSocket.out.println(0);
                else
                    clientSocket.out.println(2);

                //if(help[i][j].equals(PLACE.WHITE))
                //    System.out.print(help[i][j]);
                //else
                //    System.out.print("F\t");

            }
            System.out.println();
        }
        reciveTurn();
        recivefromOther();
    }

    public void changeToTerritory(){
        clientSocket.out.println("terr-change");
        recivefromOther();
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

    public void endGame(){
        clientSocket.out.println("end");
        setPlayState(STATE.END_GAME);
        playBoard.calculateTerritory();
        window.setTurnEnd();

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
        //hasGameEnded = 1;
        clientSocket.out.println("pass");
        //System.out.println("pass");
        //Tutaj ok, zmieniamy turę
        reciveTurn();
        //Tutaj możemy zrobić opcję nasłuchiwania na double-pass
        recivefromOther(); //Musimy się przygotować na inną wiadomość niż pass...
        /*
        Opis Implementacji:

        Gracz pierwszy pasuje -> wysyła komunikat passGame(), czyli:
            1.zmienia swoją turę
            2.przechodzi w stan nasłuchiwania.
        Po stronie servera należy zaimplementować opcję ile razy padło pass
        Gracz drugi dostaje turę
            1.Inny komunikat niż pass? -> mamy normalny stan nasłuchiwania recivefromOther()
            na Graczu pierwszym
            2.Komunikat PassGame()? -> wysyłamy do Servera komunikat pass, ten nalicza countera,
            zmienia naszą turę, dodatkowo zostaje do nas wysłany komuniakt double-pass zarówno do nas
            jak i do gracza przeciwnego. Tury zostają normalnie ustawione, i działamy teraz na
            Mark As Territory
         */
        //String line;
       // try {
            //Tutaj nasłuchujemy na kolejną wiadomość, ale jeśli jest inna niż pass? Nie zadziała prawidłowo
       //     if((line=clientSocket.in.readLine()).equals("pass"))
       //         System.out.println("Game ended");
       //         else
       //         System.out.println(line);
       // }
       // catch(IOException ex)
       // {
       //     System.out.println("problem z polaczeniem");
       // }
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
        String dead_mess = "";
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
            else if(line.equals("dead")) //Tutaj można z Clienta GUI 'send'
            {

                setPlayState(STATE.ADD_DEAD_GROUPS);

                for(int i=0;i<window.getSizeOfPlayBoard();++i){
                    for(int j=0;j<window.getSizeOfPlayBoard();++j){
                        dead_mess=clientSocket.in.readLine().substring(0,1);
                        if(Integer.parseInt(dead_mess)==1)
                            playBoard.setDeadTable(i,j,true);
                        else
                            playBoard.setDeadTable(i,j,false);
                        if(Integer.parseInt(dead_mess)==1)
                            System.out.print(dead_mess);
                        else
                            System.out.print(dead_mess);
                    }
                    System.out.println();
                }
                turn = clientSocket.in.readLine();
                window.getDrawingBoard().changeState(this.getPlayState());
                window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());


            }
            else if(line.equals("double-pass")){
                System.out.println("I ve got message!");
                /*
                dodać booleana
                 */
                setPlayState(STATE.ADD_DEAD_GROUPS);
                window.showSendandAccept();
                window.showMarkAsDead();
                turn = clientSocket.in.readLine();
                setAccept_status(turn);

            }
            else if(line.equals("terr-change")){
                playBoard.deleteDeadFromGameTable();
                System.out.println("Changing Game to Territory");
                setPlayState(STATE.ADD_TERITORITY);
                window.hideMarkAsDead();
                window.showTerritoryMarking();
                turn = clientSocket.in.readLine();
                setAccept_status(turn);
                window.repaint();
                window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
            }
            else if(line.equals("territory")){
                setPlayState(STATE.ADD_TERITORITY);
                for(int i = 0; i < window.getSizeOfPlayBoard(); i++){
                    for(int j = 0; j < window.getSizeOfPlayBoard(); j++){
                        dead_mess = clientSocket.in.readLine().substring(0,1);
                        if(Integer.parseInt(dead_mess) == 1){
                            playBoard.markAsTerritory(PLAYER.BLACK,i,j);
                        }
                        else if(Integer.parseInt(dead_mess) == 0){
                            playBoard.markAsTerritory(PLAYER.WHITE,i,j);
                        } else{
                            playBoard.unMarkAsTerritory(i,j);
                        }
                    }
                }
                turn = clientSocket.in.readLine();
                window.getDrawingBoard().changeState(this.getPlayState());
                window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
            }
            else if(line.equals("end")){
                endGame();
                return;
            } else if(line.equals("win")){
                giveUpstatus=1;
                endGame();
                return;

            } else if(line.equals("lose")){
                giveUpstatus=2;
                endGame();
                return;
            }
            /*
            Gdzie był błąd, Gracz po otrzymaniu wiadomości Dead, nie nasłuchiwał dalej tj.
            1.Wysłał do gracza nasłuchującego
            2.Przeszedł w stan nasłuchiwania
            3.Otrzymał wiadomość, wypełnił ją -> narysował deadtable
            4.Po czym powiedział że już nie słucha, bo nie wchodzi do funckji recivefromOther()
             */

            //Tego nie rozumiem, nie lepiej wyslac, odebrac u innego klienta i zmienic u innego kliena, i dac mu ture?
            //Możliwe Bugi

            setTurn(turn);

            //Jeden z nich musi dalej nasłuchiwać po double passie...
            if(line.equals("double-pass") && turn.equals("u")){
                recivefromOther();
            }
            //Analogicznie dla wiadomości dead
            if(line.equals("dead") && turn.equals("u")){
                recivefromOther();
            }
            if(line.equals("terr-change") && turn.equals("u")){
                recivefromOther();
            }

        }catch(Exception e){
            System.exit(-1);
        }
    }

    public void setEnd(){
        setPlayState(STATE.END_GAME);
        playBoard.calculateTerritory();
        window.setTurnEnd();
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

    /**
     * Prints to the client, that the move which he has done is incorrect
     * @param option takes the option of incorrect move
     */
    public void informInvaildMove(int option){
        if(option==1)
            window.InvaildMove();
        else
            window.InvalidMove_isNotEmpty();
    }

    /**
     * Getter for a turn of player
     * @return actual state of player active/unactive
     */
    public boolean getTurn(){
        return window.getTurn();
    }

    /**
     * Repaints drawing board
     */
    public void turnRepaint(){
        //window.getDrawingBoard().repaint();
        window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
    }

    /**
     * Sends Warning Message to the Client
     */
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

    public DrawingBoard getDrawingBoard(){return window.getDrawingBoard();}

    /**
     * Getter for player color
     * @return color of player
     */
    public PLAYER get_player_color() {return player_color;}




}
