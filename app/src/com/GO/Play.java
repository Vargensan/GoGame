package src.com.GO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.mvc.WebSocket;
import src.GUIGo.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import play.libs.*;
import play.libs.F.*;
import play.Logger;
import play.mvc.*;

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
    private STATE playState;

    private int hasGameEnded;
    private boolean accept_status;
    private int giveUpstatus;
    private ListenerThread worker;

    //---------------------------------------------
    //IMAGES
    private BufferedImage image,controlerImage,im_black,im_white,im_ko,im_dead,im_territoryBlack,im_territoryWhite;
    private ImageResize imageResizer;

    //----------Communication Between Clients------//
    DrawMathObject dmo;
    private int distance;
    private int sizeGameBoard;
    private int[] StartPoint = new int[2];
    private int criclefilled;
    private int[][][] Table_Intersection;

    private int javascriptHeight;
    private int javascriptWidth;
    //----------Holding Game Status---------------//
    private boolean turn;
    private boolean ifCanAdd;
    private int[] intersectinPoint = new int[2];

    //WEBSOCKETS
    WebSocket.In<JsonNode> in;
    WebSocket.Out<JsonNode> out;

    //Player Name
    private String nameofPlayer;


    /**
     * Constructor
     * Set start values, initialize client GUI
     */
    public Play()
    {
        hasGameEnded = 0;
        giveUpstatus = 0;
        playState=STATE.BEFORE_GAME;

        window=new ClientGUI();
        window.initialize(this);
        requestSizeConfirmation();

        playBoard = new Board(window.getSizeOfPlayBoard(),this);

        window.createDrawingBoard(playBoard);
        //this.clickListener=window.getDrawingBoard().getBoardOnClickListener();
        //initializeListener();



    }

    private boolean canAddHere(){
        if(playBoard.canAddHere(get_player_color(),intersectinPoint[0],intersectinPoint[1])){
            return true;
        }
        return false;
    }

    private void AddHere(){
        playBoard.addStone(get_player_color(),intersectinPoint[0],intersectinPoint[1]);

    }

    private int [] getIntersectinPoint(){
        return intersectinPoint;
    }


    public Play(String name, WebSocket.In<JsonNode>in,WebSocket.Out<JsonNode>out){
        hasGameEnded = 0;
        giveUpstatus = 0;
        playState = STATE.BEFORE_GAME;
        this.in = in;
        this.out = out;
        this.nameofPlayer = name;

        //Create Game Logic
        playBoard = new Board(19,this);
        //We dont need to create DrawingBoard but DrawMathObject
        dmo = new DrawMathObject();
        //Utworzenie Warunków Wstępnych
        initialize(in,out);
        //Utworzenie nowych Socketów do komuniakcji z serverem
        inicializeGameWithServer();




    }
    public void initialize( WebSocket.In<JsonNode>in,WebSocket.Out<JsonNode>out){
        askforHeight();
        askforWidth();
        //Kod Synchorizujacy że doopiero po dostaniu Width lecimy dalej
        //........
        StartPoint = dmo.calculateStartPoint(javascriptHeight,javascriptWidth,19);
        distance = dmo.calculateDistance(javascriptHeight,playBoard.getSize());
        criclefilled = dmo.calculateSizeOfCircle(distance);
        Table_Intersection = dmo.calculateTableIntersection(StartPoint,distance,playBoard.getSize(),criclefilled);
        setImages(this);
        initializeClicks(in,out);
    }

    //****************************************************************
    //WEBSOCKET JSON IN - ON MESSAGE ACTIONS
    public void initializeClicks (WebSocket.In<JsonNode>in,WebSocket.Out<JsonNode>out){
        in.onMessage(new Callback<JsonNode>(){
            @Override
            public void invoke(JsonNode event) throws Throwable {
                try{
                    //EVENT TO WIADOMOSC WYSLANA DO NAS
                    //TRZEBA JA TUTAJ PRZETWORZYC
                    //1.SPRAWDZ NAZWE
                    //W ZALEZNOSCI OD NAZWY SKOCZ DO FUNKCJI JA PRZETWARZAJACA

                }catch (Exception e){

                }
            }
        });

        in.onClose(new Callback0() {
            @Override
            public void invoke() throws Throwable {

            }
        });
    }
    //******************************************************************
    //******************************************************************
    //--------------COMUNICATION BETWEEN CLIENTS-----------------------//
    public void askForStartPoint(){

    }
    public void askforHeight(){

    }
    public void askforWidth(){


    }
    /*
    Metoda do utworzenia obiektu typu JSON zawierajacego
    1) X
    2) Y
    3) Gracza
    4) Wiadomosc  "Narysuj"
     */
    public void demandDraw(){

    }




    //******************************************************************
    //******************************************************************



    //-----------------------------------------------------------------
    //          PAKOWANIE OBSŁUGI BUTTONÓW JS -> JSON -> JAVA
    public void callPass(){

    }
    public void callMarkTerritory(){

    }
    public void callMarkDead(){

    }
    public void callMarkUndead(){

    }
    public void callGiveUp(){

    }
    public void callSend(){

    }
    //*****************************************************************
    //              OBSŁUGA TURY GRACZA
    //--WYWOWŁYWANIE ZA KAZDYM RAZEM GDY COKOLWIEK PRZEJDZIE PRZEZ JS -> JSON -> JAVA
    public boolean checkTurn(){
        return turn;
    }


    //*****************************************************************
    //                  CLIENT TURN

    public void setTurn(boolean value){
        turn = value;
    }


    //*****************************************************************

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
        //EDITED -> SET MOUSE BOARD OD CLICK LISTENER
        //window.getDrawingBoard().setterMouseListener(active);
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
        //JSON -> SEND TURN END
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
        //System.out.println("I change your status to "+ turn);
        if(turn.equals("a")){
            //System.out.print("A ja będę twym aniołem, będę gwiazdą na twym niebie");
            window.setTurn(true);
            //window.getDrawingBoard().setterMouseListener(true);
        }else {
            //System.out.println("let's set my turn to false");
            window.setTurn(false);
            //window.getDrawingBoard().setterMouseListener(false);
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
                //Implement here call to JSON
                //window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
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
                //window.getDrawingBoard().changeState(this.getPlayState());
                //window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());


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
                //window.hideMarkAsDead();
                //window.showTerritoryMarking();
                turn = clientSocket.in.readLine();
                setAccept_status(turn);
                //window.repaint();
                //window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
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
                //window.getDrawingBoard().changeState(this.getPlayState());
                //window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
            }
            else if(line.equals("end")){
                endGame();
                return;
            } else if(line.equals("win")){
                giveUpstatus=1;
                endGame();
                return;

            } else if(line.equals("lose")) {
                giveUpstatus = 2;
                endGame();
                return;
            }

            setTurn(turn);

            if(line.equals("double-pass") && turn.equals("u")){
                worker.setJob(1);
            }
            if(line.equals("dead") && turn.equals("u")){
                worker.setJob(1);
            }
            if(line.equals("terr-change") && turn.equals("u")){
                worker.setJob(1);
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
        //LOOK WHAT GONNA IMPLEMENT HERE
        //window.setTurnEnd();
    }

    /**
     * If KO situation is detected it prints it to the client
     */
    public void informKO(){
        //window.getDrawingBoard().set_KO_Points(playBoard.getKO_Points());
        //window.getDrawingBoard().set_KO_Situation(playBoard.getKO_Status());
        //if(playBoard.getKO_Status()){
        //    window.showWarningKO_Situation();
        //}
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
        //window.getDrawingBoard().paintImmediately(0,0,window.getDrawingBoard().getWidth(),window.getDrawingBoard().getHeight());
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

   // public DrawingBoard getDrawingBoard(){return window.getDrawingBoard();}


    /**
     * Getter for player color
     * @return color of player
     */
    public PLAYER get_player_color() {return player_color;}


    //CODE FOR RENDERING IMAGES

    private void setImages(Play window){
        imageResizer = new ImageResize();
        im_black = imageSetter(window,imageResizer,"/GoGraphics/black_button.png",criclefilled,criclefilled);
        im_white = imageSetter(window,imageResizer,"/GoGraphics/white_button2.png",criclefilled,criclefilled);
        //controlerImage = imageSetter(window,imageResizer,"/GoGraphics/DrawingBoardTexture.png",this.getWidth(),this.getHeight());
        im_ko = imageSetter(window,imageResizer,"/GoGraphics/KOsituationButton.png",criclefilled,criclefilled);
        im_dead = imageSetter(window,imageResizer,"/GoGraphics/KOsituationButton.png",criclefilled*1/2,criclefilled*1/2);
        im_territoryBlack = imageSetter(window,imageResizer,"/GoGraphics/Territory_Black.png",criclefilled,criclefilled);
        im_territoryWhite = imageSetter(window,imageResizer,"/GoGraphics/Territory_White.png",criclefilled,criclefilled);
    }
    private BufferedImage imageSetter(Play window, ImageResize imageResizer, String name, int prefW, int prefH){
        try {
            InputStream imageInputStream = window.getClass().getResourceAsStream(name);
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            return imageResizer.scale(bufferedImage,prefW,prefH);
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;

    }




}
