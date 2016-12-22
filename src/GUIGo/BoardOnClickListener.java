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

    private int distance;
    private int height;
    private int BoardSize;
    private Board board;
    private Play play;
    private int[] StartPoint = new int[2];
    private int[] mouse_coordinates = new int[2];
    private DrawingBoard obj;
    boolean puttingStone;
    boolean isClicable;
BoardOnClickListener(DrawingBoard obj,Board board,Play play,boolean isClicable)
{
    this.play=play;
    this.obj = obj;
    this.board=board;
    this.isClicable = isClicable;

}
    @Override
    public void mouseReleased(MouseEvent e) {

        //Temp Code - to do checkout
        //if game logic accepts point then it will be drown
        if(!obj.getterMouseListener()){
            play.giveWarningMessage();
            obj.repaint();
        }

        /*
        There is a bug, cant resolve where, cause i don't know if it is error in not-blocking mouse
        signals or in logic, to be ensure, that it is not logic, need to create double-check condition...
        ---CAN'T DEBUG PROGRAM CAUSE ERROR OCUERS---
         */
        System.out.println("play : Color of player" +play.get_player_color()+" Mouse State: "+obj.getterMouseListener());
        if (obj.getterMouseListener()) {
            mouse_coordinates[0] = e.getX();
            mouse_coordinates[1] = e.getY();
            obj.relasedPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates, BoardSize, StartPoint, distance);
            puttingStone = board.canAddHere(board.getPLayerColor(), obj.relasedPoint[0], obj.relasedPoint[1]);
            if (puttingStone) {
                board.addStone(board.getPLayerColor(), obj.relasedPoint[0], obj.relasedPoint[1]);
                play.informKO();
                obj.drawIntersection = true;
                obj.paintImmediately(0, 0, obj.getWidth(), obj.getHeight());
                play.game(obj.relasedPoint[0], obj.relasedPoint[1]);
            }
        } else if (play.getPlayBoard().getKO_Status()) {
            play.informKO();
            obj.paintImmediately(0, 0, obj.getWidth(), obj.getHeight());
        } else {
            System.out.println("It is not your turn!");
        }

            //End of Temp Code

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
        //obj.update();
        obj.repaint();
    }

}
