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
BoardOnClickListener(DrawingBoard obj,Board board,Play play)
{
    this.play=play;
    this.obj = obj;
    this.board=board;

}
    @Override
    public void mouseReleased(MouseEvent e) {

        //Temp Code - to do checkout
        //if game logic accepts point then it will be drown
        mouse_coordinates[0] = e.getX();
        mouse_coordinates[1] = e.getY();
        obj.relasedPoint = obj.dmo_calculate.calculateIntersection(mouse_coordinates, BoardSize, StartPoint, distance);
        puttingStone = board.canAddHere(board.getPLayerColor(),obj.relasedPoint[0],obj.relasedPoint[1]);
        if (puttingStone) {
            board.addStone(board.getPLayerColor(), obj.relasedPoint[0], obj.relasedPoint[1]);
            play.informKO();
            /*
            Zakreskowane pola, to te zbite, ale nie odświeżone, cd z nieodpowiednim odświeżaniem planszy
            zakreskowane -> przecięcie lini "na" kamyku, teraz odświeżają się co "kolejny ruch"
            tj. jak przeciwnik wykona ruch i zbije nasze, to widzimy kreski, jak my wykonamy ruch to odswieży nam
            planszę i jest ok, możliwe odświeżenie planszy w trakcie wykonania naszego ruchu, przez przeciągnięcie
            myszy w celu umieszczenia kamienia. Powód: wyowływana metoda update() przez MouseDragged

            Pomysł poradzenia sobie z problemem: repaint() po stronie klienta od razu po dodaniu kamienia.
            Metoda repaint() nie może być wywoływana od strony Board - nie ma dostępu
            Adnote: NIEDZIAŁĄ

            Adnote 2: Dodane do paintComponent wywołanie rysowanie tła -> gdy wywołamy paintImmediently musimy wszystko
            przerysować, tło też

            Adnote 3: Trzeba to synchronizować ale działa to teraz na tyle poprawnie (patrz pozostałe dodane komentarze)
            że można się pokusić o poprawnę zformuowanie logiki i odczytanie z niej co nie gra
             */
            obj.drawIntersection = true;
            obj.paintImmediately(0,0,obj.getWidth(),obj.getHeight());
            //obj.update();
           // obj.paintComponent(obj.getGraphics());
            play.game(obj.relasedPoint[0], obj.relasedPoint[1]);
            //play.informTurnChange();

        }
        else if(play.getPlayBoard().getKO_Status()){
            play.informKO();
            obj.paintImmediately(0,0,obj.getWidth(),obj.getHeight());
        }else{
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
            obj.paintImmediately(0,0,obj.getWidth(),obj.getHeight());
    }

}
