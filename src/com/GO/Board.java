/**
 * Kinga Krata 2016-11-21.
 */


package com.GO;


public class Board implements BoardI {

    private int size;
    public Play play;
    private PLACE GameTable[][];
    private int[] nextCoordinates;
    //------------------------------
    int[] koSituationXY = new int[2];
    //------------------------------
    public Board(int size,Play play)
    {
        this.size=size;
        this.play=play;
        GameTable=new PLACE[size][size];

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
    //Implement methods...
    //
    //
    private boolean canBreathHere(PLACE[][] table,PLAYER color, int placeX,int placeY,int IgnoreX,int IgnoreY) {

        PLACE actuall;
        int[] XY = new int[2];
        // loops goes for all combinations 1 and -1 for i i j
        /*
        Edit: Let's assume we have point placeX=11 placeY=11
        Check around points should be: as bellow
                (11,10)
         (10,11)(11,11)(12,11)
                (11,12)
         it this function bellow checks:
          (10,10)      (12,10)
                (11,11)
          (10,12)      (12,12)
         */
        /*
         */
        /*for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2)
                try {
                    actuall = table[placeX + i][placeY + j];
                    if (actuall == PLACE.EMPTY)
                        return true;
                    else if(actuall == color.playerToPlace()) {
                        return canBreathHere(table,color,placeX+i,placeY+j);
                    }

                } catch (ArrayIndexOutOfBoundsException ex) {
                    //we go to wall
                }

        }
        return false;*/
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
        GameTable[placeX][placeY]=color.playerToPlace();


    }
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

    public boolean canBreatheAfterSicmering(PLAYER color, int placeX, int placeY){
        PLAYER enemyplayer;
        //pentla for na tempboard[][]
        PLACE tempboard[][] = GameTable;
        int[] XY = new int[2];
        for(int i = 0; i <size ; i++) {
            for(int j =0; j < size; j++) {
                if(GameTable[i][j] == PLACE.BLACK) {
                    tempboard[i][j] = PLACE.BLACK;
                }else if(GameTable[i][j] == PLACE.WHITE){
                    tempboard[i][j] = PLACE.WHITE;
                }else {
                    tempboard[i][j] = PLACE.EMPTY;
                }
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
        if(color.playerToPlace() == PLACE.WHITE)
            enemyplayer = PLAYER.BLACK;
        else
            enemyplayer = PLAYER.WHITE;

        for(int i = 0; i<4 ; i++){
            XY = values(i);
            if(canBreathHere(tempboard,enemyplayer, placeX+XY[0],placeY+XY[1],placeX,placeY) == false){
                //then ++ ko_state_counter
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

    private boolean isItNotA_KO(PLAYER color, int placeX, int placeY){
       if( placeX == koSituationXY[0] && placeY == koSituationXY[1]) {
           System.out.println("KO Situation detected!");
           return false;
       }
       return true;
    }
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
}
