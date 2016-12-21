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
public class LogicBoardTest{

    Board gameboard;
    PLAYER player;
    PLAYER player2;
    Play play;
    PLACE[][] GameTable;
    @Test
    public void test_canaddaftersimmeric_situation(){
        setstone();
        boolean canadd;
        likeKO();

        canadd = gameboard.canAddHere(player.getEnemyColor(),4,4);
        assertTrue(canadd);
    }
    @Test
    public void test_ko_situation(){
        setstone();
        boolean canadd;
        likeKO();

        gameboard.canAddHere(player.getEnemyColor(),4,4);
        gameboard.addStone(player.getEnemyColor(),4,4);
        canadd = gameboard.canAddHere(player,5,4);
        assertFalse(canadd);
    }
    @Test
    public void test_getterKO(){
        test_ko_situation();
        assertTrue(gameboard.getKO_Status());
    }
    @Test
    public void test_null_KO_afterVaildMove(){
        setstone();
        boolean canadd;
        likeKO();
        gameboard.canAddHere(player.getEnemyColor(),4,4);
        gameboard.addStone(player.getEnemyColor(),4,4);
        canadd = gameboard.canAddHere(player,5,4);
        if(canadd == false) {
            gameboard.canAddHere(player.getEnemyColor(), 4, 8);
            gameboard.addStone(player.getEnemyColor(), 4, 8);
        }
        canadd = gameboard.canAddHere(player,5,4);
        if(canadd){
            canadd = gameboard.getKO_Status();
        }
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
    public void likeKO(){
        gameboard.addStone(player,4,3);
        gameboard.addStone(player,4,5);
        gameboard.addStone(player,3,4);
        gameboard.addStone(player,5,4);

        gameboard.addStone(player.getEnemyColor(),5,5);
        gameboard.addStone(player.getEnemyColor(),5,3);
        gameboard.addStone(player.getEnemyColor(),6,4);
    }




}