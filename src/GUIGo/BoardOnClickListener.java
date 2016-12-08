package GUIGo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Bartłomiej on 2016-12-04.
 */
public class BoardOnClickListener implements MouseMotionListener{

    private int[] IntersetionPoint = new int[2];
    private int distance;
    private int height;
    private int BoardSize;
    private int[] StartPoint = new int[2];
    private int[] mouse_coordinates = new int[2];
    DrawingBoard obj;

    /**
     * Method that sets distance between drawed lines and Start Point where
     * the drawing begins
     */
    public void initialize(){
        this.distance = obj.distance;
        StartPoint = obj.dmo_calculate.calculateStartPoint(height, BoardSize, distance);
    }

    /**
     * Method that takes a height from DrawingBoard
     * @param height
     */
    public void setHeight(int height){
        this.height = height;
    }

    /**
     * Method that takes a length of the playable area
     * @param boardSize
     */
    public void setBoardSize(int boardSize){
        this.BoardSize = boardSize;
    }

   public BoardOnClickListener(DrawingBoard obj){
        this.obj = obj;
    }

    /**
     *
     * Method which refresh actuall position of the mouse
     * and gets the closest intersection of the lines
     * @param e mouse event action was made
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        obj.drawIntersection = true;
        System.out.println("I am reactiong!");
        mouse_coordinates[0] = e.getX();
        System.out.println(mouse_coordinates[0]);
        mouse_coordinates[1] = e.getY();
        obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates,BoardSize,StartPoint,distance);
        obj.update();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
