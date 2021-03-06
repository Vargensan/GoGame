/**
 * Kinga Krata 2016-11-21.
 */


package com.GO;


public class Board implements BoardI {

    private int size;
    private int error_option;
    private double pointsBlack;
    private double pointsWhite;
    private Play play;
    private PLACE GameTable[][];
    private PLACE DeleteGameTable[][];
    private PLACE TempDeleteGameTable[][];
    //Who's Territory
    private boolean isWhom[][];
    //Table of death pools
    private boolean TableofDeath[][];
    //Table of Territory of Player
    private PLACE TerritoryTable[][];

    private int[] nextCoordinates;
    private int[] stoneCoordinates;
    //------------------------------
    private int[] koSituationXY = new int[2];
    private boolean ko_detected;
    private boolean one_time_calculate = true;
    private int invalid_option;
    //------------------------------
    //Nie sprawdzac simmerica zawsze -> dopiero po braku oddechu?
    //
    public Board(int size,Play play)
    {
        this.size=size;
        this.play=play;
        GameTable=new PLACE[size][size];
        DeleteGameTable = new PLACE[size][size];
        TempDeleteGameTable = new PLACE[size][size];
        //Czyje Terytorium true - Czarny false - Biały
        isWhom = new boolean[size][size];
        //Tablica Martwych Pól
        //+6.5 points to white player
        pointsBlack = 0.0;
        pointsWhite = 6.5;
        TableofDeath = new boolean[size][size];
        //Table of Territory
        TerritoryTable = new PLACE[size][size];

        initialize();
        nextCoordinates=new int[4];
        stoneCoordinates = new int[2];
        stoneCoordinates[0] = -1;
        stoneCoordinates[1] = -1;

        koSituationXY[0] = -1;
        koSituationXY[1] = -1;
    }

    /**
     * initialize board with empty places, same Territory Table and DeleteGameTable
     */
    private void initialize()
    {
        for(int i=0;i<size;++i)
        {
            for(int j=0;j<size;++j)
            {

                GameTable[i][j]=PLACE.EMPTY;
                DeleteGameTable[i][j]=PLACE.EMPTY;
                TableofDeath[i][j] = false;
                TerritoryTable[i][j] = PLACE.EMPTY;
                isWhom[i][j] = false;
            }
        }
    }

    /**
     * Method that calculates points for player for each dead-group
     */
    public void deleteDeadFromGameTable(){
        for(int i = 0; i < size; i++){
            for(int j=0; j< size; j++){
                if(TableofDeath[i][j]){
                    if(GameTable[i][j] == PLACE.BLACK){
                        pointsWhite+=1.0;
                    }
                    if(GameTable[i][j] == PLACE.WHITE){
                        pointsBlack+=1.0;
                    }
                    GameTable[i][j] = PLACE.EMPTY;
                    TableofDeath[i][j] = false;
                }
            }
        }
    }

    /**
     * Method that sets a selected point as dead
     * @param X takes coordinate X of point
     * @param Y takes coordinate Y of point
     */
    public void markAsDead(int X, int Y){
        if(GameTable[X][Y] != PLACE.EMPTY)
            TableofDeath[X][Y] = true;
    }

    /**
     * Method that sets selected point as not dead
     * @param X takes coordinate X of point
     * @param Y takes coordinate Y of point
     */
    public void unMarkAsDead(int X, int Y){
        TableofDeath[X][Y] = false;
    }

    /**
     * Getter for table of dead points
     * @return table of dead points
     */
    public boolean[][] getDeadTable(){
        return TableofDeath;
    }
    public void setDeadTable(int x, int y,boolean quantity){
        TableofDeath[x][y]=quantity;
    }

    /**
     * Method that marks selected point as Territory of player
     * @param player takes a color of player
     * @param X takes a coordinate X of point
     * @param Y takes a coordinate Y of point
     */
    public void markAsTerritory(PLAYER player, int X, int Y){
        if(GameTable[X][Y] != PLACE.BLACK && GameTable[X][Y] != PLACE.WHITE){
            TerritoryTable[X][Y] = player.playerToPlace();
        }
    }

    //public void setTerritoryTable(PLAYER)

    /**
     * Method that unmark selected point as Territory of player
     * @param X takes a coordinate X of selected point
     * @param Y takes a coordinate Y of selected point
     */
    public void unMarkAsTerritory(int X, int Y){
        if(GameTable[X][Y] != PLACE.BLACK || GameTable[X][Y] != PLACE.WHITE){
            TerritoryTable[X][Y] = PLACE.EMPTY;

        }
    }

    /**
     * Getter for Territory Table
     * @return table of whos is this territory
     */
    public PLACE[][] getTerritoryTable(){
        return TerritoryTable;
    }

    /**
     *  Method which calculate Territory and add points for each player
     *  depending on how much teritory they have
     *  Can be invoked only once at end of game
     */
    public void calculateTerritory(){
        if(one_time_calculate) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (TerritoryTable[i][j] == PLACE.BLACK) {
                        pointsBlack++;
                    } else if (TerritoryTable[i][j] == PLACE.WHITE) {
                        pointsWhite++;
                    }
                    //Nulling Table
                    //TerritoryTable[i][j] = PLACE.EMPTY;
                }
            }
            one_time_calculate = false;
        }
    }


    /**
     * Getter for ammount of points White player
     * @return number of points white player
    */

    public double getPoints(PLAYER player){
        if(player == PLAYER.BLACK){
            return pointsBlack;
        }else{
            return pointsWhite;
        }
    }

    private boolean checkStoneCoordinates(int tempX, int tempY){
        return (tempX == stoneCoordinates[0] && tempY == stoneCoordinates[1]);
    }
    /**
     *
     * @param color color of player=stone for which we are asking
     * @param placeX where we want to know if stone can breath
     * @param placeY where we want to know if stone can breath
     * @return returns if stone in given color can breath on given place
     */
    private boolean canBreathHere(PLACE[][] table,PLAYER color, int placeX,int placeY,int IgnoreX,int IgnoreY) {
        PLACE actuall;
        int XY[] = new int[2];
        int tempX;
        int tempY;
        boolean checkifavaliable = false;
        if(!lessandhighbool(placeX,placeY)){
            return false;
        }
        DeleteGameTable[placeX][placeY] = table[placeX][placeY];
        if(table[placeX][placeY].equals(color.playerToPlace())){
            TempDeleteGameTable[placeX][placeY] = color.playerToPlace();
        }else if(table[placeX][placeY].equals(color.getEnemyColor().playerToPlace()))
            TempDeleteGameTable[placeX][placeY] = color.getEnemyColor().playerToPlace();
        else
            TempDeleteGameTable[placeX][placeY] = PLACE.EMPTY;

        //table[placeX][placeY] = color.playerToPlace();
        /*
        Edit: Let's assume we have point placeX=11 placeY=11
        Check around points should be: as bellow
                (11,10)
         (10,11)(11,11)(12,11)
                (11,12)
         */
        /*
        Dlaczego nie działa: #GODKILLMEPLX
        Imagine:
        Przy wykryciu wroga na polu X,Y, gdzie ten wróg ma oddech, ale ten zamiast sprawdzenia
        wszystkich swoich pól i pójścia do następnego swojego sprzymierzeńca, robi coś innego, a mianowicie
        sprawdza po kolei pola jeśli napotka na swojego to robi rekurencje od niego
        ale jeśli ta rekurencja zwraca brak oddechu, to również to pole przyjmuje brak oddechu
        pomimo tego że go ma, tylko nie sprawdziło wszystkich swoich pól.

        ROZWIĄZANIE PROBLEMU:?
        if -> moje puste pola to null to
        if -> mam sąsiada -> idę do niego
        pytanie -> co gdy mam dwoch sasiadow i wejde w tego z brakiem oddechu?
        wejdzie w niego i zwroci null

         */
        //Sprawdz wszystkich otaczających i zobacz czy to są puste pola, jeśli tak zwróc true
        /*
        Question: Problem czy bedzie trzeba zastosowac Array List ze wzgledu gdy sasiedzi beda obok siebie
        Bo wtedy sasiad moze ignorowac tylko tego ktory przekazal rekurencje do niego
        Answer: No jasne ;-;
        Rozwiązanie: DeadTable przechowuje pola które zostały dotknięte rekurencją, wystarczy ich nie uwzględniać
         */
        for(int i = 0; i < 4 ; i++){
            XY = values(i);
            tempX = placeX+XY[0];
            tempY = placeY+XY[1];
            if(!lessandhighbool(tempX,tempY)){
                continue;
            }
            //przed-wstawieniem, żeby nie sprawdzało oddechu w tym punktu
            //bo tu zamierzam Ciebie wstawić...
            //Dać warunek czy miejsce jest puste
            if(IgnoreX == tempX && IgnoreY == tempY){
                continue;
            }
            //else {
                try {
                    actuall = table[tempX][tempY];
                    if (actuall.equals(PLACE.EMPTY)) {
                        //System.out.println("I can breath here: X: "+tempX+" Y:"+tempY);
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {

                }
           // }

        }
        for(int i = 0; i < 4 ; i++){
            XY = values(i);
            tempX = placeX+XY[0];
            tempY = placeY+XY[1];
            if(lessandhighbool(tempX,tempY)){
                actuall = table[tempX][tempY];
                if(actuall.equals(color.playerToPlace())) {
                    if (!table[tempX][tempY].equals(DeleteGameTable[tempX][tempY])) {
                        //sprawdz sasiadow
                        //placeX,placeY at IgnoreX,IgnoreY
                        //Błąd implementacji <3 -> stale sprawdza mi jednego
                        /*
                        Jak działa, sprawdza mi jednego dodaje ze moze oddychac sprawdza innego
                        ale nie sprawdza czy ten moze oddychac przez tego ktorego juz sprawdzil, nie pozwala na to
                        DeleteGameTable Gosh so hard bugggg ;x
                         */
                        checkifavaliable = canBreathHere(table, color, tempX, tempY, IgnoreX, IgnoreY);
                    }
                    if (checkifavaliable) {
                        //jezeli moga oddychac zwroc true
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void addStone(PLAYER color, int placeX, int placeY) {
        GameTable[placeX][placeY] = color.playerToPlace();
        canEnemyBreath(GameTable,color,placeX,placeY);
        //deleteTempTableDeleted();
        //nullKO_situation();
    }

    /**
     * Method that checks if enemy player's pawn can breath after put in
     * X,Y coordinates player's pawn
     * @param GameTable takes a GameTable
     * @param color takes a color of player
     * @param placeX takes X coordinate of pawn
     * @param placeY takes Y coordinate of pawn
     */

    private void canEnemyBreath(PLACE[][] GameTable, PLAYER color, int placeX, int placeY){
        PLAYER enemy;
        int XY[] = new int[2];
        enemy = color.getEnemyColor();
        int tempX;
        int tempY;
        boolean state = true;
        for(int i = 0; i < 4; i++){
            XY = values(i);
            tempX = placeX+XY[0];
            tempY = placeY+XY[1];
            //Dla wszystkich sąsiadów takich, że sąsiad to wróg
            if(lessandhighbool(tempX,tempY)){
                if(GameTable[tempX][tempY] == enemy.playerToPlace()){
                    //System.out.println("FOUND ENEMY AT: X: "+tempX+" Y: "+tempY);
                    //Sprawdź czy sąsiad może oddychać ignoruj przeskok, do samego siebie
                    //Can breathe here sprawdza czy moge postawić na X, Y a nie czy oddycha
                    //Wyzeruj tablice usuwan
                    deleteTableNuller();
                    //I wejdz w pole ktore trzeba sprawdzic czy oddycha
                    state = canBreathHere(GameTable,enemy,tempX,tempY,tempX,tempY);
                    //jeśli nie oddycha wywal to
                    if(state == false){
                        deleteFromTable();
                        //deleteTempTableDeleted();
                    }
                    //if(canBreathHere(GameTable,enemy,tempX,tempY,tempX,tempY) == false){
                    //    System.out.println("Gonna kick your ass: X: "+tempX+" Y: "+tempY);
                    //    DeleteGameTable[tempX][tempY] = GameTable[tempX][tempY];
                    //}
                }
            }
        }
        //wynuluj tablice po zakonczeniu pracy
        deleteTableNuller();
    }
    private void deleteFromTable(){
        int counter = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (DeleteGameTable[i][j].equals(GameTable[i][j])) {
                    if(!DeleteGameTable[i][j].equals(PLACE.EMPTY)){
                        counter++;
                        //System.out.println("Deleted: "+i+" "+j);
                    }
                    GameTable[i][j] = PLACE.EMPTY;
                }
                if(counter>1){
                    nullKO_situation();
                }
                DeleteGameTable[i][j] = PLACE.EMPTY;
            }
        }
    }
    private void deleteTableNuller(){
        for(int j = 0; j < size ; j++) {
            for (int z = 0; z < size; z++) {
                DeleteGameTable[j][z] = PLACE.EMPTY;
            }
        }
    }

    /**
     * Method that checks if position on X,Y is empty if yes, then it allows to
     * give there a pawn, unless some other functions detects errors in this action
     * @param table takes a Game Table
     * @param color takes a color of player
     * @param X takes a X coordinate of pawn in gameboard
     * @param Y takes a Y coordinate of pawn in gameboard
     * @return status of pawn(empty or not)
     */
    private boolean checkifempty(PLACE[][] table, PLAYER color, int X, int Y){
        PLACE actuall;
        actuall = GameTable[X][Y];
        if(actuall != PLACE.EMPTY)
            return false;
        return true;
    }


    public int getInvaildOption(){
        return invalid_option;
    }
    @Override
    public boolean canAddHere(PLAYER color, int placeX, int placeY) {
        //Wywyłanie metod w zależności od stanu gracza -> nie wywoływać dla bota
        //Logika nie działa na samobójstwa grupy
        nullCoordinates();
        if(!lessandhighbool(placeX,placeY)){
            return false;
        }
        if(!checkifempty(GameTable,color,placeX,placeY)){
            invalid_option=1;
            return false;
        }
        //Wyzerowanie wsp KO dopiero po ruchu
        if(!isItNotA_KO(color,placeX,placeY)){
            invalid_option=3;
            return false;
        }
        stoneCoordinates[0] = placeX;
        stoneCoordinates[1] = placeY;
        if(canBreathHere(GameTable,color,placeX,placeY,placeX,placeY)){
           nullKO_situation();
           nullCoordinates();
           return true;
        }
        if(canBreatheAfterSicmering(color, placeX, placeY)) {
            nullCoordinates();
            return true;
        }else{
            GameTable[placeX][placeY] = PLACE.EMPTY;
            invalid_option=2;
            nullCoordinates();
            return false;
        }
        //return false;
    }

    private void nullCoordinates(){
        stoneCoordinates[0] = -1;
        stoneCoordinates[1] = -1;
    }

    /**
     * Getter for Error Message
     * @return error option
     */
    public int getErrorMessage(){
        return error_option;
    }

    /**
     * Method that return status of checking if player pawn can breath after put it on a place, where
     * it has no right to be, but in situation, in which it will erase other player's
     * pawn/pawns it will be placed
     * @param color takes a color of player
     * @param placeX takes a X coordinate of pawn, where player demands to place it
     * @param placeY takes a Y coordinate of pawn, where player demands to place it
     * @return status of checking
     */
    public boolean canBreatheAfterSicmering(PLAYER color, int placeX, int placeY){
        PLAYER enemyplayer;
        int[] XY = new int[2];
        int tempX;
        int tempY;
        GameTable[placeX][placeY] = color.playerToPlace();
        int ko_state_counter = 0;
        boolean one_time = true;
        boolean dead = false;
        boolean cantBreath;
        //Chyba do usunięcia
        if(!isItNotA_KO(color,placeX,placeY)){
            return false;
        }
        //----
        nullKO_situation();
        enemyplayer = color.getEnemyColor();
        for(int i = 0; i<4 ; i++){
            XY = values(i);
            tempX = XY[0] +placeX;
            tempY = XY[1] +placeY;
            //Sprawdza czy wchodzimy w nie dozwolone punkty -> jeśli tak to idź do kolejnego wywołania
            if(!lessandhighbool(tempX,tempY)){
                continue;
            }
            //Sprawdza czy wchodzimy w punkty należące do nas -> jeśli tak to idź do kolejnego wywołania
            if(GameTable[tempX][tempY].equals(color.playerToPlace())){
                continue;
            }
            deleteTableNuller();
            cantBreath = canBreathHere(GameTable,enemyplayer, tempX,tempY,placeX,placeY) == false;
            //System.out.println("State: "+cantBreath+ " temp X: "+tempX+" temp Y: "+tempY+" color:"+ enemyplayer);
            if(cantBreath){
                //Usunięcie wszystkich pol
                deleteTempTableDeleted();
                //Dodajemy ko_state, ale po sprawdzeniu innego symerica, możemy mieć więcej niż jedną ko-like-sytuacje
                ko_state_counter++;
                if(ko_state_counter == 1 && one_time) {
                    //System.out.println("Setting KO: X: "+tempX+" Y: "+tempY);
                    koSituationXY[0] = tempX;
                    koSituationXY[1] = tempY;
                    //ko_detected = true;
                    one_time = false;
                } else if (ko_state_counter > 1){
                    //if(ko_detected==false)
                    //System.out.println("I am a nuller");
                    nullKO_situation();
                }
                //nullKO_situation();
                dead = true;
            }//else{
               // deleteTempTableDeleted();
            //}

        }
        return dead;
    }
    //Jak poprawnie zainicjalizować KO_Sytuacje...
    private void deleteTempTableDeleted(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(GameTable[i][j].equals(TempDeleteGameTable[i][j])){
                    TempDeleteGameTable[i][j] = PLACE.EMPTY;
                }
                TempDeleteGameTable[i][j]=PLACE.EMPTY;
            }
        }
    }

    /**
     * Method that checks
     * @param color takes a color of player
     * @param placeX takes a coordinate X of specific point
     * @param placeY takes a coordinate Y of specific point
     * @return status of
     */
    public boolean isItNotA_KO(PLAYER color, int placeX, int placeY){
       if( placeX == koSituationXY[0] && placeY == koSituationXY[1]) {
           //System.out.println("KO Situation detected!");
           ko_detected = true;
           return false;
       }
       return true;
    }

    private void nullKO_situation(){
        //System.out.println("\nNulling KO\n");
        ko_detected = false;
        koSituationXY[0] = -1;
        koSituationXY[1] = -1;
    }

    /**
     * Method that in dependence of value from 1 to 4 returns borderer of
     * specific pawn
     * @param i takes a number from 1 to 4
     * @return X and Y coordinate of neightboor of pawn
     */
    private int[] values(int i){
        int p[] = new int[2];
        switch(i) {
            case 1:
                p[0] = 1;
                p[1] = 0;
                break;
            case 2:
                p[0] = -1;
                p[1] = 0;
                break;
            case 3:
                p[0] = 0;
                p[1] = 1;
                break;
            default:
                p[0] = 0;
                p[1] = -1;
                break;
        }
        return p;
    }

    /**
     * Getter for point where KO SITUATION OCCURS
     * @return point where ko situation occurs
     */
    public int[] getKO_Points(){
        return koSituationXY;
    }

    /**
     * Getter for KO Status
     * @return ko status (not) detected
     */
    public boolean getKO_Status(){
        return ko_detected;
    }

    @Override
    public int howMuchteritory(PLAYER color) {

        return 0;
    }

    @Override
    public void giveToMyTerritory(PLAYER player, int placeX, int placeY) {

    }

    @Override
    public PLACE[][] getGameTable() {
        return GameTable;
    }

    /**
     * Method that returns color of actual player
     * @return color of actual player
     */
    public PLAYER getPLayerColor()
    {
        return play.get_player_color();
    }

    /**
     * Method responsiable for checking if putted stone is on right position
     * and doesnt not overflow
     * @param tempX get X of point
     * @param tempY get Y of point
     * @return boolean which tells is operation of insert legal
     */
    private boolean lessandhighbool(int tempX, int tempY){
        boolean condition = ((tempX>=0 && tempX <=(size-1)) && (tempY >=0 && tempY <=(size-1)));
        return condition;
    }
}
