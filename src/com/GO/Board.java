/**
 * Kinga Krata 2016-11-21.
 */


package com.GO;


public class Board implements BoardI {

    private int size;
    public Play play;
    private PLACE GameTable[][];
    private PLACE DeleteGameTable[][];
    private int[] nextCoordinates;
    //------------------------------
    int[] koSituationXY = new int[2];
    //------------------------------
    public Board(int size,Play play)
    {
        this.size=size;
        this.play=play;
        GameTable=new PLACE[size][size];
        DeleteGameTable = new PLACE[size][size];
        initialize();
        nextCoordinates=new int[4];
    }

    /**
     * initialize board with empty places
     */
    private void initialize()
    {
        for(int i=0;i<size;++i)
        {
            for(int j=0;j<size;++j)
            {

                GameTable[i][j]=PLACE.EMPTY;
                DeleteGameTable[i][j]=PLACE.EMPTY;
            }
        }
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
        int[] XY = new int[2];
        /*
        Edit: Let's assume we have point placeX=11 placeY=11
        Check around points should be: as bellow
                (11,10)
         (10,11)(11,11)(12,11)
                (11,12)
         */
        for(int i = 0; i<4 ; i++){
            XY = values(i);
            try {
                actuall = table[placeX + XY[0]][placeY + XY[1]];
                System.out.println("X: "+(placeX+XY[0])+" Y: "+(placeY+XY[1])+" PLACE: "+table[placeX+XY[0]][placeY+XY[1]]);
                if (actuall == PLACE.EMPTY){
                    System.out.println("I can breath");
                    return true;
                }
                else if(actuall == color.playerToPlace()) {
                    //Implement deletetable
                    DeleteGameTable[placeX+XY[0]][placeY+XY[1]] = GameTable[placeX+XY[0]][placeY+XY[1]];
                    if(placeX+XY[0] != IgnoreX && placeY+XY[1] != IgnoreY)
                        return canBreathHere(table,color,placeX+XY[0],placeY+XY[1],placeX,placeY);
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                //we go to wall
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
        //canEnemyBreath(GameTable,color,placeX,placeY);
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
        for(int i = 0; i < 4; i++){
            XY = values(i);
            if(GameTable[placeX+XY[0]][placeY+XY[1]] == enemy.playerToPlace()){
                if(!canBreathHere(GameTable,enemy,placeX+XY[0],placeY+XY[1],placeX+XY[0],placeY+XY[1])){
                    for(int j = 0; j < size ; j++){
                        for(int z = 0; z < size; z++){
                            if(DeleteGameTable[j][z] == GameTable[j][z])
                                GameTable[j][z] = PLACE.EMPTY;
                            DeleteGameTable[j][z] = PLACE.EMPTY;


                        }
                    }
                }
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

    @Override
    public boolean canAddHere(PLAYER color, int placeX, int placeY) {
        //Can only add on empty place
        if(!checkifempty(GameTable,color,placeX,placeY)){
            return false;
        }
       if(canBreathHere(GameTable,color,placeX,placeY,placeX,placeY)){
           nullKO_situation();
           return true;
       }
       //Check sicmering
       if(canBreatheAfterSicmering(color, placeX, placeY))
            return true;
       return false;
    }

    /**
     * Method that return status of checking if player pawn can breath after put it on a place, where
     * it has no right to be, but in situation, in which it will erase other player's
     * pawn/pawns it will be placed
     * @param color takes a color of player
     * @param placeX takes a X coordinate of pawn, where player demands to place it
     * @param placeY takes a Y coordiante of pawn, where player demands to place it
     * @return status of checking
     */
    public boolean canBreatheAfterSicmering(PLAYER color, int placeX, int placeY){
        PLAYER enemyplayer;
        //pentla for na tempboard[][]
        PLACE tempboard[][] = GameTable;
        int[] XY = new int[2];
        for(int i = 0; i <size ; i++) {
            for(int j =0; j < size; j++) {
                tempboard[i][j] = GameTable[i][j];
            }
        }
        tempboard[placeX][placeY] = color.playerToPlace();
        int ko_state_counter = 0;
        boolean one_time = true;
        boolean dead = false;
        //if KO situation
        if(!isItNotA_KO(color,placeX,placeY)){
            //if yes
            return false;
        }
        //nuller to KO
        nullKO_situation();
        //if KO wasn't detected then do normal checkout
        //Set enemy colour
        enemyplayer = color.getEnemyColor();
        for(int i = 0; i<4 ; i++){
            XY = values(i);
            if(canBreathHere(tempboard,enemyplayer, placeX+XY[0],placeY+XY[1],placeX,placeY) == false){
                //then ++ ko_state_counter
                //Unused-Dunno-Where-To-Put
                System.out.println("X:"+(placeX+XY[0])+ "Y:"+(placeY+XY[1]));
                ko_state_counter++;
                if(ko_state_counter == 1 && one_time) {
                    koSituationXY[0] = placeX + XY[0];
                    koSituationXY[1] = placeY + XY[1];
                    one_time = false;
                } else if (ko_state_counter > 1){
                    nullKO_situation();
                }
                dead = true;
            }

        }
        return dead;
    }

    /**
     * Method that checks
     * @param color takes a color of player
     * @param placeX takes a coordinate X of specific point
     * @param placeY takes a coordinate Y of specific point
     * @return status of
     */
    private boolean isItNotA_KO(PLAYER color, int placeX, int placeY){
       if( placeX == koSituationXY[0] && placeY == koSituationXY[1]) {
           System.out.println("KO Situation detected!");
           return false;
       }
       return true;
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
    //TODO: Add nuller to KO_SITUATION
    /*
     Problem: after detecting KO situation it returns that PLACE.color cant be added here
     and after diffrent move of that player we need to null integer ko_situationXY
     (by saying null, assume to set it to -1) so it does not attach any field on game board
     */

    /**
     * null KO situation
     */
    private void nullKO_situation(){
        koSituationXY[0] = -1;
        koSituationXY[1] = -1;
    }

    @Override
    public int howMuchteritory(PLAYER color) {
        /*
        Opis implementacji:
        1.Weź dowolny pusty punkt na planszy, rekurencja, wywołanie po wszystkich sąsiadach(4) -> odnotuj punkt
        2.  a) jeżeli wywołanie rekurencyjne jest niepuste -> odnotuj do innej tabeli, nie rób dalej rekurencji
        2   b) jeżeli wywołanie rekurencyjne jest puste -> odnotuj, wywołaj rekurencje
                a .1) jeżeli wywołanie dotyczy pola które zostało już wywołane rekurencyjnie zaniechaj
        3. jeżeli liczba odnotowanych pól jest mniejsza niż połowa planszy to wyszukaj pasmo blokujące dalszy rozwój
        rekurencji i określ jego charakter (kolor)
            a) możliwa automatyczna weryfikacja i przerwanie wykoanania gdy >= polowa planszy
        4. powtórz operacje dla dowolnego punktu należącego do game board ale nie odnotowanego wśród pustych
         */
        return 0;
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
}
