package src.GUIGo;

import src.com.GO.Play;
import src.com.GO.STATE;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Bartłomiej on 2016-12-04.
 */
public class ButtonListener implements ActionListener{

    private String butt;
    private Play play;
    private boolean isyourturn;
    public ButtonListener(Play play)
    {
        this.play=play;
    }
    /*
    Potworzyć odpowiednie metody w zależności od wiadomości
    Pass() <- wysłanie z JavaScript "Pass"
    MarkTerritory() etc...
     */
    public void callPass(){
        isyourturn = informTurn();
        if(play.getPlayState()==STATE.GAME){
            play.passGame();
        }
    }
    public void callMark(){}

    @Override
    public void actionPerformed(ActionEvent e) {
        butt=e.getActionCommand();
       switch(butt)
       {
           case "Pass":
               isyourturn = informTurn();
               if(!isyourturn)
                   break;
               if(play.getPlayState()== STATE.GAME)
               play.passGame();
             break;
           case "Mark Territory":
               isyourturn = informTurn();
               if(!isyourturn)
                   break;
               if(play.getPlayState()==STATE.ADD_DEAD_GROUPS || play.getPlayState()==STATE.REMOVE_TERRITORY){
                   play.setPlayState(STATE.ADD_TERITORITY);
               }

            break;
           case "Unmark Territory":
               isyourturn = informTurn();
               if(!isyourturn)
                   break;
               if(play.getPlayState()==STATE.ADD_DEAD_GROUPS || play.getPlayState()==STATE.ADD_TERITORITY){
                   play.setPlayState(STATE.REMOVE_TERRITORY);
               }
               break;
           case "Start":
               if(play.getPlayState()==STATE.BEFORE_GAME && play.getConfirmation()) {
                   int selectedOption = JOptionPane.showConfirmDialog(null,
                           "Do you want to play with player?",
                           "Start Game",
                           JOptionPane.YES_NO_OPTION);
                   if (selectedOption == 1) {
                       //sendBotGame();
                   } else {
                       play.setStart();
                   }
               }
               break;
           case "Mark As Dead":
               isyourturn = informTurn();
               if(!isyourturn)
                   break;
               if((play.getPlayState()==STATE.GAME)||(play.getPlayState()==STATE.REMOVE_DEAD_GROUPS)){
                   play.setPlayState(STATE.ADD_DEAD_GROUPS);
               }


               break;
           case "Unmark Dead":
               isyourturn = informTurn();
               if(!isyourturn)
                   break;
               if((play.getPlayState()==STATE.GAME)||(play.getPlayState()==STATE.ADD_DEAD_GROUPS)){
                   play.setPlayState(STATE.REMOVE_DEAD_GROUPS);
               }


               break;
           case "Give Up":
               isyourturn = informTurn();
               if(!isyourturn)
                   break;
               if(!play.getPlayState().equals(STATE.BEFORE_GAME) && !play.getPlayState().equals(STATE.END_GAME))
                   play.setGiveUpstatus();
               break;
           case "Confirm Size":
               if(play.getPlayState() == STATE.BEFORE_GAME)
               play.setConfirmation();
               break;
           case "Game Info":
               JOptionPane.showMessageDialog(null,"Created by" +
                       " Kinga Krata and Bartłomiej Woś\n\n" +
                       "Firstly pick a size from a list in right corner\n" +
                       "Next confirm it with the button 'Confirm Size'\n" +
                       "Draw Panel should be drown, next Start Game and pick \n" +
                       "option if you want to play with Bot or Normal User\n" +
                       "Wait for connection and play, after double pass\n" +
                       "at same time, You will need to select dead groups by mouse\n" +
                       "dragging, you can also remove them, if you both accept dead groups\n" +
                       "then you go to select territory and actions are same as described above\n" +
                       "After all system calculates your points and send a message if\n" +
                       "you won or lost\n","Game Information",JOptionPane.INFORMATION_MESSAGE);
               break;
           case "Send":
               isyourturn = informTurn();
               if(!isyourturn)
                   break;
               if(play.getPlayState().equals(STATE.REMOVE_DEAD_GROUPS) || play.getPlayState().equals(STATE.ADD_DEAD_GROUPS))
                   play.sendDeadGroups();
               else if(play.getPlayState().equals(STATE.ADD_TERITORITY) || play.getPlayState().equals(STATE.REMOVE_TERRITORY))
                   play.sendTerritory();
               break;
           case "Accept":
               isyourturn = informTurn();
               if(!isyourturn)
                   break;
               if(play.getAcceptStatus()) {
                   if (play.getPlayState().equals(STATE.REMOVE_DEAD_GROUPS) || play.getPlayState().equals(STATE.ADD_DEAD_GROUPS))
                       play.changeToTerritory();
                   else if (play.getPlayState().equals(STATE.ADD_TERITORITY) || play.getPlayState().equals(STATE.REMOVE_TERRITORY))
                       play.endGame();
               }
               break;
          // case "Reject":
          //     break;
           default:
               break;


       }

    }

    public boolean informTurn(){
        if(!play.getTurn()){
            play.giveWarningMessage();
            //play.getDrawingBoard().repaint();
            return false;
        }
        return true;
    }
}
