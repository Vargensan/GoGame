/**
 * Kinga Krata 2016-11-20.
 */
package com.GO;
public interface BoardI {

    /**
     *
     * @return size of board's one side (board is a square)
     */
     int getSize();

    /**
     *
     * @param color color of player=stone in this turn
     * @param placeX coordinate X where to add stone
     * @param placeY coordinate Y where to add stone
     */
     void addStone(PLAYER color, int placeX,int placeY);

    /**
     *
     * @param color color of player=stone for which we are asking
     * @param placeX where we want to know if we can add this stone
     * @param placeY coordinate Y where to add stone
     * @return true if stone added with success
     */
    boolean canAddHere(PLAYER color, int placeX, int placeY);

    /**
     *
     * @param color  color of player which teritory we want to count
     * @return amount of teritory of this player
     */
    int howMuchteritory(PLAYER color);

    /**
     *
     * @return table of PLACES informs what is on place with numer as table index
     */
    PLACE[] getGameTable();



}
