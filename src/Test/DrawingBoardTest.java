package Test;

import GUIGo.ClientGUI;
import GUIGo.DrawingBoard;
import com.GO.PLACE;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bartłomiej on 2016-11-28.
 */
public class DrawingBoardTest extends DrawingBoard{

    ClientGUI app = new ClientGUI();
    PLACE[][] GameBoard = new PLACE[19][19];

    @Test
    public void testDrawing(){
        app.startDrawing(GameBoard);
    }


}