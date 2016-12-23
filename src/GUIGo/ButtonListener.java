package GUIGo;

import com.GO.Play;

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
               play.passGame();
             break;
           case "AddTerritory":

            break;
           case "RemoveTerritory":

               break;
           case "Start":
               int selectedOption = JOptionPane.showConfirmDialog(null,
                       "Do you want to play with player?",
                       "Start Game",
                       JOptionPane.YES_NO_OPTION);
               if(selectedOption == 1){
                   //sendBotGame();
               }
               else{
                   //sendPlayerGame();
               }
               break;
           case "AddDeadGroup":

               break;
           case "RemoveDeadGroup":

               break;
           case "GiveUp":
               break;


       }

    }
}
