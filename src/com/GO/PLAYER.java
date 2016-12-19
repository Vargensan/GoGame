/**
 * Kinga Krata 2016-11-20.
 */

package com.GO;

public enum PLAYER {

    BLACK, WHITE;

    /**
     * Method that takes an enemy color
     * @param color get actuall player color
     * @return a opposite player color
     */
    public PLAYER getEnemyColor(PLAYER color){
        if(color == PLAYER.BLACK)
            return PLAYER.WHITE;
        else
            return PLAYER.BLACK;

    }

    /**
     * Method responsible for returning enum of assigned to defined previously
     * player.
     * @return assigned to player type (BLACK, WHITE)
     */
    public PLACE playerToPlace() {
        switch (this) {
            case BLACK:
                return PLACE.BLACK;
            case WHITE:
                return PLACE.WHITE;
            default:
                return PLACE.WHITE;
        }
    }
}