package Test;

import GUIGo.ClientGUI;
import GUIGo.DrawingBoard;
import com.GO.Board;
import com.GO.PLACE;
import com.GO.PLAYER;
import com.GO.Play;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bartłomiej on 2016-11-28.
 */
public class LogicBoardTest extends DrawingBoard{

    Board gameboard;
    PLAYER player;
    PLAYER player2;
    Play play;
    PLACE[][] GameTable;
    @Test
    public void test_ko_situation(){
        boolean canadd;
        GameTable[4][3] = player.playerToPlace();
        GameTable[4][5] = player.playerToPlace();
        GameTable[3][4] = player.playerToPlace();
        GameTable[5][4] = player.playerToPlace();

        GameTable[5][3] = player2.playerToPlace();
        GameTable[5][5] = player2.playerToPlace();
        GameTable[6][4] = player2.playerToPlace();
        canadd = gameboard.canAddHere(player2,4,4);
        if(canadd){
            GameTable[4][4] = player2.playerToPlace();
        }
        canadd = gameboard.canAddHere(player,4,4);
        assertFalse(canadd);
    }

    @Test
    public void testifcanbreathe(){
        boolean canadd;
        setstone();
        canadd = gameboard.canAddHere(player,10,10);
        gameboard.addStone(player,10,10);
        if(canadd == true){
            GameTable[11][11] = player.playerToPlace();
            System.out.println("Player 1: Success on adding stone!");
        }else
            System.out.println("Player 1: Fail on adding stone!");
        //Making rectangle
        canadd = gameboard.canAddHere(player,10,9);
        if(canadd == true){
            GameTable[10][9] = player.playerToPlace();
            System.out.println("Player 1: Success on adding stone!");
        }else
            System.out.println("Player 1: Fail on adding stone!");
        //Assert he cant add on same place
        canadd = gameboard.canAddHere(player2,10,9);
        if(canadd == true){
            GameTable[10][9] = player.playerToPlace();
            System.out.println("Player 2: Success on adding stone!");
        }else
            System.out.println("Player 2: Fail on adding stone!");

        //Now test arrount
    }

    @Test
    public void testdoublesameclick(){
        boolean canadd;
        setstone();
        canadd = gameboard.canAddHere(player,10,9);
        if(canadd){
            GameTable[10][9] = player.playerToPlace();
        }
        canadd = gameboard.canAddHere(player,10,9);
        assertFalse(canadd);
    }

    @Test
    public void testbreathing(){
        boolean canadd;
        setstone();
        for(int i = 10; i < 13; i++){
            canadd = gameboard.canAddHere(player,i,10);
            if(canadd == true){
                GameTable[i][10] = player.playerToPlace();
                System.out.println("Player 1: Success on adding stone! On X: "+i+" And Y: "+ 10);
            }else
                System.out.println("Player 1: Fail on adding stone! On X: "+i+" And Y: "+ 10);
            canadd = gameboard.canAddHere(player,10,i);
            if(canadd == true){
                GameTable[10][i] = player.playerToPlace();
                System.out.println("Player 1: Success on adding stone! On X: "+10+" And Y: "+ i);
            }else
                System.out.println("Player 1: Fail on adding stone! On X: "+10+" And Y: "+ i);
            canadd = gameboard.canAddHere(player,12,i);
            if(canadd == true){
                GameTable[12][i] = player.playerToPlace();
                System.out.println("Player 1: Success on adding stone! On X: "+12+" And Y: "+ i);
            }else
                System.out.println("Player 1: Fail on adding stone! On X: "+12+" And Y: "+ i);
            canadd = gameboard.canAddHere(player,i,12);
            if(canadd == true){
                GameTable[i][12] = player.playerToPlace();
                System.out.println("Player 1: Success on adding stone! On X: "+i+" And Y: "+ 12);
            }else
                System.out.println("Player 1: Fail on adding stone! On X: "+i+" And Y: "+ 12);
        }
        canadd = gameboard.canAddHere(player2,11,11);
        assertFalse(canadd);
    }

    public void setstone(){
        player = PLAYER.BLACK;
        player2 = PLAYER.WHITE;
        gameboard = new Board(19,play);
        GameTable = gameboard.getGameTable();
    }




}