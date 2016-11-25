/**
 * Bartłomiej Woś 2016-11-21.
 */

package com.GO;

import java.awt.*;

/**
 * Bartłomiej Woś 2016-11-21.
 */

public interface DrawingBoardI {
    public void drawLines(Graphics2D g2,int temp);
    public void filledCircle(Graphics2D g2, PLAYER player, int[] cordinates);
    public void update();
    public int getXofPoint();
    public int getYofPoint();


}
