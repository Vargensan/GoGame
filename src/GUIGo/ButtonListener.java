package GUIGo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Bartłomiej on 2016-12-04.
 */
public class ButtonListener implements ActionListener{
    private Object from;
    @Override
    public void actionPerformed(ActionEvent e) {
        from = e.getSource();
        System.out.println(from);

    }
}
