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
           case "AddTerritory":

            break;
           case "RemoveTerritory":

               break;
           case "Start":
               if(play.getPlayState()==STATE.BEFORE_GAME) {
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
           case "AddDeadGroup":
               if((play.getPlayState()==STATE.GAME)||(play.getPlayState()==STATE.REMOVE_DEAD_GROUPS)){
                   play.setPlayState(STATE.ADD_DEAD_GROUPS);
               }


               break;
           case "RemoveDeadGroup":
               if((play.getPlayState()==STATE.GAME)||(play.getPlayState()==STATE.ADD_DEAD_GROUPS)){
                   play.setPlayState(STATE.REMOVE_DEAD_GROUPS);
               }


               break;
           case "GiveUp":
               break;


       }

    }
}
