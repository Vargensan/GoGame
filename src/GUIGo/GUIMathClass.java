package GUIGo;

/**
 * Created by Bartłomiej on 2016-11-21.
 */
public interface GUIMathClass {

    public int[] calculateIntersection(int[] cordinats, int size, int[] startPoint, int distance);
    public int[] calculateStartPoint(int height, int width);
    public int calculateHeightandWidth(int width, int lenght);
    public int[][] calculateTableIntersection(int[] startPoint, int distance);
    public int calculateDistance(int height, int boardSize);

}
