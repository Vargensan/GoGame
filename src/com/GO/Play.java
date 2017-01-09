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
    private ListenerThread worker;


    /**
     * Constructor
     * Set start values, initialize client GUI
     */
    public Play()
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
        initializeListener();



    }

    /**
     * Method that waits for client confirm of size of Drawing Board before invoking
     * Connection with the Server
     */
    public void requestSizeConfirmation(){
        while(!window.getConfirmationofSize()){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method which is responsible for initializing Listener Thread
     * which takes a messages from server
     */

    private void initializeListener(){
        worker = new ListenerThread(this);
        worker.start();
    }

    /**
     * Method which tells if player gived up / or not gived up
     * or there was any give up in game
     * @return give up status
     */

    public int isGiveUpstatus(){
        return giveUpstatus;
    }

    /**
     * Method responsible for sending give up message to the server
     */
    public void setGiveUpstatus(){
        clientSocket.out.println("giveup");
        worker.setJob(1);
        //recivefromOther();
    }


    /**
     * Method which is getter for Accept status, which tells if click on Accept button
     * is allowed
     * @return status if click on Accept button is allowed
     */
    public boolean getAcceptStatus(){
        return accept_status;
    }

    /**
     * Method that is responisable for not allowing to accept dead table/territory table
     * at the first move of player, so his enemy could mark his own dead groups/terrirtory
     * @param turn takes a turn of player, and change depending on it state if player can click accept
     *             button
     */
    public void setAccept_status(String turn){
        if(turn.equals("a")){
            accept_status=false;
        }
        else if(turn.equals("u")){
            accept_status=true;
        }
    }

    /**
     *
     */
    public void setConfirmation(){
        window.confirmSize();
    }

    /**
     * Method which gets the size of Drawing Board, defined in Client GUI class
     * @return
     */
    public boolean getConfirmation(){
        return window.getConfirmationofSize();
    }

    /**
     * Setting start to listener thread
     */
    public void setStart(){
        worker.setJob(0);
    }

    /**
     * Method which is responsible for initializing game client with server
     */
    public void inicializeGameWithServer()
    {
        //initializeListener();
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
            worker.setJob(1);
            //recivefromOther();
        }

    }

    /**
     * Method that is responsible for getting current play-game state
     * @return actual game state
     */
    public STATE getPlayState()
    {
        return playState;
    }

    /**
     * Method that sets current Play State of game
     * @param state gets a stake to which game will be change
     */
    public void setPlayState(STATE state){
        playState=state;
    }

    /**
     * Method which is responsiable for sending dead groups table from the client
     * to the server
     */
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
        worker.setJob(4);
        //worker.setJob(1);
        //reciveTurn();
        //recivefromOther();
        //try {
        //   setTurn(clientSocket.in.readLine());
        //}catch(IOException ex){

        //}
    }

    //0-white
    //1-black

    /**
     * Method which is responsible for sending territory table from a client
     * to a server
     */
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
        worker.setJob(4);
        //worker.setJob(1);
        //reciveTurn();
        //recivefromOther();
    }

    /**
     * Method which is responsible for changing game-status to
     * territory changing
     */

    public void changeToTerritory(){
        clientSocket.out.println("terr-change");
        worker.setJob(1);
        //recivefromOther();
    }


    /**
     * Method which is responsible for printing the server 'end'
     * status also makes this client status changed
     */
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

    /**
     * Method which is responsible for invoking method which sends
     * a coordinates of putted stone to the server, besides this method
     * set listener thread to read server for two messages, where
     * first is game-situation
     * and the other one is turn-situation
     * @param x
     * @param y
     */
    public void game(int x, int y)
    {
        sendToOther(x,y);
        worker.setJob(4);
    }

    /**
     * Method responsiable for sending to the server pass message
     */
    public void passGame() {
        clientSocket.out.println("pass");
        worker.setJob(4);

    }

    /**
     * Method which sends X,Y coordinates of position where stone was placed
     * by the player to server
     * @param x takes X coordinates of that point
     * @param y takes Y coordinates of that point
     */

    private void sendToOther(int x, int y){

        clientSocket.out.println("play");
        clientSocket.out.println("x"+x);
        clientSocket.out.println("y"+y);

    }

    /**
     * Method which is responsible for receiving turn from server
     */
    public void reciveTurn(){
        String turn = "";
        try{
            turn = clientSocket.in.readLine();
            //System.out.println("I have recived: "+ turn);
            setTurn(turn);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for setting player turn after receiving from Server
     * a message what turn it should be active/unactive
     * also invokes method responsible for graphical output for received turn
     * @param turn
     */

    public void setTurn(String turn){
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

    /**
     * Method which allows receiving messages from Server
     * such as, changing game status, pass, marked territory, dead stones etc...
     * also invokes other methods responsible for changing game play status
     * such as endGame(), invokes method responsible
     * for visible graphic interface to continue next play stage
     */

    public void recivefromOther(){
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
                accept_status=true;

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
                accept_status=true;
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
                worker.setJob(1);
                //recivefromOther();
            }
            //Analogicznie dla wiadomości dead
            if(line.equals("dead") && turn.equals("u")){
                worker.setJob(1);
                //recivefromOther();
            }
            if(line.equals("terr-change") && turn.equals("u")){
                worker.setJob(1);
                //recivefromOther();
            }

        }catch(Exception e){
            System.exit(-1);
        }
    }

    /**
     * Method which change game status to end of game, and invokes
     * method to calculate each player's territory, also invokes method responsiable
     * for'Game Over'
     * message instead of 'Your Turn'/'Turn of Enemy'
     */
    public void setEnd(){
        setPlayState(STATE.END_GAME);
        playBoard.calculateTerritory();
        window.setTurnEnd();
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
