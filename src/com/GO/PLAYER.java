/**
 * Kinga Krata 2016-11-20.
 */

package com.GO;

public enum PLAYER {

    BLACK, WHITE;

    public PLAYER getEnemyColor(PLAYER color){
        if(color == PLAYER.BLACK)
            return PLAYER.WHITE;
        else
            return PLAYER.BLACK;

    }

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