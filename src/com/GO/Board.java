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
    Board(int size,Play play)
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
    private boolean canBreathHere(PLACE[][] table,PLAYER color, int placeX,int placeY) {

        PLACE actuall;

        // loops goes for all combinations 1 and -1 for i i j
        for (int i = -1; i < 2; i += 2) {
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
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean addStone(PLAYER color, int placeX, int placeY) {

        if(canAddHere(color,placeX, placeY))
            return true;
        else
            return false;

    }

    @Override
    public boolean canAddHere(PLAYER color, int placeX, int placeY) {
       if(canBreathHere(GameTable,color,placeX,placeY)){
           nullKO_situation();
           return true;
       }
       if(canBreatheAfterSicmering(color, placeX, placeY))
            return true;
       return false;
    }

    public boolean canBreatheAfterSicmering(PLAYER color, int placeX, int placeY){
        PLAYER enemyplayer;
        //pentla for na tempboard[][]
        PLACE tempboard[][] = GameTable;
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

        for(int i = -1; i < 2 ; i+=2){
            for(int j = -1; j < 2; j+=2){
                //If enemy player place[x][y] can't breathe
                if(!canBreathHere(tempboard,enemyplayer, placeX+i,placeY+j)){
                    //then ++ ko_state_counter
                    ko_state_counter++;
                    if(ko_state_counter == 1 && one_time) {
                        koSituationXY[0] = placeX + i;
                        koSituationXY[1] = placeY + j;
                        one_time = false;
                    } else if (ko_state_counter > 1){
                        nullKO_situation();
                    }
                    dead = true;
                }
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
        return 0;
    }

    @Override
    public PLACE[][] getGameTable() {
        return GameTable;
    }
}
