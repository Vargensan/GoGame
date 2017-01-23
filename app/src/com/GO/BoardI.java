/**
 * Kinga Krata 2016-11-20.
 */
package src.com.GO;
public interface BoardI {

    /**
     * Method that get size of gameboard
     * @return size of board's one side (board is a square)
     */
     int getSize();

    /**
     * Method that insert into table a stone in player's color
     *
     * @param color color of player=stone in this turn
     * @param placeX coordinate X where to add stone
     * @param placeY coordinate Y where to add stone
     * @return true if stone added with succes
     */
     void addStone(PLAYER color, int placeX,int placeY);

    /**
     * Method that check if it is avaliable to put a stone in gameboard
     * @param color color of player=stone for which we are asking
     * @param placeX where we want to know if we can add this stone
     * @param placeY coordinate Y where to add stone
     * @return true if stone can be added at this place
     */
    boolean canAddHere(PLAYER color, int placeX, int placeY);

    /**
     * Method that counts territory for each player
     * @param color  color of player which teritory we want to count
     * @return amount of teritory of this player
     */
    int howMuchteritory(PLAYER color);

    /**
     * Method that set a stone to player's territory
     * @param player takes a player to whom territory belongs
     * @param placeX takes a position of specific stone
     * @param placeY takes a position of specific stone
     */

    void giveToMyTerritory(PLAYER player,int placeX, int placeY);
    /**
     *
     * @return table of PLACES informs what is on place with numer as table index
     */
    PLACE[][] getGameTable();



}
