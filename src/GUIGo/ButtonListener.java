package GUIGo;

import com.GO.Play;
import com.GO.STATE;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Bartłomiej on 2016-12-04.
 */
public class ButtonListener implements ActionListener{

    private String butt;
    private Play play;
    ButtonListener(Play play)
    {
        this.play=play;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        butt=e.getActionCommand();
       switch(butt)
       {
           case "Pass":
               if(play.getPlayState()== STATE.GAME)
               play.passGame();
             break;
           case "Mark Territory":
               if(play.getPlayState()==STATE.ADD_DEAD_GROUPS || play.getPlayState()==STATE.REMOVE_TERRITORY){
                   play.setPlayState(STATE.ADD_TERITORITY);
               }

            break;
           case "Unmark Territory":
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
                       play.inicializeGameWithServer();
                   }
               }
               break;
           case "Mark As Dead":
               if((play.getPlayState()==STATE.GAME)||(play.getPlayState()==STATE.REMOVE_DEAD_GROUPS)){
                   play.setPlayState(STATE.ADD_DEAD_GROUPS);
               }


               break;
           case "Unmark Dead":
               if((play.getPlayState()==STATE.GAME)||(play.getPlayState()==STATE.ADD_DEAD_GROUPS)){
                   play.setPlayState(STATE.REMOVE_DEAD_GROUPS);
               }


               break;
           case "GiveUp":
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
               if(play.getPlayState().equals(STATE.REMOVE_DEAD_GROUPS) || play.getPlayState().equals(STATE.ADD_DEAD_GROUPS))
                   play.sendDeadGroups();
               else if(play.getPlayState().equals(STATE.ADD_TERITORITY) || play.getPlayState().equals(STATE.REMOVE_TERRITORY))
                   play.sendTerritory();
               break;
           case "Accept":
               if(play.getPlayState().equals(STATE.REMOVE_DEAD_GROUPS) || play.getPlayState().equals(STATE.ADD_DEAD_GROUPS))
                   play.changeToTerritory();
               else if(play.getPlayState().equals(STATE.ADD_TERITORITY) || play.getPlayState().equals(STATE.REMOVE_TERRITORY))
                   play.endGame();
               break;
          // case "Reject":
          //     break;
           default:
               break;


       }

    }
}
