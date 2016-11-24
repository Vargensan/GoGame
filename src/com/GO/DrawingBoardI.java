/**
 * Bartłomiej Woś 2016-11-21.
 */

package com.GO;

import java.awt.*;

/**
 * Bartłomiej Woś 2016-11-21.
 */

public interface DrawingBoardI {
    public void drawLine(Graphics g2, int[] X, int[] Y);
    public void update();
    public void countLines(int tableSize);
    public int setStartConditions();
    public void drawLines(Graphics2D g2, int temp);
    public int getXofPoint();
    public int getYofPoint();


}
