/**
 * Kinga Krata 2016-11-20.
 */

package com.GO;

public enum PLAYER {

    BLACK,WHITE;

public PLACE playerToPlace()
{
    switch(this)
    {
        case BLACK:
            return PLACE.BLACK;
        case WHITE:
            return PLACE.WHITE;
        default:return PLACE.WHITE;
    }

}
}
