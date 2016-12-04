/**
 * Kinga Krata 2016-11-21.
 */


package com.GO;


public class Board implements BoardI {

    private int size;
    private PLACE GameTable[][];
    private int[] nextCoordinates;
    Board(int size)
    {
        this.size=size;
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
    private boolean canBreathHere(PLAYER color, int placeX,int placeY) {

        PLACE aktual;

        // loops goes for all combinations 1 and -1 for i i j
        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2)
                try {
                    aktual = GameTable[placeX + i][placeY + j];
                    if (aktual == PLACE.EMPTY)
                        return true;
                    else if(aktual == color.playerToPlace()) {
                        return canBreathHere(color,placeX+i,placeY+j);
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
       if(canBreathHere(color,placeX,placeY))
           return true;
       //canBreatheAfterSimmering()
       // isItNotA_KO()
        return false;
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
