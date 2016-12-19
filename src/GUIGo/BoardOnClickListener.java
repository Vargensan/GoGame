package GUIGo;

import com.GO.Board;

import java.awt.event.MouseAdapter;
import com.GO.PLACE;
import com.GO.PLAYER;
import com.GO.Play;

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
    private Play play;
    private int[] StartPoint = new int[2];
    private int[] mouse_coordinates = new int[2];
    private DrawingBoard obj;
    //TEMP
    boolean color = true;
    //

BoardOnClickListener(DrawingBoard obj,Board board,Play play)
{
    this.play=play;
    this.obj = obj;
    this.board=board;
    if(play.get_player_color()==PLAYER.WHITE)
    this.setIsClicable(true);
}
    @Override
    public void mouseReleased(MouseEvent e) {
    if(isClicable) {
        //Temp Code - to do checkout
        //if game logic accepts point then it will be drown
        mouse_coordinates[0] = e.getX();
        mouse_coordinates[1] = e.getY();
        obj.relasedPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates, BoardSize, StartPoint, distance);
        if (board.canAddHere(board.getPLayerColor(), obj.relasedPoint[0], obj.relasedPoint[1])) {
            board.addStone(board.getPLayerColor(), obj.relasedPoint[0], obj.relasedPoint[1]);
            obj.update();
            setIsClicable(false);
            play.game(obj.relasedPoint[0], obj.relasedPoint[1]);

        }

        //End of Temp Code
    }
    }

    /**
     * Method that sets distance between drawed lines and Start Point where
     * the drawing begins
     */
    public void initialize(){
        this.distance = obj.distance;
        StartPoint = obj.dmo_calculate.calculateStartPoint(height, BoardSize, distance);
    }

    /**
     * Method that sets a height
     * @param height takes height from DrawingBoard
     */
    public void setHeight(int height){
        this.height = height;
    }

    /**
     * Method that takes a length of the playable area
     * @param boardSize take board size from DrawingBoard
     */
    public void setBoardSize(int boardSize){
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
            obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates, BoardSize, StartPoint, distance);
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
