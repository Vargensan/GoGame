/**
 * Bartłomiej Woś 2016-11-21.
 */

package com.GO;

import java.awt.*;

/**
 * Bartłomiej Woś 2016-11-21.
 */

public interface DrawingBoardI {

    /**
     * Method which draw lines on the DrawingBoard. The initiation
     * of the game
     * // TODO:
     * Need to add a Point to operate on
     * @param g2 graphics added to a method
     * @param temp ---unused varible(sizeofGameBoard could be replaced)
     */
    public void drawLines(Graphics2D g2,int temp);

    /**
     * Method that fill a circle in specified point of the DrawingBoard
     *@param g2 graphic added to a method
     *@param player set the player, to correctly fill the cricle in
     *              DrawingBoard
     *@param cordinates point in the DrawingBoard where the filled Cricle
     *                  is going to be
     */
    public void filledCircle(Graphics2D g2, PLAYER player, int[] cordinates);

    /**
     *
     */

    public void update();

    /**
     *
     * @return
     */
    public int getXofPoint();

    /**
     *
     * @return
     */
    public int getYofPoint();



}
