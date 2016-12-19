package GUIGo;

import com.GO.Board;

import java.awt.event.MouseAdapter;
import com.GO.PLACE;
import com.GO.PLAYER;

import java.awt.event.MouseEvent;

/**
 * Created by Bartłomiej on 2016-12-04.
 */
public class BoardOnClickListener extends MouseAdapter{

    private boolean isClicable;
    private int distance;
    private int height;
    private int BoardSize;
    private Board board;
    private int[] StartPoint = new int[2];
    private int[] mouse_coordinates = new int[2];
    private DrawingBoard obj;
    //TEMP
    boolean color = true;
    //

BoardOnClickListener(DrawingBoard obj,Board board)
{
    this.obj = obj;
    this.board=board;
}
    @Override
    public void mouseReleased(MouseEvent e) {
        //Temp Code - to do checkout
        //if game logic accepts point then it will be drown
        mouse_coordinates[0] = e.getX();
        mouse_coordinates[1] = e.getY();
        obj.relasedPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates,BoardSize,StartPoint,distance);
       if( board.canAddHere(board.getPLayerColor(),obj.relasedPoint[0],obj.relasedPoint[1]))
       {
           board.addStone(board.getPLayerColor(),obj.relasedPoint[0],obj.relasedPoint[1]);
           setIsClicable(false);
       }
        obj.update();
        //End of Temp Code
    }

    /**
     * Method that sets distance between drawed lines and Start Point where
     * the drawing begins
     */
    void initialize(){
        this.distance = obj.distance;
        StartPoint = obj.dmo_calculate.calculateStartPoint(height, BoardSize, distance);
    }

    /**
     * Method that sets a height
     * @param height takes height from DrawingBoard
     */
    void setHeight(int height){
        this.height = height;
    }

    /**
     * Method that takes a length of the playable area
     * @param boardSize take board size from DrawingBoard
     */
    void setBoardSize(int boardSize){
        this.BoardSize = boardSize;
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
        mouse_coordinates[0] = e.getX();
        mouse_coordinates[1] = e.getY();
        obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates,BoardSize,StartPoint,distance);
        obj.update();
    }

    /**
     * Getter for boolean isClicable
     * @return state of boolean isClicable
     */
   public boolean getIsClicable(){
        return isClicable;
   }

    /**
     * Setter for boolean isClicable
     * @param clicable takes a boolean and assign it to isClicable
     */
   public void setIsClicable(boolean clicable){
        this.isClicable = clicable;
   }
}
