package src.GUIGo;

import src.com.GO.*;

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
    private boolean puttingStone;
    private boolean isClicable;
    private int error;

    //Skoro implementujemy play, implementacja board może być zaniechana...
    //Dostaniemy board przez play, zmniejszone dziedziczenie i tylko klasa play bedzie powiazana,
    //z logika
    //isClicable, do usunięcia bo nie działa
    //
BoardOnClickListener(Board board,DrawingBoard obj,Play play,boolean isClicable)
{
    this.play=play;
    this.obj = obj;
    this.board = board;
    this.isClicable = isClicable;

}

    /**
     * Setter for error messages
     */
    public void setError(){
        this.error = play.getPlayBoard().getErrorMessage();
    }

    /*
    Przekazujemy e.getX e.getY z javascript mouse clicked wywołujemy funckej dodającą pole jeżeli state
    jest Game, wgl sprawdzanie w Aktorze, aktualizowanie Unactive/Active -> Sprawdzanie State -> Wykonanie ruchu

     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch(play.getPlayState()) {
            case GAME:
                informTurn();
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
                    else{
                        int i;
                        i = board.getInvaildOption();
                        if(i == 1)
                            play.informInvaildMove(1);
                        if(i == 2)
                            play.informInvaildMove(2);
                        if(i == 3)
                            play.informKO();
                        play.turnRepaint();

                    }
                }
                break;
            default:
                break;
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
     * and depending on state of game can invoke paint of some states
     * @param e mouse event action was made
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        switch(play.getPlayState()) {
            case GAME:
                obj.changeState(play.getPlayState());
                mouse_coordinates[0] = e.getX();
                mouse_coordinates[1] = e.getY();
                obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates, BoardSize, StartPoint, distance);
                obj.repaint();
                break;
            case ADD_DEAD_GROUPS:
                if(informTurn() == false){
                    break;
                }
                obj.changeState(play.getPlayState());
                mouse_coordinates[0] = e.getX();
                mouse_coordinates[1] = e.getY();
                obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates, BoardSize, StartPoint, distance);
                board.markAsDead(obj.intersectionPoint[0],obj.intersectionPoint[1]);
                obj.repaint();
                break;
            case REMOVE_DEAD_GROUPS:
                if(informTurn() == false){
                    break;
                }
                obj.changeState(play.getPlayState());
                mouse_coordinates[0] = e.getX();
                mouse_coordinates[1] = e.getY();
                obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates, BoardSize, StartPoint, distance);
                board.unMarkAsDead(obj.intersectionPoint[0],obj.intersectionPoint[1]);
                obj.repaint();
                break;
            case ADD_TERITORITY:
                if(informTurn() == false){
                    break;
                }
                obj.changeState(play.getPlayState());
                mouse_coordinates[0] = e.getX();
                mouse_coordinates[1] = e.getY();
                obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates,BoardSize,StartPoint,distance);
                board.markAsTerritory(play.get_player_color(),obj.intersectionPoint[0],obj.intersectionPoint[1]);
                obj.repaint();
                break;
            case REMOVE_TERRITORY:
                if(informTurn() == false){
                    break;
                }
                obj.changeState(play.getPlayState());
                mouse_coordinates[0] = e.getX();
                mouse_coordinates[1] = e.getY();
                obj.intersectionPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates,BoardSize,StartPoint,distance);
                board.unMarkAsTerritory(obj.intersectionPoint[0],obj.intersectionPoint[1]);
                obj.repaint();
                break;

        }
    }

    /**
     * Method which checks if it is turn of player
     * @return status of player turn true/false
     */
    public boolean informTurn(){
        if(!obj.getterMouseListener()){
            play.giveWarningMessage();
            obj.repaint();
            return false;
        }
        return true;
    }

}
