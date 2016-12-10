package GUIGo;

/**
 * Created by Bartłomiej on 2016-11-21.
 */
public interface GUIMathClass {

    /**
     * Method which allows to calculates closest intersection of lines
     * in range of mouse current position
     * @param cordinats take the mouse current position
     * @param size takes a size of board
     * @param startPoint takes a beging point of start drawing
     * @param distance takes a constant of distance between lines
     * @return closest intersection
     */

    public int[] calculateIntersection(int[] cordinats, int size, int[] startPoint, int distance);

    /**
     * Method which allows to calculate where StartPoint of drawing should be
     * @param height takes the height of drawing board
     * @param width takes the width of drawing board
     * @return start point in which drawing begins
     */

    public int[] calculateStartPoint(int height, int size, int distance);

    /**
     * Method to calculate actuall height and width of drawing board
     * @param width t
     * @param lenght
     * @return
     */

    public int calculateHeightandWidth(int width, int lenght);

    /**
     * Method which generates a table of intersections of the
     * lines on the DrawingBoard
     * @param startPoint takes a start point where drawing begins
     * @param distance takes a constant distance between lines
     * @return table of intersection of the lines
     */
    public int[][][] calculateTableIntersection(int[] startPoint, int distance, int boardSize, int criclesize);

    /**
     * Method which sets the constant distance between lines
     * @param height takes actuall height of the drawingboard
     * @param boardSize takes actuall size of drawingboard
     * @return distance in which lines are drowed
     */
    public int calculateDistance(int height, int boardSize);

    /**
     * Method that sets how large will be a filled cricle in the DrawingBoard
     * @param distance
     * @return size of filled circle
     */

    public int calculateSizeOfCircle(int distance);


}
