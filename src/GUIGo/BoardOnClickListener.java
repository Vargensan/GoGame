package GUIGo;

import com.GO.*;

import java.awt.event.MouseAdapter;

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
    int error;

BoardOnClickListener(DrawingBoard obj,Board board,Play play,boolean isClicable)
{
    this.play=play;
    this.obj = obj;
    this.board=board;
    this.isClicable = isClicable;

}
    public void setError(){
        this.error = play.getPlayBoard().getErrorMessage();
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
            //System.out.println("rel point X: "+obj.relasedPoint[0] + " rel point Y: "+obj.relasedPoint[1]);
            puttingStone = board.canAddHere(board.getPLayerColor(), obj.relasedPoint[0], obj.relasedPoint[1]);
            //System.out.println(puttingStone);
            if (puttingStone) {
                board.addStone(board.getPLayerColor(), obj.relasedPoint[0], obj.relasedPoint[1]);
                play.informKO();
                obj.drawIntersection = true;
                obj.paintImmediately(0, 0, obj.getWidth(), obj.getHeight());
                play.game(obj.relasedPoint[0], obj.relasedPoint[1]);
            }
        } else if (error == 2) {
            play.informKO();
            obj.paintImmediately(0, 0, obj.getWidth(), obj.getHeight());
        } else if (error == 1){
            System.out.println("Can't place stone at not-empty field");
        } else{
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
        switch(play.getPlayState()) {
            case GAME:
                obj.drawIntersection = true;
                mouse_coordinates[0] = e.getX();
                mouse_coordinates[1] = e.getY();
                obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates, BoardSize, StartPoint, distance);
                //obj.update();
                obj.repaint();
                break;
            case ADD_DEAD_GROUPS:
                break;
            case REMOVE_DEAD_GROUPS:
                break;
        }
    }

}
