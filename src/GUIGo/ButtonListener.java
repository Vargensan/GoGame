package GUIGo;

import com.GO.Play;

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
           case "AddDeadGroup":

               break;
           case "RemoveDeadGroup":

               break;
           case "GiveUp":
               break;


       }

    }
}
